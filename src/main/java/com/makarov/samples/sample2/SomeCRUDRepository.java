package com.makarov.samples.sample2;

import com.makarov.core.annotation.Repository;
import com.makarov.persistence.repository.CRUDRepository;

@Repository
public interface SomeCRUDRepository extends CRUDRepository<Object, Object> {

    Object someMethod();

    Object findByPriceOrColor(Double price, String color);
}
