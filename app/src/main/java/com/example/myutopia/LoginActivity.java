package com.example.myutopia;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.myutopia.classes.User;
import com.mongodb.Block;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteFindIterable;

import org.bson.Document;

import static com.mongodb.client.model.Filters.eq;

public class LoginActivity extends AppCompatActivity {

    private EditText mPasswordView;
    private EditText mUsernameView;
    private Button   mLoginButtonView;

    private static final String TAG = MainActivity.class.getSimpleName();

    private Block<Document> printBlock;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setViews();
        setListeners();
    }

    public void setViews()
    {
        mPasswordView    = (EditText) findViewById(R.id.passwordField);
        mUsernameView    = (EditText) findViewById(R.id.usernameField);
        mLoginButtonView = (Button) findViewById(R.id.buttonLogin);
    }

    public void setListeners()
    {
        mLoginButtonView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String usenam = String.valueOf(mUsernameView.getText());
                String passwo = String.valueOf(mPasswordView.getText());
                testValidUser(usenam,passwo);
            }
        });
    }

    //Methods

    public void testValidUser(String m_user, String m_password)
    {
        printBlock = document -> {
            String string = document.toString();
            boolean isFound = string.indexOf(m_password) !=-1? true: false;
            if(isFound){
                MainActivity.loggedIn = 1;
                createUser(m_user);
                MainActivity.mHandler.sendEmptyMessage(MainActivity.POT_ACTIVITY);
                finish();
            }
        };

        MainActivity.Collection.find(eq("username", m_user)).forEach(printBlock);
    }

    public void createUser(String m_user)
    {
        MainActivity.User.setUserName(m_user);
    }

    //Override methods

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MainActivity.mHandler.sendEmptyMessage(MainActivity.POT_ACTIVITY);
        finish();
    }

}
