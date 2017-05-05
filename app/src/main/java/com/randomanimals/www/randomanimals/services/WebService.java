package com.randomanimals.www.randomanimals.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.randomanimals.www.randomanimals.Constants;
import com.randomanimals.www.randomanimals.events.LeaderEvent;
import com.randomanimals.www.randomanimals.events.ProfileEvent;
import com.randomanimals.www.randomanimals.events.SoundEvent;
import com.randomanimals.www.randomanimals.events.UsernameEvent;
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
        Log.d(TAG, "Post " + url + ": " + json);
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
        String result = null;
        try {
            result = post(url, body);
        } catch (Exception e) {
            Log.e(TAG, "Error with post request: " + url);
            Log.e(TAG, e.toString());
            Toast.makeText(getApplicationContext(), "Leaderboards Disabled", Toast.LENGTH_SHORT).show();
            result = null;
        } finally {
            Log.d(TAG, "Result " + url + ": " + result);
            if (result != null) {
                String username = null;
                switch (url) {
                    case Constants.ANIMAL_URL:
                        LeaderEvent leaderEvent = new LeaderEvent(animalListFromResponse(result));
                        EventBus.getDefault().post(leaderEvent);
                        break;
                    case Constants.PROFILE_URL:
                        final List<Animal> animals = animalListFromResponse(result);
                        if (animals.size() > 0) {
                            username =  animals.get(0).username;
                        }
                        ProfileEvent profileEvent = new ProfileEvent(animals, username);
                        EventBus.getDefault().post(profileEvent);
                        break;
                    case Constants.USERNAME_URL:
                        try {
                            JSONObject resp = new JSONObject(result);
                            username = resp.getString("username");
                            UsernameEvent usernameEvent = new UsernameEvent(username);
                            EventBus.getDefault().post(usernameEvent);
                        } catch (Exception e) {
                            Log.e(TAG, e.toString());
                            EventBus.getDefault().post(new UsernameEvent());
                        }
                        break;
                    case Constants.INCREMENT_URL:
                        try {
                            JSONObject resp = new JSONObject(result);
                            final int bonus = resp.getInt("data");
                            EventBus.getDefault().post(new SoundEvent(bonus));
                        } catch (Exception e) {
                            Log.e(TAG, e.toString());
                            // TODO: handle connection error.
                            EventBus.getDefault().post(new SoundEvent(1));
                        }
                        break;
                    default:
                        Log.e(TAG, "Unknown url: " + url);
                        break;
                }
            } else {
                switch (url) {
                    case Constants.ANIMAL_URL:
                        // Error Handling - emit empty event of appropriate event class.
                        EventBus.getDefault().post(new LeaderEvent());
                        break;
                    case Constants.PROFILE_URL:
                        EventBus.getDefault().post(new ProfileEvent());
                        break;
                    case Constants.USERNAME_URL:
                        EventBus.getDefault().post(new UsernameEvent());
                        break;
                    case Constants.INCREMENT_URL:
                        Log.d(TAG, "Increment response: " + result);
                        break;
                    default:
                        Log.e(TAG, "Unknown url: " + url);
                        break;
                }
            }
        }
    }

    private List<Animal> animalListFromResponse(String result) {
        ArrayList<Animal> animals = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(result);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                Animal animal = new Animal(obj.getString("userId"), obj.getString("username"),
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
