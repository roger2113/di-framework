package com.makarov.core.context;

import com.makarov.exception.BeanNotFoundException;
import com.makarov.persistence.repository.DefaultDynamicQueryRepository;
import com.makarov.samples.applicationContextTestSamples.*;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import java.lang.reflect.Method;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertNotNull;

@RunWith(BlockJUnit4ClassRunner.class)
public class ApplicationContextTest {

    private static ApplicationContext context;

    @BeforeClass
    public static void init() {
        context = new ApplicationContext();
        context.invoke("com.makarov.samples.applicationContextTestSamples");
    }

    @Test
    public void bean_WithComponentAnnotation_Registered() {
        CarCommonService carService = context.getBean(CarCustomService.class);
        assertNotNull("Required bean has not been created", carService);
    }

    @Test(expected = BeanNotFoundException.class)
    public void class_WithoutAnyAnnotation_NotRegisteredAsBean() {
        context.getBean(NotBean.class);
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
        assertTrue("Proxy invocation handler not invoked", carServiceProxy.getResult().startsWith("Proxy passed;"));
    }

    /**
     * getting CarCustomService with Autowired ManagerCommonService dependency
     * ensure dependency method call applied string "ManagerCustomService processed;"
     * in {@link CarCustomService#getResult()} method
     */
    @Test
    public void beanDependencyAutowired_AndDependencyMethodInvoked() {
        CarCommonService carService = context.getBean(CarCustomService.class);
        String result = carService.getResult();

        assertNotNull("Dependency has not been injected", carService);

        assertTrue("Dependency method not invoked",
                result.contains("CarCustomService processed;ManagerCustomService processed;"));
    }

    @Test
    public void beanBothDependenciesAutowired() {
        CarCustomService carService = context.getBean(CarCustomService.class);
        assertNotNull("One of dependencies not injected", carService.getManagerAdditionalService());
        assertNotNull("One of dependencies not injected", carService.getManagerCustomService());
    }

    /**
     * getting proxy for {@link com.makarov.samples.applicationContextTestSamples.CarCustomRepository} and
     * ensure {@link DefaultDynamicQueryRepository#execute(Method, Object[])}
     * invoked and printed into console SQL query based on method signature
     */
    @Test
    public void proxyCreatedForRepositoryAnnotatedInterface() {
        CarCustomRepository carRepository = context.getBean(CarCustomRepository.class);
        assertNotNull(carRepository);
        assertTrue(carRepository instanceof CarCustomRepository);
        assertTrue(carRepository.getClass().getName().startsWith("com.sun.proxy"));
    }

    /**
     * getting proxy for {@link com.makarov.samples.applicationContextTestSamples.CarCRUDRepository} and
     * ensure {@link com.makarov.persistence.repository.DefaultCRUDRepository}
     * methods invoked and logged
     */
    @Test
    public void proxyCreatedForRepositoryAnnotatedInterface_WhichExtendsCRUDRepository() {
        CarCRUDRepository carRepository = context.getBean(CarCRUDRepository.class);
        assertNotNull(carRepository);
        assertTrue(carRepository instanceof CarCRUDRepository);
        assertTrue(carRepository.getClass().getName().startsWith("com.sun.proxy"));
    }

}
