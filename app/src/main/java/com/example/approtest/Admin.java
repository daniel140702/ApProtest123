package com.example.approtest;

import java.util.HashMap;
import java.util.Map;

public class Admin extends User{
    private String fullName;
    private String id;
    private HashMap<String, Event> participated_events;


    public Admin(String fullName, String id, HashMap<String, Event> participated_events) {
        this.fullName = fullName;
        this.id = id;
        this.participated_events = new HashMap<String, Event>();
        for (Map.Entry<String, Event> entry : participated_events.entrySet()) {
            this.participated_events.put(entry.getKey(), entry.getValue());
        }
    }

    public Admin(){
        this.fullName = null;
        this.id = null;
        this.participated_events = new HashMap<String, Event>();
    }


    public void addEvent(String eventName, Event event) {// add event hashmap
        participated_events.put(eventName, event);
    }

    public void removeEvent(String eventName) { // remove event from hashmap
        participated_events.remove(eventName);
    }


}


