package cs3500.pa05.controller;

import cs3500.pa05.model.AbstractOccurrence;
import cs3500.pa05.model.Event;
import cs3500.pa05.model.Task;
import cs3500.pa05.model.Week;
import cs3500.pa05.view.SceneLoader;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

/**
 * Represents a controller for a Week
 */
public class WeekController {

  private final Controller main;
  private final WeekSettingsController settings;
  private final Tab tab;
  private final Week week;
  private final Map<DayOfWeek, VBox> dayMapping;
  private final Map<AbstractOccurrence, Button> buttonMap = new HashMap<>();
  private final OccurrenceController popup;
  @FXML
  private TextField weekName;
  @FXML
  private VBox boxSun;
  @FXML
  private VBox boxMon;
  @FXML
  private VBox boxTue;
  @FXML
  private VBox boxWed;
  @FXML
  private VBox boxThu;
  @FXML
  private VBox boxFri;
  @FXML
  private VBox boxSat;
  @FXML
  private Button deleteButton;
  @FXML
  private Button settingsButton;

  /**
   * Represents the week controller
   *
   * @param week  the week
   * @param tab   the current tab
   * @param popup the occurrence controller
   * @param main  main controller
   */
  public WeekController(Week week, Tab tab, OccurrenceController popup, Controller main) {
    this.week = week;
    this.tab = tab;
    this.dayMapping = new HashMap<>();
    this.popup = popup;
    this.main = main;
    settings = new WeekSettingsController(this);
    SceneLoader loader = new SceneLoader(settings, "week_settings.fxml");
    loader.load();
  }

  /**
   * The init data
   *
   * @param scene To add to the scene
   */
  public void init(Scene scene) {
    this.settings.init();
    this.tab.setContent(scene.getRoot());
    this.weekName.textProperty().addListener((observable, oldValue, newValue) -> {
      this.tab.setText(newValue);
      this.week.setName(newValue);
    });
    this.weekName.setText(this.week.getName());
    this.tab.setText(this.week.getName());

    deleteButton.setOnAction(event -> {
      main.removeWeek(this, this.tab);
    });
    settingsButton.setOnAction(event -> {
      settings.show();
    });
    initDayMap();
    for (DayOfWeek day : this.dayMapping.keySet()) {
      String border = """
          -fx-border-color: black;
          -fx-border-insets: -.25;
          -fx-border-width: 1;
          -fx-border-style: solid;
          """;
      VBox box = this.dayMapping.get(day);
      box.setStyle(border);
      Button addEvent = new Button("+");
      addEvent.setOnAction(
          event ->
              this.popup.addOccurrence(this, day));
      addEvent.setPrefWidth(Double.MAX_VALUE);
      box.getChildren().add(addEvent);
    }
    for (AbstractOccurrence occurrence : this.week.getOccurrences()) {
      AnchorPane pane = addButtonPane(occurrence);
      VBox dayBox = dayMapping.get(occurrence.getDay());
      dayBox.getChildren().add(dayBox.getChildren().size() - 1, pane);
      Button button = buttonMap.get(occurrence);
      button.setOnAction(event -> {
        this.popup.viewOccurrence(this, occurrence);
      });
    }
    this.reloadTaskQueue();
  }

  /**
   * Initializes the day map
   */
  private void initDayMap() {
    this.dayMapping.put(DayOfWeek.SUNDAY, boxSun);
    this.dayMapping.put(DayOfWeek.MONDAY, boxMon);
    this.dayMapping.put(DayOfWeek.TUESDAY, boxTue);
    this.dayMapping.put(DayOfWeek.WEDNESDAY, boxWed);
    this.dayMapping.put(DayOfWeek.THURSDAY, boxThu);
    this.dayMapping.put(DayOfWeek.FRIDAY, boxFri);
    this.dayMapping.put(DayOfWeek.SATURDAY, boxSat);
  }

  /**
   * Adds an AbstractOccurrence to the AnchorPane
   *
   * @param occurrence The AbstractOccurrence
   * @return The AnchorPan
   */
  private AnchorPane addButtonPane(AbstractOccurrence occurrence) {
    Button button = new Button();
    buttonMap.put(occurrence, button);
    button.setId("occurrence");
    setOccurrenceButton(button);
    Button goUp = new Button("Up");
    Button goDown = new Button("Dn");
    AnchorPane.setTopAnchor(goUp, 0.0);
    AnchorPane.setRightAnchor(goUp, 0.0);
    AnchorPane.setBottomAnchor(goDown, 0.0);
    AnchorPane.setRightAnchor(goDown, 0.0);
    goUp.setVisible(false);
    goDown.setVisible(false);
    this.styleButton(occurrence);
    AnchorPane pane = new AnchorPane();
    pane.setOnMouseEntered(event -> {
      goUp.setVisible(true);
      goDown.setVisible(true);
      VBox box = (VBox) pane.getParent();
      int index = box.getChildren().indexOf(pane);
      goUp.setDisable(index == 1);
      goDown.setDisable(index == box.getChildren().size() - 2);
    });
    pane.setOnMouseExited(event -> {
      goUp.setVisible(false);
      goDown.setVisible(false);
    });
    pane.getChildren().addAll(button, goUp, goDown);

    button.setOnAction(event -> {
      this.popup.viewOccurrence(this, occurrence);
    });
    goUp.setOnAction(event -> {
      VBox box = (VBox) pane.getParent();
      int index = box.getChildren().indexOf(pane);
      box.getChildren().remove(pane);
      box.getChildren().add(index
          - 1, pane);
    });
    goDown.setOnAction(event -> {
      VBox box = (VBox) pane.getParent();
      int index = box.getChildren().indexOf(pane);
      box.getChildren().remove(pane);
      box.getChildren().add(index
          + 1, pane);
    });
    return pane;
  }

  /**
   * Sets the occurrence button the AnchorPane
   *
   * @param button The button
   */
  private void setOccurrenceButton(Button button) {
    AnchorPane.setLeftAnchor(button, 0.0);
    AnchorPane.setRightAnchor(button, 0.0);
    AnchorPane.setTopAnchor(button, 0.0);
    AnchorPane.setBottomAnchor(button, 0.0);
  }

  /**
   * To add an occurrence
   *
   * @param occurrence the occurrence to add
   */
  public void addOccurrence(AbstractOccurrence occurrence) {
    if (this.week.isValidOccurence(occurrence)) {
      this.week.getOccurrences().add(occurrence);
      AnchorPane pane = addButtonPane(occurrence);
      VBox dayBox = dayMapping.get(occurrence.getDay());
      dayBox.getChildren().add(dayBox.getChildren().size() - 1, pane);
      buttonMap.get(occurrence).setOnAction(event -> {
        this.popup.viewOccurrence(this, occurrence);
      });
      this.reloadTaskQueue();
    } else {
      this.main.showWarning();
    }
  }

  /**
   * To edit an occurrence
   *
   * @param original   the original occurrence
   * @param occurrence the occurrence to edit
   */
  public void editOccurrence(AbstractOccurrence original, AbstractOccurrence occurrence) {
    Button button = this.buttonMap.get(original);
    List<AbstractOccurrence> occurrenceList = this.week.getOccurrences();
    occurrenceList.set(occurrenceList.indexOf(original), occurrence);
    this.buttonMap.remove(original);
    this.buttonMap.put(occurrence, button);
    this.styleButton(occurrence);

    VBox btnBox = (VBox) button.getParent().getParent();
    VBox dayBox = dayMapping.get(occurrence.getDay());
    if (!dayBox.equals(btnBox)) {
      btnBox.getChildren().remove(button.getParent());
      dayBox.getChildren().add(dayBox.getChildren().size() - 1, button.getParent());
    }
    button.setOnAction(event -> {
      this.popup.viewOccurrence(this, occurrence);
    });
    this.reloadTaskQueue();
  }

  /**
   * To remove the occurrence
   *
   * @param occurrence the occurrence to remove
   */
  public void removeOccurrence(AbstractOccurrence occurrence) {
    Button button = this.buttonMap.get(occurrence);
    VBox parent = (VBox) button.getParent().getParent();
    parent.getChildren().remove(button.getParent());
    this.week.getOccurrences().remove(occurrence);
    this.buttonMap.remove(occurrence);
    this.reloadTaskQueue();
  }

  /**
   * To get the tab
   *
   * @return this tab
   */
  public Tab getTab() {
    return this.tab;
  }

  /**
   * To reload the task Queue
   */
  public void reloadTaskQueue() {
    List<Task> tasks = new ArrayList<>();
    for (AbstractOccurrence occurrence : this.week.getOccurrences()) {
      if (Task.class.isAssignableFrom(occurrence.getClass())) {
        tasks.add((Task) occurrence);
      }
    }
    this.main.updateQueue(tasks);
  }

  /**
   * To reload the colors
   */
  public void reloadColors() {
    for (AbstractOccurrence occurrence : this.buttonMap.keySet()) {
      styleButton(occurrence);
    }
  }

  /**
   * To style the button
   *
   * @param occurrence the occurrence to style the button in
   */
  public void styleButton(AbstractOccurrence occurrence) {
    Button button = this.buttonMap.get(occurrence);
    button.setMaxHeight(0);
    Text header = new Text("Task:\n");
    if (occurrence.isEvent()) {
      Event event = (Event) occurrence;
      header = new Text("Event: "
          + event.getStartTime()
          + " - "
          +
          event.getStartTime().plusHours(event.getDuration().getHour())
              .plusMinutes(event.getDuration().getMinute())
          + "\n");
    }
    Text name = new Text(occurrence.getName()
        + "\n");
    name.setStyle("-fx-font-weight: bold");
    Label description = new Label(occurrence.getDescription());
    description.setMaxWidth(120);
    description.setEllipsisString("...");
    Tooltip tooltip = new Tooltip(occurrence.getDescription());
    Tooltip.install(description, tooltip);
    button.setGraphic(new Group(new TextFlow(header, name, description)));
    String style = "-fx-background-color: "
        +
        occurrence.getCategory().getColor().toString().replace("0x", "#")
        +
        ";";
    button.setStyle(style);
    button.setAlignment(Pos.BASELINE_LEFT);
    button.setPrefWidth(Double.MAX_VALUE);
  }

  /**
   * Gets the stage
   *
   * @return the main stage
   */
  public Stage getStage() {
    return main.getStage();
  }

  /**
   * Gets the week
   *
   * @return the current week
   */
  public Week getWeek() {
    return this.week;
  }
}
