package com.makarov.samples.applicationContextTestSamples;

import com.makarov.core.annotation.Repository;
import com.makarov.persistence.repository.CRUDRepository;

@Repository
public interface CarCRUDRepository extends CRUDRepository<Integer, Car> {

}
