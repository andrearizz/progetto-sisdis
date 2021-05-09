package it.unical.progettosisdis.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.util.Arrays;
import java.util.Base64;

@Component
public class Encrypter {


    public String encrypt(String input, String secretKey) throws
            NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidAlgorithmParameterException, InvalidKeyException,
            IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException, InvalidParameterSpecException, UnsupportedEncodingException {

        byte[] IV = new byte[12];
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[20];
        random.nextBytes(bytes);
        byte[] saltBytes = bytes;

        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");

        // Derive the key
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        PBEKeySpec spec = new PBEKeySpec(secretKey.toCharArray(), saltBytes, 65556, 256);

        SecretKey secretKey1 = factory.generateSecret(spec);
        SecretKeySpec secret = new SecretKeySpec(secretKey1.getEncoded(), "AES");

        random.nextBytes(IV);

        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(16 * 8, IV);
        //Encrypt the plaintext

        cipher.init(Cipher.ENCRYPT_MODE, secret, gcmParameterSpec);

        byte[] encryptedTextBytes = cipher.doFinal(input.getBytes(StandardCharsets.UTF_8));


        //prepend salt and iv
        byte[] buffer = new byte[saltBytes.length + IV.length + encryptedTextBytes.length];

        System.arraycopy(saltBytes, 0, buffer, 0, saltBytes.length);
        System.arraycopy(IV, 0, buffer, saltBytes.length, IV.length);
        System.out.println(Arrays.toString(IV));
        System.arraycopy(encryptedTextBytes, 0, buffer,
                saltBytes.length + IV.length, encryptedTextBytes.length);
        return new org.apache.tomcat.util.codec.binary.Base64().encodeToString(buffer);


    }

    public String decrypt(String ciphertext, String secretKey)
            throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException {

        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");

        ByteBuffer buffer = ByteBuffer.wrap(new org.apache.tomcat.util.codec.binary.Base64().decode(ciphertext));

        byte[] saltBytes = new byte[20];
        buffer.get(saltBytes, 0, saltBytes.length);
        byte[] IV = new byte[12];
        buffer.get(IV, 0, IV.length);
        byte[] encryptedTextBytes = new byte[buffer.capacity() - saltBytes.length - IV.length];
        buffer.get(encryptedTextBytes);

        System.out.println(Arrays.toString(IV));

        // Derive the key
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        PBEKeySpec spec = new PBEKeySpec(secretKey.toCharArray(), saltBytes, 65556, 256);

        SecretKey secretKey1 = factory.generateSecret(spec);
        SecretKeySpec secret = new SecretKeySpec(secretKey1.getEncoded(), "AES");


        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(16*8, IV);


        cipher.init(Cipher.DECRYPT_MODE, secret, gcmParameterSpec);



        byte[] plainText = cipher.doFinal(encryptedTextBytes);
        return new String(plainText);
    }

}
