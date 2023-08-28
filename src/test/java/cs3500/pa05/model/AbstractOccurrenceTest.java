package cs3500.pa05.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import cs3500.pa05.model.json.CategoryRecord;
import cs3500.pa05.model.json.SaveItem;
import java.time.DayOfWeek;
import java.time.LocalTime;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Represents tests and examples for the AbstractOccurrence class
 */
public class AbstractOccurrenceTest {
  private Task taskInstance;
  private Event eventInstance;
  private SaveItem eventSaveItem;
  private SaveItem taskSaveItem;

  private Task anotherTaskInstance;
  private Event anotherEventInstance;

  /**
   * The setup before each test
   */
  @BeforeEach
  public void setup() {
    taskInstance = new Task("rara task", "rara description",
        DayOfWeek.MONDAY, Category.DEFAULT, false);
    eventInstance = new Event("rara event", "rara description", DayOfWeek.MONDAY,
        LocalTime.of(10, 0), LocalTime.of(1, 0), Category.DEFAULT);
    CategoryRecord category = new CategoryRecord("DEFAULT", "0x00FFFFFF");
    this.eventSaveItem = new SaveItem(Event.class, "Test Event",
        "Test Event Description", DayOfWeek.MONDAY, category,
        false, "12:00", "02:00");
    this.taskSaveItem = new SaveItem(Task.class, "Test Task",
        "Test Task Description", DayOfWeek.MONDAY, category,
        false, "12:00", "02:00");

    anotherTaskInstance = new Task("another task", "another task description",
        DayOfWeek.MONDAY, Category.DEFAULT, false);
    anotherEventInstance = new Event("another event", "another event description",
        DayOfWeek.MONDAY,
        LocalTime.of(11, 0), LocalTime.of(2, 0), Category.DEFAULT);
  }

  /**
   * Test the is event method
   */
  @Test
  public void testIsEvent() {
    assertTrue(eventInstance.isEvent());
    assertFalse(taskInstance.isEvent());

    assertTrue(anotherEventInstance.isEvent());
    assertFalse(anotherTaskInstance.isEvent());
  }

  /**
   * test the is task method
   */
  @Test
  public void testIsTask() {
    assertTrue(taskInstance.isTask());
    assertFalse(eventInstance.isTask());

    assertTrue(anotherTaskInstance.isTask());
    assertFalse(anotherEventInstance.isTask());
  }

  /**
   * Test the get default Category method
   */
  @Test
  public void testGetCategoryDefault() {
    assertEquals(Category.DEFAULT, anotherTaskInstance.getCategory());
    assertEquals(Category.DEFAULT, anotherEventInstance.getCategory());
  }

  /**
   * Tests for the record invalid
   */
  @Test
  public void testFromRecordInvalidType() {
    SaveItem invalidSaveItem = new SaveItem(String.class, "Invalid",
        "Malformed SaveItem!",
        DayOfWeek.MONDAY, new CategoryRecord("DEFAULT", "0x00FFFFFF"),
        false, "12:00", "02:00");

    Exception exception = assertThrows(Exception.class, () ->
        AbstractOccurrence.fromRecord(invalidSaveItem));
    assertEquals("Malformed SaveItem!", exception.getMessage());
  }

  /**
   * Tests for the get name method
   */
  @Test
  public void testGetName() {
    assertEquals("rara task", taskInstance.getName());
    assertEquals("rara event", eventInstance.getName());
  }

  /**
   * Tests for the set name method
   */
  @Test
  public void testSetName() {
    taskInstance.setName("RARA");
    assertEquals("RARA", taskInstance.getName());

    eventInstance.setName("New Test Event");
    assertEquals("New Test Event", eventInstance.getName());
  }

  /**
   * tests for the get description method
   */
  @Test
  public void testGetDescription() {
    assertEquals("rara description", taskInstance.getDescription());
    assertEquals("rara description", eventInstance.getDescription());
  }

  /**
   * tests the set description method
   */
  @Test
  public void testSetDescription() {
    taskInstance.setDescription("New Test Task Description");
    assertEquals("New Test Task Description", taskInstance.getDescription());

    eventInstance.setDescription("New Test Event Description");
    assertEquals("New Test Event Description", eventInstance.getDescription());
  }

  /**
   * Tests the get day method
   */
  @Test
  public void testGetDay() {
    assertEquals(DayOfWeek.MONDAY, taskInstance.getDay());
    assertEquals(DayOfWeek.MONDAY, eventInstance.getDay());
  }

  /**
   * Test the set day method
   */
  @Test
  public void testSetDay() {
    taskInstance.setDay(DayOfWeek.TUESDAY);
    assertEquals(DayOfWeek.TUESDAY, taskInstance.getDay());

    eventInstance.setDay(DayOfWeek.TUESDAY);
    assertEquals(DayOfWeek.TUESDAY, eventInstance.getDay());
  }

  /**
   * Tests the get category method
   */
  @Test
  public void testGetCategory() {
    assertEquals(Category.DEFAULT, taskInstance.getCategory());
    assertEquals(Category.DEFAULT, eventInstance.getCategory());
  }

  /**
   * test the set category method
   */
  @Test
  public void testSetCategory() {
    Category newCategory = new Category("New Category",
        new Color(1, 1, 1, 1));

    taskInstance.setCategory(newCategory);
    assertEquals(newCategory, taskInstance.getCategory());

    eventInstance.setCategory(newCategory);
    assertEquals(newCategory, eventInstance.getCategory());
  }

  /**
   * tests for the record event method
   */
  @Test
  public void testFromRecordEvent() {
    AbstractOccurrence occurrence = AbstractOccurrence.fromRecord(eventSaveItem);
    assertTrue(occurrence instanceof Event);

    Event event = (Event) occurrence;
    assertEquals("Test Event", event.getName());
    assertEquals("Test Event Description", event.getDescription());
    assertEquals(DayOfWeek.MONDAY, event.getDay());
    assertEquals(LocalTime.parse("12:00"), event.getStartTime());
    assertEquals(LocalTime.parse("02:00"), event.getDuration());
  }

  /**
   * the tests for if the category is null
   */
  @Test
  public void testGetCategoryWhenNull() {
    AbstractOccurrence taskWithNullCategory = new Task("Null Category",
        " null", DayOfWeek.WEDNESDAY, null, false);
    AbstractOccurrence eventWithNullCategory = new Event(" Null Category",
        " null", DayOfWeek.WEDNESDAY, LocalTime.of(10, 0),
        LocalTime.of(1, 0), null);

    assertEquals(Category.DEFAULT, taskWithNullCategory.getCategory());
    assertEquals(Category.DEFAULT, eventWithNullCategory.getCategory());
  }

  /**
   * test for the  record task
   */
  @Test
  public void testFromRecordTask() {
    AbstractOccurrence occurrence = AbstractOccurrence.fromRecord(taskSaveItem);
    assertTrue(occurrence instanceof Task);

    Task task = (Task) occurrence;
    assertEquals("Test Task", task.getName());
    assertEquals("Test Task Description", task.getDescription());
    assertEquals(DayOfWeek.MONDAY, task.getDay());
    assertFalse(task.isComplete());
  }
}

