package com.example.android.booklisting;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    public final static String SEARCH_STRING = "search_string";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

/*        final Button button = (Button) findViewById(R.id.search_button);


        button.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v) {

                EditText editText = (EditText) findViewById(R.id.search_string);
                String searchString =  editText.getText().toString();
                Intent intent = new Intent(this, BookListing.class);
                intent.putExtra("search_string", searchString);



            }
        });
        */

    }

    public void searchString(View view) {

        EditText editText = (EditText) findViewById(R.id.search_string);
        String searchString =  editText.getText().toString();
        Intent intent = new Intent(this, BookListing.class);
        intent.putExtra(SEARCH_STRING, searchString);
        startActivity(intent);

    }



}
