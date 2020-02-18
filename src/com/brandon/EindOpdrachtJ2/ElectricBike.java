package com.brandon.EindOpdrachtJ2;

/*
 * Brandon Abbenhuis
 * brandon.abbenhuis@student.nhlstenden.com
 * Date: 28-11-2019
 */
public class ElectricBike extends Bike {
    public static Double Price;

    public ElectricBike() {
        super("Electric Bike");
    }

    @Override
    public Double getPricePerKilometer() {
        return Price;
    }
}
