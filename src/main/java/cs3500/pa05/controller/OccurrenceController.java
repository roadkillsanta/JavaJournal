package cs3500.pa05.controller;

import static javafx.beans.binding.Bindings.isEmpty;

import cs3500.pa05.model.AbstractOccurrence;
import cs3500.pa05.model.Category;
import cs3500.pa05.model.Event;
import cs3500.pa05.model.ParseableTimeValue;
import cs3500.pa05.model.Task;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Represents the occurrence Controller class
 */
public class OccurrenceController {
  private final Stage parent;
  private Scene scene;
  private boolean isEvent = true;
  @FXML
  private TextField nameInput;
  @FXML
  private TextField hourStartTimeInput;
  @FXML
  private TextField minuteStartTimeInput;
  @FXML
  private TextArea description;
  @FXML
  private Button eventButton;
  @FXML
  private Button taskButton;
  @FXML
  private Button submitButton;
  @FXML
  private Button deleteButton;
  @FXML
  private Button daySun;
  @FXML
  private Button dayMon;
  @FXML
  private Button dayTue;
  @FXML
  private Button dayWed;
  @FXML
  private Button dayThu;
  @FXML
  private Button dayFri;
  @FXML
  private Button daySat;
  @FXML
  private Spinner<Integer> hourDurationInput;
  @FXML
  private Spinner<Integer> minuteDurationInput;
  @FXML
  private ChoiceBox<Category> categoryChoices;
  private final Map<Button, DayOfWeek> dayToBtn;
  private DayOfWeek current;
  private Stage currentStage = null;

  /**
   * Initializes the controller for the Occurrences with a Stage
   *
   * @param parent The parent Stage
   */
  public OccurrenceController(Stage parent) {
    this.parent = parent;
    this.dayToBtn = new HashMap<>();
  }

  /**
   * Initializes the OccurrenceController controls
   */
  public void init() {
    initSpinner(hourDurationInput, minuteDurationInput);
    taskButton.setOnAction(event -> taskButtonAction());
    eventButton.setOnAction(event -> eventButtonAction());
    bindEventSubmitButton();
    dayToBtn.put(daySun, DayOfWeek.SUNDAY);
    dayToBtn.put(dayMon, DayOfWeek.MONDAY);
    dayToBtn.put(dayTue, DayOfWeek.TUESDAY);
    dayToBtn.put(dayWed, DayOfWeek.WEDNESDAY);
    dayToBtn.put(dayThu, DayOfWeek.THURSDAY);
    dayToBtn.put(dayFri, DayOfWeek.FRIDAY);
    dayToBtn.put(daySat, DayOfWeek.SATURDAY);
  }

  /**
   * checks if the binding is correct
   *
   * @param timeInput the time input text field
   * @param min       the min
   * @param max       the max
   * @return if the binding works or not
   */
  private static BooleanBinding startTimeBinding(TextField timeInput, int min, int max) {
    return Bindings.or(Bindings.isEmpty(timeInput.textProperty()),
        new ParseableTimeValue(timeInput.textProperty(), min, max));
  }

  /**
   * binding for submit button
   */
  private void bindEventSubmitButton() {
    submitButton.disableProperty().bind(Bindings.or(startTimeBinding(minuteStartTimeInput, 0, 59),
            startTimeBinding(hourStartTimeInput, 0, 23))
        .or(isEmpty(nameInput.textProperty())));
  }

  /**
   * Binding for task submit button
   */
  private void bindTaskSubmitButton() {
    submitButton.disableProperty().bind(Bindings.isEmpty(nameInput.textProperty()));
  }

  /**
   * Initializes an hour and minute Spinner of Integer
   *
   * @param hourSpinner The Spinner for the hour
   * @param minSpinner  The Spinner for the minute
   */
  private static void initSpinner(Spinner<Integer> hourSpinner, Spinner<Integer> minSpinner) {
    SpinnerValueFactory<Integer> hourSpinnerValues =
        new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23);
    hourSpinner.setValueFactory(hourSpinnerValues);
    SpinnerValueFactory<Integer> minuteSpinnerValues =
        new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59);
    minSpinner.setValueFactory(minuteSpinnerValues);
  }

  /**
   * Shows the stage for the OccurrenceController
   */
  private void showModal() {
    Scene scene = this.scene;
    final Stage dialog = new Stage();
    dialog.initModality(Modality.APPLICATION_MODAL);
    dialog.initOwner(parent);
    dialog.setScene(scene);
    dialog.show();
    this.currentStage = dialog;
  }

  /**
   * Displays the mini-view of an Occurrence
   *
   * @param issuer     The WeekController of the Occurrence
   * @param occurrence The Occurrence
   */
  public void viewOccurrence(WeekController issuer, AbstractOccurrence occurrence) {
    this.clear(issuer);
    this.updateInfo(occurrence);
    this.resetDayButtons(occurrence.getDay(), false);
    this.nameInput.setEditable(false);
    this.description.setEditable(false);
    this.hourStartTimeInput.setEditable(false);
    this.minuteStartTimeInput.setEditable(false);
    this.categoryChoices.setDisable(true);
    this.submitButton.setText("Edit");
    this.submitButton.setOnAction(event -> {
      this.currentStage.close();
      this.editEvent(issuer, occurrence, occurrence.getDay());
    });
    this.deleteButton.setText("Close");
    this.deleteButton.setOnAction(event -> {
      this.currentStage.close();
    });
    this.deleteButton.setVisible(true);
    this.showModal();
  }

  /**
   * Adds an Occurrence to a WeekController for a selected Day
   *
   * @param issuer The WeekController
   * @param day    The selected day
   */
  public void addOccurrence(WeekController issuer, DayOfWeek day) {
    this.current = day;
    this.clear(issuer);
    this.resetDayButtons(day, true);
    submitButton.setOnAction(event -> {
      AbstractOccurrence occurrence = makeOccurrence();
      issuer.addOccurrence(occurrence);
      currentStage.close();
    });
    this.deleteButton.setText("Cancel");
    this.deleteButton.setOnAction(event -> {
      this.currentStage.close();
    });
    this.showModal();
  }


  /**
   * Modal call for editing an occurrence
   *
   * @param issuer     WeekController that issued the call
   * @param occurrence Occurrence to be edited
   * @param day        day which the Occurrence belongs to
   */
  public void editEvent(WeekController issuer, AbstractOccurrence occurrence, DayOfWeek day) {
    this.current = day;
    this.clear(issuer);
    this.updateInfo(occurrence);
    this.resetDayButtons(day, true);
    submitButton.setOnAction(event -> {
      AbstractOccurrence newOccurrence = makeOccurrence();
      issuer.editOccurrence(occurrence, newOccurrence);
      currentStage.close();
    });
    deleteButton.setOnAction(event -> {
      issuer.removeOccurrence(occurrence);
      currentStage.close();
    });
    this.showModal();
  }

  /**
   * Makes an Occurrence from the controls
   *
   * @return The Occurrence
   */
  private AbstractOccurrence makeOccurrence() {
    String name = nameInput.getText();
    String desc = description.getText();
    Category category = categoryChoices.getSelectionModel().getSelectedItem();
    if (isEvent) {
      int hour = Integer.parseInt(hourStartTimeInput.getText());
      int min = Integer.parseInt(minuteStartTimeInput.getText());
      return new Event(name, desc, this.current,
          LocalTime.of(hour, min),
          LocalTime.of(hourDurationInput.getValue(), minuteDurationInput.getValue()), category);
    } else {
      return new Task(name, desc, this.current, category, false);
    }
  }

  /**
   * Changes the controls for the OccurrenceController based on the press of the Event button
   */
  private void eventButtonAction() {
    this.isEvent = true;
    bindEventSubmitButton();
    hourStartTimeInput.setDisable(false);
    minuteStartTimeInput.setDisable(false);
    hourDurationInput.setDisable(false);
    minuteDurationInput.setDisable(false);
    taskButton.setStyle("--fx-background-color: transparent;");
    eventButton.setStyle("-fx-background-color: lightgray;");

  }

  /**
   * Changes the controls for the OccurrenceController based on the press of the Task button
   */
  private void taskButtonAction() {
    this.isEvent = false;
    bindTaskSubmitButton();
    hourStartTimeInput.setDisable(true);
    minuteStartTimeInput.setDisable(true);
    hourStartTimeInput.setText("");
    minuteStartTimeInput.setText("");
    hourDurationInput.setDisable(true);
    minuteDurationInput.setDisable(true);
    eventButton.setStyle("--fx-background-color: transparent;");
    taskButton.setStyle("-fx-background-color: lightgray;");
  }

  /**
   * Sets the Scene for this OccurrenceController
   *
   * @param scene The Scene
   */
  public void setScene(Scene scene) {
    this.scene = scene;
  }

  /**
   * Clears the controls for the OccurrenceController
   */
  private void clear(WeekController issuer) {
    this.eventButtonAction();
    this.submitButton.setText("Submit");
    this.nameInput.clear();
    this.description.clear();
    this.hourStartTimeInput.clear();
    this.minuteStartTimeInput.clear();
    this.nameInput.setEditable(true);
    this.description.setEditable(true);
    this.hourStartTimeInput.setEditable(true);
    this.minuteStartTimeInput.setEditable(true);
    this.categoryChoices.getItems().clear();
    this.categoryChoices.setDisable(false);
    for (Category category : issuer.getWeek().getCategories()) {
      categoryChoices.getItems().add(category);
    }
  }

  /**
   * Updates the info of an Occurrence
   *
   * @param occurrence The given Occurrence
   */
  private void updateInfo(AbstractOccurrence occurrence) {
    this.nameInput.setText(occurrence.getName());
    this.description.setText(occurrence.getDescription());
    if (Event.class.isAssignableFrom(occurrence.getClass())) {
      this.eventButtonAction();
      Event event = (Event) occurrence;
      this.hourStartTimeInput.setText(String.valueOf(event.getStartTime().getHour()));
      this.minuteStartTimeInput.setText(String.valueOf(event.getStartTime().getMinute()));
      this.hourDurationInput.getValueFactory().setValue(event.getDuration().getHour());
      this.minuteDurationInput.getValueFactory().setValue(event.getDuration().getMinute());
    } else {
      this.taskButtonAction();
    }
    this.submitButton.setText("Submit");
    this.deleteButton.setText("Delete");
    categoryChoices.getSelectionModel().select(occurrence.getCategory());
    categoryChoices.getSelectionModel().selectedItemProperty().addListener(
        (observable, oldValue, newValue) -> {
          occurrence.setCategory(newValue);
        });
  }

  /**
   * Resets the Buttons for each day
   *
   * @param day    The day to be selected
   * @param enable If the button should be enabled
   */
  private void resetDayButtons(DayOfWeek day, boolean enable) {
    String unselected = "-fx-background-color: transparent;";
    String selected = "-fx-background-color: lightgray;";
    for (Button btn : dayToBtn.keySet()) {
      btn.setStyle(unselected);
      if (enable) {
        btn.setOnAction(event -> {
          for (Button button : dayToBtn.keySet()) {
            button.setStyle(unselected);
          }
          btn.setStyle(selected);
          this.current = dayToBtn.get(btn);
        });
      } else {
        btn.setOnAction(null);
      }
      if (dayToBtn.get(btn).equals(day)) {
        btn.setStyle(selected);
      }
    }
  }
}
