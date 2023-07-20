package com.example.approtest;

import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseUser;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class User{
    private String fullName;
    private String token;
    private String image;
    private HashMap<String, Event> participated_events;


    public User(String fullName, String token, HashMap<String, Event> participated_events) {
        this.fullName = fullName;
        this.token = token;
        this.participated_events = new HashMap<String, Event>();
        for (Map.Entry<String, Event> entry : participated_events.entrySet()) {
            this.participated_events.put(entry.getKey(), entry.getValue());
        }
    }


    public User(User other){
        this(other.fullName, other.token, other.participated_events);
    }

    public User(){
        this.fullName = null;
        this.token = null;
        this.participated_events = new HashMap<String, Event>();
    }

    public String getToken(){
        return token;
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
