package com.example.android.booklisting;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BookListing extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_listing);

        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.SEARCH_STRING);
        TextView textView = new TextView(this);
        textView.setTextSize(40);
        textView.setText(message);

        LinearLayout layout = (LinearLayout) findViewById(R.id.book_listing);
        layout.addView(textView);

    }
}
