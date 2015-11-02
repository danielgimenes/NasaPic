package br.com.dgimenes.nasapic.util;

import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import br.com.dgimenes.nasapic.R;

public class DateUtils {
    private static final String OLDER_THAN_ONE_WEEK_FORMAT = "MMM, dd";

    private static final String WEEK_DAY_FORMAT = "EEEE";

    private static final String OLDER_THAN_ONE_YEAR_FORMAT = "yyyy-MM-dd";

    private static final int ONE_DAY_IN_MILI = 1000 * 60 * 60 * 24;

    private static SimpleDateFormat olderThanOneWeekDateFormatter =
            new SimpleDateFormat(OLDER_THAN_ONE_WEEK_FORMAT);

    private static SimpleDateFormat weekDayDateFormatter =
            new SimpleDateFormat(WEEK_DAY_FORMAT);

    private static SimpleDateFormat olderThanOneYearDateFormatter =
            new SimpleDateFormat(OLDER_THAN_ONE_YEAR_FORMAT);

    public static String friendlyDateString(Context context, Date date) {
        Calendar calNow = Calendar.getInstance();
        long nowInMili = calNow.getTime().getTime();
        long targetInMili = date.getTime();
        long differenceInMili = nowInMili - targetInMili;

        if (differenceInMili < ONE_DAY_IN_MILI) {
            return context.getResources().getString(R.string.today);
        } else if (differenceInMili < ONE_DAY_IN_MILI * 2) {
            return context.getResources().getString(R.string.yesterday);
        } else if (differenceInMili < ONE_DAY_IN_MILI * 7) {
            String weekDay = weekDayDateFormatter.format(date);
            return StringUtils.capitalizeFirstLetter(weekDay);
        } else if (differenceInMili < (long) ONE_DAY_IN_MILI * 365) {
            String dateStr = olderThanOneWeekDateFormatter.format(date);
            return StringUtils.capitalizeFirstLetter(dateStr);
        } else {
            String dateStr = olderThanOneYearDateFormatter.format(date);
            return StringUtils.capitalizeFirstLetter(dateStr);
        }
    }
}
