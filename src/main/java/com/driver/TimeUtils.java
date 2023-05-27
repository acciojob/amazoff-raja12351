package com.driver;

public class TimeUtils {
    private TimeUtils(){

    }

    public static int convertTime(String deliveryTime){
        String[] time = deliveryTime.split(":");
        return Integer.parseInt(time[0])*60 + Integer.parseInt(time[1]);
    }

    public static String convertTime(int deliveryTime){
        int hh = deliveryTime / 60;
        int mm = deliveryTime % 60;
        String HH = String.valueOf(hh);
        String MM = String.valueOf(mm);

        if(HH.length()==1){
            HH = "0"+HH;
        }
        if(MM.length()==1){
            MM = "0"+MM;
        }
        return String.format("%s:%s",HH,MM);
    }
}
