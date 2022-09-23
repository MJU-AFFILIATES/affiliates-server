package com.example.affiliates.Util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexPattern {
    public static boolean isRegexPwd(String target) {
        String regex = "^(?=.*[a-zA-z])(?=.*[0-9])(?=.*[$`~!@$!%*#^?&\\\\(\\\\)\\-_=+]).{8,16}$";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(target);
        return matcher.find();
    }
}
