package com.makarov.persistence;

import com.makarov.persistence.repository.DefaultDynamicQueryRepository;
import com.makarov.samples.applicationContextTestSamples.Car;
import com.makarov.samples.applicationContextTestSamples.CarCRUDRepository;
import com.makarov.samples.applicationContextTestSamples.CarCustomRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import java.lang.reflect.Method;

import static org.junit.Assert.assertNotNull;

@RunWith(BlockJUnit4ClassRunner.class)
public class MethodSignatureBasedQueryResolverTest {

//    /**
//     * getting proxy for {@link com.makarov.samples.applicationContextTestSamples.CarCustomRepository} and
//     * ensure {@link DefaultDynamicQueryRepository#execute(Method, Object[])}
//     * invoked and printed into console SQL query based on method signature
//     */
//    @Test
//    public void testCarCustomRepository() {
//        CarCustomRepository carRepository = context.getBean(CarCustomRepository.class);
//        assertNotNull(carRepository);
//        carRepository.findById(132);
//        //INFO: Executing SQL query: SELECT * FROM car WHERE id = 132;
//
//        carRepository.findByModelAndPrice("Logan", 5500.);
//        //INFO: Executing SQL query: SELECT * FROM car WHERE model = 'Logan' AND price = 5500.0;
//
//        carRepository.findByModelOrPrice("Golf", 8450.34);
//        //INFO: Executing SQL query: SELECT * FROM car WHERE model = 'Golf' OR price = 8450.34;
//
//        carRepository.findByIdOrModelAndPrice(1, "Tuatara", 2300.99);
//        //INFO: Executing SQL query: SELECT * FROM car WHERE id = 1 OR model = 'Tuatara' AND price = 2300.99;
//    }
//
//    /**
//     * getting proxy for {@link com.makarov.samples.applicationContextTestSamples.CarCRUDRepository} and
//     * ensure {@link com.makarov.persistence.repository.DefaultCRUDRepository}
//     * methods invoked and logged
//     */
//    @Test
//    public void testCarCRUDRepository() {
//        CarCRUDRepository carRepository = context.getBean(CarCRUDRepository.class);
//        assertNotNull(carRepository);
//        carRepository.findOne(3);
//        carRepository.findAll();
//        carRepository.save(new Car(3, "Juke", 4500.));
//        carRepository.delete(3);
//    }

}
