package com.example.administrator.bookreader;

import android.graphics.Bitmap;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by Administrator on 2018/6/28 0028.
 */

public class NetBook extends DataSupport{

    private String title;
    private int id;
    private String bookid;
    private String author;
    private String cover;
    private String shortIntr;
    private String lastChapter;
    private String type;
    private int chapterNum;
    private int pageNum;
    private int isAdd = 0;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBookid() {
        return bookid;
    }

    public void setBookid(String bookid) {
        this.bookid = bookid;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getShortIntr() {
        return shortIntr;
    }

    public void setShortIntr(String shortIntr) {
        this.shortIntr = shortIntr;
    }

    public String getLastChapter() {
        return lastChapter;
    }

    public void setLastChapter(String lastChapter) {
        this.lastChapter = lastChapter;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public int getChapterNum() {
        return chapterNum;
    }

    public void setChapterNum(int chapterNum) {
        this.chapterNum = chapterNum;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getIsAdd() {
        return isAdd;
    }

    public void setIsAdd(int isAdd) {
        this.isAdd = isAdd;
    }
}
