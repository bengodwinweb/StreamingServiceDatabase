package com.streamingservicebackend.util;

public class StringUtil {

    public static String normalizeStringForQuery(String s) {
        return s.replaceAll("[\"'/+()*<>\\[\\]`~\\-_{}]", " ").replaceAll("[!,.:;=?|]", "").toLowerCase();
    }
}
