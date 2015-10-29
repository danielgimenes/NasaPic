package br.com.dgimenes.nasapic.util;

public class StringUtils {
    public static String limitString(String text, int maxCharacters) {
        if (maxCharacters < text.length()) {
            return text.substring(0, maxCharacters - 3) + "...";
        } else {
            return text;
        }
    }

    public static String addQuotes(String text) {
        return "\"" + text + "\"";
    }
}
