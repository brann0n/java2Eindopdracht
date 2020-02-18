package com.brandon.EindOpdrachtJ2;

public class EindOpdracht {

    /**
     * Main entry point of the program
     * @param args
     */
    public static void main(String[] args) throws Exception {
        //create a new NSBikesShop
        NSBikeShop shop = new NSBikeShop();
        printLine();

        //MountainBikes
        for(int i = 0; i < 20; i++){
           shop.addNewBike(BikeType.MountainBike);
        }
        //NormalBikes
        for(int i = 0; i < 30; i++){
            shop.addNewBike(BikeType.NormalBike);
        }
        //ElectricBikes
        for(int i = 0; i < 10; i++){
            shop.addNewBike(BikeType.ElectricBike);
        }

        //print the total bikes count, for checking
        shop.printTotalBikesCount();
        printLine();
        try{
            //try lending a few bikes
            String bikeID1 = shop.lendBike(BikeType.MountainBike, "Sjaak Visser", 5);
            String bikeID2 = shop.lendBike(BikeType.ElectricBike, "Lieke Visser", 10);
            printLine();

            //now return the bikes
            shop.returnBike(bikeID1, 1500);
            shop.returnBike(bikeID2, 10000);

            //now lend bikes again
            String bikeID3 = shop.lendBike(BikeType.ElectricBike, "Jan Smit", 9);
            shop.returnBike(bikeID3, 2000);

        }catch(BikeTypeNoLongerAvailableException exc){
            System.out.println("Error BikeTypeNoLongerAvailableException: " + exc.getAvailableBikes());
            //throw exc;
        }
        printLine();
        shop.printAvailableBikesCount();
        printLine();
        shop.getAllBikes().forEach(n -> System.out.println(n.getUniqueID() + ": " + n.getTraveledDistance()));
    }

    public static void print(String text){
        System.out.println(text);
    }

    public static void printLine(){
        System.out.println("______________________________________________________________");
    }
}
