package cs3500.pa05.view;

import cs3500.pa05.controller.WeekController;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

/**
 * Represents a simple Whack-a-Mole GUI view.
 */
public class WeekLoader {

  FXMLLoader loader;

  /**
   * Represents the constructor of the WeekLoader
   *
   * @param controller given the week Controller
   */
  public WeekLoader(WeekController controller) {
    // look up and store the layout
    this.loader = new FXMLLoader();
    this.loader.setLocation(getClass().getClassLoader().getResource("week.fxml"));
    loader.setController(controller);
  }

  /**
   * @return Returns the Scene
   */
  public Scene load() {
    // load the layout
    try {
      return this.loader.load();
    } catch (IOException exc) {
      throw new IllegalStateException("Unable to load layout.");
    }
  }
}
