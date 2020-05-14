package com.amanirshad.news;

public class News {

    private String mTitle;
    private String mCategory;
    private String mAuthor;
    private String mDate;
    private String mUrl;

    //The onstructor
    public News(String title, String category, String author, String date,String url) {
        mTitle = title;
        mAuthor = author;
        mCategory = category;
        mDate = date;
        mUrl = url;
    }

    //Grabing methods
    public String getTitle() {
        return mTitle;
    }
    public String getDate() {
        return mDate;
    }
    public String getAuthor() {
        return mAuthor;
    }
    public String getCategory() {
        return mCategory;
    }
    public String getUrl() {
        return mUrl;
    }
}