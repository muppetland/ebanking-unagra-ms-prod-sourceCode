package com.unagra.ebankingapi.utils.RandomValues;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class TrackingKeyRandom {
    public String getTrackingKeyValue(Integer vpNoChars) {
        Random r = new Random();
        String randomString = "";
        String randomValue = "123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        for (int i = 0; i < vpNoChars; i++) {
            randomString = randomString + randomValue.charAt(r.nextInt(randomValue.length()));
        }

        // we need to add current date to trackingKey...
        DateFormat dateFormat = new SimpleDateFormat("yyMMddHHmmss");
        Date date = new Date();

        // System.out.println("Length" + randomString.length());
        // System.out.println("String " + randomString);
        return "UNG-" + dateFormat.format(date) + "******" + randomString;
    }
}
