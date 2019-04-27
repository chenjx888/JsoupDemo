package com.jx.jsoupdemo;

public class DetailBean {

    private String name;
    private String avatar;
    private String time;
    private String content;

    public DetailBean(String name, String avatar, String time, String content) {
        this.name = name;
        this.avatar = avatar;
        this.time = time;
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}