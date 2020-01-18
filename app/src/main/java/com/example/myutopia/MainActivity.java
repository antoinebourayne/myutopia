package com.example.myutopia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

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

import java.util.Collections;
import java.util.concurrent.Executor;

public class MainActivity extends AppCompatActivity {

    //Constantes
    public static final int MAIN_ACTIVITY    = 1;
    public static final int LOGIN_ACTIVITY   = 2;
    public static final int SIGN_IN_ACTIVITY = 3;
    public static final int POT_ACTIVITY     = 4;


    // Finals
    private static final String TAG = MainActivity.class.getSimpleName();

    //Statics
    public static RemoteMongoClient mongoClient;
    public static RemoteMongoCollection Collection;
    public static StitchAppClient stitchAppClient;
    public static int loggedIn = 0; // 0 : not logged in  1 : logged in
    public static User User;


    //Handlers
    public static HandlerMainActivity mHandler;

    //Variables

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHandler = new HandlerMainActivity(this);
        User = new User();
        setContentView(R.layout.activity_pot);
        dbLogin();
        choseActivity(POT_ACTIVITY);
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


}
