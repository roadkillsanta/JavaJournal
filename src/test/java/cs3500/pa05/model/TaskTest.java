package cs3500.pa05.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import cs3500.pa05.model.json.CategoryRecord;
import cs3500.pa05.model.json.SaveItem;
import java.time.DayOfWeek;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Represents the task Test class
 */
class TaskTest {
  private Task task;
  private Task completedTask;


  @BeforeEach
  void setUp() {
    task = new Task("Test Task", "Test Description", DayOfWeek.MONDAY,
        Category.DEFAULT, false);
    completedTask = new Task("Completed Task", "Test Description",
        DayOfWeek.MONDAY, Category.DEFAULT, true);
  }

  @Test
  void toRecord() {
    SaveItem expectedSaveItem = new SaveItem(
        Task.class,
        "Test Task",
        "Test Description",
        DayOfWeek.MONDAY,
        new CategoryRecord("default", "0xffffffff"),
        false,
        null,
        null
    );
    assertEquals(expectedSaveItem, task.toRecord());
  }

  @Test
  void testComplete() {
    assertFalse(task.isComplete());
    task.complete();
    assertTrue(task.isComplete());
  }

  @Test
  void testUncomplete() {
    assertTrue(completedTask.isComplete());
    completedTask.uncomplete();
    assertFalse(completedTask.isComplete());
  }

  @Test
  void testConstructorWithComplete() {
    assertTrue(completedTask.isComplete());
  }
}
