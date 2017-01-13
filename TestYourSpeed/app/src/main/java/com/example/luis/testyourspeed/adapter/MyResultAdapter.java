package com.example.luis.testyourspeed.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.luis.testyourspeed.database.DBManager;
import com.example.luis.testyourspeed.R;
import com.example.luis.testyourspeed.gameModel.Result;

import java.util.ArrayList;

public class MyResultAdapter extends ArrayAdapter<Result> {
    ArrayList<Result> results;

    public MyResultAdapter(Context context, ArrayList<Result> results) {
        super(context, R.layout.view_result_entry ,results);
        this.results = results;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final Result result = getItem(position);

        if(convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.view_result_entry, parent, false);

        ImageView iv_testLanguage = (ImageView) convertView.findViewById(R.id.iv_testLanguage);
        TextView tv_username = (TextView) convertView.findViewById(R.id.tv_name);
        TextView tv_wpm = (TextView) convertView.findViewById(R.id.tv_wpm_entry);
        TextView tv_accuracy = (TextView) convertView.findViewById(R.id.tv_accuracy_entry);
        ImageView ib_removeResult = (ImageView)convertView.findViewById(R.id.ib_removeResult);

        ib_removeResult.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                DBManager dbManager = new DBManager(getContext());
                dbManager.delete(result.getId());
                results.remove(result);
                notifyDataSetChanged();
            }
        });

        try {
            if(result.getTestLanguage() == 0)
                iv_testLanguage.setImageResource(R.drawable.ic_country_uk);
            else
                iv_testLanguage.setImageResource(R.drawable.ic_country_pt);
            tv_username.setText(result.getName());
            tv_wpm.setText(result.getWpm());
            tv_accuracy.setText(result.getAccuracy());
        }catch(NullPointerException e){
            e.printStackTrace();
        }

        return convertView;
    }
}
