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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.myutopia.methods.GlideApp;

public class PotActivity extends AppCompatActivity {

    private final float BACKGROUND_TRANSPARENCY = (float) 0.3;

    private ImageView      mSignIn;
    private ImageView      mLights;
    private RelativeLayout mBackground;
    private ImageView      mDownPot;
    private ImageView      mViewTemperature;

    private static final String TAG = MainActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pot);
        setViews();
        setListeners();
        updateView();
    }

    public void setViews()
    {
        mSignIn             = (ImageView)   findViewById(R.id.userButton);
        mLights             = (ImageView)   findViewById(R.id.upperLights);
        mBackground         = (RelativeLayout)findViewById(R.id.potBackground);
        mDownPot            = (ImageView)findViewById(R.id.downPot);
        mViewTemperature    = (ImageView) findViewById(R.id.viewTemperature);
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
                openWeather(v);
            }
        });
        mLights.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openLightsPopUp(v);
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

        View popupView = inflater.inflate(R.layout.popup_lights, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
        popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);



        // dismiss the popup window when touched
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });
    }

    public void openLightsPopUp(View v)
    {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        View popupView = inflater.inflate(R.layout.popup_lights, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
        popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);

        //PopUp light controls
        SeekBar mLightControl = (SeekBar) popupView.findViewById(R.id.jaugeLights);
        mLightControl.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int lightPower = seekBar.getProgress();
                Log.i(TAG, "lightPower : "+lightPower);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

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

    public void updateView()
    {
        String urLogo        = "https://i.ibb.co/M21kQVL/Artboard-2.png";
        String urlLights     = "https://i.ibb.co/Vg3M87W/2574778-8718964060108-110.png";
        String urlBackground = "https://i.ibb.co/K6nQDhg/background.jpg";
        String urlPot        = "https://i.ibb.co/GvS1VKr/potombre.png";
        String urlThermo     = "https://i.ibb.co/17mxT3g/thermometre-bois-publicitaire-personnalisable-0013713-jpg.png";

        GlideApp.with(this).load(urLogo).override(150,150).into(mSignIn);
        GlideApp.with(this).load(urlLights).override(1400,500).into(mLights);
        GlideApp.with(this).load(urlPot).override(3000,1000).into(mDownPot);
        GlideApp.with(this).load(urlThermo).override(300,300).into(mViewTemperature);

        //GlideApp.with(this).load(url).override(300,200).into(mBackground);

        GlideApp.with(this).load(urlBackground).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    //mBackground.setBackground(resource);
                }
            }
        });

        if(MainActivity.loggedIn == 1){

        }


    }
}
