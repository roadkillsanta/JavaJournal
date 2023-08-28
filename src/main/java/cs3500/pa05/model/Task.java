package cs3500.pa05.model;

import cs3500.pa05.model.json.SaveItem;
import java.time.DayOfWeek;

/**
 * Represents the task class
 */
public class Task extends AbstractOccurrence {

  private boolean complete;

  /**
   * Initializes a Task
   *
   * @param name A given name
   * @param description A given description
   * @param day A given DayOfWeek
   * @param category A given Category
   * @param complete If the task is complete
   */
  public Task(String name, String description, DayOfWeek day, Category category, boolean complete) {
    super(name, description, day, category);
    this.complete = complete;
  }

  /**
   * If this Task is complete
   *
   * @return The value of if the Task is complete
   */
  public boolean isComplete() {
    return complete;
  }

  /**
   * Sets the complete state to uncompleted
   */
  public void uncomplete() {
    this.complete = false;
  }

  /**
   * Sets the complete state to completed
   */
  public void complete() {
    this.complete = true;
  }

  /**
   * Serializes a Task as a SaveItem Record
   *
   * @return The SaveItem Record
   */
  public SaveItem toRecord() {
    return new SaveItem(Task.class, this.name, this.description, this.day, category.toRecord(),
        this.complete,
        null, null);
  }
}
