package com.uw.gearmax.gearmaxapi.util;

public class CommonUtility {

    public static String convertUrlParamValue(String val) {
        // for ex, convert abc-def to Abc Def
        StringBuilder sb = new StringBuilder();
        // split the val by "-"
        String[] words = val.split(CommonSymbol.DASH.val());
        for (int i = 0; i < words.length - 1; i++) {
            sb.append(words[i].substring(0, 1).toUpperCase() + words[i].substring(1));
            // use one space to connect/split each word
            sb.append(" ");
        }
        sb.append(words[words.length - 1].substring(0, 1).toUpperCase() + words[words.length - 1].substring(1));
        return sb.toString();
    }
}
