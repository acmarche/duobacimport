package be.marche.duobac;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

class Utils {

    private static final String OLD_FORMAT = "dd/MM/yyyy";
    private static final String NEW_FORMAT = "yyyy-MM-dd";

    static String convertDate(String date) {

        if (date.length() < 5) {
            return null;
        }

        SimpleDateFormat sdf = new SimpleDateFormat(OLD_FORMAT, Locale.FRANCE);
        Date d = null;
        try {
            d = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        sdf.applyPattern(NEW_FORMAT);
        return sdf.format(d);
    }

    static float convertNumber(String poids) {

        try {
            return NumberFormat.getNumberInstance(Locale.FRANCE).parse(poids).floatValue();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return 0;
    }

    static int convertToInt(String number) {
        try {
            return Integer.parseInt(number);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
