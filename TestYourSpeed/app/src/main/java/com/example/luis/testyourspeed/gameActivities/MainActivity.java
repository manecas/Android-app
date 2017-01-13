package com.example.luis.testyourspeed.gameActivities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.luis.testyourspeed.R;
import com.example.luis.testyourspeed.controller.SingleplayerActivity;
import com.example.luis.testyourspeed.settings.SettingsActivity;

public class MainActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClickBtn(View v) {
        switch (v.getId()){
            case R.id.btn_singleplayer:
                startActivity(new Intent(MainActivity.this, SingleplayerActivity.class));
                break;
            case R.id.btn_multiplayer:
                //startActivity(new Intent(MainActivity.this, SingleplayerActivity.class));
                Toast.makeText(this, "Developing...", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_highscore:
                startActivity(new Intent(MainActivity.this, HighscoresActivity.class));
                break;
            case R.id.btn_credits:
                startActivity(new Intent(MainActivity.this, CreditsActivity.class));
                break;
            default:
                Toast.makeText(this, "Button click problem", Toast.LENGTH_SHORT).show(); break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                break;
            case R.id.action_rate:
                //Open page to rate
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
