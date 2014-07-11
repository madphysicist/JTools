/*
 * TimeUtilities.java
 *
 * Mad Physicist JTools Project (General Purpose Utilities)
 *
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 by Joseph Fox-Rabinovitz
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.madphysicist.tools.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Provides a set of utilities for manipulating {@code Date}s, {@code Calendar}s, and their {@code String}
 * representations.
 * <p>
 * This class can not be instantiated.
 *
 * @author Joseph Fox-Rabinovitz
 * @version 1.0.0, 22 Jun 2014 - J. Fox-Rabinovitz: Created
 * @since 1.0.2
 */
public class TimeUtilities
{
    /**
     * A {@link SimpleDateFormat} pattern string to represent a date and time. The format is as follows: {@value}. It is
     * used by the {@code nowString()} and {@code timeString()} methods.
     *
     * @since 1.0.0
     */
    public static final String DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss.SSS";
    
    /**
     * A {@link SimpleDateFormat} pattern string to represent a date and time. The format is as follows: {@value}. It is
     * used by the {@code simpleNowString()} and {@code simpleTimeString()} methods.
     *
     * @since 1.0.0
     */
    public static final String SIMPLE_DATETIME_PATTERN = "yyyyMMdd HHmmss.SSS";

    /**
     * A date formatter which uses the {@link #DATETIME_FORMAT} pattern string.
     *
     * @since 1.0.0
     */
    public static final DateFormat DATETIME_FORMAT = new SimpleDateFormat(DATETIME_PATTERN);

    /**
     * A date formatter which uses the {@link #SIMPLE_DATETIME_FORMAT} pattern string.
     *
     * @since 1.0.0
     */
    public static final DateFormat SIMPLE_DATETIME_FORMAT = new SimpleDateFormat(SIMPLE_DATETIME_PATTERN);

    /**
     * Private constructor to prevent the class from being instantiated.
     *
     * @since 1.0.0
     */
    private TimeUtilities() {}

    /**
     * <p>
     * Returns a string representation of the current date and time. The string is formatted as
     * <pre>
     *      {@value DATETIME_PATTERN}
     * </pre>
     * where {@code yyyy} is a four-digit year, {@code MM} is a two digit month number, {@code dd} is the two-digit day
     * of the month, {@code HH} is the two-digit hour of the day, {@code mm} is the two digit minute of the hour, {@code
     * ss} is the two digit second and {@code SSS} is the fractional part of the second in milliseconds.
     * </p>
     * <p>
     * This method is equivalent to {@link #timeString(java.util.Date) timeString(new Date())}.
     * </p>
     *
     * @return a string representing the date and time at which the method is invoked.
     * @see SimpleDateFormat
     * @since 1.0.0
     */
    public static String nowString()
    {
        return timeString(new Date());
    }

    /**
     * <p>
     * Returns a string representation of the current date and time. The string is formatted as
     * <pre>
     *      {@value SIMPLE_DATETIME_PATTERN}
     * </pre>
     * where {@code yyyy} is a four-digit year, {@code MM} is a two digit month number, {@code dd} is the two-digit day
     * of the month, {@code HH} is the two-digit hour of the day, {@code mm} is the two digit minute of the hour, {@code
     * ss} is the two digit second and {@code SSS} is the fractional part of the second in milliseconds.
     * </p>
     * <p>
     * This method is equivalent to {@link #simpleTimeString(java.util.Date) simpleTimeString(new Date())}.
     * </p>
     *
     * @return a string representing the date and time at which the method is invoked.
     * @see SimpleDateFormat
     * @since 1.0.0
     */
    public static String simpleNowString()
    {
        return simpleTimeString(new Date());
    }

    /**
     * Returns a string representation of the selected date and time. The string is formatted as
     * <pre>
     *      {@value DATETIME_PATTERN}
     * </pre>
     * where {@code yyyy} is a four-digit year, {@code MM} is a two digit month number, {@code dd} is the two-digit day
     * of the month, {@code HH} is the two-digit hour of the day, {@code mm} is the two digit minute of the hour, {@code
     * ss} is the two digit second and {@code SSS} is the fractional part of the second in milliseconds.
     *
     * @param cal a calendar representing the date and time.
     * @return a string representing the specified date and time.
     * @see SimpleDateFormat
     * @since 1.0.0
     */
    public static String timeString(Calendar cal)
    {
        return DATETIME_FORMAT.format(cal.getTime());
    }

    /**
     * Returns a string representation of the selected date and time. The string is formatted as
     * <pre>
     *      {@value DATETIME_PATTERN}
     * </pre>
     * where {@code yyyy} is a four-digit year, {@code MM} is a two digit month number, {@code dd} is the two-digit day
     * of the month, {@code HH} is the two-digit hour of the day, {@code mm} is the two digit minute of the hour, {@code
     * ss} is the two digit second and {@code SSS} is the fractional part of the second in milliseconds.
     *
     * @param date a date representing the date and time.
     * @return a string representing the specified date and time.
     * @see SimpleDateFormat
     * @since 1.0.0
     */
    public static String timeString(Date date)
    {
        return DATETIME_FORMAT.format(date);
    }

    /**
     * Returns a string representation of the current date and time. The string is formatted as
     * <pre>
     *      {@value SIMPLE_DATETIME_PATTERN}
     * </pre>
     * where {@code yyyy} is a four-digit year, {@code MM} is a two digit month number, {@code dd} is the two-digit day
     * of the month, {@code HH} is the two-digit hour of the day, {@code mm} is the two digit minute of the hour, {@code
     * ss} is the two digit second and {@code SSS} is the fractional part of the second in milliseconds.
     *
     * @param cal a calendar representing the date and time.
     * @return a string representing the specified date and time.
     * @see SimpleDateFormat
     * @since 1.0.0
     */
    public static String simpleTimeString(Calendar cal)
    {
        return SIMPLE_DATETIME_FORMAT.format(cal.getTime());
    }

    /**
     * Returns a string representation of the current date and time. The string is formatted as
     * <pre>
     *      {@value SIMPLE_DATETIME_PATTERN}
     * </pre>
     * where {@code yyyy} is a four-digit year, {@code MM} is a two digit month number, {@code dd} is the two-digit day
     * of the month, {@code HH} is the two-digit hour of the day, {@code mm} is the two digit minute of the hour, {@code
     * ss} is the two digit second and {@code SSS} is the fractional part of the second in milliseconds.
     *
     * @param date a date representing the date and time.
     * @return a string representing the specified date and time.
     * @see SimpleDateFormat
     * @since 1.0.0
     */
    public static String simpleTimeString(Date date)
    {
        return SIMPLE_DATETIME_FORMAT.format(date);
    }

    public static void main(String[] args)
    {
        System.out.println("Now String: " + nowString());
        System.out.println("Simple Now String: " + simpleNowString());

        System.out.println("String from Date: " + timeString(new Date()));
        System.out.println("Simple String from Date: " + simpleTimeString(new Date()));

        System.out.println("String from Calendar: " + timeString(new GregorianCalendar()));
        System.out.println("Simple String from Calendar: " + simpleTimeString(new GregorianCalendar()));

        System.out.println("String from Calendar with TimeZone: " + timeString(new GregorianCalendar(TimeZone.getDefault())));
        System.out.println("Simple String from Calendar with TimeZone: " + simpleTimeString(new GregorianCalendar(TimeZone.getDefault())));

        System.out.println("Locale: " + Locale.getDefault());
        System.out.println("TimeZone: " + TimeZone.getDefault());
    }
}
