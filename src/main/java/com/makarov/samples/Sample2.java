package com.makarov.samples;

import com.makarov.core.context.ContextHandler;
import com.makarov.samples.sample2.SomeCRUDRepository;

public class Sample2 {

    public static void main(String[] args) {
        ContextHandler.invoke("com.makarov.samples.sample2");
        SomeCRUDRepository repository = (SomeCRUDRepository) ContextHandler.getBean("someCRUDRepository");
        repository.save(new Car(114));
        repository.findOne(8);
        repository.findAll();
        repository.delete(312);
    }

    private static class Car {

        Car(Integer id) {
            this.id = id;
        }

        Integer id;

        @Override
        public String toString() {
            return "Car{" +
                    "id=" + id +
                    '}';
        }
    }
}
