package com.philips.onespace.util;

import static com.philips.onespace.util.Constants.DATE_FORMAT_WITHOUT_TIMEZONE;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimeZone;

public class DateUtil {

	/**
	 * This method format the DateTime string provided.
	 *
	 * @param dateTime
	 * @return LocalDateTime, the local date time 
	 */
    public static LocalDateTime formatDateTime(String dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT_WITHOUT_TIMEZONE);
        return LocalDateTime.parse(dateTime,formatter);
    }

    /**
	 * This method format the ChronoLocalDateTime provided.
	 *
	 * @param dateTime
	 * @return dateTime, the datetime
	 */
    public static String formatDateTime(ChronoLocalDateTime<?> dateTime) {
        return dateTime.format(DateTimeFormatter.ofPattern(DATE_FORMAT_WITHOUT_TIMEZONE));
    }
    
    /**
	 * This method gets the current date and time in UTC format.
	 *
	 * @return dateTime, the UTC datetime
	 */
    public static String getCurrentDateUTC() throws ParseException {
    	SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT_WITHOUT_TIMEZONE); 
    	SimpleDateFormat localDateFormatter = new SimpleDateFormat(DATE_FORMAT_WITHOUT_TIMEZONE);  
    	formatter.setTimeZone(TimeZone.getTimeZone("UTC"));  
        return localDateFormatter.format(localDateFormatter.parse( formatter.format(new Date())));
    }
    
}
