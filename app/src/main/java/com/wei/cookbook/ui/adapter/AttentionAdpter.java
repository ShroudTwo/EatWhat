package com.wei.cookbook.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import com.wei.cookbook.R;
import com.wei.cookbook.model.Speaking;
import com.wei.cookbook.sql.DBMaster;

import java.text.SimpleDateFormat;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;

public class AttentionAdpter extends BaseAdapter {

    private List<Speaking> mSpeakingList;
    private Context context;
    private LayoutInflater mInflater;

    DBMaster mDBMaster;
    public  AttentionAdpter(List<Speaking> list,Context context)
    {
        this.mSpeakingList=list;
        this.context=context;
        mInflater = LayoutInflater.from(context);

    }

    @Override
    public int getCount() {
        return mSpeakingList.size();
    }
    public void setData(List<Speaking> data) {
        this.mSpeakingList = data;
        notifyDataSetChanged();
    }

    @Override
    public Speaking getItem(int position) {
        return mSpeakingList.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder;
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            convertView = mInflater.inflate(R.layout.showing_item, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        String mDate=" ";
        if(getItem(position).getDate()!=null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd :HH:mm:ss");

            mDate = dateFormat.format(getItem(position).getDate());
        }
        holder.mTextView.setText(getItem(position).getSpeaking()+"\n"+ mDate);
        holder.mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDBMaster = new DBMaster(context);
                mDBMaster.delete(getItem(position));
                mSpeakingList.remove(position);
                notifyDataSetChanged();
            }
        });

        return convertView;
    }

    public class ViewHolder{
       @Bind(R.id.show_item)
       TextView mTextView ;
       @Bind((R.id.delete_button))
       Button mButton;
        public ViewHolder(View convertView) {
            ButterKnife.bind(this, convertView);
        }
    }

}
