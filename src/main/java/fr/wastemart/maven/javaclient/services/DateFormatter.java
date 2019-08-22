package fr.wastemart.maven.javaclient.services;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public class DateFormatter {
    public static String dateToString(String date) {
        List<String> patterns = Arrays.asList("yyyy-MM-dd'T'HH:mm:ss.SSSX", "yyyy-MM-dd'T'HH:mm", "yyyy-MM-dd");
        for (String pattern : patterns) {
            try {
                return String.valueOf(LocalDate.parse(date, DateTimeFormatter.ofPattern(pattern)));
            } catch (Exception ignored) {}
        }
        return "0000-00-00";

    }
}
