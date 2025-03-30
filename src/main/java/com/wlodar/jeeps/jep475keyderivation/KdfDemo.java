package com.wlodar.jeeps.jep475keyderivation;

import org.bouncycastle.crypto.DerivationFunction;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.generators.HKDFBytesGenerator;
import org.bouncycastle.crypto.params.HKDFParameters;

import javax.crypto.KDF;
import javax.crypto.SecretKey;
import javax.crypto.spec.HKDFParameterSpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.util.HexFormat;


public class KdfDemo {
    public static void main(String[] args) throws Exception {
        legacyBouncyCastle();
        updated();
    }

    private static  void updated() throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        byte[] ikm = "initialKeyMaterial".getBytes();
        byte[] salt = "randomSaltValue".getBytes();
        byte[] info = "contextInfo".getBytes();

        // Create the parameter spec: extract then expand
        AlgorithmParameterSpec params = HKDFParameterSpec.ofExtract()
                .addIKM(ikm)
                .addSalt(salt)
                .thenExpand(info, 32); // output key length

        // Create the KDF instance using preview API
        KDF hkdf = KDF.getInstance("HKDF-SHA256");

        // Derive a key for AES
        SecretKey key = hkdf.deriveKey("AES", params);

        System.out.println("Derived key new way: " + HexFormat.of().formatHex(key.getEncoded()));
    }

    private static void legacyBouncyCastle() throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] ikm = "initialKeyMaterial".getBytes();
        byte[] salt = "randomSaltValue".getBytes();
        byte[] info = "contextInfo".getBytes();
        int length = 32;

        // Set up HKDF with SHA-256
        DerivationFunction hkdf = new HKDFBytesGenerator(new SHA256Digest());
        hkdf.init(new HKDFParameters(ikm, salt, info));

        byte[] outputKey = new byte[length];
        hkdf.generateBytes(outputKey, 0, length);

        System.out.println("Derived key (legacy way): " +
                HexFormat.of().formatHex(outputKey));
    }
}
