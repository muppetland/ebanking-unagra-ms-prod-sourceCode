package com.unagra.ebankingapi.utils.RandomValues;

import java.util.Random;
public class TransactionKeyRandom {
    public String getTransactionKeyValue(Integer vpNoChars) {
        Random r = new Random();
        String randomString = "";
        String randomValue = "123456789!@#$%&/()=¿*{}.[]?ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        for (int i = 0; i < vpNoChars; i++) {
            randomString = randomString + randomValue.charAt(r.nextInt(randomValue.length()));
        }
        //System.out.println("Length" + randomString.length());
        //System.out.println("String " + randomString);
        return randomString;
    }
}
