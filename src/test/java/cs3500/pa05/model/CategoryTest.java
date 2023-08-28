package cs3500.pa05.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.time.DayOfWeek;
import java.time.LocalTime;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


/**
 * Represents tests and examples for the Category class
 */
public class CategoryTest {
  private Category category;

  /**
   * runs before each test
   */
  @BeforeEach
  public void setup() {
    category = new Category("#rara", Color.BLACK);
  }

  /**
   * testing add event method
   */
  @Test
  public void testAddEvent() {
    Event event = new Event("rara event", "rara description", DayOfWeek.MONDAY,
        LocalTime.of(10, 0), LocalTime.of(10, 0), Category.DEFAULT);
    category.addOccurence(event);

    AbstractOccurrence retrievedEvent = category.list.get(0);

    assertEquals(event, retrievedEvent);
    assertEquals("rara event", retrievedEvent.getName());
    assertEquals("rara description", retrievedEvent.getDescription());
    assertEquals(DayOfWeek.MONDAY, retrievedEvent.getDay());
  }

  /**
   * Testing add task method
   */
  @Test
  public void testAddTask() {
    Task task = new Task("rara task", "rara description", DayOfWeek.MONDAY,
        Category.DEFAULT, false);
    category.addOccurence(task);

    AbstractOccurrence retrievedTask = category.list.get(0);

    assertEquals(task, retrievedTask);
    assertEquals("rara task", retrievedTask.getName());
    assertEquals("rara description", retrievedTask.getDescription());
    assertEquals(DayOfWeek.MONDAY, retrievedTask.getDay());
  }

  /**
   * Testing CategoryToString
   */
  @Test
  public void testCategoryToString() {
    assertEquals("#rara", category.toString());

    Category defaultCategory = Category.DEFAULT;
    assertEquals("default (Default)", defaultCategory.toString());
  }

  /**
   * tests for the category equals method
   */
  @Test
  public void testCategoryEquals() {
    Category anotherCategory = new Category("#rara", Color.BLACK);
    assertEquals(category, anotherCategory);

    Category differentCategory = new Category("#diff", Color.BLACK);
    assertNotEquals(category, differentCategory);

    assertNotEquals(category, new Object());
  }

  /**
   * tests for the category record method
   */
  @Test
  public void testCategoryRecord() {
    Category categoryFromRecord = new Category(category.toRecord());
    assertEquals(category, categoryFromRecord);
  }

  /**
   * tests for the get name method
   */
  @Test
  public void testGetName() {
    assertEquals("#rara", category.getName());
  }

  /**
   * tests for the set name record
   */
  @Test
  public void testSetName() {
    category.setName("newName");
    assertEquals("newName", category.getName());
  }

  /**
   * tests for the get color method
   */
  @Test
  public void testGetColor() {
    assertEquals(Color.BLACK, category.getColor());
  }

  /**
   * tests for the set color method
   */
  @Test
  public void testSetColor() {
    category.setColor(Color.BLUE);
    assertEquals(Color.BLUE, category.getColor());
  }
}
