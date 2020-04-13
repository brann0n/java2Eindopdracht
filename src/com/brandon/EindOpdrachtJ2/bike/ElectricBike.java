package com.brandon.EindOpdrachtJ2.bike;

import com.brandon.EindOpdrachtJ2.bike.Bike;

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
