package com.example.luis.testyourspeed.gameModel;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Game {
    private Context context;
    private String inputWord;
    private List<String> words;
    private List<String> rightWords;
    private List<String> wrongWords;
    private String username;
    private int wordCount;
    private float wpm;
    private float accuracy;
    private int lineNumber;
    private int testLanguage;
    private long testTime;
    private boolean isOn;
    private boolean isPaused;
    private long timeRemaining;
    private String tempWord;

    public Game(Context c) {
        context = c;
        words = new ArrayList<>();
        rightWords = new ArrayList<>();
        wrongWords = new ArrayList<>();
        inputWord = "";
        tempWord = "";
        wordCount = 0;
        lineNumber = 0;
        wpm = 0.0f;
        isOn = false;
        isPaused = false;
        timeRemaining = 0;
    }

    public boolean checkInputWord(){
        String word = words.get(wordCount++);

        if(word.equals(inputWord)){
            rightWords.add(word);
            return true;
        }else{
            wrongWords.add(word);
            return false;
        }
    }

    public String getCurrentWord(){
        return words.get(wordCount);
    }

    public String getLastWord(){
        return words.get(wordCount - 1);
    }

    public void shuffleWords(){
        Collections.shuffle(words);
    }

    public String getWordsAsString(){
        StringBuilder builder = new StringBuilder();
        for (String w : words) builder.append(w).append(" ");
        return builder.toString();
    }

    public void getWordsFromFile(String filePath){
        try{
            final InputStream file = context.getAssets().open(filePath);
            BufferedReader reader = new BufferedReader(new InputStreamReader(file));
            String line;
            while((line = reader.readLine()) != null){
                String [] split = line.split(" ");
                for (String s : split) { //Em caso de haver mais palavra numa linha
                    words.add(s.trim());
                }
            }
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    public void reset(){
        rightWords.clear();
        wrongWords.clear();
        inputWord = "";
        tempWord = "";
        wordCount = 0;
        lineNumber = 0;
        wpm = 0.0f;
        isOn = false;
        isPaused = false;
        timeRemaining = testTime;
    }

    public String getTempWord() {
        return tempWord;
    }

    public void setTempWord(String tempWord) {
        this.tempWord = tempWord;
    }

    public long getTimeRemaining() {
        return timeRemaining;
    }

    public void setTimeRemaining(long timeRemaining) {
        this.timeRemaining = timeRemaining;
    }

    public boolean isPaused() {
        return isPaused;
    }

    public void setPaused(boolean paused) {
        isPaused = paused;
    }

    public boolean isOn() {
        return isOn;
    }

    public void setOn(boolean on) {
        this.isOn = on;
    }

    public String getInputWord() {
        return inputWord;
    }

    public void setInputWord(String inputWord) {
        this.inputWord = inputWord;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public float getWpm() {
        return wpm;
    }

    public void setWpm(float wpm) {
        this.wpm = wpm;
    }

    public float getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(float accuracy) {
        this.accuracy = accuracy;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public void incrementLineNumber(int lineNumber) {
        this.lineNumber += lineNumber;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    public int getTestLanguage() {
        return testLanguage;
    }

    public void setTestLanguage(int testLanguage) {
        this.testLanguage = testLanguage;
    }

    public long getTestTime() {
        return testTime;
    }

    public void setTestTime(long testTime) {
        this.testTime = testTime;
    }

    public int getNumberRigthWords(){
        return rightWords.size();
    }

    public int getNumberWrongWords(){
        return wrongWords.size();
    }
}
