package com.brandon.EindOpdrachtJ2;

import org.junit.jupiter.api.*;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class NSBikeShopTest {

    private NSBikeShop shop;
    private int mCount;
    private int nCount;
    private int eCount;

    private String lastBikeId;


    @BeforeEach //add 60 bikes
    void setUp() {
         shop = new NSBikeShop();
         mCount = 20;
         nCount = 30;
         eCount = 10;

        //MountainBikes
        for(int i = 0; i < mCount; i++){
            shop.addNewBike(BikeType.MountainBike);
        }
        //NormalBikes
        for(int i = 0; i < nCount; i++){
            shop.addNewBike(BikeType.NormalBike);
        }
        //ElectricBikes
        for(int i = 0; i < eCount; i++){
           shop.addNewBike(BikeType.ElectricBike);
        }
    }

    @Test
    void TestGetTotalNumberOfBikesPerType() {
        HashMap<BikeType, Integer> bikeCountMap = new HashMap<>();
        bikeCountMap.put(BikeType.ElectricBike, eCount);
        bikeCountMap.put(BikeType.NormalBike, nCount);
        bikeCountMap.put(BikeType.MountainBike, mCount);
        assertEquals(bikeCountMap, shop.getTotalNumberOfBikesPerType());
    }

    @Test
    void TestGetAvailableBikesPerType() throws Exception{
        //try lending a few bikes and check if the hashmap is correct
        String bikeId1 = shop.lendBike(BikeType.MountainBike, "Sjaak Visser", 5);
        String bikeId2 = shop.lendBike(BikeType.ElectricBike, "Lieke Visser", 10);

        HashMap<BikeType, Integer> bikeCountMap = new HashMap<>();
        bikeCountMap.put(BikeType.ElectricBike, eCount -1);
        bikeCountMap.put(BikeType.NormalBike, nCount);
        bikeCountMap.put(BikeType.MountainBike, mCount - 1);

        assertEquals(bikeCountMap, shop.getAvailableNumberOfBikesPerType());
    }

    @Test
    void TestGetCurrentAvailableBikes() throws Exception{
        //try lending a few bikes and then check if the count is true
        String bikeId1 = shop.lendBike(BikeType.MountainBike, "test1", 5);
        String bikeId2 = shop.lendBike(BikeType.ElectricBike, "test2", 10);
        String bikeId3 = shop.lendBike(BikeType.NormalBike, "test3", 9);
        int bikeCount =  shop.getAvailableBikes().size();
        //expect 3 less bikes than the total.
        assertEquals(57, bikeCount);
    }

    @Test
    @Order(2)
    void TestAddAndGetBikeById() {
        //this function should be tested because it adds a different class based on an enum
        String AddedBike = shop.addNewBike(BikeType.NormalBike);

        Bike testBike = shop.getBike(AddedBike);
        assertTrue(testBike instanceof NormalBike);
        assertEquals("Normal Bike", testBike.getName());
    }

    @Test
    @Order(1)
    void TestLendBike() throws Exception{
        //throws exception if there are no bikes
        shop.lendBike(BikeType.NormalBike, "John Doe", 0);

    }

    @Test
    @Order(3)
    void TestReturnBikeAfterLendingItOut() throws Exception{
        //will throw an error if the bike returned has not been rented out yet

        //first lend a bike, this will fail if test 1 failed.
        String lendBikeId = shop.lendBike(BikeType.MountainBike, "John Doe", 12);

        shop.returnBike(lendBikeId, 20);
    }

    @Test
    void TestCalculateTotalPrice() {
        //create new bike and a rental object
        MountainBike bike = new MountainBike();
        BikeRentalObject rObject = new BikeRentalObject();
        rObject.Hours = 5;
        rObject.CustomerName = "Frank";
        double price = shop.calculateTotalPrice(bike, rObject, 20000);

        assertEquals(15d, price);
    }

    @Test
    void TestGetTotalTravelDistancePerBike() throws Exception{
        String prevId = shop.lendBike(BikeType.ElectricBike, "John Doe", 5);
        //first lend out and return a few bikes
        for(int i = 0; i < 10; i++){
            shop.returnBike(prevId, 5000);

            String id = shop.lendBike(BikeType.ElectricBike, "John Doe", 5);
            if(prevId != id){
                throw new Exception("Not lending the same bike");
            }
        }

        double TotalDistance = shop.getBike(prevId).getTraveledDistance();

        //waarom 50.000?: 10x 5000 meter op de zelfde fiets
        assertEquals(50000d, TotalDistance);
    }
}