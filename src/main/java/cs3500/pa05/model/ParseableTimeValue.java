package cs3500.pa05.model;

import javafx.beans.InvalidationListener;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableBooleanValue;

/**
 * Represents a boolean value if a LocalTime can be parsed
 */
public class ParseableTimeValue implements ObservableBooleanValue {

  private final StringProperty timeString;
  private final int max;
  private final int min;

  /**
   * Initializes the ParseableTimeValue with an hour and minute String Properties
   *
   * @param timeString the time
   * @param min min value
   * @param max max value
   */
  public ParseableTimeValue(StringProperty timeString, int min, int max) {
    this.timeString = timeString;
    this.min = min;
    this.max = max;
  }


  /**
   * Returns the current value of this {@code ObservableBooleanValue}.
   *
   * @return The current value
   */
  @Override
  public boolean get() {
    try {
      int time = Integer.parseInt(timeString.getValue());
      return min > time || time > max;
    } catch (NumberFormatException e) {
      return true;
    }
  }

  /**
   * Adds a {@link ChangeListener} which will be notified whenever the value
   * of the {@code ObservableValue} changes. If the same listener is added
   * more than once, then it will be notified more than once. That is, no
   * check is made to ensure uniqueness.
   * Note that the same actual {@code ChangeListener} instance may be safely
   * registered for different {@code ObservableValues}.
   * The {@code ObservableValue} stores a strong reference to the listener
   * which will prevent the listener from being garbage collected and may
   * result in a memory leak. It is recommended to either unregister a
   * listener by calling {@link #removeListener(ChangeListener)
   * removeListener} after use or to use an instance of
   *
   * @param listener The listener to register
   * @throws NullPointerException if the listener is null
   * @see #removeListener(ChangeListener)
   */
  @Override
  public void addListener(ChangeListener<? super Boolean> listener) {

  }

  /**
   * Adds an {@link InvalidationListener} which will be notified whenever the
   * {@code Observable} becomes invalid. If the same
   * listener is added more than once, then it will be notified more than
   * once. That is, no check is made to ensure uniqueness.
   * Note that the same actual {@code InvalidationListener} instance may be
   * safely registered for different {@code Observables}.
   * The {@code Observable} stores a strong reference to the listener
   * which will prevent the listener from being garbage collected and may
   * result in a memory leak. It is recommended to either unregister a
   * listener by calling {@link #removeListener(InvalidationListener)
   * removeListener} after use or to use an instance of
   *
   * @param listener The listener to register
   * @throws NullPointerException if the listener is null
   * @see #removeListener(InvalidationListener)
   */
  @Override
  public void addListener(InvalidationListener listener) {

  }

  /**
   * Removes the given listener from the list of listeners that are notified
   * whenever the value of the {@code ObservableValue} changes.
   * If the given listener has not been previously registered (i.e. it was
   * never added) then this method call is a no-op. If it had been previously
   * added then it will be removed. If it had been added more than once, then
   * only the first occurrence will be removed.
   *
   * @param listener The listener to remove
   * @throws NullPointerException if the listener is null
   * @see #addListener(ChangeListener)
   */
  @Override
  public void removeListener(ChangeListener<? super Boolean> listener) {

  }

  /**
   * Removes the given listener from the list of listeners, that are notified
   * whenever the value of the {@code Observable} becomes invalid.
   * If the given listener has not been previously registered (i.e. it was
   * never added) then this method call is a no-op. If it had been previously
   * added then it will be removed. If it had been added more than once, then
   * only the first occurrence will be removed.
   *
   * @param listener The listener to remove
   * @throws NullPointerException if the listener is null
   * @see #addListener(InvalidationListener)
   */
  @Override
  public void removeListener(InvalidationListener listener) {

  }

  /**
   * Returns the current value of this {@code ObservableValue}
   *
   * @return The current value
   */
  @Override
  public Boolean getValue() {
    return null;
  }
}