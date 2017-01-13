package com.example.luis.testyourspeed.gameModel;

public class Result {
    public static final String TABLE = "RESULTS";

    public static final String KEY_ID = "id";
    public static final String KEY_TEST_LANGUAGE = "testlanguage";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_WPM = "wpm";
    public static final String KEY_RIGTH = "rigth";
    public static final String KEY_WRONG = "wrong";
    public static final String KEY_ACCURACY = "accuracy";

    private int id;
    private int testLanguage;
    private String name;
    private String wpm;
    private String rigth;
    private String wrong;
    private String accuracy;

    public Result() {
        name = "Name";
        wpm = "0.0";
        rigth = "0";
        wrong = "0";
        accuracy = "0.0";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTestLanguage() {
        return testLanguage;
    }

    public void setTestLanguage(int testLanguage) {
        this.testLanguage = testLanguage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWpm() {
        return wpm;
    }

    public void setWpm(String wpm) {
        this.wpm = wpm;
    }

    public String getRigth() {
        return rigth;
    }

    public void setRigth(String rigth) {
        this.rigth = rigth;
    }

    public String getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(String accuracy) {
        this.accuracy = accuracy;
    }

    public String getWrong() {
        return wrong;
    }

    public void setWrong(String wrong) {
        this.wrong = wrong;
    }
}
