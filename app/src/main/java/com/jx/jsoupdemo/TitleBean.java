package com.jx.jsoupdemo;

public class TitleBean {

    private String name;
    private String author;
    private String time;
    private String replayCount;
    private String lookCount;
    private String plate;
    private String url;

    public TitleBean(String name, String author, String time, String replayCount, String lookCount, String plate, String url) {
        this.name = name;
        this.author = author;
        this.time = time;
        this.replayCount = replayCount;
        this.lookCount = lookCount;
        this.plate = plate;
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getReplayCount() {
        return replayCount;
    }

    public void setReplayCount(String replayCount) {
        this.replayCount = replayCount;
    }

    public String getLookCount() {
        return lookCount;
    }

    public void setLookCount(String lookCount) {
        this.lookCount = lookCount;
    }
}
