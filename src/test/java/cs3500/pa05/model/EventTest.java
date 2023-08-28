package cs3500.pa05.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import cs3500.pa05.model.json.SaveItem;
import java.time.DayOfWeek;
import java.time.LocalTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Represents the event test class
 */
public class EventTest {
  private Event event;

  /**
   * before each test
   */
  @BeforeEach
  public void setup() {
    event = new Event("rara", "rara description", DayOfWeek.MONDAY,
        LocalTime.of(10, 0), LocalTime.of(0, 59), Category.DEFAULT);
  }

  /**
   * Tests the get start time method
   */
  @Test
  public void testGetStartTime() {
    assertEquals(LocalTime.of(10, 0), event.getStartTime());
  }

  /**
   * Tests the get duration method
   */
  @Test
  public void testGetDuration() {
    assertEquals(LocalTime.of(0, 59), event.getDuration());
  }

  /**
   * Tests the to String method
   */
  @Test
  public void testToString() {
    String expectedString = "rara: 10:0010:59\nrara description";
    assertEquals(expectedString, event.toString());
  }

  /**
   * Tests the to Record method
   */
  @Test
  public void testToRecord() {
    SaveItem saveItem = event.toRecord();
    assertNotNull(saveItem);
    assertEquals(Event.class, saveItem.type());
    assertEquals("rara", saveItem.name());
    assertEquals("rara description", saveItem.description());
    assertEquals(DayOfWeek.MONDAY, saveItem.day());
  }
}
