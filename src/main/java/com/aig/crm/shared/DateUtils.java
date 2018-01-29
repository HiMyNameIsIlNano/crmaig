package com.aig.crm.shared;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    private static String DATE_FORMAT = "dd/mm/yyyy";

    public static Date fromString(String date) {
        DateFormat df = new SimpleDateFormat(DATE_FORMAT);
        try {
            return df.parse(date);
        } catch (ParseException e) {
            throw new RuntimeException("Errore durante la lettura della data", e);
        }
    }
}
