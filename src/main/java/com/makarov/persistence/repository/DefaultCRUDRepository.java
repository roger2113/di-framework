package com.makarov.persistence.repository;

import com.makarov.persistence.query.QueryResolver;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public class DefaultCRUDRepository<K, T> implements CRUDRepository<K, T> {

    private static Logger log = Logger.getLogger(DefaultCRUDRepository.class.getName());

    private DynamicMethodRepository dynamicMethodRepository;

    public DefaultCRUDRepository() {
        this.dynamicMethodRepository = new DefaultDynamicMethodRepository();
    }

    public DefaultCRUDRepository(DynamicMethodRepository dynamicMethodRepository) {
        this.dynamicMethodRepository = dynamicMethodRepository;
    }

    public DefaultCRUDRepository(QueryResolver queryResolver) {
        this.dynamicMethodRepository = new DefaultDynamicMethodRepository(queryResolver);
    }

    @Override
    public Optional<T> findOne(K id) {
        log.info("Executing DefaultCRUDRepository.findOne(" + id + ")");
        return Optional.empty();
    }

    @Override
    public List<T> findAll() {
        log.info("Executing DefaultCRUDRepository.findAll()");
        return new ArrayList<>();
    }

    @Override
    public T save(T entity) {
        log.info("Executing DefaultCRUDRepository.save(entity)"
                + "method, where entity=" + entity);
        return entity;
    }

    @Override
    public void delete(K id) {
        log.info("Executing DefaultCRUDRepository.delete(" + id + ")");
    }

    @Override
    public Object execute(Method method, Object[] args) {
        return dynamicMethodRepository.execute(method, args);
    }
}
