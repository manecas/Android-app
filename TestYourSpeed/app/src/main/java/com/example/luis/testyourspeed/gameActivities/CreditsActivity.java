package com.example.luis.testyourspeed.gameActivities;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.luis.testyourspeed.R;

public class CreditsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credits);

        ActionBar actionBar = getActionBar();
        if(actionBar != null)
            actionBar.hide();

        ImageView iv_isec = (ImageView)findViewById(R.id.iv_isec);
        ImageView iv_deis = (ImageView)findViewById(R.id.iv_deis);
        View layout_bottom = findViewById(R.id.layout_bottom) ;

        Animation animation_isec = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.top_bottom_isec);
        Animation animation_deis = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.left_rigth_deis);
        Animation animation_layout = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bottom_top_layout);

        iv_isec.startAnimation(animation_isec);
        iv_deis.startAnimation(animation_deis);
        layout_bottom.startAnimation(animation_layout);
    }

    public void onClickImageView(View view) {
        int id = view.getId();

        if(id == R.id.iv_isec){
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.addCategory(Intent.CATEGORY_BROWSABLE);
            intent.setData(Uri.parse("https://www.isec.pt/"));
            startActivity(intent);
        }else if(id == R.id.iv_deis){
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.addCategory(Intent.CATEGORY_BROWSABLE);
            intent.setData(Uri.parse("http://www.deis.isec.pt/"));
            startActivity(intent);
        }
    }
}
