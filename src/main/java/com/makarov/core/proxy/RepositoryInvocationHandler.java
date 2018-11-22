package com.makarov.core.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class associated with proxy instances for interfaces,
 * annotated with {@link com.makarov.annotation.Repository}
 *
 * Mainly created for intercepting "findBy...()" method calls
 * to create and log SQL query, based on method name and given arguments
 */
public class RepositoryInvocationHandler implements InvocationHandler {

    private static Logger log = Logger.getLogger(RepositoryInvocationHandler.class.getName());

    private Object target;

    public RepositoryInvocationHandler(Object target) {
        this.target = target;
    }

    private static final String FIND_BY = "findBy";

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        logMethod(method, args);
        return target != null ? method.invoke(target, args) : Optional.empty();
    }

    private void logMethod(Method method, Object[] args) {
        if (method.getName().startsWith(FIND_BY))
            log.info("Performing SQL query: " + buildSQLQuery(method, args));
    }

    /**
     * builds SQL query with table name assigned as given method return type
     * !!! Arguments have to be in the same order as params in method name
     *
     * @return - string representation of SQL query, or empty string in case of exceptions
     *
     */
    private String buildSQLQuery(Method method, Object[] args) {
        Class clazz;
        try {
            clazz = getClass(method);
        } catch (ClassNotFoundException e) {
            log.log(Level.SEVERE, "Cannot define SQL query");
            return "";
        }
        //let's define table name as method return type
        String tableName = clazz.getSimpleName().toLowerCase();
        String condition = defineCondition(method.getName(), args);
        return "SELECT * FROM " + tableName + " WHERE " + condition;
    }

    private Class getClass(Method method) throws ClassNotFoundException {
        String rt = method.getGenericReturnType().getTypeName();
        String className = rt.substring(rt.lastIndexOf('<') + 1, rt.lastIndexOf('>'));
        return Class.forName(className);
    }

    /**
     * Builds after 'WHERE' part of SQL query, extracting
     * query params and conditional operators from method name,
     * and consequentially concatenating them with query values
     *
     * @param methodName  - given simple method name, as source for query params and operators
     * @param queryValues - query values to match query params
     * @return - string representation of after 'WHERE' part of SQL query
     */
    private String defineCondition(String methodName, Object[] queryValues) {
        StringBuilder condition = new StringBuilder();

        String[] queryParams = methodName.substring(FIND_BY.length())
                .replace("And", " ")
                .replace("Or", " ")
                .toLowerCase()
                .split(" ");
        char[] conditionalOperators = methodName
                .replace("And", "1")
                .replace("Or", "0")
                .replaceAll("[^\\d.]", "")
                .toCharArray();

        try {
            for (int i = 0; i < queryParams.length; i++) {
                condition
                        .append(queryParams[i] + " = ")
                        .append(resolveQuotes(queryValues[i]))
                        .append(i == queryParams.length - 1 ? "" : conditionalOperators[i] == '1' ? " AND " : " OR ");
            }
        } catch (IndexOutOfBoundsException e) {
            log.log(Level.SEVERE, "Cannot build SQL query, params and arguments quantity do not match");
            return "";
        }
        return condition.toString() + ";";
    }

    /**
     * Wraps in single quotes if argument is not digit, as SQL requires
     */
    private String resolveQuotes(Object queryValue) {
        String value = String.valueOf(queryValue);
        boolean onlyDigits = value.replaceAll("[\\d.]", "").isEmpty();
        return onlyDigits ? value : "'" + value + "'";
    }
}
