package cs3500.pa05.model;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Represents tests and examples for the ParseableTimeValue class
 */
public class ParseableTimeValueTest {

  private ParseableTimeValue ptvMin;
  private ParseableTimeValue ptvHour;
  private StringProperty minString;
  private StringProperty hourString;

  /**
   * Sets up examples for the ParseableTimeValue tests
   */
  @BeforeEach
  public void setup() {
    minString = new SimpleStringProperty("5");
    ptvMin = new ParseableTimeValue(minString, 0, 59);
    hourString = new SimpleStringProperty("12");
    ptvHour = new ParseableTimeValue(hourString, 0, 23);
  }

  /**
   * Tests the method get
   */
  @Test
  public void testGet() {
    assertFalse(ptvMin.get());
    assertFalse(ptvHour.get());
    StringProperty invalid = new SimpleStringProperty("number");
    assertTrue(new ParseableTimeValue(invalid, 3, 10).get());
    assertTrue(new ParseableTimeValue(minString, 6, 20).get());
    assertTrue(new ParseableTimeValue(hourString, 1, 10).get());
  }

  /**
   * Tests other methods in the ParseableTimeValue class
   */
  @Test
  public void testOther() {
    assertNull(ptvHour.getValue());
  }
}