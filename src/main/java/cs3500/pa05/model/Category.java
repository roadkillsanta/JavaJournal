package cs3500.pa05.model;

import cs3500.pa05.model.json.CategoryRecord;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.paint.Color;

/**
 * Represents a category in a Java Journal
 */
public class Category {

  /**
   * Represents the constructor of Category
   */
  public static final Category DEFAULT = new Category("default", Color.WHITE);
  String name;
  Color color;
  List<AbstractOccurrence> list = new ArrayList<>();

  /**
   * Initializes a Category with a name and color
   *
   * @param name  The given name
   * @param color The given color
   */
  public Category(String name, Color color) {
    this.name = name;
    this.color = color;
  }

  /**
   * Initializes a Category with a CategoryRecord
   *
   * @param categoryRecord The given CategoryRecord
   */
  public Category(CategoryRecord categoryRecord) {
    this.name = categoryRecord.name();
    this.color = Color.web(categoryRecord.color());
  }

  /**
   * Adds an occurrence to this Category's list of occurrences
   *
   * @param occurrence The given AbstractOccurrence
   */
  public void addOccurence(AbstractOccurrence occurrence) {
    this.list.add(occurrence);
  }

  /**
   * Overrides equals to compare Categories by fields
   *
   * @param o A given Object
   * @return Whether the object equals this Category
   */
  @Override
  public boolean equals(Object o) {
    if (o instanceof Category c) {
      return c.name.equals(name);
    }
    return false;
  }

  /**
   * Returns this Category as a Record
   *
   * @return The CategoryRecord
   */
  public CategoryRecord toRecord() {
    return new CategoryRecord(this.name, this.color.toString());
  }

  /**
   * Overrides toString to create a String representation of a category
   *
   * @return The String representation
   */
  @Override
  public String toString() {
    return this.equals(Category.DEFAULT) ? this.name + " (Default)" : this.name;
  }

  /**
   * Gets the name from a Category
   *
   * @return The name
   */
  public String getName() {
    return this.name;
  }

  /**
   * Sets the name of Category from a given name
   *
   * @param name The given name
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Gets the color from a Category
   *
   * @return The color
   */
  public Color getColor() {
    return this.color;
  }

  /**
   * Sets the color for a Category from a given color
   *
   * @param color The given color
   */
  public void setColor(Color color) {
    this.color = color;
  }
}
