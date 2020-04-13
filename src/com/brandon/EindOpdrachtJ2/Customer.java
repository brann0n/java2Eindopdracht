package com.brandon.EindOpdrachtJ2;
/*
 * Brandon Abbenhuis
 * brandon.abbenhuis@student.nhlstenden.com
 * Date: 13-04-2020
 */
import java.util.HashSet;

public class Customer {
    private String name;
    private HashSet<BikeRentalObject> rentedBikes;
    private HashSet<BikeRentalObject> returnedBikes;

    public Customer(String name){
        this.name = name;
        this.rentedBikes = new HashSet<>();
        this.returnedBikes = new HashSet<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HashSet<BikeRentalObject> getRentedBikes() {
        return rentedBikes;
    }

    public HashSet<BikeRentalObject> getReturnedBikes() {
        return returnedBikes;
    }

    public void addRentedBike(BikeRentalObject bikeRentalObject){
        rentedBikes.add(bikeRentalObject);
    }

    public void returnBike(String bikeId){
        for(BikeRentalObject rObject: rentedBikes){
            if(rObject.BikeId == bikeId){
                returnedBikes.add(rObject);
                rentedBikes.remove(rObject);
                return;
            }
        }
    }
}
