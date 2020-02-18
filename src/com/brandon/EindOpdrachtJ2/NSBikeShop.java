package com.brandon.EindOpdrachtJ2;
/*
 * Brandon Abbenhuis
 * brandon.abbenhuis@student.nhlstenden.com
 * Date: 28-11-2019
 */

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.HashSet;

public class NSBikeShop {

    private Double PricePerHour;

    private HashSet<Bike> Bikes = new HashSet<>();
    private HashMap<String, BikeRentalObject> LendBikes = new HashMap<>(); //BikeID, CustomerName

    public NSBikeShop(){
        ElectricBike.Price = 0.50;
        MountainBike.Price = 0.25;
        NormalBike.Price = 0.20;
        setPricePerHour(2.00);
    }

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

    public HashMap<BikeType, Integer> getAvailableNumberOfBikesPerType(){

        int mCount = getTotalNumberOfBikesPerType().get(BikeType.MountainBike);
        int nCount = getTotalNumberOfBikesPerType().get(BikeType.NormalBike);
        int eCount = getTotalNumberOfBikesPerType().get(BikeType.ElectricBike);

        HashMap<BikeType, Integer> map = new HashMap<>();

        for(Bike entry : Bikes){
            if(LendBikes.containsKey(entry.getUniqueID()))
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

    public HashSet<Bike> getAvailableBikes(){
        HashSet<Bike> availBikes = new HashSet<>();
        for(Bike bike: Bikes){
            if(!LendBikes.containsKey(bike.getUniqueID())){
                availBikes.add(bike);
            }
        }
        return availBikes;
    }

    public void printTotalBikesCount(){
        int mCount = getTotalNumberOfBikesPerType().get(BikeType.MountainBike);
        int nCount = getTotalNumberOfBikesPerType().get(BikeType.NormalBike);
        int eCount = getTotalNumberOfBikesPerType().get(BikeType.ElectricBike);

        System.out.println("Total Mountain bikes: " + mCount);
        System.out.println("Total Normal bikes: " + nCount);
        System.out.println("Total Electric bikes: " + eCount);
    }

    public void printAvailableBikesCount(){
        int mCount = getAvailableNumberOfBikesPerType().get(BikeType.MountainBike);
        int nCount = getAvailableNumberOfBikesPerType().get(BikeType.NormalBike);
        int eCount = getAvailableNumberOfBikesPerType().get(BikeType.ElectricBike);

        System.out.println("Available Mountain bikes: " + mCount);
        System.out.println("Available Normal bikes: " + nCount);
        System.out.println("Available Electric bikes: " + eCount);
    }

    public HashSet<Bike> getAllBikes(){
        return Bikes;
    }

    public Bike getBike(String id){
        return Bikes.stream().filter(bike -> bike.getUniqueID() == id).findAny().orElse(null);
    }

    public String lendBike(BikeType type, String customerName, int Hours) throws BikeTypeNoLongerAvailableException, Exception{
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
        //first check if this BikeType has available bikes
        if(getAvailableNumberOfBikesPerType().get(type) > 0){
            //get any available bike.
            for(Bike bike: getAvailableBikes()){
                if(bikeClass.isInstance(bike)){
                    //this is the first available bike
                    BikeRentalObject rentalObj = new BikeRentalObject();
                    rentalObj.CustomerName = customerName;
                    rentalObj.Hours = Hours;
                    LendBikes.put(bike.getUniqueID(), rentalObj);
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

    public void returnBike(String bikeId, double travelDistance) throws Exception{
        //check if the bike is rented out.
        if(LendBikes.containsKey(bikeId)){
            BikeRentalObject rObject = LendBikes.get(bikeId);
            Bike rBike = getBike(bikeId);
            double priceToPay = calculateTotalPrice(rBike, rObject, travelDistance);
            DecimalFormat df = new DecimalFormat("###.##");
            System.out.println("Successfully returned bike " + bikeId);
            System.out.println("                           Subtotal: €" + df.format(priceToPay));
            rBike.setTraveledDistance(rBike.getTraveledDistance() + travelDistance);
            LendBikes.remove(bikeId);
        }
        else{
            throw new Exception("This bike is not rented out at the moment.");
        }
    }

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

    public void removeBike(String id){
        Bikes.remove(getBike(id));
    }

    public Double calculateTotalPrice(Bike bike, BikeRentalObject rentalObject, double distance){
        double factor = bike.getPricePerKilometer();
        double totalPrice = (factor * (distance / 1000)) + (getPricePerHour() * rentalObject.Hours);

        return totalPrice;
    }

    public double getPricePerHour() {
        return PricePerHour;
    }

    public void setPricePerHour(Double pricePerHour) {
        PricePerHour = pricePerHour;
    }

}
