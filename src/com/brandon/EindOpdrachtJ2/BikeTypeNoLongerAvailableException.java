package com.brandon.EindOpdrachtJ2;

import java.util.HashMap;

public class BikeTypeNoLongerAvailableException extends Exception {
    private HashMap<BikeType, Integer> AvailableBikes;
    public BikeTypeNoLongerAvailableException(HashMap<BikeType, Integer> availBikes){
        super("There are no more bikes of this type available.");
        AvailableBikes = availBikes;
    }
    public HashMap<BikeType, Integer> getAvailableBikes() {
        return AvailableBikes;
    }
}
