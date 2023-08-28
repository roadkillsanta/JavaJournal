package cs3500.pa05.model;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Utility for encrypting and decrypting data
 */
public class PasswordUtil {

  /**
   * Hashes password and salt
   *
   * @param salted password + salt byte array
   * @return hashed value of password + salt
   */
  public static String getHash(byte[] salted) {
    try {
      MessageDigest md = MessageDigest.getInstance("MD5");
      md.update(salted);
      byte[] bytes = md.digest();
      StringBuilder sb = new StringBuilder();
      for (byte abyte : bytes) {
        sb.append(Integer.toString((abyte & 0xff) + 0x100, 16).substring(1));
      }
      return sb.toString();
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Returns a salted password and salt
   *
   * @param password The password
   * @param salt     The salt
   * @return The salted array
   */
  public static byte[] salted(String password, String salt) {
    byte[] salted = new byte[password.getBytes().length + salt.getBytes().length];
    System.arraycopy(password.getBytes(), 0, salted, 0, password.getBytes().length);
    System.arraycopy(salt.getBytes(), 0, salted, password.getBytes().length,
        salt.getBytes().length);
    return salted;
  }

  /**
   * Gets the key from a password and salt
   *
   * @param password The password
   * @param salt     The salt
   * @return The key
   * @throws NoSuchAlgorithmException If the algorithm is not supported
   * @throws InvalidKeySpecException  If the KeySpec cannot produce a SecretKey
   */
  public static SecretKey keyFromPassSalt(String password, String salt)
      throws NoSuchAlgorithmException, InvalidKeySpecException {
    SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
    KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), 65536, 256);
    return new SecretKeySpec(factory.generateSecret(spec)
        .getEncoded(), "AES");
  }

  /**
   * Encrypts given data with a key
   *
   * @param key  The key
   * @param data The given data
   * @return The String encryption
   * @throws Exception If the cipher cannot be completed
   */
  public static String encrypt(SecretKey key, String data) throws Exception {
    Cipher c = Cipher.getInstance("AES");
    c.init(Cipher.ENCRYPT_MODE, key);
    byte[] encValue = c.doFinal(data.getBytes());
    return Base64.getEncoder().encodeToString(encValue);
  }

  /**
   * Decrypts given data from a key
   *
   * @param key  The key
   * @param data The given data
   * @return The String decryption
   * @throws Exception If the cipher cannot decrypt the data
   */
  public static String decrypt(SecretKey key, String data) throws Exception {
    Cipher cipher = Cipher.getInstance("AES");
    cipher.init(Cipher.DECRYPT_MODE, key);
    byte[] plainText = cipher.doFinal(Base64.getDecoder()
        .decode(data));
    return new String(plainText);
  }
}
