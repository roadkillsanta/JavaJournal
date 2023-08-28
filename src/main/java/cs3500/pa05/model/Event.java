package cs3500.pa05.model;

import cs3500.pa05.model.json.SaveItem;
import java.time.DayOfWeek;
import java.time.LocalTime;

/**
 * Represents an Event in a Java Journal
 */
public class Event extends AbstractOccurrence {

  private final LocalTime startTime;
  private final LocalTime duration;

  /**
   * Initializes an Event
   *
   * @param name A given name
   * @param description A given description
   * @param day A given DayOfWeek
   * @param startTime A given startTime
   * @param duration A given duration
   * @param category A given category
   */
  public Event(String name, String description, DayOfWeek day, LocalTime startTime,
               LocalTime duration, Category category) {
    super(name, description, day, category);
    this.startTime = startTime;
    this.duration = duration;
  }

  /**
   * Serializes an Event as a SaveItem Record
   *
   * @return The SaveItem Record
   */
  public SaveItem toRecord() {
    return new SaveItem(Event.class, this.name, this.description,
        this.day, category.toRecord(), false,
        this.startTime.toString(), this.duration.toString());
  }


  /**
   * Gets the startTime of this Event
   *
   * @return The startTime
   */
  public LocalTime getStartTime() {
    return this.startTime;
  }

  /**
   * Gets the duration of this Event
   *
   * @return The duration
   */
  public LocalTime getDuration() {
    return this.duration;
  }

  @Override
  public String toString() {
    return name
        + ": "
        + startTime
        +
        startTime.plusHours(duration.getHour()).plusMinutes(duration.getMinute())
        + "\n"
        +
        description;
  }
}
