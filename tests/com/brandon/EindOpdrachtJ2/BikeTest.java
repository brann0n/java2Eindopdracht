package com.brandon.EindOpdrachtJ2;

import com.brandon.EindOpdrachtJ2.bike.ElectricBike;
import com.brandon.EindOpdrachtJ2.bike.MountainBike;
import com.brandon.EindOpdrachtJ2.bike.NormalBike;
import org.junit.jupiter.api.Test;
import java.util.Objects;
import static org.junit.jupiter.api.Assertions.*;

class BikeTest {
    private MountainBike mountainBike = new MountainBike();
    private ElectricBike electricBike = new ElectricBike();
    private NormalBike normalBike = new NormalBike();

    /**
     * Only function worthy of testing in Bike class because its unique
     */
    @Test
    void getUniqueID() {
        String id1 = mountainBike.getUniqueID();
        String id2 = electricBike.getUniqueID();
        String id3 = normalBike.getUniqueID();

        assertTrue(!Objects.equals(id1, id2) && !Objects.equals(id2, id3) && !Objects.equals(id3, id1));
    }

}