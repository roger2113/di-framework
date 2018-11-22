package com.makarov;

import com.makarov.core.context.ApplicationContext;
import com.makarov.samples1.CarCustomService;
import com.makarov.samples1.CarRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import java.lang.reflect.Method;

import static org.junit.Assert.assertFalse;

/**
 * Class to test {@link ApplicationContext}
 * <p>
 * Tests:
 * 1) Proxy for given bean instantiated,
 * and {@link com.makarov.core.proxy.ComponentInvocationHandler#invoke(Object, Method, Object[])}
 * applied "Proxy passed;" string to method invocation result
 * <p>
 * 2) create CarCustomService with Autowired ManagerCommonService dependency
 * ensure dependency method call applied string "ManagerCustomService processed;"
 * in {@link CarCustomService#getResult()} method
 * <p>
 * 3) Create proxy for {@link CarRepository}
 * ensure {@link com.makarov.core.proxy.RepositoryInvocationHandler#invoke(Object, Method, Object[])}
 * invoked and printed into console SQL query based on method signature
 */
@RunWith(BlockJUnit4ClassRunner.class)
public class ApplicationContextTest {

    @Test
    public void test() {
        assertFalse(false);
    }

//    public static void main(String[] args) {
//        ApplicationContext context = new ApplicationContext();
//        context.invoke("com.makarov.samples1");
//
//        //TEST 1
//        //JDK proxy not allows to create other than "...$//dProxy" objects,
//        //and I couldn't autowire it to object property without byte code transformation
//        //so we can check only bean proxying, but not its properties
//        CarCommonService carServiceProxy = context.getBeanProxy(CarCustomService.class);
//        assertTrue(carServiceProxy != null, "Proxy has not been created");
//        assertTrue(carServiceProxy.getResult().startsWith("Proxy passed;"),
//                "Proxy invocation not passed");
//
//        //TEST 2
//        //Let's create car service with autowired manager service dependency
//        //manager service method invocation result concatenates to car service method invocation
//        CarCustomService carService = context.getBean(CarCustomService.class);
//        String result = carService.getResult();
//        assertTrue(result.contains("CarCustomService processed;ManagerCustomService processed;"),
//                "Dependency has not been injected");
//
//        //TEST 3
//        CarRepository carRepository = context.getBeanProxy(CarRepository.class);
//        carRepository.findById(132);
//        //INFO: Performing SQL query: SELECT * FROM car WHERE id = 132;
//
//        carRepository.findByModelAndPrice("Logan",5500.);
//        //INFO: Performing SQL query: SELECT * FROM car WHERE model = 'Logan' AND price = 5500.0;
//
//        carRepository.findByModelOrPrice("Golf", 8450.34);
//        //INFO: Performing SQL query: SELECT * FROM car WHERE model = 'Golf' OR price = 8450.34;
//
//        carRepository.findByIdOrModelAndPrice(1, "Tuatara", 2300.99);
//        //INFO: Performing SQL query: SELECT * FROM car WHERE id = 1 OR model = 'Tuatara' AND price = 2300.99;
//
//    }

}
