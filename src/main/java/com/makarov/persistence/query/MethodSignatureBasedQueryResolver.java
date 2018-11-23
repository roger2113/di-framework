package com.makarov.persistence.query;

import com.makarov.core.annotation.Component;

import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class to resolve SQL query from given Method object and method arguments array
 * ! only methods of type "findBy...Or/And..." implemented
 *
 *  - table name is defined as lowercase of Method return Type simple name
 *  - logical expression 'WHERE' is defined as extracted from Method name parameters
 *    consequentially concatenated with given arguments
 *
 *  For example: <Car> findByPriceAndColor(Double priceValue, String colorValue)
 *             = SELECT * FROM car WHERE price=:priceValue AND color=:colorValue
 *
 * !!! Arguments have to be in the same order as params in method name
 */
@Component
public class MethodSignatureBasedQueryResolver implements QueryResolver {

    private static Logger log = Logger.getLogger(MethodSignatureBasedQueryResolver.class.getName());

    private static final String FIND_BY = "findBy";

    @Override
    public String resolveQuery(Method method, Object[] args) {
        String query = "SOME SQL QUERY";
        if (method.getName().startsWith(FIND_BY))
            query = buildQuery(method, args);
        return query;
    }

    private String buildQuery(Method method, Object[] args) {
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
        String className = method.getGenericReturnType().getTypeName();
        return Class.forName(className);
    }

    /**
     * Builds after-WHERE part of SQL query, extracting
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
                        .append(queryParams[i] + "=")
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
