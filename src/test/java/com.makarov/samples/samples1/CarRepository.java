package com.makarov.samples.samples1;

import com.makarov.core.annotation.Repository;
import com.makarov.persistence.repository.CRUDRepository;

import java.util.Optional;

@Repository
public interface CarRepository extends CRUDRepository<Integer, Car> {

    Optional<Car> findOne(Integer id);

    Optional<Car> findByModelAndPrice(String model, Double price);

    Optional<Car> findByModelOrPrice(String model, Double price);

    Optional<Car> findByIdOrModelAndPrice(Integer id, String model, Double price);

}
