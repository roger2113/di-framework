package com.makarov.samples.sample1;

import com.makarov.core.context.ContextHandler;

public class Sample {

    public static void main(String[] args) {
        ContextHandler.invoke("com.makarov.samples.sample1");
        SomeRepository repository = ContextHandler.getBean(SomeRepository.class);
        repository.findById(45);
        repository.findByModelAndPrice("Juke", 4500.);
        repository.findByModelOrPrice("Jetta", 5530.41);
        repository.findByIdOrModelAndPrice(31, "Polo", 3450.);
    }
}
