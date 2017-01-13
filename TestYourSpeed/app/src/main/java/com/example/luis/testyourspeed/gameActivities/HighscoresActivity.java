package com.example.luis.testyourspeed.gameActivities;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.luis.testyourspeed.database.DBManager;
import com.example.luis.testyourspeed.R;
import com.example.luis.testyourspeed.adapter.MyResultAdapter;
import com.example.luis.testyourspeed.gameModel.Result;

import java.util.ArrayList;

public class HighscoresActivity extends Activity {

    ImageView previousButton = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hiscores);
        ActionBar actionBar = getActionBar();
        if(actionBar != null)
            actionBar.hide();

        DBManager dbManager = new DBManager(this);
        final ArrayList<Result> resultList =  dbManager.getStudentList();

        if(resultList.size()!=0) {
            ListView lv = (ListView)findViewById(R.id.lv_highscores);

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if(previousButton != null){
                        previousButton.setVisibility(View.GONE);
                    }
                    ImageView ib_removeResult = (ImageView) view.findViewById(R.id.ib_removeResult);
                    ib_removeResult.setVisibility(View.VISIBLE);
                    previousButton = ib_removeResult;
                }
            });

            final MyResultAdapter adapter = new MyResultAdapter(getApplicationContext(), resultList);
            lv.setAdapter(adapter);
        }else{
            Toast.makeText(this, R.string.no_results,Toast.LENGTH_SHORT).show();
        }
    }
}
