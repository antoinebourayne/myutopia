package com.example.myutopia;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.myutopia.classes.User;

import org.bson.Document;

public class SignInActivity extends AppCompatActivity {

    private EditText mPasswordView;
    private EditText mUsernameView;
    private EditText mFirstNameView;
    private EditText mSecondNameView;
    private Button   mSubmitButtonView;

    private static final String TAG = MainActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setViews();
        setListeners();
    }

    public void setViews()
    {
        mPasswordView      = (EditText) findViewById(R.id.passwordField);
        mUsernameView      = (EditText) findViewById(R.id.usernameField);
        mFirstNameView     = (EditText) findViewById(R.id.firstNameField);
        mSecondNameView    = (EditText) findViewById(R.id.secondNameField);
        mSubmitButtonView  = (Button) findViewById(R.id.buttonLogin);
    }

    public void setListeners()
    {
        mSubmitButtonView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String usenam = String.valueOf(mUsernameView.getText());
                String passwo = String.valueOf(mPasswordView.getText());
                String fn = String.valueOf(mFirstNameView.getText());
                String sn = String.valueOf(mSecondNameView.getText());
                User newUser = new User(usenam,passwo,fn,sn);
                Document userDocument = createUserDocument(newUser);
                insertNewUser(userDocument);
            }
        });
    }

    //Methods

    public org.bson.Document createUserDocument(User m_user)
    {
        org.bson.Document userDocument = new Document();
        userDocument.put("username",m_user.getUserName());
        userDocument.put("password",m_user.getPassword());
        userDocument.put("firstname",m_user.getFirstName());
        userDocument.put("secondname",m_user.getSecondName());
        return userDocument;
    }

    public void insertNewUser(org.bson.Document m_document)
    {
        MainActivity.Collection.insertOne(m_document).addOnSuccessListener(user -> {
            Log.i(TAG, "One document inserted");
            MainActivity.mHandler.sendEmptyMessage(MainActivity.SIGN_IN_ACTIVITY);
            finish();
        });
    }

    //Override methods

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MainActivity.mHandler.sendEmptyMessage(MainActivity.POT_ACTIVITY);
        finish();
    }


}
