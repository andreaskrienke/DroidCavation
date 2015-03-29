package de.andreaskrienke.android.droidcavation.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Utility Helper class.
 */
public class Utility {

    public static final String myFormat = "MMM dd, yyyy"; //In which you need put here
    public static final SimpleDateFormat sdformat = new SimpleDateFormat(myFormat, Locale.US);

    public static Long persistDate(Date date) {
        if (date != null) {
            return date.getTime();
        }
        return null;
    }

    public static Date loadDate(long date) {
        if (date > 0) {
            return new Date(date);
        }
        return null;
    }
}
