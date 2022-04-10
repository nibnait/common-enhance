package com.java.common.mock;

import lombok.Data;

import java.util.List;

/**
 * Created by nibnait on 2021/10/17
 */
@Data
public class Person {

    private String name;

    private Integer age;

    private List<Car> carList;

    @Data
    public static class Car {

        private String color;

        private String brand;
    }

}
