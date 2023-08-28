package cs3500.pa05.view;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;

/**
 * Represents the DurationSpinner class
 */
public class DurationSpinner implements Initializable {
  @FXML
  private Spinner<Integer> durationTimeInput2;

  /**
   * Need to override the initialize method inorder for the spinner to have set values
   *
   * @param location  The location of the Url
   *                  The location used to resolve relative paths for the root object, or
   *                  {@code null} if the location is not known.
   * @param resources to load from resources bundle
   *                  The resources used to localize the root object, or {@code null} if
   *                  the root object was not localized.
   */
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    SpinnerValueFactory<Integer> valueFactory =
        new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 59);
    valueFactory.setValue(1);
    durationTimeInput2.setValueFactory(valueFactory);
  }
}