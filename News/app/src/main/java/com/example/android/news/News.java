package com.example.android.news;

public class News {

    private String mTitle;
    private String mInformation;
    private String mDate;
    private String mAuthor;
    private String mUrl;

    News(String title, String information, String date, String author, String url){
        mTitle = title;
        mInformation = information;
        mDate = date;
        mAuthor = author;
        mUrl = url;
    }
    public String getTitle(){
        return mTitle;
    }

    public String getInformation() {
        return mInformation;
    }

    public String getDate() {
        return mDate;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getUrl() {
        return mUrl;
    }
}
