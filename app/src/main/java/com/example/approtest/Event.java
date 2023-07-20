package com.example.approtest;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
//import com.google.


public class Event implements Serializable {
    protected String eventName;
    protected ArrayList<User> participants;
    protected Date date;
    protected ArrayList<Message> chat;
    protected LatLng place;

    protected String encodedImage;


    public Event (String name, Date date, LatLng place){
        this.eventName=new String();
        this.date = new Date(date.getTime());
        this.place = place;
        this.participants = new ArrayList<User>();
        this.chat = new ArrayList<Message>();
    }


    public String getEventName(){
        return eventName;
    }

}
