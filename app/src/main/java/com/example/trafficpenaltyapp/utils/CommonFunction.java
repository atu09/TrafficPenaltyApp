package com.example.trafficpenaltyapp.utils;

import android.util.Patterns;

import java.util.regex.Pattern;

public class CommonFunction {

    public static  boolean checkString(String s){

        if (s!=null && s!="null" && s.length() > 0){
            return true;
        }
        else
            return false;
    }
    public static  boolean checkMobilenumber(String s){

        if (s!=null && s!="null" && s.length() == 10){
            return true;
        }
        else
            return false;
    }
    public static  boolean checkPassword(String s){

        if (s!=null && s!="null" && s.length() >= 6){
            return true;
        }
        else
            return false;
    }
    public static  boolean checkEmail(String s){

        if (s!=null && s!="null" && s.length() > 0 && Pattern.matches(Patterns.EMAIL_ADDRESS.pattern(),s)){
            return true;
        }
        else
            return false;
    }



}
