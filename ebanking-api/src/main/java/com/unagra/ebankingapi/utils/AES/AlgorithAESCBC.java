package com.unagra.ebankingapi.utils.AES;

import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.IvParameterSpec;

public class AlgorithAESCBC {
    public static byte[] encrypt(String plainText, String encryptionKey, String vector) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/pkcs5padding", "SunJCE");
        SecretKeySpec key = new SecretKeySpec(encryptionKey.getBytes("UTF-8"), "AES");
        cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(vector.getBytes("UTF-8")));
        return cipher.doFinal(plainText.getBytes("UTF-8"));
    }

    public static String decrypt(byte[] cipherText, String encryptionKey, String vector) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/pkcs5padding", "SunJCE");
        SecretKeySpec key = new SecretKeySpec(encryptionKey.getBytes("UTF-8"), "AES");
        cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(vector.getBytes("UTF-8")));
        return new String(cipher.doFinal(cipherText), "UTF-8");
    }

    public String resultadoDesencriptado(String vpTexto, String vpKey, String vector) {
        try {
            byte[] base64Decrypted = Base64.getDecoder().decode(vpTexto);
            String decryptedMsg = decrypt(base64Decrypted, vpKey, vector);
            return decryptedMsg;
        } catch (Exception ex) {
            System.out.println("Lo sentimos, hay un error en el Desencriptado de la información...");
            return "x";
        }
    }

    public String resultadoEncriptado(String vpTexto, String vpKey, String vector) {
        try {
            byte[] encryptedMsg = encrypt(vpTexto, vpKey, vector);
            String base64Encrypted = Base64.getEncoder().encodeToString(encryptedMsg);
            return base64Encrypted;
        } catch (Exception ex) {
            System.out.println("Lo sentimos, hay un error en el Encriptado de la información...");
            return "x";
        }
    }
}
