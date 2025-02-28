package com.example.footballresults.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Utility class for handling date formatting and conversion throughout the app.
 * Ensures consistent date format across all screens and provides helper methods
 * for date manipulation.
 */
public class DateFormatter {

    // Standard date format for display and storage
    private static final String DATE_FORMAT = "dd/MM/yyyy";

    // Formatter instance with default locale
    private static final SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());

    /**
     * Formats a Date object to the standard date string format (dd/MM/yyyy)
     *
     * @param date The Date object to format
     * @return Formatted date string
     */
    public static String formatDate(Date date) {
        if (date == null) {
            return "";
        }
        return formatter.format(date);
    }

    /**
     * Parses a date string in the standard format to a Date object
     *
     * @param dateStr The date string to parse
     * @return Date object or null if parsing fails
     */
    public static Date parseDate(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) {
            return null;
        }

        try {
            return formatter.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Gets today's date as a formatted string
     *
     * @return Today's date as a string in standard format
     */
    public static String getTodayAsString() {
        return formatDate(new Date());
    }

    /**
     * Checks if the provided date string is valid according to app's format
     *
     * @param dateStr Date string to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidDate(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) {
            return false;
        }

        try {
            // Try parsing the date and check constraints
            Date date = formatter.parse(dateStr);

            // Ensure format is correct by formatting back and comparing
            String reformatted = formatter.format(date);

            // Check if original and reformatted strings match
            if (!dateStr.equals(reformatted)) {
                return false;
            }

            // Additional validations if needed (e.g., not in future)
            Date today = new Date();

            // You might want to uncomment this if you want to prevent future dates
            // if (date.after(today)) {
            //     return false;
            // }

            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    /**
     * Formats a date string from one format to another
     *
     * @param dateStr The input date string
     * @param inputFormat The format of the input string
     * @param outputFormat The desired output format
     * @return Formatted date string or empty string if conversion failed
     */
    public static String convertDateFormat(String dateStr, String inputFormat, String outputFormat) {
        if (dateStr == null || dateStr.isEmpty()) {
            return "";
        }

        try {
            SimpleDateFormat inputFormatter = new SimpleDateFormat(inputFormat, Locale.getDefault());
            SimpleDateFormat outputFormatter = new SimpleDateFormat(outputFormat, Locale.getDefault());

            Date date = inputFormatter.parse(dateStr);
            return outputFormatter.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * Gets the day of week string for a given date
     *
     * @param dateStr Date string in standard format
     * @return Name of day of week, or empty string if parsing fails
     */
    public static String getDayOfWeek(String dateStr) {
        Date date = parseDate(dateStr);
        if (date == null) {
            return "";
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.getDefault());
        return dayFormat.format(date);
    }

    /**
     * Adds or subtracts days from a date string
     *
     * @param dateStr Original date string
     * @param days Number of days to add (positive) or subtract (negative)
     * @return Modified date string, or original string if parsing fails
     */
    public static String addDays(String dateStr, int days) {
        Date date = parseDate(dateStr);
        if (date == null) {
            return dateStr;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, days);

        return formatDate(calendar.getTime());
    }
}