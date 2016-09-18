package com.example.android.booklisting;

import java.util.ArrayList;

/**
 * Created by Salvador on 17/09/2016.
 */
public class Book {

    private String mtitle;
    private ArrayList<String> mauthors;
    private ArrayList<String> mcategories;
    private String mpublisher;


    public Book(String title, ArrayList<String> authors, ArrayList<String> categories, String publisher ) {

        mtitle = title;
        mauthors = authors;
        mcategories = categories;
        mpublisher = publisher;

    }

    public Book(String title, ArrayList<String> authors ) {

        mtitle = title;
        mauthors = authors;


    }

    public  Book ( String title ) {

        mtitle = title;
    }

    public String getMtitle() {

        return mtitle;
    }

    public ArrayList<String> getMauthors() {

        return  mauthors;
    }

    public ArrayList<String> getMcategories() {
        return mcategories;
    }

    public String getMpublisher() {
        return mpublisher;
    }
}
