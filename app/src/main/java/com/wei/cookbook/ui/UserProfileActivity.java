package com.wei.cookbook.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.wei.cookbook.R;
import com.wei.cookbook.model.Profile;
import com.wei.cookbook.sql.DBMaster;

import java.util.List;

public class UserProfileActivity extends AppCompatActivity {

    DBMaster mDBMaster;
    TextView mTextView1;
    TextView mTextView2;
    TextView mTextView3;
    TextView mTextView4;
    //TextView mTextView5;
    Button mButton;
    List<Profile> mProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        mTextView1 = findViewById(R.id.my_signature1);
        mTextView2 = findViewById(R.id.username1);
        mTextView3 = findViewById(R.id.gender1);
        mTextView4 = findViewById(R.id.age1);
        mTextView1.setText("HEllo world!!!");
        //mTextView5 = findViewById(R.id.phone1);
        mDBMaster = new DBMaster(UserProfileActivity.this);
        mProfile = mDBMaster.searchAllProfile();
        if(mProfile.size()>=1) {
            mTextView1.setText(mProfile.get(mProfile.size() - 1).getCharacter());
            mTextView2.setText(mProfile.get(mProfile.size() - 1).getUsername());
            mTextView3.setText(mProfile.get(mProfile.size() - 1).getGender());
            mTextView4.setText(mProfile.get(mProfile.size() - 1).getAge());
        }
        mButton = findViewById(R.id.alter_button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProfileActivity.this,UserSetting.class);
                startActivity(intent);
            }
        });

        //mTextView5.setText(mProfile.getId().toString());
    }

}
