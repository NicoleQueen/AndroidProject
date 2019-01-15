package com.example.android.news;

import android.text.TextUtils;
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


public final class QueryUtils {

    private static  final String LOG_TAG = QueryUtils.class.getSimpleName();

    private QueryUtils() {
    }

    public static List<News> fetchNewsData(String requestUrl) {
        URL url = createUrl(requestUrl);
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);

        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making Http request!", e);
        }

        List<News> newsList = extractFeatureFromJson(jsonResponse);
        return newsList;
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building URL!", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) return jsonResponse;
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
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }

        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving JSON results.", e);

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

    private static List<News> extractFeatureFromJson(String newsJSON) {
        if (TextUtils.isEmpty(newsJSON)) {
                return null;
        }

        List<News> news = new ArrayList<>();
        try {
            JSONObject newsJsonResponse = new JSONObject ( newsJSON );
            JSONObject response = newsJsonResponse.getJSONObject ( "response" );
            JSONArray newsArray = response.getJSONArray ( "results" );
            for (int i = 0; i < newsArray.length(); i++) {
                JSONObject currentNews = newsArray.getJSONObject(i);
                String title = currentNews.getString("sectionName");
                String information = currentNews.getString("webTitle");
                String date = currentNews.getString("webPublicationDate");
                StringBuilder author = new StringBuilder ( "By: " );
                String url = currentNews.getString("webUrl");

                JSONArray authorArray = currentNews.getJSONArray ( "tags" );
                if (authorArray != null && authorArray.length () > 0) {
                    for (int j = 0; j < authorArray.length (); j++) {
                        JSONObject authors = authorArray.getJSONObject ( j );
                        String authorsListed = authors.optString ( "webTitle" );

                        if (authorArray.length () > 1) {
                            author.append ( authorsListed );
                            author.append ( "\t\t\t" );

                        } else {
                            author.append ( authorsListed );
                        }
                    }
                } else {
                    author.replace ( 0, 3, "No author(s) listed" );
                }

                News newsItem = new News(title, information, date, author.toString(), url);
                news.add(newsItem);

            }
        } catch (JSONException e){
            Log.e("QueryUtils", "Problem parsing the news JSON results", e);
        }

        return news;

    }

}
