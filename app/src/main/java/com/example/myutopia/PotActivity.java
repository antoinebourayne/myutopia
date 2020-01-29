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
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.myutopia.classes.DayInfo;
import com.example.myutopia.methods.GlideApp;

import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class PotActivity extends AppCompatActivity {

    private final static int LIGHTS_OFF = 0;
    private final static int LIGHTS_ON  = 1;

    private static int LIGHT_STATE;

    private ImageView      mSignIn;
    private ImageView      mLights;
    private RelativeLayout mBackground;
    private ImageView      mDownPot;
    private ImageView      mViewTemperature;
    private ImageView      mPlantView;
    private ImageView      mWeather;

    private static final String TAG = MainActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pot);
        setViews();
        setListeners();
        updateView();
        LIGHT_STATE = LIGHTS_OFF;
    }

    public void setViews()
    {
        mSignIn             = (ImageView)   findViewById(R.id.userButton);
        mLights             = (ImageView)   findViewById(R.id.upperLights);
        mBackground         = (RelativeLayout)findViewById(R.id.potBackground);
        mDownPot            = (ImageView)findViewById(R.id.downPot);
        mViewTemperature    = (ImageView) findViewById(R.id.viewTemperature);
        mWeather            = (ImageView) findViewById(R.id.weatherButton);
        mPlantView          = (ImageView) findViewById(R.id.mainPlant);
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

        mViewTemperature.setOnClickListener(new View.OnClickListener() {
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
    }

    public void setLights(int m_color, int m_power)
    {
        Log.i(TAG, "Lights Power : "+m_power+"  Color : "+m_color);
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

    public void toggleLight(View v)
    {
        ImageView mLightToggle = (ImageView) findViewById(R.id.upperLights);

        mLightToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (LIGHT_STATE == LIGHTS_OFF){
                    LIGHT_STATE = LIGHTS_ON;
                    mLightToggle.setImageResource(R.drawable.icone_light_bulb);

                    Document filterDoc = new Document().append("username", "prototype");
                    Document updateDoc = new Document().append("$set", new Document().append("light", "1"));
                    MainActivity.Collection.updateOne(filterDoc, updateDoc);

                }else{
                    LIGHT_STATE = LIGHTS_OFF;
                    mLightToggle.setImageResource(R.drawable.icone_light_bulb_off);

                    Document filterDoc = new Document().append("username", "prototype");
                    Document updateDoc = new Document().append("$set", new Document().append("light", "0"));
                    MainActivity.Collection.updateOne(filterDoc, updateDoc);
                }
            }
        });
    }

    public void updateView()
    {
        String urLogo        = "https://i.ibb.co/M21kQVL/Artboard-2.png";
        String urlBackground = "https://i.ibb.co/vBx6LNq/window-background.png";
        String urlPot        = "https://i.ibb.co/GvS1VKr/potombre.png";
        String urlThermo     = "https://i.ibb.co/17mxT3g/thermometre-bois-publicitaire-personnalisable-0013713-jpg.png";
        String urlPlant      = "https://i.ibb.co/F5tTPv2/plante-1-Copie.png";

        GlideApp.with(this).load(urLogo).override(150,150).into(mSignIn);
        GlideApp.with(this).load(urlPot).override(3000,1000).into(mDownPot);
        GlideApp.with(this).load(urlThermo).override(300,300).into(mViewTemperature);
        GlideApp.with(this).load(urlPlant).into(mPlantView);

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
