package com.makarov.core.context;

import com.makarov.core.annotation.Autowired;
import com.makarov.core.annotation.Repository;
import com.makarov.core.context.loader.SimpleContextClassLoader;
import com.makarov.core.proxy.CRUDRepositoryInvocationHandler;
import com.makarov.core.proxy.ProxyFactory;
import com.makarov.exception.BeanNotFoundException;
import com.makarov.exception.ContextInvocationException;
import com.makarov.persistence.query.MethodSignatureBasedQueryResolver;
import com.makarov.persistence.repository.DefaultCRUDRepository;
import com.makarov.persistence.repository.DefaultDynamicQueryRepository;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

import static com.makarov.core.annotation.AnnotationUtil.getBeanAnnotations;
import static com.makarov.core.proxy.ProxyFactory.getRepositoryProxy;
import static java.util.stream.Collectors.toList;

/**
 * Core class to represent application context;
 * all beans contained in Map <beanName<String>, bean<Object>>
 *
 * for interfaces annotated with {@link Repository}
 * bean map returns proxy-object with {@link CRUDRepositoryInvocationHandler}
 *
 * In contrast to CGlib, JDK proxy implementation does not allow autowire proxy-object into bean property,
 * so real bean instances are autowired as beans dependencies,
 * and both getBean and getProxyBean implemented
 */
public class ApplicationContext implements Context {

    private static Logger log;
    private static final Class[] beansAnnotationTypes;
    private String[] packages;

    static {
        log = Logger.getLogger(ApplicationContext.class.getName());
        beansAnnotationTypes = getBeanAnnotations();
    }

    private Map<String, Object> beans;

    /**
     * package visible constructor to make instantiation available only with {@link ContextHandler}
     */
    ApplicationContext(String... packages) {
        this.beans = new HashMap<>();
        this.packages = packages;
        registerDefaultBeans();
        registerAnnotatedBeans();
        resolveBeansDependencies();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> clazz) {
        T bean = (T) beans.get(getBeanName(clazz.getSimpleName()));
        if (bean == null) {
            throw new BeanNotFoundException("Bean not found for class: " + clazz.getName());
        }
        return bean;
    }

    /**
     * Because of JDK proxy constraints method returns bean proxy,
     * but not its autowired bean dependencies are not proxy objects
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> T getBeanProxy(Class<T> clazz) {
        T bean = (T) beans.get(getBeanName(clazz.getSimpleName()));
        if (bean == null) {
            throw new BeanNotFoundException("Bean not found for class: " + clazz.getName());
        }
        if (clazz.isAnnotationPresent(Repository.class)) {
            return (T) beans.get(getBeanName(clazz.getSimpleName()));
        }
        return ProxyFactory.getComponentProxy(clazz, bean);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getBean(String name) {
        T bean = (T) beans.get(name);
        if (bean == null) {
            throw new BeanNotFoundException("Bean not found with name: " + name);
        }
        return bean;
    }

    private void registerAnnotatedBeans() {
        log.info("Registering annotated classes...");
        new SimpleContextClassLoader().loadJavaClasses(packages).forEach(clazz -> {
            if (beanAnnotated(clazz)) {

                if (!clazz.isInterface()) {
                    try {
                        beans.put(getBeanName(clazz.getSimpleName()), clazz.newInstance());
                    } catch (InstantiationException | IllegalAccessException e) {
                        log.log(Level.SEVERE, " Instantiation exception for: " + clazz.getName(), e);
                    }

                } else if (clazz.isAnnotationPresent(Repository.class)) {
                    List<Object> repositoryImplementations = getInterfaceImplementations(clazz);
                    if (repositoryImplementations.isEmpty()) {
                        beans.put(getBeanName(clazz.getSimpleName()), getRepositoryProxy(clazz, null));
                    } else {
                        repositoryImplementations.forEach(implementation ->
                                beans.put(getBeanName(clazz.getSimpleName()), getRepositoryProxy(clazz, implementation)));
                    }
                }
            }
        });

        log.info("Beans registered: " + beans.keySet());
    }

    private void resolveBeansDependencies() {
        for (Object bean : beans.values()) {
            for (Field field : bean.getClass().getDeclaredFields()) {
                if (field.isAnnotationPresent(Autowired.class)) {
                    Class clazz = field.getType();
                    Object dependency = resolveBeanDependency(clazz);
                    if (dependency == null) {
                        throw new BeanNotFoundException(String.format("Cannot resolve dependency (bean not found) '%s' for bean '%s'",
                                clazz.getName(), bean.getClass().getName()));
                    }
                    field.setAccessible(true);
                    try {
                        field.set(bean, dependency);
                        log.info(String.format("Dependency for bean '%s' injected: %s",
                                bean.getClass().getName(), clazz.getName()));
                    } catch (IllegalAccessException e) {
                        throw new ContextInvocationException(String.format("Cannot set dependency '%s' for bean '%s'",
                                clazz.getName(), bean.getClass().getName()));
                    }
                }
            }
        }
    }

    private Object resolveBeanDependency(Class<?> clazz) {
        if (clazz.isInterface()) {
            List<Object> dependencyCandidates = getInterfaceImplementations(clazz);
            if (!dependencyCandidates.isEmpty()) {
                if (!(dependencyCandidates.size() > 1)) {
                    return dependencyCandidates.get(0);
                } else {
                    throw new BeanNotFoundException(String.format("Ambiguous (%d) interface implementations: %s",
                            dependencyCandidates.size(), clazz.getName()));
                }
            } else {
                throw new BeanNotFoundException(String.format("Cannot find find interface implementation: %s", clazz.getName()));
            }

        } else {
            return beans.get(getBeanName(clazz.getSimpleName()));
        }
    }

    /**
     * Hardcoded now, but can be conveniently resolved by some kind of @ConditionalOnMissingBean annotation
     */
    private void registerDefaultBeans() {
        Class[] defaultBeans = new Class[]{
                DefaultDynamicQueryRepository.class,
                DefaultCRUDRepository.class,
                MethodSignatureBasedQueryResolver.class
        };
        Arrays.stream(defaultBeans).forEach(
                bean -> {
                    try {
                        beans.put(getBeanName(bean.getSimpleName()), bean.newInstance());
                    } catch (InstantiationException | IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
        );
    }

    private List<Object> getInterfaceImplementations(Class<?> clazz) {
        return beans.values().stream()
                .filter(bean -> clazz.isAssignableFrom(bean.getClass()))
                .collect(toList());
    }

    private String getBeanName(String className) {
        return className.substring(0, 1).toLowerCase() + className.substring(1);
    }

    private boolean beanAnnotated(Class<?> clazz) {
        return Stream.of(beansAnnotationTypes).anyMatch(annotation -> clazz.isAnnotationPresent(annotation));
    }

}
