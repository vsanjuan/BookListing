package com.example.android.booklisting;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

   /** API KEY to access the Google Books API from the app*/
    private static final String API_KEY = "AIzaSyDa513lfqgdeMYV5zLLnH5MYXc9g4wXoXs";

    /** Message for the queries with empty response */
    public static final String EXCEPTION_MESSAGE = "ExceptionMessage";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.books);

        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.SEARCH_STRING);

        GOOGLE_BOOKS_API = GOOGLE_BOOKS_API + message + "&key=" + API_KEY ;

        Log.v("URLSEARCH",GOOGLE_BOOKS_API);

        if (checkInternetConnection()) {

            BookListingAsyncTask task = new BookListingAsyncTask();
            task.execute();

        } else {

            Intent intent1 = new Intent(this,DisplayMessageActivity.class);
            intent1.putExtra(EXCEPTION_MESSAGE,getString(R.string.error_internet_connection));
            startActivity(intent1);


        }



    }

    /**
     * Update the screen to display information from the given {@link Book}.
     */
    private void updateUi(ArrayList<Book> books) {

        if (books.size() <= 0) {

            String message = getString(R.string.error_no_books);
            Intent intent = new Intent(this, DisplayMessageActivity.class);
            intent.putExtra(EXCEPTION_MESSAGE, message);
            startActivity(intent);

        }

        // Display the book title in the UI
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
            ArrayList<Book> books = extractFeatureFromJson(jsonResponse);

            // Return the {@link Event} object as the result fo the {@link TsunamiAsyncTask}
            return books;
        }

        private URL createUrl(String stringUrl) {
            URL url = null;
            try {
                url = new URL(formUrlString(stringUrl));
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
                // Create an array where to store the results
                ArrayList<Book> bookList = new ArrayList<Book>();

                JSONObject baseJsonResponse = new JSONObject(bookJSON);
                JSONArray featureArray;
                // Check if there are any books found
                if (baseJsonResponse.has("items")){
                    featureArray = baseJsonResponse.getJSONArray("items");
                } else {

                    return bookList;

                }
                // If there are results in the features array
                if (featureArray.length() > 0) {

                    // Iterate over the results
                    for (int i = 0; i < featureArray.length(); i++) {

                        // Extract out the first feature (which is an book)
                        JSONObject firstFeature = featureArray.getJSONObject(i);
                        JSONObject volumeInfo = firstFeature.getJSONObject("volumeInfo");

                        // Extract out the title
                        String title = volumeInfo.getString("title");
                        if (volumeInfo.has("subtitle")){
                            title = title + ". " + volumeInfo.getString("subtitle");
                        }

                        // Extract out the authors
                        ArrayList<String> authorsList = new ArrayList<String>();

                        if (volumeInfo.has("authors")){

                            JSONArray authors = volumeInfo.getJSONArray("authors");
                            authorsList.add(authors.getString(0));

                        } else {authorsList.add("No author mentioned");}

                        bookList.add(new Book(title,authorsList));

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

    // Reviews the string to give the right format for the query
    private String formUrlString(String string) {

        String formatedString = string.replaceAll(" ", "+");
        return formatedString;

    }

    // Check if there is an internet conection
    private boolean checkInternetConnection() {

        ConnectivityManager cm =
                (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();


        return isConnected;


    }


}
