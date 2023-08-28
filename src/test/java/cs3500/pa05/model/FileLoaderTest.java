package cs3500.pa05.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import cs3500.pa05.model.json.BujoWrapper;
import cs3500.pa05.model.json.CategoryRecord;
import cs3500.pa05.model.json.ProgramState;
import cs3500.pa05.model.json.SaveItem;
import cs3500.pa05.model.json.WeekRecord;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.DayOfWeek;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Represents the file loader tests
 */
public class FileLoaderTest {
  private ProgramState state;
  private File file;

  /**
   * Runs before each tests
   *
   * @throws IOException throws exception when needed
   */
  @BeforeEach
  public void setup() throws IOException {
    CategoryRecord categoryRecord = new CategoryRecord("Work", "RED");
    SaveItem saveItem = new SaveItem(SaveItem.class, "Meeting", "Important Meeting",
        DayOfWeek.MONDAY, categoryRecord, false, "10:00", "2h");
    WeekRecord weekRecord = new WeekRecord("Week 1", 5, 5,
        List.of(categoryRecord),
        List.of(saveItem));
    state = new ProgramState(List.of(weekRecord));
    file = Files.createTempFile("test", "json").toFile();
    file.deleteOnExit();
  }

  /**
   * Tests the save method
   */
  @Test
  public void testSave() {
    FileLoader.save(file, state);

    assertTrue(file.exists());
    assertTrue(file.length() > 0);
  }

  /**
   * Tests the save with password method
   */
  @Test
  public void testSaveWithPassword() {
    FileLoader.save(file, state, "password");

    assertTrue(file.exists());
    assertTrue(file.length() > 0);
  }

  /**
   * Tests the load method
   */
  @Test
  public void testLoad() {
    FileLoader.save(file, state);

    BujoWrapper loaded = FileLoader.load(file);
    assertNotNull(loaded);
  }

  /**
   * Tests when file doesn't exist
   */
  @Test
  public void testLoadWhenFileNotExists() {
    file = new File("/path/to/non/existent/file");

    BujoWrapper loaded = FileLoader.load(file);
    assertNull(loaded);
  }


  /**
   * Tests for the parse method
   */
  @Test
  public void testParse() {
    ObjectMapper mapper = new ObjectMapper();
    JsonNode node = mapper.convertValue(state, JsonNode.class);

    ProgramState parsed = FileLoader.parse(node.toString());
    assertEquals(state, parsed);
  }
}
