package android.trithe.sqlapp.utils;

import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class DateUtils {
    public static final String DATE_FORMAT = "yyyy/MM/dd";  //or use "M/d/yyyy"

    public static void parseDateFormat(TextView textView, String strDate) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = format.parse(strDate);
            SimpleDateFormat dateformat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
            textView.setText(dateformat.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public static void parseDateFormatVN(TextView textView, String strDate) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = format.parse(strDate);
            SimpleDateFormat dateformat = new SimpleDateFormat("yyyy");
            textView.setText(dateformat.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public static void parseDateFormatProfile(TextView textView, String strDate) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            Date date = format.parse(strDate);
            SimpleDateFormat dateformat = new SimpleDateFormat("yyyy/MM/dd", Locale.US);
            textView.setText(dateformat.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public static String parseDateFormatString(String strDate) {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        try {
            Date date = format.parse(strDate);
            SimpleDateFormat dateformat = new SimpleDateFormat("yyyy/MM/dd(E)", Locale.US);
            return dateformat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String parseSimpleDateFormatString(String strDate) {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        try {
            if (strDate != null) {
                Date date = format.parse(strDate);
                SimpleDateFormat dateformat = new SimpleDateFormat("yyyy/MM/dd", Locale.JAPAN);
                return dateformat.format(date);
            } else {
                strDate = "";
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static long getDaysBetweenDates(String start, String end) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.US);
        Date startDate, endDate;
        long numberOfDays = 0;
        try {
            startDate = dateFormat.parse(start);
            endDate = dateFormat.parse(end);
            numberOfDays = getUnitBetweenDates(startDate, endDate, TimeUnit.DAYS);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return numberOfDays;
    }

    private static long getUnitBetweenDates(Date startDate, Date endDate, TimeUnit unit) {
        long timeDiff = endDate.getTime() - startDate.getTime();
        return unit.convert(timeDiff, TimeUnit.MILLISECONDS);
    }

}
