package com.wei.cookbook.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.wei.cookbook.R;
import com.wei.cookbook.model.Speaking;
import com.wei.cookbook.sql.DBMaster;

import java.text.SimpleDateFormat;
import java.util.Date;

public class FriendsCirActivity extends AppCompatActivity {


    DBMaster mDBMaster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_cir);
        final  EditText mEditText = findViewById(R.id.write_something);
        Button mButton = findViewById(R.id.saving_button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String stringText = mEditText.getText().toString();
                if(!TextUtils.isEmpty(stringText)){
                    Speaking speaking = new Speaking();
                    speaking.setSpeaking(stringText);
                    Date date = new Date();
                    speaking.setDate(date);
                    mDBMaster=new DBMaster(FriendsCirActivity.this);
                    mDBMaster.insertOrReplace(speaking);
                    Intent intent=new Intent(FriendsCirActivity.this,ShowList.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        Button mButton1 = findViewById(R.id.showing_button);
        mButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FriendsCirActivity.this, ShowList.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
