package com.makarov.samples.applicationContextTestSamples;

public class Car {

    private Integer id;
    private String model;
    private Double price;

    public Car() {
    }

    public Car(Integer id, String model, Double price) {
        this.id = id;
        this.model = model;
        this.price = price;
    }

    @Override
    public String toString() {
        return "Car{" +
                "id=" + id +
                ", model='" + model + '\'' +
                ", price=" + price +
                '}';
    }
}
