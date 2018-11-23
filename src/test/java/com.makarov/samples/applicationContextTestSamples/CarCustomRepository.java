package com.makarov.samples.applicationContextTestSamples;

import com.makarov.core.annotation.Repository;
import com.makarov.persistence.repository.CRUDRepository;

import java.util.Optional;

@Repository
public interface CarCustomRepository {

    Car findById(Integer id);

    Car findByModelAndPrice(String model, Double price);

    Car findByModelOrPrice(String model, Double price);

    Car findByIdOrModelAndPrice(Integer id, String model, Double price);

}
