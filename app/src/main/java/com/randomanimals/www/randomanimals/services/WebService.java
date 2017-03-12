package com.randomanimals.www.randomanimals.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.randomanimals.www.randomanimals.Constants;
import com.randomanimals.www.randomanimals.events.LeaderEvent;
import com.randomanimals.www.randomanimals.events.ProfileEvent;
import com.randomanimals.www.randomanimals.models.Animal;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 */
public class WebService extends IntentService {
    private static final String TAG = "WebService";
    public WebService() {
        super("WebService");
    }

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private static OkHttpClient client = new OkHttpClient();

    String post(String url, String json) throws IOException {
        Log.d(TAG, url + ": " + json);
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String url = intent.getStringExtra("url");
        String body = intent.getStringExtra("body");
        Log.d(TAG, "WebService");
        String result;
        try {
            result = post(url, body);
        } catch (Exception e) {
            Log.e(TAG, "Error with post request: " + url);
            Log.e(TAG, e.toString());
            result = null;
        }
        Log.d(TAG, "Post result: " + result);
        if (result != null) {
            switch (url) {
                case Constants.ANIMAL_URL:
                    LeaderEvent leaderEvent = new LeaderEvent(animalListFromResponse(result));
                    EventBus.getDefault().post(leaderEvent);
                    break;
                case Constants.PROFILE_URL:
                    ProfileEvent profileEvent = new ProfileEvent(animalListFromResponse(result));
                    EventBus.getDefault().post(profileEvent);
                    break;
                case Constants.INCREMENT_URL:
                    Log.d(TAG, "Increment: " + result);
                    break;
                default:
                    Log.e(TAG, "Unknown url: " + url);
                    break;
            }
        }
    }

    private List<Animal> animalListFromResponse(String result) {
        ArrayList<Animal> animals = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(result);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                Animal animal = new Animal(obj.getString("userid"), obj.getString("username"),
                        obj.getString("animal"), obj.getInt("count"), null);
                animals.add(animal);
            }
            return animals;
        } catch (Exception e) {
            Log.e("profileEventFromResponse", e.toString());
        }
        return new ArrayList<Animal>();

    }
}
