package com.ysmstudio.doittomorrow;

public class AppInfoData {

    private String title, content;

    public AppInfoData(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public AppInfoData() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}