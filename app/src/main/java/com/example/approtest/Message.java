package com.example.approtest;

public class Message {

    private User user;
    private String content;

    public Message (String content, User user){
        this.user = user;
        this.content = content;
    }

    public Message()
    {

    }

    public Message (Message msg){
        this(msg.content, msg.user);
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getUser() {
        return new User(user);
    }


}
