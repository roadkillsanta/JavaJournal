package cs3500.pa05.model.json;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Record for Category class
 *
 * @param name name of Category
 */
public record CategoryRecord(
    @JsonProperty("name") String name,
    @JsonProperty("color") String color) {
}
