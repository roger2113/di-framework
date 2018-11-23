package com.makarov.persistence.repository;

import com.makarov.persistence.query.QueryResolver;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public class DefaultCRUDRepository<K, T> implements CRUDRepository<K, T> {

    private static Logger log = Logger.getLogger(DefaultCRUDRepository.class.getName());

    private DynamicQueryRepository dynamicQueryRepository;

    public DefaultCRUDRepository() {
        this.dynamicQueryRepository = new DefaultDynamicQueryRepository();
    }

    public DefaultCRUDRepository(DynamicQueryRepository dynamicQueryRepository) {
        this.dynamicQueryRepository = dynamicQueryRepository;
    }

    public DefaultCRUDRepository(QueryResolver queryResolver) {
        this.dynamicQueryRepository = new DefaultDynamicQueryRepository(queryResolver);
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
        return dynamicQueryRepository.execute(method, args);
    }
}
