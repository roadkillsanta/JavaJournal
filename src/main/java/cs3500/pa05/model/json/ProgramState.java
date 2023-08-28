package cs3500.pa05.model.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Represents the program's state with a list of weekRecords
 *
 * @param weeks The list of weekRecords
 */
public record ProgramState(
    @JsonProperty("weeks") List<WeekRecord> weeks) {
}
