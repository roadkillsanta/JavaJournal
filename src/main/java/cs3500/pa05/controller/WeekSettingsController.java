package cs3500.pa05.controller;

import cs3500.pa05.model.Category;
import cs3500.pa05.model.Week;
import java.util.HashMap;
import java.util.Map;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * Represents the weekSettingsController class
 */
public class WeekSettingsController {
  @FXML
  private SplitPane root;
  @FXML
  private Spinner<Integer> eventMax;
  @FXML
  private Spinner<Integer> taskMax;
  @FXML
  private Button save;
  @FXML
  private Button cancel;
  @FXML
  private ListView categoryList;
  @FXML
  private Button addCategory;
  @FXML
  private TextField categoryName;
  @FXML
  private ColorPicker categoryColor;
  @FXML
  private Button deleteCategory;
  @FXML
  private Button saveCategory;
  @FXML
  private VBox categoryEditor;
  private final WeekController parent;
  private final Week week;
  private Scene scene;
  private final Map<ListCell, Category> cellCategoryMap = new HashMap<>();

  WeekSettingsController(WeekController parent) {
    this.parent = parent;
    week = parent.getWeek();
  }

  /**
   * to init the values of spinners and set default color
   */
  public void init() {
    initSpinners();
    taskMax.setEditable(false);
    addCategory.setOnAction(event -> {
      week.getCategories().add(new Category("new category", Color.WHITE));
      reloadCategories();
    });
    categoryEditor.disableProperty()
        .bind(Bindings.isNull(categoryList.getSelectionModel().selectedItemProperty()));
    categoryList.getSelectionModel().selectedItemProperty()
        .addListener((observable, oldValue, newValue) -> {
          Category category = cellCategoryMap.get(newValue);
          if (category != null) {
            categoryName.setText(category.getName());
            categoryColor.setValue(category.getColor());
            categoryName.setEditable(true);
            deleteCategory.setDisable(false);
            if (category.equals(Category.DEFAULT)) {
              categoryName.setEditable(false);
              deleteCategory.setDisable(true);
            }
          }
        });
    deleteCategory.setOnAction(event -> {
      week.getCategories()
          .remove(cellCategoryMap.get(categoryList.getSelectionModel().getSelectedItem()));
      reloadCategories();
    });
    saveCategory.setOnAction(event -> {
      Category category = cellCategoryMap.get(categoryList.getSelectionModel().getSelectedItem());
      category.setName(categoryName.getText());
      category.setColor(categoryColor.getValue());
    });
    reloadCategories();
  }

  /**
   * Initializes the spinners
   */
  private void initSpinners() {
    SpinnerValueFactory<Integer> eventFactory =
        new SpinnerValueFactory.IntegerSpinnerValueFactory(1, Integer.MAX_VALUE);
    eventFactory.setValue(week.getMaxEvents());
    eventMax.setValueFactory(eventFactory);
    eventMax.setEditable(false);
    SpinnerValueFactory<Integer> taskFactory =
        new SpinnerValueFactory.IntegerSpinnerValueFactory(1, Integer.MAX_VALUE);
    taskFactory.setValue(week.getMaxTasks());
    taskMax.setValueFactory(taskFactory);
  }

  /**
   * To show the settings stage on screen
   */
  public void show() {
    final Stage dialog = new Stage();
    dialog.initOwner(parent.getStage());
    dialog.setScene(this.root.getScene());
    save.setOnAction(event -> {
      this.week.setMaxEvents(eventMax.getValueFactory().getValue());
      this.week.setMaxTasks(taskMax.getValueFactory().getValue());
      parent.reloadColors();
      dialog.close();
    });
    cancel.setOnAction(event -> {
      parent.reloadColors();
      dialog.close();
    });
    dialog.show();
  }

  /**
   * To reload the categories
   */
  private void reloadCategories() {
    categoryList.getItems().clear();
    cellCategoryMap.clear();
    for (Category category : week.getCategories()) {
      ListCell cell = new ListCell();
      cell.setText(category.getName());
      cell.setMinWidth(Region.USE_COMPUTED_SIZE);
      cell.setPrefWidth(400);
      categoryList.getItems().add(cell);
      cellCategoryMap.put(cell, category);
    }
  }
}
