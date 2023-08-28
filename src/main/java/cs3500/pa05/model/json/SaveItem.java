package cs3500.pa05.model.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.DayOfWeek;

/**
 * Represents a SavedItem record
 *
 * @param type        The class type (Task or Event)
 * @param name        the name of the saved item
 * @param description the description of th saved item
 * @param day         the day of the saved item
 * @param category    the category of the saved item
 * @param complete    if the saved item is marked as complete or not
 * @param startTime   The start time of the Saved item
 * @param duration    the duration of the saved item
 */
public record SaveItem(
    @JsonProperty("type") Class type,
    @JsonProperty("name") String name,
    @JsonProperty("desc") String description,
    @JsonProperty("day") DayOfWeek day,
    @JsonProperty("category") CategoryRecord category,
    @JsonProperty("complete") boolean complete,
    @JsonProperty("startTime") String startTime,
    @JsonProperty("duration") String duration) {
}
