package com.example.myutopia;

import androidx.appcompat.app.AppCompatActivity;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.myutopia.methods.GlideApp;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.mongodb.Block;
import com.mongodb.lang.NonNull;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteFindIterable;
import com.mongodb.stitch.core.services.mongodb.remote.RemoteFindOptions;

import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class PotActivity extends AppCompatActivity {

    private final static int LIGHTS_OFF = 0;
    private final static int LIGHTS_ON  = 1;

    private final static int NM_OFF = 0;
    private final static int NM_ON  = 1;

    private final static int WATER_OFF = 0;
    private final static int WATER_ON  = 1;

    private static int LIGHT_STATE;
    private static int NM_STATE;
    private static int WATER_STATE;

    private ImageView      mSignIn;
    private ImageView      mLights;
    private RelativeLayout mBackground;
    private ImageView      mDownPot;
    private ImageView      mDashboard;
    private ImageView      mPlantView;
    private ImageView      mWeather;
    private TextView       mPlantTitle;
    private TextView       mPlantInfo;
    private ImageView      mFontain;
    private ImageView      mTap;
    private ImageView      mSettings;

    private static final String TAG = MainActivity.class.getSimpleName();

    private Block<Document> printBlock;

    public static String enlightment;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pot);
        setViews();
        setListeners();
        updateView();
        LIGHT_STATE = LIGHTS_OFF;
        NM_STATE = NM_OFF;
        WATER_STATE = WATER_OFF;
    }

    public void setViews()
    {
        mSignIn             = (ImageView)   findViewById(R.id.userButton);
        mLights             = (ImageView)   findViewById(R.id.upperLights);
        mBackground         = (RelativeLayout)findViewById(R.id.potBackground);
        mDownPot            = (ImageView)findViewById(R.id.downPot);
        mDashboard          = (ImageView) findViewById(R.id.viewTemperature);
        mWeather            = (ImageView) findViewById(R.id.weatherButton);
        mPlantView          = (ImageView) findViewById(R.id.mainPlant);
        mFontain            = (ImageView) findViewById(R.id.fontain);
        mPlantInfo          = (TextView) findViewById(R.id.plantInfoTextView);
        mPlantTitle         = (TextView) findViewById(R.id.plantInfoTitle);
        mTap                = (ImageView) findViewById(R.id.waterTap);
        mSettings           = (ImageView) findViewById(R.id.settingsButton);
    }

    public void setListeners()
    {
        mSignIn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(MainActivity.loggedIn == 0) {
                    MainActivity.mHandler.sendEmptyMessage(MainActivity.SIGN_IN_ACTIVITY);
                    finish();
                }
            }
        });

        mDashboard.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openDashboard(v);
            }
        });

        mSettings.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openSettings(v);
            }
        });

        mPlantView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            }
        });

        mLights.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                toggleLight(v);
            }
        });

        mWeather.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openWeather(v);
            }
        });

        mFontain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleWater(mTap);
            }
        });
    }

    public void toggleWater(ImageView v)
    {
        ImageView mWaterToggle = (ImageView) findViewById(R.id.fontain);

        mWaterToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (WATER_STATE == WATER_OFF){
                    WATER_STATE = WATER_ON;

                    ImageView img = (ImageView)findViewById(R.id.waterTap);
                    Animation outFade = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_out);
                    img.startAnimation(outFade);

                    Document filterDoc = new Document().append("username", "prototype");
                    Document updateDoc = new Document().append("$set", new Document().append("arrosage", 1));
                    MainActivity.Collection.updateOne(filterDoc, updateDoc);

                }else{
                    WATER_STATE = WATER_OFF;

                    ImageView img = (ImageView)findViewById(R.id.waterTap);
                    Animation outFade = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_in);
                    img.startAnimation(outFade);

                    Document filterDoc = new Document().append("username", "prototype");
                    Document updateDoc = new Document().append("$set", new Document().append("arrosage", 0));
                    MainActivity.Collection.updateOne(filterDoc, updateDoc);
                }
            }
        });
    }

    public void openDashboard(View v)
    {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        View popupView = inflater.inflate(R.layout.popup_dashboard, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
        popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);


        TextView mSensorName1     = (TextView) popupView.findViewById(R.id.nameSensor1);
        TextView mSensorName2     = (TextView) popupView.findViewById(R.id.nameSensor2);
        TextView mSensorName3     = (TextView) popupView.findViewById(R.id.nameSensor3);

        TextView mSensorValue1     = (TextView) popupView.findViewById(R.id.valueSensor1);
        TextView mSensorValue2     = (TextView) popupView.findViewById(R.id.valueSensor2);
        TextView mSensorValue3     = (TextView) popupView.findViewById(R.id.valueSensor3);

        mSensorName1.setText("Température :");
        mSensorName2.setText("Humidité :");
        mSensorName3.setText("Luminosité :");

        printBlock = document -> { String sensorValue = document.toString();
        enlightment = sensorValue.substring(sensorValue.lastIndexOf("enlightment=") + 12);
        enlightment = enlightment.replace("}}","");
            Log.i(TAG, "enlightment : "+enlightment);
                        }
            ;

        MainActivity.Collection.find(eq("id_rpi", "prototype")).limit(1).forEach(printBlock);

        mSensorValue1.setText("21");
        mSensorValue2.setText("73");
        mSensorValue3.setText(enlightment);



        // dismiss the popup window when touched
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });
    }

    public void openWeather(View v)
    {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        View popupView = inflater.inflate(R.layout.popup_weather, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
        popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);

        ListView mWeatherListView     = (ListView) popupView.findViewById(R.id.popupWeatherListView);
        TextView mTextViewWeatherLine = (TextView) findViewById(R.id.textViewLineWeather);

        List<String> weekInfo = new ArrayList<String>();
        for (int i=0; i<5 ; i++)
        {
            weekInfo.add(MainActivity.weekInfo.get(i).getDayName() + "  :  " + (int) MainActivity.weekInfo.get(i).getMinTemp()+"°C");
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.line_day_weather, R.id.textViewLineWeather, weekInfo);

        mWeatherListView.setAdapter(adapter);

        // dismiss the popup window when touched
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });
    }


    public void openSettings(View v)
    {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        View popupView = inflater.inflate(R.layout.pop_up_settings, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
        popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);

        ImageView mNMToggle = (ImageView) popupView.findViewById(R.id.buttonSetting1);

        mNMToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NM_STATE == NM_OFF){
                    NM_STATE = NM_ON;
                    mNMToggle.setImageResource(R.drawable.icone_moon);

                    Document filterDoc = new Document().append("username", "prototype");
                    Document updateDoc = new Document().append("$set", new Document().append("nightmode", 1));
                    MainActivity.Collection.updateOne(filterDoc, updateDoc);

                }else{
                    NM_STATE = NM_OFF;
                    mNMToggle.setImageResource(R.drawable.icone_sun);

                    Document filterDoc = new Document().append("username", "prototype");
                    Document updateDoc = new Document().append("$set", new Document().append("nightmode",0));
                    MainActivity.Collection.updateOne(filterDoc, updateDoc);
                }
            }
        });


        // dismiss the popup window when touched
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });
    }

    public void toggleLight(View v)
    {
        ImageView mLightToggle = (ImageView) findViewById(R.id.upperLights);

        mLightToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (LIGHT_STATE == LIGHTS_OFF){
                    LIGHT_STATE = LIGHTS_ON;
                    mLightToggle.setImageResource(R.drawable.nice_lightbulb_on);

                    Document filterDoc = new Document().append("username", "prototype");
                    Document updateDoc = new Document().append("$set", new Document().append("light", 1));
                    MainActivity.Collection.updateOne(filterDoc, updateDoc);

                }else{
                    LIGHT_STATE = LIGHTS_OFF;
                    mLightToggle.setImageResource(R.drawable.nice_lightbulb_off);

                    Document filterDoc = new Document().append("username", "prototype");
                    Document updateDoc = new Document().append("$set", new Document().append("light", 0));
                    MainActivity.Collection.updateOne(filterDoc, updateDoc);
                }
            }
        });
    }

    public void updateView()
    {
        String urLogo          = "https://i.ibb.co/M21kQVL/Artboard-2.png";
        String urlBackground   = "https://i.ibb.co/vBx6LNq/window-background.png";
        String urlPot          = "https://i.ibb.co/p472900/Pot-nu.png";
        String urlThermo       = "https://i.ibb.co/17mxT3g/thermometre-bois-publicitaire-personnalisable-0013713-jpg.png";
        String urlPlant        = "https://i.ibb.co/F5tTPv2/plante-1-Copie.png";
        String urlrockyFontain = "https://i.ibb.co/CP2Y8Jm/nice-rocky-fountain.png";

        GlideApp.with(this).load(urLogo).override(150,150).into(mSignIn);
        GlideApp.with(this).load(urlPot).override(3000,1000).into(mDownPot);
        GlideApp.with(this).load(urlThermo).override(300,300).into(mDashboard);
        GlideApp.with(this).load(urlPlant).into(mPlantView);
        GlideApp.with(this).load(urlrockyFontain).into(mFontain);

        GlideApp.with(this).load(urlBackground).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    mBackground.setBackground(resource);
                }
            }
        });

        if(MainActivity.loggedIn == 1){

        }
    }
}
