package com.example.approtest;

import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseUser;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class User{
    private String fullName;
    private String token;
    private String email;
    private ArrayList<String> userEvents;

    private boolean admin;
    public User(String fullName, String token,  String email , ArrayList<String> userEvents) {
        this.fullName = fullName;
        this.token = token;
        this.userEvents = new ArrayList<String>();
        for (int i = 0;i < userEvents.size();i++) {
            this.userEvents.add(String.valueOf(userEvents.get(i)));
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
        this.userEvents = new ArrayList<String>();
        this.email = null;
        this.admin = false;
    }

    public String getToken(){
        return token;
    }

    public String getFullName() {return fullName;}

    public ArrayList<String> getUserEvents() {
        ArrayList<String> events = new ArrayList<String>();
        for (int i = 0;i < userEvents.size();i++) {
            this.userEvents.add(String.valueOf(userEvents.get(i)));
        }
        return events;
    }
    public String getEmail(){return email;}

    public boolean getAdmin(){return admin;}

    public void addEvent(Event event)
    {
        userEvents.add(String.valueOf(event.eventName));
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
