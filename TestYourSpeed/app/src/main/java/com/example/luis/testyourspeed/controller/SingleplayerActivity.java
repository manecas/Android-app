package com.example.luis.testyourspeed.controller;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.Layout;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.luis.testyourspeed.R;
import com.example.luis.testyourspeed.gameModel.MyApplication;
import com.example.luis.testyourspeed.gameActivities.SingleplayerResultActivity;
import com.example.luis.testyourspeed.gameModel.Game;

import java.util.Locale;

public class SingleplayerActivity extends Activity{
    static final String EN_WORDS_FILE_PATH = "words/EnglishWords.txt";
    static final String PT_WORDS_FILE_PATH = "words/PortugueseWords.txt";

    private MyApplication app;
    private Game game;
    private CountDownTimer timer;
    private ProgressBar progressbar;

    private Menu myMenu;

    private TextView tv_wordsGenerated;
//    private TextView tv_timer;
    private TextView tv_right;
    private TextView tv_wrong;
    private TextView tv_wpm;
    private EditText et_wordInput;

    private SpannableString spanString;

    public static boolean firstTime = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singleplayer);
        setTitle(getString(R.string.app_name));

        tv_wordsGenerated = (TextView) findViewById(R.id.tv_wordsGenerated);
//        tv_timer = (TextView) findViewById(R.id.tv_timer);
        et_wordInput = (EditText) findViewById(R.id.et_wordInput);
        tv_right = (TextView) findViewById(R.id.tv_rigth);
        tv_wrong = (TextView) findViewById(R.id.tv_wrong);
        tv_wpm = (TextView) findViewById(R.id.tv_wpm);
        progressbar = (ProgressBar) findViewById(R.id.pbar_time);
        app = (MyApplication)getApplication();

        if(firstTime) {
            Log.i("primeira vez ","sim");
            firstTime = false;
            game = new Game(this);
            getSharedPreferenceValues();

            if(game.getTestLanguage() == 0)
                game.getWordsFromFile(EN_WORDS_FILE_PATH);
            else if(game.getTestLanguage() == 1)
                game.getWordsFromFile(PT_WORDS_FILE_PATH);

            game.shuffleWords();
            spanString = new SpannableString(game.getWordsAsString());
            setSpanCurrentWord();
            tv_wordsGenerated.setText(spanString);
            tv_wordsGenerated.setMovementMethod(new ScrollingMovementMethod());
            setCountDownTimer(game.getTestTime(), 1000);
            setTextWatcher();
//            formatTvTimer(game.getTestTime());
            tv_wpm.setText("0.0");
            game.setTimeRemaining(game.getTestTime());
            progressbar.setRotation(180);
            progressbar.setMax((int) game.getTestTime() / 1000);
            app.setGame(game);
            app.setSpanString(spanString);
            goToLine(game.getLineNumber());
        } else {
            Log.i("primeira vez ","nao");
            game = app.getGame();
            getSharedPreferenceValues();

            if(game.getTestLanguage() == 0)
                game.getWordsFromFile(EN_WORDS_FILE_PATH);
            else if(game.getTestLanguage() == 1)
                game.getWordsFromFile(PT_WORDS_FILE_PATH);

            progressbar.setRotation(180);
            progressbar.setMax((int) game.getTestTime() / 1000);
            spanString = app.getSpanString();
            tv_wordsGenerated.setText(spanString);
            tv_wordsGenerated.setMovementMethod(new ScrollingMovementMethod());
//            formatTvTimer(game.getTimeRemaining());
            tv_wrong.setText(String.valueOf(game.getNumberWrongWords()));
            tv_right.setText(String.valueOf(game.getNumberRigthWords()));
            tv_wpm.setText(String.format(Locale.getDefault(), "%.1f", game.getWpm()));
            setTextWatcher();

            ViewTreeObserver viewTreeObserver = tv_wordsGenerated.getViewTreeObserver();
            viewTreeObserver.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    try {
                        updateLineNumber();
                        return true;
                    } finally {
                        tv_wordsGenerated.getViewTreeObserver().removeOnPreDrawListener(this);
                    }
                }
            });

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        resetGame();
        firstTime = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        timer.cancel();
        game.setPaused(true);
        et_wordInput.setText("");
        et_wordInput.setHint(R.string.paused_type_start);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(game.isPaused()) {
            setCountDownTimer(game.getTimeRemaining(), 1000);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_game, menu);
        myMenu = menu;

        if(game.isOn())
            myMenu.findItem(R.id.action_shuffle).setVisible(false);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_shuffle:
                shuffleWords();
                break;
            case R.id.action_replay:
                resetGame();
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void shuffleWords(){
        game.shuffleWords();
        spanString = new SpannableString(game.getWordsAsString());
        app.setSpanString(spanString);
        setSpanCurrentWord();
        tv_wordsGenerated.setText(spanString);
    }

    public void getSharedPreferenceValues(){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        game.setUsername(sharedPref.getString("user_name", "Name"));
        try {
            game.setTestTime(Long.parseLong(sharedPref.getString("test_time", "60")) * 1000);
            game.setTestLanguage(Integer.parseInt(sharedPref.getString("test_language", "0")));
        }catch(NumberFormatException e){
            e.printStackTrace();
        }
    }

    private void goToLine(final int lineNumber){
        tv_wordsGenerated.post(new Runnable() {
            @Override
            public void run() {
                int y;
                if(lineNumber < 0)
                    y = tv_wordsGenerated.getLayout().getLineTop(0);
                else
                    y = tv_wordsGenerated.getLayout().getLineTop(lineNumber);
                tv_wordsGenerated.scrollTo(0, y);
            }
        });
    }

    public void goDownOneLine(){
        tv_wordsGenerated.post(new Runnable() {
            @Override
            public void run() {
                updateLineNumber();
                int lineNumber = game.getLineNumber(); Log.i("lineNumber: ", lineNumber+"");
                String line = getCurrentLine(lineNumber); Log.i("line: ", line);

                if(line == null) {
                    Log.i("getCurrentLine: ", "null");
                    return;
                }

                String endWord = getLastWordOfLine(line); Log.i("endWord: ", endWord);
                Log.i("current Word: ", game.getCurrentWord());

                if(endWord.equals(game.getCurrentWord())) {
                    goToLine(lineNumber);
                }
            }
        });
    }

    private void updateLineNumber(){
        TextView tv = tv_wordsGenerated;
        Layout tvLayout = tv.getLayout();

        if(tvLayout == null){
            Log.i("layout: ", "null");
//            return;
        }

        for (int i = 0; i < tvLayout.getLineCount(); i++){
            String line = getCurrentLine(i);
            if(line.contains(game.getCurrentWord())) {
                Log.i("updatedLine: ", i+"");
                game.setLineNumber(i);
                goToLine(i - 1);
                return;
            }
        }
    }

    public String getLastWordOfLine(final String line){
        String[] split = line.split(" ");
        return split[split.length - 1];
    }

    public String getCurrentLine(int line){
        Layout tvLayout = tv_wordsGenerated.getLayout();

        if(tvLayout == null){
            Log.i("layout: ", "null");
            return null;
        }

        String text = tv_wordsGenerated.getText().toString();
        int start = tvLayout.getLineStart(line);
        int end = tvLayout.getLineEnd(line);

        return text.substring(start,end);
    }

    public void setSpanCurrentWord(){
        int startCurrent = game.getWordsAsString().indexOf(game.getCurrentWord());
        int endCurrent = startCurrent + game.getCurrentWord().length();
        spanString.setSpan(new BackgroundColorSpan(Color.parseColor("#0288D1")), startCurrent, endCurrent, Spanned.SPAN_INTERMEDIATE);
        spanString.setSpan(new ForegroundColorSpan(Color.WHITE), startCurrent, endCurrent,  Spanned.SPAN_INTERMEDIATE);
    }

    public void changeColorOnValidation(boolean val){
        String text = game.getWordsAsString();
        int startLast = text.indexOf(game.getLastWord());
        int endLast = startLast + game.getLastWord().length();

        setSpanCurrentWord();

        if(val) {
            spanString.setSpan(new BackgroundColorSpan(Color.TRANSPARENT), startLast, endLast, 0);
            spanString.setSpan(new ForegroundColorSpan(Color.parseColor("#388E3C")), startLast, endLast, 0);
        }
        else {
            spanString.setSpan(new BackgroundColorSpan(Color.TRANSPARENT), startLast, endLast, 0);
            spanString.setSpan(new ForegroundColorSpan(Color.parseColor("#D84315")), startLast, endLast, 0);
        }
        tv_wordsGenerated.setText(spanString, TextView.BufferType.SPANNABLE);
    }

    private void calculateWpm(float seconds){
        float minutes = seconds / 60;
        int rigthW = game.getNumberRigthWords();
        game.setWpm(rigthW / minutes);

        if(game.getWpm() > 0) {
            tv_wpm.setText(String.format(Locale.getDefault(), "%.1f", game.getWpm()));
        }
    }

//    private void formatTvTimer(long millis){
//        int seconds = (int) Math.round((millis) / 1000.0);
//        int minutes = seconds / 60;
//        seconds = seconds % 60;
//        tv_timer.setText(String.format(Locale.getDefault(), "%02d", minutes) + ":"
//                + String.format(Locale.getDefault(), "%02d", seconds));
//    }

    private void setCountDownTimer(long start, long interval){
        timer = new CountDownTimer(start, interval) {
            @Override
            public void onTick(final long millisUntilFinished) {
//                Log.i("millis ", millisUntilFinished+"\n");
                game.setTimeRemaining(millisUntilFinished);
//                Log.i("progress ", (int) Math.round((game.getTestTime() - millisUntilFinished) / 1000.0)+"\n");
                progressbar.setProgress((int) Math.round((game.getTestTime() - millisUntilFinished) / 1000.0));
                calculateWpm((game.getTestTime() - millisUntilFinished) / 1000f);
//                formatTvTimer(millisUntilFinished);
            }

            @Override
            public void onFinish() {
                game.setAccuracy((game.getWpm() * 100) / (game.getNumberRigthWords() +
                        game.getNumberWrongWords()));
                startResultActivity();
                resetGame();
            }
        };
    }

    private void startResultActivity(){
        Intent intent = new Intent(this, SingleplayerResultActivity.class);
        intent.putExtra("wpm", game.getWpm());
        intent.putExtra("rigth", game.getNumberRigthWords());
        intent.putExtra("wrong", game.getNumberWrongWords());
        intent.putExtra("accuracy", game.getAccuracy());
        startActivity(intent);
        finish();
    }

    private void resetGame() {
        timer.cancel();
        goToLine(0);
        Log.i("getTimeRemaining ", game.getTimeRemaining()+"\n");
        game.reset();
        spanString = new SpannableString(game.getWordsAsString());
        setSpanCurrentWord();
        tv_wordsGenerated.setText(spanString);
        app.setSpanString(spanString);
        tv_wpm.setText("0.0");
        tv_right.setText("0");
        tv_wrong.setText("0");
        et_wordInput.setText("");
        setCountDownTimer(game.getTestTime(), 1000);
//        formatTvTimer(game.getTestTime());
        progressbar.setProgress(0);
        (myMenu.findItem(R.id.action_shuffle)).setVisible(true);
    }

    private void setTextWatcher(){
        et_wordInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable e) {
                int len = e.length();

                if(len > 0) { //Tamanho maior do que 0
                    char begin = e.charAt(0);
                    char end = e.charAt(len - 1);

                    if (begin == ' ' || begin == '\n' || begin == '\t') {
                        e.clear();
                        return;
                    }

                    if (!game.isOn()) {
                        timer.start();
                        game.setOn(true);
                        (myMenu.findItem(R.id.action_shuffle)).setVisible(false);
                    }else if(game.isPaused()){
                        timer.start();
                        game.setPaused(false);
                        et_wordInput.setHint(getString(R.string.type_to_start));
                        (myMenu.findItem(R.id.action_shuffle)).setVisible(false);
                    }

                    if (end == ' ') {
                        game.setInputWord(e.subSequence(0, len - 1).toString());  //-1 por causa do espaço ou enter
                        changeColorOnValidation(game.checkInputWord());
                        goDownOneLine();
                        tv_right.setText(String.valueOf(game.getNumberRigthWords()));
                        tv_wrong.setText(String.valueOf(game.getNumberWrongWords()));
                        e.clear();
                    }
                    else if(end == '\n'){
                        String word = e.toString().trim();
                        e.clear();
                        e.append(word);
                    }
                }
            }
        });
    }
}
