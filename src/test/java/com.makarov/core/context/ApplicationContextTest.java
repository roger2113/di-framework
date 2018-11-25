package com.makarov.core.context;

import com.makarov.common.exception.BeanNotFoundException;
import com.makarov.core.proxy.jdk.ComponentInvocationHandler;
import com.makarov.samples.applicationContextTestSamples.*;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import java.lang.reflect.Method;

import static com.makarov.core.context.ContextHandler.getBean;
import static com.makarov.core.context.ContextHandler.getBeanProxy;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(BlockJUnit4ClassRunner.class)
public class ApplicationContextTest {

    @BeforeClass
    public static void init() {
        ContextHandler.invoke("com.makarov.samples.applicationContextTestSamples");
    }

    @Test
    public void bean_WithComponentAnnotation_Registered() {
        CarCommonService carService = getBean(CarCustomService.class);
        assertNotNull("Required bean has not been created", carService);
    }

    @Test(expected = BeanNotFoundException.class)
    public void class_WithoutAnyAnnotation_NotRegisteredAsBean() {
        getBean(NotBean.class);
    }

    /**
     * Ensure proxy for given bean created,
     * and {@link ComponentInvocationHandler#invoke(Object, Method, Object[])}
     * applied "Proxy passed;" string to method invocation result
     *
     * JDK proxy not allows to create other than "...$//dProxy" objects,
     * and I couldn't autowire it to object property without byte-code transformation
     * so we can check only bean proxying, but not its properties
     */
    @Test
    public void proxyCreatedForRequiredBean_AndInvocationHandlerInvoked() {
        CarCommonService carServiceProxy = getBeanProxy(CarCustomService.class);
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
        CarCommonService carService = getBean(CarCustomService.class);
        String result = carService.getResult();

        assertNotNull("Dependency has not been injected", carService);

        assertTrue("Dependency method not invoked",
                result.contains("CarCustomService processed;ManagerCustomService processed;"));
    }

    @Test
    public void beanBothDependenciesAutowired() {
        CarCustomService carService = getBean(CarCustomService.class);
        assertNotNull("One of dependencies not injected", carService.getManagerAdditionalService());
        assertNotNull("One of dependencies not injected", carService.getManagerCustomService());
    }

    @Test
    public void proxyCreatedForRepositoryAnnotatedInterface() {
        CarCustomRepository carRepository = getBean(CarCustomRepository.class);
        assertNotNull(carRepository);
        assertTrue(carRepository instanceof CarCustomRepository);
        assertTrue(carRepository.getClass().getName().startsWith("com.sun.proxy"));
    }

    @Test
    public void proxyCreatedForRepositoryAnnotatedInterface_WhichExtendsCRUDRepository() {
        CarCRUDRepository carRepository = getBean(CarCRUDRepository.class);
        assertNotNull(carRepository);
        assertTrue(carRepository instanceof CarCRUDRepository);
        assertTrue(carRepository.getClass().getName().startsWith("com.sun.proxy"));
    }

}
