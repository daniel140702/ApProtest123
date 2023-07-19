package com.example.approtest;

import com.google.android.gms.maps.model.LatLng;

public class Place {
    private LatLng placement;
    private String name;
    private String description;


    public Place (LatLng placement, String name, String description){
        this.placement = placement;
        this.description = description;
        this.name = name;
    }

    public Place (Place place){
        this(place.placement, place.name, place.description);
    }
}
