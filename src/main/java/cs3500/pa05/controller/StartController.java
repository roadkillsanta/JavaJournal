package cs3500.pa05.controller;

import cs3500.pa05.model.Week;
import cs3500.pa05.model.json.ProgramState;
import cs3500.pa05.view.SaveLoadView;
import cs3500.pa05.view.SceneLoader;
import java.util.ArrayList;
import java.util.Collections;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Represents a Controller that starts the Journal
 */
public class StartController {
  private final Stage stage;
  @FXML
  private Button create;
  @FXML
  private Button open;
  private ProgramState state;

  /**
   * Initializes the StartController with a given Stage
   *
   * @param stage A Stage
   */
  public StartController(Stage stage) {
    this.stage = stage;
  }

  /**
   * Runs the StartController functionality
   *
   * @param scene the scene to add stuff on
   */
  public void run(Scene scene) {
    open.setVisible(false);
    create.setVisible(false);
    //fade in
    FadeTransition fadeIn = new FadeTransition(Duration.seconds(2), scene.getRoot());
    setFade(fadeIn, 0, 1);
    //fade out
    FadeTransition fadeOut = new FadeTransition(Duration.seconds(1), scene.getRoot());
    setFade(fadeOut, 1, 0);
    fadeIn.play();
    stage.setScene(scene);
    //After fade in, start fade out
    fadeIn.setOnFinished((e) -> {
      open.setVisible(true);
      create.setVisible(true);
    });
    open.setOnAction(event -> {
      this.state = SaveLoadView.openFile(this.stage);
      if (state != null) {
        fadeOut.play();
      }
    });
    create.setOnAction(event -> {
      this.state = new ProgramState(new ArrayList<>(
          Collections.singletonList(new Week("Week 1").toRecord())));
      if (SaveLoadView.saveAs(state, true)) {
        fadeOut.play();
      }
    });
    //After fade out, load actual content
    fadeOut.setOnFinished((e) -> {
      Controller main = new Controller(stage);
      SceneLoader loadMain = new SceneLoader(main, "main.fxml");
      loadMain.load();
      main.init(this.state);
      main.run();
    });
  }

  /**
   * Sets the FadeTransition
   *
   * @param fade      The given FadeTransition
   * @param fromValue The value for the FromValue
   * @param toValue   The value for the ToValue
   */
  private static void setFade(FadeTransition fade, int fromValue, int toValue) {
    fade.setFromValue(fromValue);
    fade.setToValue(toValue);
    fade.setCycleCount(1);
  }
}
