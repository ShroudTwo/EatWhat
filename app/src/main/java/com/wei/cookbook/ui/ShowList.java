package com.wei.cookbook.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.wei.cookbook.R;
import com.wei.cookbook.model.Speaking;
import com.wei.cookbook.sql.DBMaster;
import com.wei.cookbook.ui.adapter.AttentionAdpter;

import java.util.List;

public class ShowList extends AppCompatActivity {
    ListView mListView;
    AttentionAdpter mAttentionAdpter;
    List<Speaking> mSpeakingList;
    DBMaster mDBMaster;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_listview);
        mListView = findViewById(R.id.show_listview);
        mDBMaster = new DBMaster(ShowList.this);
        mSpeakingList =  mDBMaster.searchAll();
        mAttentionAdpter = new AttentionAdpter(mSpeakingList,ShowList.this);
        mListView.setAdapter(mAttentionAdpter);

    }
}
