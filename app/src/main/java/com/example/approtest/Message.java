package com.example.approtest;

public class Message {
    private static int sync;
    private final int sync_index = ++sync;
    private String content;

    public Message (String content){
        this.content = content;
    }

    public Message (Message msg){
        this(msg.content);
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getSync() {
        return sync;
    }
}
