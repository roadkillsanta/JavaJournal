package cs3500.pa05.model;

import cs3500.pa05.model.json.CategoryRecord;
import cs3500.pa05.model.json.SaveItem;
import cs3500.pa05.model.json.WeekRecord;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents the week class
 */
public class Week {
  private int maxEvents;
  private int maxTasks;
  private String name;
  private final List<Category> categories = new ArrayList<>();
  private final List<AbstractOccurrence> items = new ArrayList<>();

  /**
   * Represents the week constructor
   *
   * @param record   the WeekRecord
   * @param template The templete
   */
  public Week(WeekRecord record, boolean template) {
    this(template ? record.name() + "(TEMPLATE)" : record.name(), record.maxEvents(),
        record.maxTasks(), record.categories());
    if (!template) {
      for (SaveItem item : record.occurrences()) {
        try {
          this.items.add(AbstractOccurrence.fromRecord(item));
        } catch (Exception e) {
          System.err.println("UNABLE TO RECALL ITEM: " + item.name());
        }
      }
    }
    for (AbstractOccurrence occurrence : items) {
      Category currentCategory = occurrence.getCategory();
      if (!categories.contains(currentCategory)) {
        categories.add(currentCategory);
      }
    }
  }

  /**
   * Represents the week controller
   *
   * @param name the name of the week
   */
  public Week(String name) {
    this(name, 6, 6, new ArrayList<>(Collections.singletonList(Category.DEFAULT.toRecord())));
  }

  /**
   * Represents another one of week's controller
   *
   * @param name      The name of the week
   * @param maxEvents the max event per day in the week
   * @param maxTasks  the max tasks per day in the week
   * @param records   the records of category
   */
  public Week(String name, int maxEvents, int maxTasks, List<CategoryRecord> records) {
    this.name = name;
    this.maxEvents = maxEvents;
    this.maxTasks = maxTasks;
    for (CategoryRecord categoryRecord : records) {
      this.categories.add(new Category(categoryRecord));
    }
    if (!categories.contains(Category.DEFAULT)) {
      categories.add(Category.DEFAULT);
    }
  }

  /**
   * Gets the name of the week
   *
   * @return the weeks name
   */
  public String getName() {
    return this.name;
  }

  /**
   * Sets the name for the week
   *
   * @param name the weeks name to set to
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Gets the max events for each day of the week
   *
   * @return the number of max events
   */
  public int getMaxEvents() {
    return this.maxEvents;
  }

  /**
   * Sets the max events for the week
   *
   * @param max the number to set to
   */
  public void setMaxEvents(int max) {
    this.maxEvents = max;
  }

  /**
   * gets the Max Tasks for each day of the week
   *
   * @return the max tasks
   */
  public int getMaxTasks() {
    return this.maxTasks;
  }

  /**
   * Sets the max tasks for the week
   *
   * @param max the number to set to
   */
  public void setMaxTasks(int max) {
    this.maxTasks = max;
  }

  /**
   * Gets the categories that the user created
   *
   * @return the list of category
   */
  public List<Category> getCategories() {
    return this.categories;
  }

  /**
   * Gets all the occurrences that the user created
   *
   * @return the list of all occurrences (events and tasks)
   */
  public List<AbstractOccurrence> getOccurrences() {
    return this.items;
  }

  /**
   * Converts the week to a  weekRecord
   *
   * @return the weekRecord
   */
  public WeekRecord toRecord() {
    List<CategoryRecord> categories = new ArrayList<>();
    for (Category category : this.categories) {
      categories.add(category.toRecord());
    }
    List<SaveItem> items = new ArrayList<>();
    for (AbstractOccurrence occurrence : this.items) {
      items.add(occurrence.toRecord());
    }
    return new WeekRecord(this.name, this.maxEvents, this.maxTasks, categories, items);
  }

  /**
   * Checks if the week is a has valid occurrence
   *
   * @param occurrence the occurrence
   * @return true if valid else false
   */
  public boolean isValidOccurence(AbstractOccurrence occurrence) {
    DayOfWeek day = occurrence.getDay();
    boolean isEvent = occurrence.isEvent();
    int count = 0;
    for (AbstractOccurrence item : items) {
      if (item.isEvent() == isEvent && item.day.equals(day)) {
        count++;
      }
    }
    if (isEvent) {
      return count < maxEvents;
    } else {
      return count < maxTasks;
    }
  }
}
