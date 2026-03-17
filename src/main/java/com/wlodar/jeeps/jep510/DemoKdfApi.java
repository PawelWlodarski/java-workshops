package com.wlodar.jeeps.jep510;

import com.wlodar.WorkshopPrinter;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.HexFormat;

public class DemoKdfApi {

    static void main() throws Exception {


        WorkshopPrinter.title("KDF usage - encrypt with derived key");

        var password = "secret-password".toCharArray();

        byte[] salt = new byte[16];
        new SecureRandom().nextBytes(salt);

        int iterations = 10_000;
        int keyLength = 256;

        // Step 1: derive key from password
        var spec = new PBEKeySpec(password, salt, iterations, keyLength);
        var factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        SecretKey derived = factory.generateSecret(spec);

        // Convert to AES key
        SecretKey aesKey = new SecretKeySpec(derived.getEncoded(), "AES");

        WorkshopPrinter.subtitle("Encrypting data");

        var cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, aesKey);

        byte[] encrypted = cipher.doFinal("Hello JEP 510".getBytes());

        WorkshopPrinter.print("encrypted (hex)",
                HexFormat.of().formatHex(encrypted));
    }
}
