package com.example.trafficpenaltyapp.utils;

import android.util.Patterns;

import java.util.regex.Pattern;

public class CommonFunction {

    public static boolean checkString(String s) {

        if (s != null && !s.equalsIgnoreCase("null") && s.length() > 0) {
            return true;
        }
        return false;
    }

    public static boolean checkMobilenumber(String s) {

        if (s != null && !s.equalsIgnoreCase("null") && s.length() == 10) {
            return true;
        }
        return false;
    }

    public static boolean checkverificationcode(String s) {

        if (s != null && !s.equalsIgnoreCase("null") && s.length() == 4) {
            return true;
        }
        return false;
    }

    public static boolean checkPassword(String s) {

        if (s != null && !s.equalsIgnoreCase("null") && s.length() >= 3) {
            return true;
        }
        return false;
    }

    public static boolean checkEmail(String s) {

        if (s != null && !s.equalsIgnoreCase("null") && s.length() > 0 && Pattern.matches(Patterns.EMAIL_ADDRESS.pattern(), s)) {
            return true;
        }
        return false;
    }


}
