package cs3500.pa05.model;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import cs3500.pa05.model.json.BujoWrapper;
import cs3500.pa05.model.json.ProgramState;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

/**
 * Represents a class that loads files for a JavaJournal
 */
public class FileLoader {

  /**
   * Saves a ProgramState to a file
   *
   * @param path The file
   * @param state The ProgramState
   */
  public static void save(File path, ProgramState state) {
    ObjectMapper mapper = new ObjectMapper();
    BujoWrapper wrapper =
        new BujoWrapper(false, null, null,
            mapper.convertValue(state, JsonNode.class).toString());
    try {
      FileWriter writer = new FileWriter(path);
      JsonNode serialized = mapper.convertValue(wrapper, JsonNode.class);
      writer.write(serialized.toString());
      writer.flush();
      writer.close();
    } catch (IOException e) {
      System.err.println("Unable to write file!");
    }
  }

  /**
   * Saves a ProgramState to a file that is encrypted with a password
   *
   * @param path The file
   * @param state The ProgramState
   * @param password The password
   */
  public static void save(File path, ProgramState state, String password) {
    String salt = String.valueOf(new Random().nextInt());
    ObjectMapper mapper = new ObjectMapper();
    try {
      BujoWrapper wrapper =
          new BujoWrapper(true,
              PasswordUtil.getHash(PasswordUtil.salted(password, salt)), salt,
              PasswordUtil.encrypt(PasswordUtil.keyFromPassSalt(password, salt),
                  mapper.convertValue(state, JsonNode.class).toString()));
      FileWriter writer = new FileWriter(path);
      JsonNode serialized = mapper.convertValue(wrapper, JsonNode.class);
      writer.write(serialized.toString());
      writer.flush();
      writer.close();
    } catch (IOException e) {
      System.err.println("Unable to write file!");
    } catch (Exception e) {
      System.err.println("Unable to encrypt file!");
    }
  }

  /**
   * Loads the contents of a file as a BujoWrapper
   *
   * @param file The file
   * @return The BujoWrapper
   */
  public static BujoWrapper load(File file) {
    ObjectMapper mapper = new ObjectMapper();
    try {
      FileReader reader = new FileReader(file);
      JsonParser parser = mapper.getFactory().createParser(reader);
      return parser.readValueAs(BujoWrapper.class);
    } catch (IOException e) {
      System.err.println("Could not load file!");
    }
    return null;
  }

  /**
   * Parses data from a String as a ProgramState
   *
   * @param data The data
   * @return The ProgramState
   */
  public static ProgramState parse(String data)  {
    ObjectMapper mapper = new ObjectMapper();
    try {
      return mapper.getFactory().createParser(data).readValueAs(ProgramState.class);
    } catch (IOException e) {
      throw new RuntimeException(e.getMessage());
    }
  }
}
