package com.mobiauto.backendpaulo.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Utils {

    public static String convertLocalDateTimeToString(LocalDateTime data, String formato) {

        if (data == null) {
            return "";
        }

        try {
            DateTimeFormatter parser = DateTimeFormatter.ofPattern(formato);
            return data.format(parser);
        } catch (Exception e) {
            return "";
        }
    }
}
