package com.example.myutopia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.androdocs.httprequest.HttpRequest;
import com.bumptech.glide.Glide;
import com.example.myutopia.classes.DayInfo;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executor;

public class MainActivity extends AppCompatActivity {

    //Constantes
    public static final int MAIN_ACTIVITY          = 1;
    public static final int LOGIN_ACTIVITY         = 2;
    public static final int SIGN_IN_ACTIVITY       = 3;
    public static final int POT_ACTIVITY           = 4;

    public static final int RAS                    = 0;
    public static final int DANGER_GEL             = 1;
    public static final int DANGER_CANICULE        = 2;
    public static final int ALERTE_GEL             = 3;
    public static final int ALERTE_CANICULE        = 4;

    public static final int SEUIL_DANGER_GEL       = 2;
    public static final int SEUIL_DANGER_CANICULE  = 35;
    public static final int SEUIL_ALERTE_GEL       = 0;
    public static final int SEUIL_ALERTE_CANICULE  = 39;

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
    public static List<DayInfo> weekInfo = new ArrayList<>();


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

    //Methods

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

    public int weatherAnalysis()
    {
        List<DayInfo> localWeekInfo = weekInfo;

        for (int i=0; i<localWeekInfo.size(); i++)
        {
            if(localWeekInfo.get(i).getMinTemp()<SEUIL_ALERTE_GEL) { return ALERTE_GEL; }
            if(localWeekInfo.get(i).getMinTemp()<SEUIL_DANGER_GEL) {return DANGER_GEL; }
            if(localWeekInfo.get(i).getMaxTemp()>SEUIL_ALERTE_CANICULE) {return ALERTE_CANICULE; }
            if(localWeekInfo.get(i).getMaxTemp()>SEUIL_DANGER_CANICULE) {return DANGER_CANICULE; }
        }

        return RAS;
    }

    //Asynchrones

    class weatherTask extends AsyncTask<String, Void, String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.i(TAG, "Downloading the weather data...");
        }

        @Override
        protected String doInBackground(String... strings) {
            String response = HttpRequest.excuteGet("https://api.openweathermap.org/data/2.5/forecast?q=" + CITY + "&units=metric&cnt=56&appid=" + API);
            return response;
        }

        @Override
        protected void onPostExecute(String result) {

            try {
                JSONObject jsonObj = new JSONObject(result);
                JSONArray jArray = jsonObj.getJSONArray("list");
                for(int i=0; i < jArray.length(); i+=8)
                {
                    JSONObject main     = jArray.getJSONObject(i).getJSONObject("main");
                    String date         = jArray.getJSONObject(i).getString("dt_txt");
                    float minTemp       = Float.parseFloat(main.getString("temp_min"));
                    float maxTemp       = Float.parseFloat(main.getString("temp_max"));

                    String dateNiceFormat = String.valueOf(date.charAt(5))+ date.charAt(6) +"/"+ date.charAt(8)+ date.charAt(9);
                    weekInfo.add(new DayInfo(minTemp,maxTemp,dateNiceFormat));
                }

            } catch (JSONException e) {
                Log.i(TAG, "onPostExecute Exception : "+e);
            }

        }
    }


}
