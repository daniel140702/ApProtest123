package com.example.approtest;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
//import com.google.


public class Event {
    protected String eventName;
    private ArrayList<User> participants;
    private Date date;
    private ArrayList<Message> chat;
    private LatLng place;


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
