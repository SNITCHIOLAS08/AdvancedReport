package dev.snitchiolas.advancedReport.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ColorUtils {
    public static String color(Object o) {
        if (o == null) {
            return "";
        }

        final String message = (o instanceof String) ? (String) o : o.toString();
        if (message.isEmpty() || message.isBlank()) {
            return "";
        }

        final char[] chars = message.toCharArray();
        for (int i = 0; i < chars.length - 1; ++i) {
            if (chars[i] == '&' && "0123456789AaBbCcDdEeFfKkLlMmNnOoRrXx".indexOf(chars[i + 1]) > -1) {
                chars[i] = '§';
                chars[i + 1] = Character.toLowerCase(chars[i + 1]);
            }
        }

        final String colored = new String(chars);
        final Matcher matcher = Pattern.compile("&#([A-Fa-f0-9]{6})").matcher(colored);
        final StringBuilder buffer = new StringBuilder(colored.length() + 32);

        while (matcher.find()) {
            final String group = matcher.group(1);
            matcher.appendReplacement(buffer, "§x§" + group.charAt(0) + "§" + group.charAt(1) + "§" + group.charAt(2) + "§" + group.charAt(3) + "§" + group.charAt(4) + "§" + group.charAt(5));
        }

        return matcher.appendTail(buffer).toString();
    }

    public static List<String> colorList(List<String> input) {
        List<String> result = new ArrayList<>();
        for (String line : input) {
            result.add(color(line));
        }
        return result;
    }
}