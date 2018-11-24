package com.makarov.samples;

import com.makarov.core.context.ContextHandler;
import com.makarov.samples.sample1.SomeRepository;

public class Sample1 {

    public static void main(String[] args) {
        ContextHandler.invoke("com.makarov.samples.sample1");
        SomeRepository repository = ContextHandler.getBean(SomeRepository.class);
        repository.findById(45);
        repository.findByModelAndPrice("Juke", 4500.);
        repository.findByModelOrPrice("Jetta", 5530.41);
        repository.findByIdOrModelAndPrice(31, "Polo", 3450.);
    }
}
