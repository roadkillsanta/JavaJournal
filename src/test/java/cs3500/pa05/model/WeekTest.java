package cs3500.pa05.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import cs3500.pa05.model.json.CategoryRecord;
import cs3500.pa05.model.json.SaveItem;
import cs3500.pa05.model.json.WeekRecord;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Represents th test for the week class
 */
public class WeekTest {
  private Week week;
  private List<AbstractOccurrence> occurrences;

  /**
   * Runs before each test
   */
  @BeforeEach
  public void setup() {
    week = new Week("Week1");
    AbstractOccurrence occurrence = new Event("Event1", "description",
        DayOfWeek.MONDAY, LocalTime.MIDNIGHT, LocalTime.MAX, Category.DEFAULT);
    occurrences = new ArrayList<>();
    occurrences.add(occurrence);
  }

  /**
   * Tests the get name method
   */
  @Test
  public void testGetName() {
    assertEquals("Week1", week.getName());
  }

  /**
   * tests the set name method
   */
  @Test
  public void testSetName() {
    week.setName("Week2");
    assertEquals("Week2", week.getName());
  }

  /**
   * tests the get occurrences method
   */
  @Test
  public void testGetOccurrences() {
    assertTrue(week.getOccurrences().isEmpty());
    week.getOccurrences().addAll(occurrences);
    assertEquals(1, week.getOccurrences().size());
  }

  /**
   * Tests the to record method
   */
  @Test
  public void testToRecord() {
    week.getOccurrences().addAll(occurrences);
    WeekRecord record = week.toRecord();
    assertNotNull(record);
    assertEquals("Week1", record.name());
    assertEquals(1, record.occurrences().size());
  }

  /**
   * tests the get max events method
   */
  @Test
  public void testGetMaxEvents() {
    assertEquals(6, week.getMaxEvents());
  }

  /**
   * Tests the set max event method
   */
  @Test
  public void testSetMaxEvents() {
    week.setMaxEvents(7);
    assertEquals(7, week.getMaxEvents());
  }

  /**
   * Tests to get max tasks
   */
  @Test
  public void testGetMaxTasks() {
    assertEquals(6, week.getMaxTasks());
  }

  /**
   * Tests the set max tasks
   */
  @Test
  public void testSetMaxTasks() {
    week.setMaxTasks(7);
    assertEquals(7, week.getMaxTasks());
  }

  /**
   * Tests to get categories
   */
  @Test
  public void testGetCategories() {
    // The category should be at least the default category
    assertTrue(week.getCategories().size() >= 1);
  }

  /**
   * tests to see if its a valid occurrence
   */
  @Test
  public void testIsValidOccurrence() {
    week.getOccurrences().addAll(occurrences);
    week.setMaxEvents(2);
    week.setMaxTasks(1);
    AbstractOccurrence newEvent = new Event("New Event", "description",
        DayOfWeek.MONDAY, LocalTime.MIDNIGHT, LocalTime.MAX, Category.DEFAULT);
    AbstractOccurrence newTask = new Task("New Task", "description",
        DayOfWeek.MONDAY, Category.DEFAULT, true);

    assertTrue(week.isValidOccurence(newEvent));
    assertTrue(week.isValidOccurence(newTask));
    week.setMaxEvents(1);
    week.setMaxTasks(1);
  }

  /**
   * Test the weeks constructor
   */
  @Test
  public void testWeekConstructor() {
    SaveItem saveItem = new SaveItem(
        Task.class,
        "SaveItem1",
        "desc",
        DayOfWeek.MONDAY,
        new CategoryRecord("DEFAULT", "#FFFFFF"),
        true,
        LocalTime.MIN.toString(),
        LocalTime.MAX.toString()
    );
    List<SaveItem> saveItems = new ArrayList<>();
    saveItems.add(saveItem);

    CategoryRecord categoryRecord = new CategoryRecord("Category1", "#FFFFFF");
    List<CategoryRecord> categoryRecords = new ArrayList<>();
    categoryRecords.add(categoryRecord);
    WeekRecord weekRecord = new WeekRecord(
        "TestWeek", 3, 2, categoryRecords, saveItems);
    Week weekTemplate = new Week(weekRecord, true);
    assertEquals("TestWeek(TEMPLATE)", weekTemplate.getName());
    assertEquals(3, weekTemplate.getMaxEvents());
    assertEquals(2, weekTemplate.getMaxTasks());
    assertTrue(weekTemplate.getOccurrences().isEmpty());
    Week weekNonTemplate = new Week(weekRecord, false);
    assertEquals("TestWeek", weekNonTemplate.getName());
    assertEquals(3, weekNonTemplate.getMaxEvents());
    assertEquals(2, weekNonTemplate.getMaxTasks());
    assertEquals(1, weekNonTemplate.getOccurrences().size());
  }
}
