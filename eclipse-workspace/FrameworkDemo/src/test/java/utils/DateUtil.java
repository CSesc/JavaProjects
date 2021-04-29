package utils;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    public static String getCurrentDate(){
        return new Date().toString();
    }

    public static String getCurrentTimeInMilis(){
        return String.valueOf(Calendar.getInstance().getTimeInMillis());
    }

    public static String getDateInFormat(String format){
        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        String dateInFormat = dateTime.format(formatter);
        return dateInFormat;
    }
}
