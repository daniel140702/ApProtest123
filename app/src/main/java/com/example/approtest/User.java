package com.example.approtest;

import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseUser;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class User{
    private String fullName;
    private String token;
    private String email;
    private HashMap<String, Event> userEvents;

    private boolean admin;
    public User(String fullName, String token,  String email , HashMap<String,Event> participated_events) {
        this.fullName = fullName;
        this.token = token;
        this.userEvents = new HashMap<String, Event>();
        for (Map.Entry<String, Event> entry : participated_events.entrySet()) {
            this.userEvents.put(entry.getKey(), entry.getValue());
        }
        this.email=email;
        this.admin = false;
    }

    public User(User other){
        this(other.getFullName(), other.getToken(), other.getEmail(), other.getUserEvents());
    }

    public User(){
        this.fullName = null;
        this.token = null;
        this.userEvents = new HashMap<String, Event>();
        this.email = null;
        this.admin = false;
    }

    public String getToken(){
        return token;
    }

    public String getFullName() {return fullName;}

    public HashMap<String, Event> getUserEvents() {
        HashMap<String, Event> events = new HashMap<String, Event>();
        for (Map.Entry<String, Event> entry : userEvents.entrySet()) {
           events.put(entry.getKey(), entry.getValue());
        }
        return events;
    }
    public String getEmail(){return email;}

    public boolean getAdmin(){return admin;}

    public void addEvent(Event event)
    {
        userEvents.put(event.getEventName(),event);
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof User){
            User usr = (User) obj;
            return this.token.equals(usr.token);
        }
        return false;
    }
}
