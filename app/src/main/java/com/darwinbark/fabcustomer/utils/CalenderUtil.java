package com.darwinbark.fabcustomer.utils;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class CalenderUtil {
    static ArrayList<Calendar> calendarsDateAvailableArray = new ArrayList<>();
    public static int enddOfMonth;

    private CalenderUtil() {
    }

    /* CalenderUtil(){
         this.doctorIsAvailableOn=doctorIsAvailableOn;
     }*/

    @RequiresApi(api = Build.VERSION_CODES.O)
    static public Calendar[] getAvailableDatesOfCalenderArray(String[] doctorIsAvailableOn) {
        String[] availableDays = doctorIsAvailableOn;
        for (int i = 0; i <= availableDays.length - 1; i++)
            availableDays[i] = availableDays[i].toLowerCase();

        ArrayList<String> availableDaysList = new ArrayList<>(Arrays.asList(availableDays));

        int year = LocalDate.now().getYear();
        int month = LocalDate.now().getMonthValue();
        int date = 1;
        if (!isLeapYear(year)) {
             enddOfMonth = LocalDate.now().getMonth().length(false);
        }else{
             enddOfMonth = LocalDate.now().getMonth().maxLength();
        }
       int endOfMonth=enddOfMonth;

        int todayDate = LocalDate.now().getDayOfMonth();
        Log.d("mydate", "todays date is: " + todayDate);

        for (; todayDate <= endOfMonth; todayDate++) {
            String dateString;
            String dayname = getDaynameByDate(year, month, todayDate);

            if (isDoctorAvailable(availableDaysList, dayname)) {

                dateString = todayDate + "-" + month + "-" + year; //23-5-2021
                calendarsDateAvailableArray.add(getCalenderByDateString(dateString));

                System.out.println("is avaialable on " + dayname + " " + todayDate + " " + month + " " + year);

            } else
                System.out.println("not avaialable on " + dayname + " " + todayDate + " " + month + " " + year);
        }

        System.out.println("+++++========================******************________");


        Calendar[] cal = calendarsDateAvailableArray.toArray(new Calendar[0]);
        printAllAvailableCalendar(cal);


        return cal;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    static String getDaynameByDate(int year, int months, int dateWeek) {
        String day = null;
        LocalDate localDate = LocalDate.of(year, months, dateWeek);
        //LocalDate localDate =  LocalDate.withDayOfMonth(year, months, dateWeek);
        java.time.DayOfWeek dayOfWeek = localDate.getDayOfWeek();
        return dayOfWeek.toString();
    }

    static boolean isDoctorAvailable(ArrayList<String> availableDaysList, String dayName) {
        return availableDaysList.contains(dayName.toLowerCase());
    }


    static Calendar getCalenderByDateString(String strDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Date date = null;
        try {
            date = sdf.parse(strDate);
        } catch (ParseException e) {
            System.out.println("invalid date foramat string");
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }

    // print all available calender array
    static void printAllAvailableCalendar(Calendar[] calendarsAvailable) {
        for (int i = 0; i < calendarsAvailable.length; i++)
            System.out.println(calendarsAvailable[i].getTime());
    }

    public static boolean isLeapYear(int year) {
        if ((year % 400 == 0) || ((year % 4 == 0) && (year % 100 != 0))) {
            return true;
        } else {
            return false;
        }
    }

}
