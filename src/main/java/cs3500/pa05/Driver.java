package cs3500.pa05;

import cs3500.pa05.controller.StartController;
import cs3500.pa05.view.SceneLoader;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Driver class (entry point) for the application
 */
public class Driver extends Application {
  /**
   * To start the stage
   *
   * @param stage the primary stage for this application, onto which
   *              the application scene can be set.
   *              Applications may create other stages, if needed, but they will not be
   *              primary stages.
   */
  public void start(Stage stage) {
    StartController controller = new StartController(stage);
    try {
      SceneLoader loader = new SceneLoader(controller, "splash.fxml");
      Scene scene = loader.load();
      stage.setTitle("Java Journal");
      controller.run(scene);
      stage.show();
    } catch (IllegalStateException exc) {
      System.err.println("Unable to load GUI.");
    }
  }

  /**
   * Entry point
   *
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    launch();
  }
}
