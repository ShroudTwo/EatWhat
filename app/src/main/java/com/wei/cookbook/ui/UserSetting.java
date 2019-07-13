package com.wei.cookbook.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wei.cookbook.R;
import com.wei.cookbook.model.Profile;
import com.wei.cookbook.model.Speaking;
import com.wei.cookbook.sql.DBMaster;

import java.util.Date;

public class UserSetting extends AppCompatActivity {
    Button mButton;  //保存按钮
    EditText mEditText1;  //个性签名
    EditText mEditText2;  //用户名
    EditText mEditText3;  //性别
    EditText mEditText4;  //年龄
   // EditText mEditText5;  //手机号
    DBMaster mDBMaster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_setting);
        final EditText mEditText1 = findViewById(R.id.my_signature);
        final EditText mEditText2 = findViewById(R.id.username);
        final EditText mEditText3 = findViewById(R.id.gender);
        final EditText mEditText4 = findViewById(R.id.age);
        //final EditText mEditText5 = findViewById(R.id.phone);
        Button mButton = findViewById(R.id.saving11_button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String stringText1 = mEditText1.getText().toString();
                String stringText2 = mEditText2.getText().toString();
                String stringText3 = mEditText3.getText().toString();
                String stringText4 = mEditText4.getText().toString();
                //String stringText5 = mEditText5.getText().toString();
                if (!TextUtils.isEmpty(stringText1) && !TextUtils.isEmpty(stringText2)) {
                    Profile profile = new Profile();
                    profile.setCharacter(stringText1);
                    profile.setUsername(stringText2);
                    profile.setGender(stringText3);
                    profile.setAge(stringText4);
                    //profile.setId(Long.parseLong(stringText5));
                    mDBMaster = new DBMaster(UserSetting.this);
                    mDBMaster.insertOrReplace(profile);
                    Intent intent=new Intent(UserSetting.this,UserProfileActivity.class);
                    //Toast.makeText(UserSetting.this,"fjlkajfld",Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                    finish();
                }
                else{
                    Toast.makeText(UserSetting.this,"请编辑您的个人信息",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
