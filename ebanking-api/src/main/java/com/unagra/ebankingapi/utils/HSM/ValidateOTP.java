package com.unagra.ebankingapi.utils.HSM;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ValidateOTP {
    public Integer validarOTP(Long vpClienteID, String vpOTP, String vpURL) {
        // return...
        Integer vlResponsecode = 500;

        // JSON to sent...
        String vpJSON = "{" + "\"clienteID\"" + ":" + "\"" + vpClienteID + "\"," + "\"otp\"" + ":" + "\"" + vpOTP + "\""
                + "}";

        try {
            // URL and parameters for the connection, This particulary returns the
            // information sent...
            URL url = new URL(vpURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            // add headers....
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Content-Type", "application/json");
            OutputStream out = conn.getOutputStream();
            out.write(vpJSON.getBytes());
            out.flush();
            out.close();

            // get response...
            if (conn.getResponseCode() != 200) {
                vlResponsecode = conn.getResponseCode();
            } else {
                vlResponsecode = conn.getResponseCode();
            }

        } catch (Exception e) {
            // Ya nos la pelamos...
            e.printStackTrace();
            vlResponsecode = 500;
        }
        return vlResponsecode;
    }
}
