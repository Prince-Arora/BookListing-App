package com.example.laptop.booklistingapp;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Adapter;
import android.widget.ListView;
import android.widget.TextView;
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
import java.util.List;


public class HttpActivity extends AppCompatActivity {
    ListView listView;
    private TextView mEmptyStateTextView;

    private static final String USGS_URL = "https://www.googleapis.com/books/v1/volumes?q=";
    private BooksAdapter mAdapter;
    private String query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


        query = getIntent().getExtras().get("toSearch").toString();
        listView = (ListView) findViewById(R.id.list);
        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        listView.setEmptyView(mEmptyStateTextView);
        doSearch();
    }

    private void doSearch() {

        BookAsync task = new BookAsync();
        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        if (isConnected) {
            task.execute(USGS_URL);
        } else {
            mEmptyStateTextView.setText(R.string.NoInternet);

        }
    }
    private class BookAsync extends AsyncTask<String, Void, List<Custom>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<Custom> doInBackground(String... urls) {
            URL url = createUrl(USGS_URL + query);
            String jsonResponse = "";
            try {
                jsonResponse = makeHttpRequest(url);
            } catch (IOException e) {
            }
            ArrayList<Custom> books = extractBookInfoFromJson(jsonResponse);
            return books;
        }

        @Override
        protected void onPostExecute(List<Custom> data) {

            mEmptyStateTextView.setText(R.string.Nofound);
            if (data != null && !data.isEmpty()) {
                mAdapter = new BooksAdapter(HttpActivity.this);
                mAdapter.addAll(data);
                listView.setAdapter(mAdapter);
            }
        }
    }

    private URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException exception) {
            return null;
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
            }
        } catch (IOException e) {
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
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

    private ArrayList<Custom> extractBookInfoFromJson(String bookJSON) {
        if (TextUtils.isEmpty(bookJSON)) {
            return null;
        }
        ArrayList<Custom> books = new ArrayList<>();
        try {
            JSONObject base = new JSONObject(bookJSON);
            JSONArray itemArray = base.getJSONArray("items");
            for (int i = 0; i < itemArray.length(); i++) {
                JSONObject currentItem = itemArray.getJSONObject(i);
                JSONObject bookInfo = currentItem.getJSONObject("volumeInfo");
                String Title = bookInfo.getString("title");
                String Publisher = bookInfo.getString("publisher");
                String Author="";
                if (bookInfo.has("authors")) {
                    JSONArray arr = bookInfo.getJSONArray("authors");
                    Author = arr.getString(0);

                } else {
                Author="Not Available";
                }
                String desc="";
                if (bookInfo.has("description")) {
                     desc = bookInfo.getString("description");

                } else {
                    desc="Not Available";
                }
                books.add(new Custom(Title, Author, Publisher, desc));
            }
        } catch (JSONException e) {
        }
        return books;
    }
}
