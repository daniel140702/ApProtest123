package com.example.approtest;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
//import com.google.


public class Event implements Serializable {
    protected String eventName;
    protected ArrayList<User> participants;
    protected String date;
    protected ArrayList<Message> chat;

    protected double latitude;

    protected double longitude;

    protected String encodedImage;



    public Event (String eventName, String date, double latitude, double longitude, ArrayList<User> participants, ArrayList<Message> messages){
        this(eventName,date, latitude, longitude);
        for(int i=0 ;i  < participants.size();i++)
        {
           this.participants.add(participants.get(i)) ;
        }
        for(int i=0 ;i  < chat.size();i++)
        {
            this.chat.add(chat.get(i));
        }

    }

    public Event (String eventName, String date, double latitude, double longitude){
        this.eventName = String.valueOf(eventName);
        this.date = String.valueOf(date);
        this.latitude = latitude;
        this.longitude = longitude;
        this.participants = new ArrayList<User>();
        this.chat = new ArrayList<Message>();
    }


    public Event(Event event)
    {
        this.eventName = event.getEventName();
        this.date = event.getDate();
        this.latitude = event.getLatitude();;
        this.longitude = event.getLongitude();
        this.participants = event.getParticipants();
        this.chat = event.getChat();
    }
    public Event()
    {
        this.participants = new ArrayList<User>();
        this.chat = new ArrayList<Message>();
    }

    public String getEventName(){
        return eventName;
    }

    public double getLatitude(){return latitude;}

    public double getLongitude(){return longitude;}

    public String getDate() {
        return date;
    }

    public ArrayList<User> getParticipants() {
        ArrayList<User> users = new ArrayList<User>();
        for(int i = 0; i < participants.size();i++)
        {
            users.add(new User(participants.get(i)));
        }
        return users;
    }

    public ArrayList<Message> getChat() {
        ArrayList<Message> chat = new ArrayList<Message>();
        for(int i = 0; i < this.chat.size();i++)
        {
            chat.add(new Message(this.chat.get(i)));
        }
        return chat;
    }

    public void addUser(User user) {this.participants.add(new User(user)); }
}
