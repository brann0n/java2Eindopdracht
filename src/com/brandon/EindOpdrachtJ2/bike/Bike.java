package com.brandon.EindOpdrachtJ2.bike;

import java.util.UUID;

/*
 * Brandon Abbenhuis
 * brandon.abbenhuis@student.nhlstenden.com
 * Date: 28-11-2019
 */
public abstract class Bike {

    private String Name;
    private double TraveledDistance;
    private String UniqueID;

    public Bike(String BikeName){
        setName(BikeName);
        UniqueID = UUID.randomUUID().toString();
    }

    /**
     * Gets this bikes unique id
     * @return a string containing the unique id
     */
    public String getUniqueID() {
        return UniqueID;
    }

    /**
     * Gets the price per kilometer set in the individual extended classes.
     * @return price as integer
     */
    public abstract Double getPricePerKilometer();

    /**
     * Get the bike name
     *
     * @return the bike name
     */
    public String getName() {
        return Name;
    }

    /**
     * Set the Bike name
     *
     * @param Name the name of the bike
     */
    public void setName(String Name) {
        this.Name = Name;
    }

    /**
     * Get the bikes traveled distance
     *
     * @return the distance as a float
     */
    public double getTraveledDistance() {
        return TraveledDistance;
    }

    /**
     * Set the bikes traveled distance
     * @param TraveledDistance the distance in meters
     */
    public void setTraveledDistance(double TraveledDistance) {
        this.TraveledDistance = TraveledDistance;
    }

}