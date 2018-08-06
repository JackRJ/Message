package com.example;

/**
 * Created by 帅郑 on 2018/6/2.
 */

public class Title {
    private String title;
    private String date;
    private String author_name;
    private String imageUrl;
    private String uri;

    public Title(String title,String author_name, String imageUrl, String uri, String date){
        this.title = title;
        this.imageUrl = imageUrl;
        this.author_name = author_name;
        this.date = date;
        this.uri = uri;
    }

    public String getdate()
    {
        return date;
    }

    public String getTitle() {
        return title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getDescr() {
        return author_name;
    }

    public String getUri() {
        return uri;
    }
}
