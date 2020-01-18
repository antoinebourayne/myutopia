package com.example.myutopia;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;

public class PotActivity extends AppCompatActivity {

    private TextView  mSignIn;
    private ImageView mLights;

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
        mSignIn    = (TextView) findViewById(R.id.userButton);
        mLights    = (ImageView) findViewById(R.id.loupeLights);
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
        if(MainActivity.loggedIn == 1)
        {
            mSignIn.setText(MainActivity.User.getUserName());
        }else{
            mSignIn.setText("Sign In");
        }
    }
}
