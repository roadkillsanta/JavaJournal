package cs3500.pa05.model.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Record for Week class
 *
 * @param name        name of Week
 * @param occurrences events and tasks occurring on that Week
 */
public record WeekRecord(
    @JsonProperty("name") String name,
    @JsonProperty("maxEvents") int maxEvents,
    @JsonProperty("maxTasks") int maxTasks,
    @JsonProperty("categories") List<CategoryRecord> categories,
    @JsonProperty("occurrences") List<SaveItem> occurrences) {
}
