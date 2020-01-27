package com.example.myutopia.handlers;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;
import com.example.myutopia.MainActivity;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class HandlerMainActivity extends Handler {

    //Constantes
    public static final int MAIN_ACTIVITY    = 1;
    public static final int LOGIN_ACTIVITY   = 2;
    public static final int SIGN_IN_ACTIVITY = 3;
    public static final int POT_ACTIVITY     = 4;

    private MainActivity mActivity;

    public HandlerMainActivity(MainActivity mActivity)
    {
        this.mActivity = mActivity;
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
        switch (msg.what)
        {
            case MAIN_ACTIVITY:
                if(mActivity != null) {
                    mActivity.choseActivity(MAIN_ACTIVITY);
                }
                break;

            case LOGIN_ACTIVITY:
                if(mActivity != null) {
                    mActivity.choseActivity(LOGIN_ACTIVITY);
                }
                break;

            case SIGN_IN_ACTIVITY:
                if(mActivity != null) {
                    mActivity.choseActivity(SIGN_IN_ACTIVITY);
                }
                break;

            case POT_ACTIVITY:
                if(mActivity != null) {
                    mActivity.choseActivity(POT_ACTIVITY);
                }
                break;

        }

    }
}