package com.makarov.persistence;

import com.makarov.persistence.query.MethodSignatureBasedQueryResolver;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import static org.junit.Assert.assertEquals;

@RunWith(BlockJUnit4ClassRunner.class)
public class MethodSignatureBasedQueryResolverTest {

    private static MethodSignatureBasedQueryResolver queryResolver;
    private static SampleRepository repository;

    @BeforeClass
    public static void init() {
        queryResolver = new MethodSignatureBasedQueryResolver();
        repository = (SampleRepository)Proxy.newProxyInstance(
                SampleRepository.class.getClassLoader(),
                new Class[]{SampleRepository.class},
                new SampleInvocationHandler()
        );
    }

    @Test
    public void buildFrom_findById() {
        String query = repository.findById(3);
        assertEquals("SELECT * FROM string WHERE id=3;", query);
    }

    @Test
    public void buildFrom_findByModelAndPrice() {
        String query = repository.findByModelAndPrice("Logan", 5500.);
        assertEquals("SELECT * FROM string WHERE model='Logan' AND price=5500.0;", query);
    }

    @Test
    public void buildFrom_findByModelOrPrice() {
        String query = repository.findByModelOrPrice("Golf", 8450.34);
        assertEquals("SELECT * FROM string WHERE model='Golf' OR price=8450.34;", query);
    }

    @Test
    public void buildFrom_findByIdOrModelAndPrice() {
        String query = repository.findByIdOrModelAndPrice(1, "Tuatara", 2300.99);
        assertEquals("SELECT * FROM string WHERE id=1 OR model='Tuatara' AND price=2300.99;", query);
    }


    interface SampleRepository {
        String findById(Integer id);
        String findByModelAndPrice(String model, Double price);
        String findByModelOrPrice(String model, Double price);
        String findByIdOrModelAndPrice(Integer id, String model, Double price);
    }

    static class SampleInvocationHandler implements InvocationHandler {
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) {
            return queryResolver.resolveQuery(method, args);
        }
    }
}

