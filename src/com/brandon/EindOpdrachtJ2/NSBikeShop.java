package com.brandon.EindOpdrachtJ2;
/*
 * Brandon Abbenhuis
 * brandon.abbenhuis@student.nhlstenden.com
 * Date: 28-11-2019, edited on: 13-04-2020
 */

import com.brandon.EindOpdrachtJ2.bike.Bike;
import com.brandon.EindOpdrachtJ2.bike.ElectricBike;
import com.brandon.EindOpdrachtJ2.bike.MountainBike;
import com.brandon.EindOpdrachtJ2.bike.NormalBike;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.HashSet;

public class NSBikeShop {

    private Double PricePerHour;
    private HashSet<Bike> Bikes = new HashSet<>();
    private HashSet<Customer> Customers = new HashSet<>();

    public NSBikeShop(){
        //set the prices of each bike per kilometer
        ElectricBike.Price = 0.50;
        MountainBike.Price = 0.25;
        NormalBike.Price = 0.20;

        //set the price of the store per hour
        setPricePerHour(2.00);
    }

    /**
     * Get the price per hour for this shop
     * @return
     */
    public double getPricePerHour() {
        return PricePerHour;
    }

    /**
     * Set the price per hour for this shop
     * @param pricePerHour
     */
    public void setPricePerHour(Double pricePerHour) {
        PricePerHour = pricePerHour;
    }

    /**
     * Function that gets a count for each bike type in the shop
     * @return
     */
    public HashMap<BikeType, Integer> getTotalNumberOfBikesPerType(){
        int mCount = 0;
        int nCount = 0;
        int eCount = 0;
        HashMap<BikeType, Integer> map = new HashMap<>();
        for(Bike entry : Bikes){

                if(entry instanceof MountainBike){
                    mCount++;
                }

                if(entry instanceof ElectricBike)
                {
                    eCount++;
                }

                if(entry instanceof NormalBike)
                {
                    nCount++;
                }
        }
        map.put(BikeType.ElectricBike, eCount);
        map.put(BikeType.NormalBike, nCount);
        map.put(BikeType.MountainBike, mCount);
        return map;
    }

    /**
     * Gets the available amount of bikes per bike type in a hashmap form
     * @return
     */
    public HashMap<BikeType, Integer> getAvailableNumberOfBikesPerType(){

        int mCount = getTotalNumberOfBikesPerType().get(BikeType.MountainBike);
        int nCount = getTotalNumberOfBikesPerType().get(BikeType.NormalBike);
        int eCount = getTotalNumberOfBikesPerType().get(BikeType.ElectricBike);

        HashMap<BikeType, Integer> map = new HashMap<>();

        for(Bike entry : Bikes){
            if(isBikeRentedOut(entry.getUniqueID()))
            {
                if(entry instanceof MountainBike){
                    mCount--;
                }

                if(entry instanceof ElectricBike)
                {
                    eCount--;
                }

                if(entry instanceof NormalBike)
                {
                    nCount--;
                }
            }
        }

        map.put(BikeType.ElectricBike, eCount);
        map.put(BikeType.NormalBike, nCount);
        map.put(BikeType.MountainBike, mCount);

        return map;
    }

    /**
     * Gets a HashSet with all available bikes.
     * @return
     */
    public HashSet<Bike> getAvailableBikes(){
        HashSet<Bike> availBikes = new HashSet<>();
        for(Bike bike: Bikes){
            if(!isBikeRentedOut(bike.getUniqueID())){
                availBikes.add(bike);
            }
        }
        return availBikes;
    }

    /**
     * Returns a HashSet of Bike objects
     * @return
     */
    public HashSet<Bike> getAllBikes(){
        return Bikes;
    }

    /**
     * Gets the bike using the bikes unique id
     * @param id unique id of the bike
     * @return Bike object
     */
    public Bike getBike(String id){
        return Bikes.stream().filter(bike -> bike.getUniqueID() == id).findAny().orElse(null);
    }

    /**
     * Takes the type of the bike and the customer name to lend a bike for a specified amount of hours.
     * @param type
     * @param customerName
     * @param Hours
     * @return
     * @throws Exception throws BikeTypeNoLongerAvailableException when there are no more available bikes
     */
    public String lendBike(BikeType type, String customerName, int Hours) throws Exception{
        if(Hours <= 0)
            throw new Exception("Cannot lend bike for 0 hours");

        Class bikeClass = null;
        switch(type){
            case MountainBike:
                bikeClass = MountainBike.class;
                break;
            case ElectricBike:
                bikeClass = ElectricBike.class;
                break;
            case NormalBike:
                bikeClass = NormalBike.class;
                break;
        }
        Customer currentCustomer = getCustomer(customerName);
        //first check if the customer already exists, even if there are no bikes available we want the customer in the system
        if(currentCustomer == null){
            Customers.add(new Customer(customerName));
            currentCustomer = getCustomer(customerName);
        }

        //check if this BikeType has available bikes
        if(getAvailableNumberOfBikesPerType().get(type) > 0){
            //get any available bike.
            for(Bike bike: getAvailableBikes()){
                if(bikeClass.isInstance(bike)){
                    //this is the first available bike
                    BikeRentalObject rentalObj = new BikeRentalObject();
                    rentalObj.BikeId = bike.getUniqueID();
                    rentalObj.Hours = Hours;
                    currentCustomer.addRentedBike(rentalObj);
                    System.out.println ("Lend bike " + bike.getUniqueID());
                    return bike.getUniqueID();
                }
            }
            throw new Exception("Could not lend a bike");
        }
        else{
            throw new BikeTypeNoLongerAvailableException(getAvailableNumberOfBikesPerType());
        }
    }

    /**
     * Return a bike by its unique Id and specified travel distance
     * @param bikeId
     * @param travelDistance
     * @throws Exception
     */
    public void returnBike(String bikeId, double travelDistance) throws Exception{
        //check if the bike is rented out.
        if(isBikeRentedOut(bikeId)){
            BikeRentalObject rObject = getActiveRentalObjectByBikeId(bikeId);
            rObject.Distance = travelDistance;
            Bike rBike = getBike(bikeId);
            double priceToPay = calculateTotalPrice(rObject);
            DecimalFormat df = new DecimalFormat("###.##");
            System.out.println("Successfully returned bike " + bikeId);
            System.out.println("                           Subtotal: €" + df.format(priceToPay));
            rBike.setTraveledDistance(rBike.getTraveledDistance() + travelDistance);
            getCurrentCustomerByBikeId(bikeId).returnBike(bikeId);
        }
        else{
            throw new Exception("This bike is not rented out at the moment.");
        }
    }

    /**
     * Boolean that checks if the bike specified is rented out at the moment
     * @param bikeId
     * @return
     */
    public Boolean isBikeRentedOut(String bikeId){
        //loop through each customer
        for(Customer customer: Customers){
            //loop through the rented bike list
            for(BikeRentalObject bike: customer.getRentedBikes()){
                if(bike.BikeId == bikeId){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Gets the active rental object by bike Id
     * @param bikeId
     * @return
     */
    public BikeRentalObject getActiveRentalObjectByBikeId(String bikeId){
        //loop through each customer
        for(Customer customer: Customers){
            //loop through the rented bike list
            for(BikeRentalObject bike: customer.getRentedBikes()){
                if(bike.BikeId == bikeId){
                    return bike;
                }
            }
        }

        //if code reaches here the customer doesnt exists and therefore the bike is not rented out.
        return null;
    }

    /**
     * Gets a Customer object by unique bike id, returns null if the bike is not rented out at the moment
     * @param bikeId
     * @return
     */
    public Customer getCurrentCustomerByBikeId(String bikeId){
        //loop through each customer
        for(Customer customer: Customers){
            //loop through the rented bike list
            for(BikeRentalObject bike: customer.getRentedBikes()){
                if(bike.BikeId == bikeId){
                    return customer;
                }
            }
        }

        //if code reaches here the customer doesnt exists and therefore the bike is not rented out.
        return null;
    }

    /**
     * Gets a Customer object by the customers name
     * @param name
     * @return
     */
    public Customer getCustomer(String name){
        return Customers.stream().filter(customer -> customer.getName() == name).findAny().orElse(null);
    }

    /**
     * Add a new bike to the store's inventory
     * @param type
     * @return
     */
    public String addNewBike(BikeType type){
        String bikeId = "";
        switch(type){
            case ElectricBike:
                ElectricBike bike1 = new ElectricBike();
                bikeId = bike1.getUniqueID();
                Bikes.add(bike1);
                break;
            case MountainBike:
                MountainBike bike2 = new MountainBike();
                bikeId = bike2.getUniqueID();
                Bikes.add(bike2);
                break;
            case NormalBike:
                NormalBike bike3 = new NormalBike();
                bikeId = bike3.getUniqueID();
                Bikes.add(bike3);
                break;
        }

        return bikeId;
    }

    /**
     * Remove a bike from the store's inventory
     * @param id
     */
    public void removeBike(String id){
        Bikes.remove(getBike(id));
    }

    /**
     * Calculate the price that the customer needs to pay
     * @param rentalObject object that contains the bikeId, the amount of hours that were driven on it and the distance
     * @return returns a double with the price to pay
     */
    public Double calculateTotalPrice(BikeRentalObject rentalObject){
        double factor = getBike(rentalObject.BikeId).getPricePerKilometer();
        return (factor * (rentalObject.Distance / 1000)) + (getPricePerHour() * rentalObject.Hours);
    }

    /**
     * Print a count of the total bikes
     */
    public void printTotalBikesCount(){
        int mCount = getTotalNumberOfBikesPerType().get(BikeType.MountainBike);
        int nCount = getTotalNumberOfBikesPerType().get(BikeType.NormalBike);
        int eCount = getTotalNumberOfBikesPerType().get(BikeType.ElectricBike);

        System.out.println("Total Mountain bikes: " + mCount);
        System.out.println("Total Normal bikes: " + nCount);
        System.out.println("Total Electric bikes: " + eCount);
    }

    /**
     * print a count of the total available bikes.
     */
    public void printAvailableBikesCount(){
        int mCount = getAvailableNumberOfBikesPerType().get(BikeType.MountainBike);
        int nCount = getAvailableNumberOfBikesPerType().get(BikeType.NormalBike);
        int eCount = getAvailableNumberOfBikesPerType().get(BikeType.ElectricBike);

        System.out.println("Available Mountain bikes: " + mCount);
        System.out.println("Available Normal bikes: " + nCount);
        System.out.println("Available Electric bikes: " + eCount);
    }

}
