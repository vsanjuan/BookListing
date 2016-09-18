package com.example.android.booklisting;

import java.util.ArrayList;

/**
 * Created by Salvador on 17/09/2016.
 */
public class Book {

    private String mtitle;
    private ArrayList<String> mauthors;

    public Book(String title, ArrayList<String> authors ) {
        mtitle = title;
        mauthors = authors;
    }

    public String getMtitle() {return mtitle;}

    public ArrayList<String> getMauthors() {return  mauthors;}

}
