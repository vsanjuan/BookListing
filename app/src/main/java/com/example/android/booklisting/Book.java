package com.example.android.booklisting;

/**
 * Created by Salvador on 17/09/2016.
 */
public class Book {

    private String mtitle;
    private String[] mauthors;
    private String [] mcategories;
    private String mpublisher;


    public Book( String title, String[] authors, String[] categories, String publisher ) {

        mtitle = title;
        mauthors = authors;
        mcategories = categories;
        mpublisher = publisher;

    }

    public  Book ( String title ) {

        mtitle = title;
    }

    public String getMtitle() {

        return mtitle;
    }

    public String[] getMauthors() {

        return  mauthors;
    }

    public String[] getMcategories() {
        return mcategories;
    }

    public String getMpublisher() {
        return mpublisher;
    }
}
