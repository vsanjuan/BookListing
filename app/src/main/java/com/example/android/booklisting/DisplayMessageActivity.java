package com.example.android.booklisting;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class DisplayMessageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty_search);

        Intent intent = getIntent();
        String message = intent.getStringExtra(BookListing.EXCEPTION_MESSAGE);

        TextView textView = (TextView) findViewById(R.id.error_message);
        textView.setText(message);


    }
}
