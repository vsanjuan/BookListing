package com.example.android.booklisting;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class BookListing extends AppCompatActivity {

    /** URL to query the Google Books API */
    private  String GOOGLE_BOOKS_API =
            "https://www.googleapis.com/books/v1/volumes?q=";

    /** Tag for the log messages */
    public static final String LOG_TAG = MainActivity.class.getSimpleName();

    public static final String API_KEY = "AIzaSyDa513lfqgdeMYV5zLLnH5MYXc9g4wXoXs";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.books);

        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.SEARCH_STRING);
        
/*        TextView textView = new TextView(this);
        textView.setTextSize(40);
        textView.setText(message);

        LinearLayout layout = (LinearLayout) findViewById(R.id.book_listing);
        layout.addView(textView);*/
        
        GOOGLE_BOOKS_API = GOOGLE_BOOKS_API + message + "&key=" + API_KEY ;
        // GOOGLE_BOOKS_API + message + "&maxResults=1&key=" + API_KEY ;

        Log.v("URLSEARCH",GOOGLE_BOOKS_API);

        BookListingAsyncTask task = new BookListingAsyncTask();
        task.execute();

    }

    /**
     * Update the screen to display information from the given {@link Book}.
     */
    private void updateUi(ArrayList<Book> books) {
        // Display the book title in the UI
//        TextView titleTextView = (TextView) findViewById(R.id.title);
//        titleTextView.setText(book.getMtitle());

        BookAdapter adapter = new BookAdapter(this,books);
        ListView listView = (ListView) findViewById(R.id.book_list);

        listView.setAdapter(adapter);


    }

    private class BookListingAsyncTask extends AsyncTask<URL,Void, ArrayList<Book>> {

        @Override
        protected ArrayList<Book> doInBackground(URL... urls) {
            // Create URL object
            URL url = createUrl(GOOGLE_BOOKS_API);


            // Perform HTTP request to the URL and receive a JSON response back
            String jsonResponse = "";
            try {
                jsonResponse = makeHttpRequest(url);
            } catch (IOException e) {

                Log.e(LOG_TAG, "Problem URL trying to connect ", e);

            }

            // Extract relevant fields from the JSON response and create an {@link Event} object
            ArrayList<Book> book = extractFeatureFromJson(jsonResponse);

            // Return the {@link Event} object as the result fo the {@link TsunamiAsyncTask}
            return book;
        }

        private URL createUrl(String stringUrl) {
            URL url = null;
            try {
                url = new URL(stringUrl);
            } catch (MalformedURLException exception) {
                Log.e(LOG_TAG, "Error with creating URL", exception);
                return null;
            }
            return url;
        }

        /**
         * Make an HTTP request to the given URL and return a String as the response.
         */
        private String makeHttpRequest(URL url) throws IOException {
            String jsonResponse = "";
            HttpURLConnection urlConnection = null;
            InputStream inputStream = null;
            try {
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setReadTimeout(10000 /* milliseconds */);
                urlConnection.setConnectTimeout(15000 /* milliseconds */);
                urlConnection.connect();

                if (urlConnection.getResponseCode() == 200) {

                    inputStream = urlConnection.getInputStream();
                    jsonResponse = readFromStream(inputStream);

                } else {

                    jsonResponse = "";
                }


            } catch (IOException e) {

                Log.e(LOG_TAG, "Problem connecting with the URL", e);


            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (inputStream != null) {
                    // function must handle java.io.IOException here
                    inputStream.close();
                }
            }
            return jsonResponse;
        }

        /**
         * Update the screen with the given book (which was the result of the
         * {@link BookListingAsyncTask}).
         */
        @Override
        protected void onPostExecute(ArrayList<Book> book) {
            if (book == null) {
                return;
            }

            updateUi(book);
        }

        /**
         * Convert the {@link InputStream} into a String which contains the
         * whole JSON response from the server.
         */
        private String readFromStream(InputStream inputStream) throws IOException {
            StringBuilder output = new StringBuilder();
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String line = reader.readLine();
                while (line != null) {
                    output.append(line);
                    line = reader.readLine();
                }
            }
            return output.toString();
        }

        /**
         * Return an {@link Book} object by parsing out information
         * about the first book from the input bookJSON string.
         */
        private ArrayList<Book> extractFeatureFromJson(String bookJSON) {
            try {
                JSONObject baseJsonResponse = new JSONObject(bookJSON);
                JSONArray featureArray = baseJsonResponse.getJSONArray("items");

                // If there are results in the features array
                if (featureArray.length() > 0) {
                    // Create an array where to store the results
                    ArrayList<Book> bookList = new ArrayList<Book>();
                    // Iterate over the results
                    for (int i = 0; i < featureArray.length(); i++) {

                        // Extract out the first feature (which is an book)
                        JSONObject firstFeature = featureArray.getJSONObject(i);
                        JSONObject volumeInfo = firstFeature.getJSONObject("volumeInfo");

                        // Extract out the title, time, and tsunami values
                        String title = volumeInfo.getString("title");

                        bookList.add(new Book(title));

                    }

                    // Create a new {@link Book} object
                    return bookList;
                }
            } catch (JSONException e) {
                Log.e(LOG_TAG, "Problem parsing the book JSON results", e);
            }
            return null;
        }

    }
}
