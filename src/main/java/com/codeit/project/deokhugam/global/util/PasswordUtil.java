package com.codeit.project.deokhugam.global.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PasswordUtil {

  public static String encrypt(String password) {

    try {

      MessageDigest md = MessageDigest.getInstance("SHA-256");
      md.update(password.getBytes());
      byte[] data = md.digest();

      StringBuilder sb = new StringBuilder();
      for(byte b : data) {
        sb.append(String.format("%02x", b));
      }

      return sb.toString();

    } catch(NoSuchAlgorithmException e) {

      log.error("암호화 실패 : ", e);
      throw new RuntimeException("sha-256 암호화 실패");

    }
  }

  public static boolean matches(String password, String encryptedPassword) {
    String hashed = encrypt(password);
    return hashed.equals(encryptedPassword);
  }
}
