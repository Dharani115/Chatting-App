package com.android.chattingapp.Notification;

public class Data {

    private String body;
    private String title;

    public Data(String body, String title ) {

        this.body = body;
        this.title = title;
    }

    public Data() {
    }


    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


}
