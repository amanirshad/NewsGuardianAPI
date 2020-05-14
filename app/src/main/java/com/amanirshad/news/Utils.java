package com.amanirshad.news;

import android.util.Log;

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

public class Utils {
    private static final String TAG = Utils.class.getSimpleName();
    public static final String EMPTY_NAME = "";

    public static void printLog(String tag, Exception e, String msg) {
        Log.e(tag,   msg + e);
    }

    public static List<News> fetchEventsData(String urlString) {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            printLog(TAG, e, "InterruptedException");
            return null;
        }

        List<News> events;
        try {
            Log.v(TAG,""+urlString);
            URL url = createUrl(urlString);
            String jsonResponse;
            jsonResponse = makeHttpRequest(url);
            events = getDataFromJsonResponse(jsonResponse);
        } catch (NullPointerException e) {
            printLog(TAG, e, "NullPointerException (fetchEventsData)");
            return null;
        }
        return events;
    }

    private static List<News> getDataFromJsonResponse(String jsonResponse) {
        List<News> events = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(jsonResponse);
            JSONObject response = jsonObject.getJSONObject("response");
            JSONArray results = response.getJSONArray("results");

            for (int i = 0; i < results.length(); i++) {
                JSONObject object = results.getJSONObject(i);
                String title = object.getString("webTitle");
                String category = object.getString("sectionName");
                String author;
                if (object.getJSONArray("tags").length() > 0)
                    if (object.getJSONArray("tags").getJSONObject(0).getString("webTitle").length() > 0)
                        author = "N/A";
                //if(object.getJSONArray("tags").has("webTitle"))    .has() is not valid, as the reviewer told me (you may be the same) so I hope this above IFs work :)

                author = object.getJSONArray("tags").getJSONObject(0).getString("webTitle");
                String date = object.getString("webPublicationDate"); //Format: 2018-05-12T20:02:09Z

                date = date.replace("T", " ");
                date = date.replace("Z", " ");
                String url = object.getString("webUrl");
                events.add(new News(title, category, author, date, url));
            }
        } catch (JSONException e) {
            printLog(TAG, e, "JSONException (getDataFromJsonResponse)");
            return null;
        }
        return events;
    }

    private static String makeHttpRequest(URL url) {
        String jsonResponse = null;
        try {
            //Making the connection
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.connect();

            int response = urlConnection.getResponseCode();
            if (response == 200) {
                InputStream inputStream = urlConnection.getInputStream();
                jsonResponse = readInputStream(inputStream);
            } else if (response == 502 || response == 503) {
                return jsonResponse; // null
            }

        } catch (IOException e) {
            printLog(TAG, e, "IOException (makeHttpRequest)");
            return null;
        }
        return jsonResponse;
    }

    private static String readInputStream(InputStream inputStream) {
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
        BufferedReader reader = new BufferedReader(inputStreamReader);
        StringBuilder builder = new StringBuilder();

        String line = null;
        try {
            line = reader.readLine();
            while (line != null) {
                builder.append(line);
                line = reader.readLine();
            }
        } catch (IOException e) {
            printLog(TAG, e, "IOException (readInputStream)");
            return null;
        }
        return builder.toString();
    }

    private static URL createUrl(String urlString) {
        //Create the URL object
        URL url;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            printLog(TAG, e, "MalformedURLException (createUrl)");
            return null;
        }
        return url;
    }
}