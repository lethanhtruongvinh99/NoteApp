package com.example.testnoteapp;

public class Note {
    private String title, content, date, tag;
    public Note(String tit, String content, String date, String tag){
        this.title = tit;
        this.content = content;
        this.date = date;
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }

    public String getDate() {
        return date;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
