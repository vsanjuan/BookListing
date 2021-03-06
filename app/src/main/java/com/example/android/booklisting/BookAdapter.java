package com.example.android.booklisting;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Salvador on 18/09/2016.
 */
public class BookAdapter extends ArrayAdapter<Book> {

    public BookAdapter(Context context, ArrayList<Book> books) {

        super(context,0,books);

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent ){

        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.activity_book_listing, parent, false);
        }

        // Get the {@link AndroidFlavor} object located at this position in the list
        Book currentBook = getItem(position);

        // Find the TextView in the activity_book_listing.xml layout with the ID version_name
        TextView titleTextView = (TextView) listItemView.findViewById(R.id.title);
        titleTextView.setText(currentBook.getMtitle());


        // Find the TextView in the activity_book_listing.xml layout with the ID version_name
        TextView authorTextView = (TextView) listItemView.findViewById(R.id.authors);
        authorTextView.setText(joinAuthors(currentBook.getMauthors()));

        return listItemView;
    }


    // Takes an authors list and makes a String
    private String joinAuthors(ArrayList<String> authorlist) {
        String authors = "";
        for (int i = 0; i< authorlist.size();i++) {

            authors = authors + authorlist.get(i);
            Log.v("Authors", authors);

        }

        return authors;

    }


}
