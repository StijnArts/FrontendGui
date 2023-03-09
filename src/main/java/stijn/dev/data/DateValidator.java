package stijn.dev.data;

import java.text.*;

public class DateValidator {

    public static boolean isValid(String dateStr,String dateFormat) {
        DateFormat sdf = new SimpleDateFormat(dateFormat);
        sdf.setLenient(false);
        try {
            sdf.parse(dateStr);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }
}