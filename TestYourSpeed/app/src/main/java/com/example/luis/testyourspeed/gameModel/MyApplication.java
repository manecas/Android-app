package com.example.luis.testyourspeed.gameModel;

import android.app.Application;
import android.text.SpannableString;
import android.view.Menu;
import android.widget.ProgressBar;

public class MyApplication extends Application {
    private Game game;
    private SpannableString spanString;
    private Menu myMenu;
    private ProgressBar progressbar;

    public MyApplication(){
        game = null;
    }

    public Menu getMyMenu() {
        return myMenu;
    }

    public ProgressBar getProgressbar() {
        return progressbar;
    }

    public void setProgressbar(ProgressBar progressbar) {
        this.progressbar = progressbar;
    }

    public void setMyMenu(Menu myMenu) {
        this.myMenu = myMenu;
    }

    public SpannableString getSpanString() {
        return spanString;
    }

    public void setSpanString(SpannableString spanString) {
        this.spanString = spanString;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }
}
