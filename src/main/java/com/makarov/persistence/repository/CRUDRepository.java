package com.makarov.persistence.repository;

import java.util.List;
import java.util.Optional;

public interface CRUDRepository<K, T>  extends DynamicQueryRepository {

    public Optional<T> findOne(K id);

    List<T> findAll();

    T save(T entity);

    void delete(K id);
}
