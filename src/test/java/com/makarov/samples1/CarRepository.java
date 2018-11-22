package com.makarov.samples1;

import com.makarov.annotation.Repository;

import java.util.Optional;

@Repository
public interface CarRepository {

    Optional<Car> findById(Integer id);

    Optional<Car> findByModelAndPrice(String model, Double price);

    Optional<Car> findByModelOrPrice(String model, Double price);

    Optional<Car> findByIdOrModelAndPrice(Integer id, String model, Double price);

}
