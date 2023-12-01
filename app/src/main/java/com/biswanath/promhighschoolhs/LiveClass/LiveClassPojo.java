package com.biswanath.promhighschoolhs.LiveClass;

public class LiveClassPojo {
    private String UpcomingClassName;
    private String Date;
    private String Time;

    public LiveClassPojo() {
    }

    public LiveClassPojo(String upcomingClassName, String date, String time) {
        UpcomingClassName = upcomingClassName;
        Date = date;
        Time = time;
    }

    public String getUpcomingClassName() {
        return UpcomingClassName;
    }

    public void setUpcomingClassName(String upcomingClassName) {
        UpcomingClassName = upcomingClassName;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }
}
