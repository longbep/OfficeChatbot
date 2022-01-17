package com.example.officechatbot.Model;

public class Message {
    private String text;
    private boolean isBot;
    private String messageOf;
    private String date;
    private String time;

    public Message() {

    }

    public Message(String text, boolean isBot, String messageOf,String date , String time) {
        this.text = text;
        this.isBot = isBot;
        this.messageOf = messageOf;
        this.date = date;
        this.time = time;
    }

    public String getMessageOf() {
        return messageOf;
    }

    public void setMessageOf(String messageOf) {
        this.messageOf = messageOf;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isBot() {
        return isBot;
    }

    public void setBot(boolean bot) {
        isBot = bot;
    }

    public String getDate() {return date; }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {return time; }

    public void setTime(String time) {
        this.time = time;
    }
}
