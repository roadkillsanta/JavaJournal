package cs3500.pa05.view;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

/**
 * Represents a simple Whack-a-Mole GUI view.
 */
public class SceneLoader {

  FXMLLoader loader;

  /**
   * Represents the constructor for SceneLoader
   *
   * @param controller controller of the object
   * @param file       given file name
   */
  public SceneLoader(Object controller, String file) {
    // look up and store the layout
    this.loader = new FXMLLoader();
    this.loader.setLocation(getClass().getClassLoader().getResource(file));
    this.loader.setController(controller);
  }

  /**
   * Loads scene
   *
   * @return the layout
   */
  public Scene load() {
    try {
      return this.loader.load();
    } catch (IOException exc) {
      throw new IllegalStateException("Unable to load layout.");
    }
  }
}
