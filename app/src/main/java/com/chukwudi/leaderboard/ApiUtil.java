package com.chukwudi.leaderboard;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

public class ApiUtil {
    private ApiUtil() {}

    public static final String BASE_API_URL = "https://gadsapi.herokuapp.com";
    public static final String LEARNING_API_URL = "api/hours";
    public static final String SKILL_IQ_API_URL = "api/skilliq";

    public static URL buildUrl (String title) {
        URL url = null;
        Uri uri = Uri.parse(BASE_API_URL)
                .buildUpon()
                .appendEncodedPath(title)
                .build();
        try {
            url = new URL(uri.toString());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return url;
    }

    public static String getJson(URL url) throws IOException {
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

        try {
            InputStream stream = connection.getInputStream();
            Scanner scanner = new Scanner(stream);
            scanner.useDelimiter("\\A");
            boolean hasData = scanner.hasNext();
            if (hasData) {
                return scanner.next();
            } else {
                return null;
            }
        } catch (Exception e) {
            Log.d("Error", e.toString());
            return null;
        } finally {
            connection.disconnect();
        }
    }

    public static ArrayList<Learner> getLearnersFromJson(String json) {
        final String NAME = "name";
        final String HOURS = "hours";
        final String COUNTRY = "country";

        ArrayList<Learner> learners = new ArrayList<Learner>();
        try {
            JSONArray jsonLearners = new JSONArray(json);
            int numberOfLearners = jsonLearners.length();
            for (int i = 0; i < numberOfLearners; i++) {
                JSONObject learnerJSON = jsonLearners.getJSONObject(i);
                Learner learner = new Learner(
                        learnerJSON.getString(NAME),
                        learnerJSON.getInt(HOURS),
                        learnerJSON.getString(COUNTRY)
                );
                learners.add(learner);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return learners;
    }

    public static ArrayList<Skill> getSkillsFromJson(String json) {
        final String NAME = "name";
        final String SCORE = "score";
        final String COUNTRY = "country";

        ArrayList<Skill> skills = new ArrayList<Skill>();
        try {
            JSONArray jsonLearners = new JSONArray(json);
            int numberOfLearners = jsonLearners.length();
            for (int i = 0; i < numberOfLearners; i++) {
                JSONObject learnerJSON = jsonLearners.getJSONObject(i);
                Skill learner = new Skill(
                        learnerJSON.getString(NAME),
                        learnerJSON.getInt(SCORE),
                        learnerJSON.getString(COUNTRY)
                );
                skills.add(learner);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return skills;
    }
}
