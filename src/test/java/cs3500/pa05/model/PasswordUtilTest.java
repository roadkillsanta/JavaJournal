package cs3500.pa05.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Represents the password utils test class
 */

public class PasswordUtilTest {

  private SecretKey key1;
  private SecretKey key2;
  private String data;
  private String encryptedData;

  /**
   * Represents the setup to run before any tests
   *
   * @throws Exception throws Exception when needed
   */
  @BeforeEach
  public void setup() throws Exception {
    String password1 = "myPassword1";
    String salt1 = "mySalt1";
    key1 = PasswordUtil.keyFromPassSalt(password1, salt1);

    String password2 = "myPassword2";
    String salt2 = "mySalt2";
    key2 = PasswordUtil.keyFromPassSalt(password2, salt2);

    data = "someData";
    encryptedData = PasswordUtil.encrypt(key1, data);
  }

  /**
   * Represents the tests for the decrypt success method
   *
   * @throws Exception throws Exception when reasonable
   */
  @Test
  public void testDecryptSuccess() throws Exception {
    String decryptedData = PasswordUtil.decrypt(key1, encryptedData);
    assertEquals(data, decryptedData);
  }

  /**
   * To test when the Decry fails
   */
  @Test
  public void testDecryptFailureDueToIncorrectData() {
    String incorrectEncryptedData = "someIncorrectEncryptedData";
    assertThrows(
        IllegalBlockSizeException.class, () -> PasswordUtil.decrypt(key1, incorrectEncryptedData));
  }

  /**
   * To test another case of decrypt failing
   */
  @Test
  public void testDecryptFailureDueToIncorrectKey() {
    assertThrows(Exception.class, () -> PasswordUtil.decrypt(key2, encryptedData));
  }
}
