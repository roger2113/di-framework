package com.makarov.samples.sample1;

import com.makarov.core.annotation.Repository;

@Repository
public interface SomeRepository {
    String findById(Integer id);
    String findByModelAndPrice(String model, Double price);
    String findByModelOrPrice(String model, Double price);
    String findByIdOrModelAndPrice(Integer id, String model, Double price);
}
