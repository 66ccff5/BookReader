package com.example.administrator.bookreader;

/**
 * Created by Administrator on 2018/6/30 0030.
 */

public class Chapter {

    private String title;

    private String link;


    public Chapter(String t,String l){
        this.title = t;
        this.link = l;
    }

    public Chapter(){

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
