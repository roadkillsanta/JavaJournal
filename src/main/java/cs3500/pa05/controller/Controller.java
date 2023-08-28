package cs3500.pa05.controller;

import static cs3500.pa05.model.FileLoader.parse;
import static cs3500.pa05.view.SaveLoadView.promptDecrypt;

import cs3500.pa05.model.FileLoader;
import cs3500.pa05.model.Task;
import cs3500.pa05.model.Week;
import cs3500.pa05.model.json.BujoWrapper;
import cs3500.pa05.model.json.ProgramState;
import cs3500.pa05.model.json.WeekRecord;
import cs3500.pa05.view.SaveLoadView;
import cs3500.pa05.view.SceneLoader;
import cs3500.pa05.view.WeekLoader;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javafx.animation.PauseTransition;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;


/**
 * Represents the main controller
 */
public class Controller {
  @FXML
  private MenuItem save;
  @FXML
  private MenuItem saveAs;
  @FXML
  private MenuItem open;
  @FXML
  private MenuItem newWeek;
  @FXML
  private MenuItem templateWeek;
  @FXML
  private TextField maxTasksInput;
  @FXML
  private TextField maxEventsInput;
  @FXML
  private Tab addWeekTab;
  @FXML
  private TabPane tabWeeks;
  @FXML
  private VBox scene;
  @FXML
  private VBox taskQueue;
  @FXML
  private Label statusLeft;
  private final Map<Tab, WeekController> tabControllerMap = new HashMap<>();
  private final Map<Week, WeekController> weeks = new LinkedHashMap<>();
  private final OccurrenceController popup;
  private final Stage stage;

  /**
   * Initializes the controller with a given stage
   *
   * @param stage The given stage
   */
  public Controller(Stage stage) {
    this.popup = new OccurrenceController(stage);
    SceneLoader popupLoader = new SceneLoader(popup, "addPopup.fxml");
    popup.setScene(popupLoader.load());
    this.stage = stage;
  }

  /**
   * Initializes the weeks of the controller with a saved ProgramState
   *
   * @param state The saved ProgramState
   */
  public void init(ProgramState state) {
    tabWeeks.getSelectionModel().select(tabWeeks.getTabs().size() - 1);
    this.load(state);
    this.addWeekTab.setClosable(false);
    this.tabWeeks.getSelectionModel().selectedItemProperty()
        .addListener((observable, oldTab, newTab) -> {
          if (newTab == addWeekTab) {
            this.addWeek(new Week("Week "
                + tabWeeks.getTabs().size()));
            tabWeeks.getSelectionModel().select(tabWeeks.getTabs().size() - 1);
          } else {
            for (WeekController controller : this.weeks.values()) {
              if (controller.getTab().equals(newTab)) {
                controller.reloadTaskQueue();
              }
            }
          }
        });
    save.setOnAction(event -> {
      FileLoader.save(new File("default.bujo"), this.toRecord());
    });
    saveAs.setOnAction(event -> {
      SaveLoadView.saveAs(this.toRecord(), false);
    });
    open.setOnAction(event -> {
      ProgramState loadedState = SaveLoadView.openFile(this.stage);
      if (loadedState != null) {
        load(loadedState);
      }
    });
    newWeek.setOnAction(event -> {
      this.addWeek(new Week("Week "
          + tabWeeks.getTabs().size()));
    });
    templateWeek.setOnAction(event -> {
      this.weekFromTemplate();
    });
    this.popup.init();
  }

  /**
   * Runs the functionality of the controller
   */
  public void run() {
    this.stage.setScene(scene.getScene());
    tabWeeks.getSelectionModel().select(1);
  }

  /**
   * Adds a week to the controller
   *
   * @param week The given Week
   */
  private void addWeek(Week week) {
    Tab tab = new Tab();
    List<Tab> tabs = tabWeeks.getTabs();
    tabs.add(tab);
    WeekController controller = new WeekController(week, tab, popup, this);
    WeekLoader fxml = new WeekLoader(controller);
    this.weeks.put(week, controller);
    Scene scene = fxml.load();
    this.tabControllerMap.put(tab, controller);
    controller.init(scene);
  }

  /**
   * Removes a week from the controller
   *
   * @param child The controller for the Week
   * @param tab   The tab of the week
   */
  public void removeWeek(WeekController child, Tab tab) {
    this.tabControllerMap.remove(tab);
    this.weeks.remove(child.getWeek());
    if (tabWeeks.getTabs().size() > 2 && tabWeeks.getTabs().indexOf(tab) == 1) {
      tabWeeks.getSelectionModel().select(2);
    }
    this.tabWeeks.getTabs().remove(tab);
  }

  /**
   * Updates the task queue with a list of tasks
   *
   * @param tasks The given List of Task
   */
  public void updateQueue(List<Task> tasks) {
    this.taskQueue.getChildren().clear();
    for (Task task : tasks) {
      AnchorPane pane = new AnchorPane();
      pane.setMinWidth(0);
      pane.setMaxWidth(this.taskQueue.getPrefWidth());
      Button remove = new Button("X");
      RadioButton radioButton = new RadioButton();
      radioButton.setText(task.getName());
      radioButton.setOnAction(event -> {
        if (radioButton.isSelected()) {
          task.complete();
          remove.setVisible(true);
        } else {
          task.uncomplete();
          remove.setVisible(false);
        }
      });
      remove.setOnAction(event -> {
        this.tabControllerMap.get(this.tabWeeks.getSelectionModel().getSelectedItem())
            .removeOccurrence(task);
      });
      remove.setVisible(false);
      if (task.isComplete()) {
        radioButton.setSelected(true);
        remove.setVisible(true);
      }
      AnchorPane.setLeftAnchor(radioButton, 5.0);
      AnchorPane.setTopAnchor(radioButton, 5.0);
      AnchorPane.setRightAnchor(remove, 5.0);
      AnchorPane.setTopAnchor(remove, 0.0);
      pane.getChildren().add(radioButton);
      pane.getChildren().add(remove);
      this.taskQueue.getChildren().add(pane);
    }
  }


  /**
   * Loads this Controller from a program state
   *
   * @param state The ProgramStae
   */
  private void load(ProgramState state) {
    this.tabControllerMap.clear();
    this.weeks.clear();
    for (Tab tab : this.tabWeeks.getTabs()) {
      if (tab != addWeekTab) {
        this.tabWeeks.getTabs().remove(tab);
      }
    }
    for (WeekRecord record : state.weeks()) {
      this.addWeek(new Week(record, false));
    }
  }

  private void weekFromTemplate() {
    Stage pane = new Stage();
    pane.initModality(Modality.APPLICATION_MODAL);
    pane.setTitle("Open Template");
    //elements
    VBox box = new VBox(10.0);
    box.setPadding(new Insets(0, 5, 5, 5));
    AnchorPane.setTopAnchor(box, 5.0);
    AnchorPane.setLeftAnchor(box, 0.0);
    AnchorPane.setRightAnchor(box, 0.0);
    box.setAlignment(Pos.TOP_CENTER);

    TextField fileField = new TextField();
    fileField.setPrefWidth(150);
    Button chooseDir = new Button("Choose File");

    ObservableList<WeekRecord> weeks = FXCollections.observableArrayList();
    Map<ListCell, WeekRecord> cellToWeek = new HashMap<>();

    chooseDir.setOnAction(event -> {
      FileChooser fileChooser = new FileChooser();
      fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
      FileChooser.ExtensionFilter extFilter =
      new FileChooser.ExtensionFilter("Java Journal files (*.bujo)",
          "*.bujo");
      fileChooser.getExtensionFilters().add(extFilter);
      fileChooser.setTitle("Open file");
      try {
        File chosen = fileChooser.showOpenDialog(stage);
        BujoWrapper data = FileLoader.load(chosen);
        assert data != null;
        if (data.encrypted()) {
          weeks.clear();
          weeks.addAll(promptDecrypt(data).weeks());
        } else {
          weeks.clear();
          weeks.addAll(parse(data.data()).weeks());
        }
        fileField.setText(chosen.getName());
      } catch (Exception e) {
        Alert alert = new Alert(Alert.AlertType.ERROR,
            "Couldn't load file!", ButtonType.OK);
        alert.showAndWait();
      }
    });

    HBox fileBox = new HBox(5.0, new Label("File:"), fileField, chooseDir);
    fileBox.setAlignment(Pos.BASELINE_CENTER);

    ListView weeksView = new ListView();
    weeks.addListener((ListChangeListener<WeekRecord>) c -> {
      weeksView.getItems().clear();
      cellToWeek.clear();
      for (WeekRecord record : weeks) {
        String serialized =
            "Name: "
                + record.name()
                + ", maxEvents: "
                + record.maxEvents()
                + ", maxTasks: "
                +
                record.maxTasks();
        ListCell cell = new ListCell();
        cell.setText(serialized);
        cell.setMinWidth(Region.USE_COMPUTED_SIZE);
        cell.setPrefWidth(1024);
        cellToWeek.put(cell, record);
        weeksView.getItems().add(cell);
      }
    });

    Button createBtn = new Button("Create");
    createBtn.disableProperty()
        .bind(Bindings.isNull(weeksView.getSelectionModel().selectedItemProperty()));
    createBtn.setOnAction(event -> {
      WeekRecord record = cellToWeek.get(weeksView.getSelectionModel().getSelectedItem());
      this.addWeek(new Week(record, true));
      pane.close();
    });
    Button cancelBtn = new Button("Cancel");
    cancelBtn.setOnAction(event -> {
      pane.close();
    });

    HBox buttonBox = new HBox(40.0, createBtn, cancelBtn);
    buttonBox.setAlignment(Pos.BOTTOM_CENTER);
    buttonBox.setPadding(new Insets(10, 0, 0, 0));

    box.getChildren().addAll(fileBox, weeksView, buttonBox);
    pane.setScene(new Scene(box));
    pane.showAndWait();
  }

  /**
   * Returns the Stage from this Controller
   *
   * @return The stage
   */
  public Stage getStage() {
    return this.stage;
  }

  /**
   * Serializes this controller to a ProgramState Record
   *
   * @return The ProgramState Record
   */
  private ProgramState toRecord() {
    List<WeekRecord> items = new ArrayList<>();
    for (Week week : weeks.keySet()) {
      items.add(week.toRecord());
    }
    return new ProgramState(items);
  }

  /**
   * Shows the warning for an Occurrence
   */
  public void showWarning() {
    statusLeft.setText("CAN'T EXCEED THE MAX OCCURRENCE NUMBER");
    statusLeft.setTextFill(Color.RED);
    statusLeft.setStyle("-fx-font-weight: bold");
    PauseTransition delay = new PauseTransition(Duration.seconds(4));
    delay.setOnFinished(event -> statusLeft.setText(""));
    delay.play();
  }
}
