package br.com.dgimenes.nasapic.service;

import java.util.Calendar;
import java.util.Date;

public class BestPicturesConfig {

    public static final Date[] bestPicsDates = {
            date(2015, 10, 27),
            date(2015, 10, 24),
            date(2015, 10, 23),
            date(2015, 9, 25),
            date(2015, 8, 21),
            date(2015, 8, 20),
            date(2015, 8, 19),
            date(2015, 7, 24),
            date(2014, 12, 5),
            date(2014, 12, 1),
            date(2014, 10, 30),
            date(2014, 9, 20),
            date(2014, 8, 10),
            date(2014, 5, 19),
            date(2014, 5, 4),
            date(2014, 5, 2),
            date(2012, 3, 25),
            date(2011, 5, 17),
            date(2007, 10, 18),
    };

    private static Date date(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month - 1, day); // month is zero-based
        return cal.getTime();
    }
}
