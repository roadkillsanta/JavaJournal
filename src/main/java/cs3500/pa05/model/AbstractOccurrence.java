package cs3500.pa05.model;

import cs3500.pa05.model.json.SaveItem;
import java.time.DayOfWeek;
import java.time.LocalTime;

/**
 * An abstract occurrence
 */
public abstract class AbstractOccurrence {
  protected String name;
  protected String description;
  protected DayOfWeek day;
  protected Category category;


  /**
   * @param name        The name of the Occurrence
   * @param description the description of the occurrence
   * @param day         the day of the week
   * @param category    the category of the occurrence
   */
  public AbstractOccurrence(String name, String description, DayOfWeek day, Category category) {
    this.name = name;
    this.description = description;
    this.day = day;
    this.category = category;
  }

  /**
   * Serializes an AbstractOccurrence to a Record
   *
   * @return The SaveItem Json Record
   */
  public abstract SaveItem toRecord();

  /**
   * Gets the name from this AbstractOccurrence
   *
   * @return The name
   */
  public String getName() {
    return this.name;
  }

  /**
   * Sets the name for this AbstractOccurrence
   *
   * @param name The given name
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Gets the description from this AbstractOccurrence
   *
   * @return The description
   */
  public String getDescription() {
    return this.description;
  }

  /**
   * Sets the description of this AbstractOccurrence to a given description
   *
   * @param description The given description
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Gets the day of the week from this AbstractOccurrence
   *
   * @return The DayOfWeek
   */
  public DayOfWeek getDay() {
    return this.day;
  }

  /**
   * Sets the day of this AbstractOccurrence
   *
   * @param day The DayOfWeek
   */
  public void setDay(DayOfWeek day) {
    this.day = day;
  }

  /**
   * Gets the category from this AbtractOccurrence
   *
   * @return The category
   */
  public Category getCategory() {
    if (this.category == null) {
      this.category = Category.DEFAULT;
    }
    return this.category;
  }

  /**
   * Sets the category of this AbstractOccurrence
   *
   * @param category The given category
   */
  public void setCategory(Category category) {
    this.category = category;
  }

  /**
   * Creates an AbstractOccurrence from a SaveItem Record
   *
   * @param item The given SaveItem record
   * @return The AbtractOccurrence created
   * @throws IllegalArgumentException Throws exception
   */
  public static AbstractOccurrence fromRecord(SaveItem item) throws IllegalArgumentException {
    if (item.type().equals(Event.class)) {
      return new Event(item.name(), item.description(), item.day(),
          LocalTime.parse(item.startTime()), LocalTime.parse(item.duration()),
          new Category(item.category()));
    } else if (item.type().equals(Task.class)) {
      return new Task(item.name(), item.description(), item.day(), new Category(item.category()),
          item.complete());
    }
    throw new IllegalArgumentException("Malformed SaveItem!");
  }

  /**
   * Whether this AbstractOccurrence is an Event
   *
   * @return The boolean value representing the if it is an Event
   */
  public boolean isEvent() {
    return Event.class.isAssignableFrom(this.getClass());
  }

  /**
   * Whether this AbstractOccurrence is a Task
   *
   * @return The boolean value representing the if it is a Task
   */
  public boolean isTask() {
    return Task.class.isAssignableFrom(this.getClass());
  }
}
