package cs3500.pa05.model.json;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents the Bujo Wrapper record class
 *
 * @param encrypted is the password encrypted?
 * @param hash      the hash for password
 * @param salt      adds random characters before and after password
 * @param data      the data
 */
public record BujoWrapper(
    @JsonProperty("encrypted") boolean encrypted,
    @JsonProperty("hash") String hash,
    @JsonProperty("salt") String salt,
    @JsonProperty("data") String data) {
}
