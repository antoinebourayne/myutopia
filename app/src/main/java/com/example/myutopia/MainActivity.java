package com.example.myutopia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.androdocs.httprequest.HttpRequest;
import com.bumptech.glide.Glide;
import com.example.myutopia.classes.User;
import com.example.myutopia.handlers.HandlerMainActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.stitch.android.core.Stitch;
import com.mongodb.stitch.android.core.StitchAppClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoCollection;
import com.mongodb.stitch.core.auth.providers.anonymous.AnonymousCredential;

import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.Executor;

public class MainActivity extends AppCompatActivity {

    //Constantes
    public static final int MAIN_ACTIVITY    = 1;
    public static final int LOGIN_ACTIVITY   = 2;
    public static final int SIGN_IN_ACTIVITY = 3;
    public static final int POT_ACTIVITY     = 4;

    public static final String CITY = "paris,fr";
    public static final String API = "7a92826f74b3e959996e9a46080c4aad";


    // Finals
    private static final String TAG = MainActivity.class.getSimpleName();

    //Statics
    public static RemoteMongoClient mongoClient;
    public static RemoteMongoCollection Collection;
    public static StitchAppClient stitchAppClient;
    public static int loggedIn = 0; // 0 : not logged in  1 : logged in
    public static User User;
    public static String TEMPERATURE;


    //Handlers
    public static HandlerMainActivity mHandler;

    //Variables

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHandler = new HandlerMainActivity(this);
        User = new User();
        new weatherTask().execute();
        setContentView(R.layout.activity_pot);
        dbLogin();
        choseActivity(LOGIN_ACTIVITY);
    }

    public void dbLogin()
    {
        Stitch.initializeDefaultAppClient(getString(R.string.my_app_id));
        stitchAppClient = Stitch.getDefaultAppClient();

        stitchAppClient.getAuth().loginWithCredential(new AnonymousCredential())
                .addOnSuccessListener(user -> {
                    Log.i(TAG, "Requesting connection");
                })
                .addOnFailureListener((e -> {
                    Log.d(TAG, "error", e);
                }));

        mongoClient    = stitchAppClient.getServiceClient(RemoteMongoClient.factory, "mongodb-atlas");
        Collection = mongoClient.getDatabase("Data").getCollection("Users");
    }

    public void choseActivity(int activity)
    {
        switch (activity)
        {
            case MAIN_ACTIVITY:
                Intent mainActiivity = new Intent(this, MainActivity.class);
                startActivity(mainActiivity);
                break;

            case LOGIN_ACTIVITY:
                Intent loginActivity = new Intent(this, LoginActivity.class);
                startActivity(loginActivity);
                break;

            case SIGN_IN_ACTIVITY:
                Intent signInActivity = new Intent(this, SignInActivity.class);
                startActivity(signInActivity);
                break;

            case POT_ACTIVITY:
                Intent potActivity = new Intent(this, PotActivity.class);
                startActivity(potActivity);
                break;
        }
    }

    class weatherTask extends AsyncTask<String, Void, String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.i(TAG, "Downloading the weather data...");
        }

        @Override
        protected String doInBackground(String... strings) {
            String response = HttpRequest.excuteGet("https://api.openweathermap.org/data/2.5/forecast?q=" + CITY + "&units=metric&cnt=5&appid=" + API);
            return response;
        }

        @Override
        protected void onPostExecute(String result) {


            try {
                JSONObject jsonObj = new JSONObject(result);
                JSONArray jArray = jsonObj.getJSONArray("list");

                for(int i=0; i < jArray.length(); i++)
                {
                    //jArray.getJSONObject(i).getString("dt");
                    JSONObject main = jArray.getJSONObject(i).getJSONObject("main");
                    String temperature = "Day "+i+" "+main.getString("temp");
                    Log.i(TAG, temperature);
                }
                /*
                JSONObject main = jsonObj.getJSONObject("main");
                JSONObject sys = jsonObj.getJSONObject("sys");
                JSONObject wind = jsonObj.getJSONObject("wind");
                JSONObject weather = jsonObj.getJSONArray("weather").getJSONObject(0);

                Long updatedAt = jsonObj.getLong("dt");
                String updatedAtText = "Updated at: " + new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH).format(new Date(updatedAt * 1000));
                String temp = main.getString("temp") + "°C";
                String tempMin = "Min Temp: " + main.getString("temp_min") + "°C";
                String tempMax = "Max Temp: " + main.getString("temp_max") + "°C";
                String pressure = main.getString("pressure");
                String humidity = main.getString("humidity");

                Long sunrise = sys.getLong("sunrise");
                Long sunset = sys.getLong("sunset");
                String windSpeed = wind.getString("speed");
                String weatherDescription = weather.getString("description");

                String address = jsonObj.getString("name") + ", " + sys.getString("country");

                TEMPERATURE = temp;

                 */

            } catch (JSONException e) {
                Log.i(TAG, "onPostExecute Exception : "+e);
            }

        }
    }


}
