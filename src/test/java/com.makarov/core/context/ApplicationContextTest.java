package com.makarov.core.context;

import com.makarov.samples.applicationContextTestSamples.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import java.io.PrintStream;
import java.lang.reflect.Method;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertNotNull;

@RunWith(BlockJUnit4ClassRunner.class)
public class ApplicationContextTest {

    private ApplicationContext context;
    private PrintStream out;

    @Before
    public void init() {
        context = new ApplicationContext();
        context.invoke("com.makarov.samples.applicationContextTestSamples");
        out = System.out;
    }

    /**
     * Ensure proxy for given bean created,
     * and {@link com.makarov.core.proxy.ComponentInvocationHandler#invoke(Object, Method, Object[])}
     * applied "Proxy passed;" string to method invocation result
     * <p>
     * JDK proxy not allows to create other than "...$//dProxy" objects,
     * and I couldn't autowire it to object property without byte-code transformation
     * so we can check only bean proxying, but not its properties
     */
    @Test
    public void proxyCreatedForRequiredBean_AndInvocationHandlerInvoked() {
        CarCommonService carServiceProxy = context.getBeanProxy(CarCustomService.class);
        assertNotNull("Proxy has not been created", carServiceProxy);
        assertTrue("Proxy invocation not passed", carServiceProxy.getResult().startsWith("Proxy passed;"));
    }

    /**
     * create CarCustomService with Autowired ManagerCommonService dependency
     * ensure dependency method call applied string "ManagerCustomService processed;"
     * in {@link CarCustomService#getResult()} method
     */
    @Test
    public void beanDependencyAutowired_AndDependencyMethodInvoked() {
        CarCustomService carService = context.getBean(CarCustomService.class);
        String result = carService.getResult();

        assertNotNull("Dependency has not been injected", carService);

        assertTrue("Dependency method not invoked",
                result.contains("CarCustomService processed;ManagerCustomService processed;"));
    }

    /**
     * getting proxy for {@link com.makarov.samples.applicationContextTestSamples.CarCustomRepository} and
     * ensure {@link com.makarov.persistence.repository.DefaultDynamicMethodRepository#execute(Method, Object[])}
     * invoked and printed into console SQL query based on method signature
     */
    @Test
    public void testCarCustomRepository() {
        CarCustomRepository carRepository = context.getBean(CarCustomRepository.class);
        assertNotNull(carRepository);
        carRepository.findById(132);
        //INFO: Executing SQL query: SELECT * FROM car WHERE id = 132;

        carRepository.findByModelAndPrice("Logan", 5500.);
        //INFO: Executing SQL query: SELECT * FROM car WHERE model = 'Logan' AND price = 5500.0;

        carRepository.findByModelOrPrice("Golf", 8450.34);
        //INFO: Executing SQL query: SELECT * FROM car WHERE model = 'Golf' OR price = 8450.34;

        carRepository.findByIdOrModelAndPrice(1, "Tuatara", 2300.99);
        //INFO: Executing SQL query: SELECT * FROM car WHERE id = 1 OR model = 'Tuatara' AND price = 2300.99;
    }

    /**
     * getting proxy for {@link com.makarov.samples.applicationContextTestSamples.CarCRUDRepository} and
     * ensure {@link com.makarov.persistence.repository.DefaultCRUDRepository}
     * methods invoked and logged
     */
    @Test
    public void testCarCRUDRepository() {
        CarCRUDRepository carRepository = context.getBean(CarCRUDRepository.class);
        assertNotNull(carRepository);
        carRepository.findOne(3);
        carRepository.findAll();
        carRepository.save(new Car(3, "Juke", 4500.));
        carRepository.delete(3);
    }

    private void assertConsoleLogContains(String output) {

    }
}
