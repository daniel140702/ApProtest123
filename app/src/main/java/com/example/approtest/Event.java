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
    protected String date;
    protected ArrayList<Message> chat;
    protected LatLng place;

    protected String encodedImage;


    public Event (String name, String date, LatLng place){
        this.eventName = String.valueOf(name);
        this.date = String.valueOf(date);
        this.place = place;
        this.participants = new ArrayList<User>();
        this.chat = new ArrayList<Message>();
    }


    public String getEventName(){
        return eventName;
    }

    public LatLng getLatLang() {return place;}

    public void addUser(User user) {this.participants.add(user); }
}
