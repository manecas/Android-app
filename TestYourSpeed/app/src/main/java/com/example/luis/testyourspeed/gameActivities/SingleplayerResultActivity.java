package com.example.luis.testyourspeed.gameActivities;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.luis.testyourspeed.database.DBManager;
import com.example.luis.testyourspeed.R;
import com.example.luis.testyourspeed.controller.SingleplayerActivity;
import com.example.luis.testyourspeed.gameModel.Result;

import java.util.Locale;

public class SingleplayerResultActivity extends Activity{

    private DBManager dbManager;

    private TextView tv_wpm;
    private TextView tv_rigth;
    private TextView tv_wrong;
    private TextView tv_accuracy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singleplayer_result);
        setTitle(getString(R.string.game_result));
        dbManager = new DBManager(this);

        float wpm = getIntent().getExtras().getFloat("wpm");
        int right = getIntent().getExtras().getInt("rigth");
        int wrong = getIntent().getExtras().getInt("wrong");
        float accuracy = getIntent().getExtras().getFloat("accuracy");

        tv_wpm = (TextView) findViewById(R.id.tv_wpm_result);
        tv_rigth = (TextView) findViewById(R.id.tv_rigth_result);
        tv_wrong = (TextView) findViewById(R.id.tv_wrong_result);
        tv_accuracy = (TextView) findViewById(R.id.tv_accuracy);

        try {
            tv_wpm.setText(String.format(Locale.getDefault(), "%.1f", wpm));
            tv_rigth.setText(String.valueOf(right));
            tv_wrong.setText(String.valueOf(wrong));
            if(accuracy != 0)
                tv_accuracy.setText(String.format(Locale.getDefault(), "%.1f", accuracy));
            else
                tv_accuracy.setText("0");
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SingleplayerActivity.firstTime = true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_result, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_addHighscores:
                DBManager dbManager = new DBManager(this);
                Result result = new Result();
                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
                try {
                    result.setTestLanguage(Integer.parseInt(sharedPref.getString("test_language", "0")));
                    result.setName(sharedPref.getString("user_name", "Name"));
                }catch(ClassCastException e){
                    result.setName("Default");
                    Log.i("ClassCastException", e.toString());
                }
                result.setWpm(tv_wpm.getText().toString());
                result.setRigth(tv_rigth.getText().toString());
                result.setWrong(tv_wrong.getText().toString());
                result.setAccuracy(tv_accuracy.getText().toString());
                int result_Id = dbManager.insert(result);
//                if(result_Id != -1){
//                    Toast.makeText(this,"New Result Inserted: "+result_Id, Toast.LENGTH_SHORT).show();
//                }else{
//                    Toast.makeText(this,"Error inserting Result",Toast.LENGTH_SHORT).show();
//                }
                SingleplayerActivity.firstTime = true;
                finish();
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

}
