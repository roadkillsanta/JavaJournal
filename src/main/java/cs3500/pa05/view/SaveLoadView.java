package cs3500.pa05.view;

import static javafx.beans.binding.Bindings.not;

import cs3500.pa05.model.FileLoader;
import cs3500.pa05.model.PasswordUtil;
import cs3500.pa05.model.json.BujoWrapper;
import cs3500.pa05.model.json.ProgramState;
import java.io.File;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * To represent the SavaLoadView class
 */
public class SaveLoadView {
  /**
   * To save a file with a certain name and other functionality
   *
   * @param state  represents the current program state
   * @param create created or not
   * @return boolean if its successful
   */
  public static boolean saveAs(ProgramState state, boolean create) {
    Stage pane = new Stage();
    pane.initModality(Modality.APPLICATION_MODAL);
    pane.setTitle("Save as...");
    //elements
    VBox box = new VBox(10.0);
    AnchorPane.setTopAnchor(box, 5.0);
    AnchorPane.setLeftAnchor(box, 0.0);
    AnchorPane.setRightAnchor(box, 0.0);
    box.setAlignment(Pos.TOP_CENTER);
    box.setPadding(new Insets(0, 5, 5, 5));

    TextField directoryField = new TextField();
    directoryField.setPrefWidth(300);
    Button chooseDir = new Button("Choose Directory");
    chooseDir.setOnAction(event -> {
      DirectoryChooser chooser = new DirectoryChooser();
      chooser.setInitialDirectory(new File(System.getProperty("user.dir")));
      File file = chooser.showDialog(pane.getOwner());
      if (file != null) {
        directoryField.setText(file.getPath());
      }
    });
    HBox dirBox = new HBox(5.0, new Label("Directory:"), directoryField, chooseDir);
    dirBox.setAlignment(Pos.BASELINE_CENTER);

    TextField fileField = new TextField();
    HBox fileBox = new HBox(5.0, new Label("Filename: "),
        fileField, new Label(".bujo"));
    fileBox.setAlignment(Pos.BASELINE_CENTER);

    CheckBox encrypt = new CheckBox("Encrypt");
    encrypt.setPadding(new Insets(-5, 140, 5, 0));
    PasswordField password = new PasswordField();
    password.disableProperty().bind(not(encrypt.selectedProperty()));
    HBox passBox = new HBox(new Label("Password: "), password);
    passBox.setAlignment(Pos.BASELINE_CENTER);
    AtomicBoolean successful = new AtomicBoolean(false);
    Button saveBtn = new Button("Save");
    saveBtn.setOnAction(event -> {
      File file = new File(directoryField.getText() + "\\" + "/" + fileField.getText() + ".bujo");
      if (encrypt.isSelected()) {
        FileLoader.save(file, state, password.getText());
      } else {
        FileLoader.save(file, state);
      }

      successful.set(true);
      pane.close();
    });
    saveBtn.disableProperty().bind(Bindings.isEmpty(directoryField.textProperty())
        .or(Bindings.isEmpty(fileField.textProperty()))
        .or(encrypt.selectedProperty().and(Bindings.isEmpty(password.textProperty()))));
    Button cancelBtn = new Button(create ? "Use defaults" : "Cancel");
    if (create) {
      cancelBtn.setOnAction(event -> {
        String path = System.getProperty("user.dir") + "\\" + "/" + "new";
        File file = new File(path + ".bujo");
        int i = 1;
        while (file.exists()) {
          file = new File(path + " " + i + ".bujo");
          i++;
        }
        FileLoader.save(file, state);
        successful.set(true);
        pane.close();
      });
    } else {
      cancelBtn.setOnAction(event -> pane.close());
    }

    HBox buttonBox = new HBox(40.0, saveBtn, cancelBtn);
    buttonBox.setAlignment(Pos.BOTTOM_CENTER);
    buttonBox.setPadding(new Insets(10, 0, 5, 0));

    VBox encryptBox = new VBox(encrypt, passBox);
    encryptBox.setAlignment(Pos.BASELINE_CENTER);

    box.getChildren().addAll(dirBox, fileBox, encryptBox, buttonBox);
    pane.setScene(new Scene(box));
    pane.showAndWait();
    return successful.get();
  }

  /**
   * To open the file
   *
   * @param stage the given stage to open file from
   * @return ProgramState with decryption of the data
   */
  public static ProgramState openFile(Stage stage) {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
    FileChooser.ExtensionFilter extFilter =
        new FileChooser.ExtensionFilter("Java Journal files (*.bujo)", "*.bujo");
    fileChooser.getExtensionFilters().add(extFilter);
    fileChooser.setTitle("Open file");
    try {
      BujoWrapper data = FileLoader.load(fileChooser.showOpenDialog(stage));
      assert data != null;
      if (data.encrypted()) {
        return promptDecrypt(data);
      }
      return FileLoader.parse(data.data());
    } catch (Exception e) {
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle("Error!");
      alert.setHeaderText("Unable to parse file!");
      alert.setContentText("Please choose a different file.");
      alert.showAndWait();
      return null;
    }
  }

  /**
   * To decrypt the data
   *
   * @param data the data from the record
   * @return a program state
   */
  public static ProgramState promptDecrypt(BujoWrapper data) {
    TextInputDialog dialog = new TextInputDialog();
    dialog.setGraphic(null);
    dialog.setTitle("Decrypt file");
    dialog.setHeaderText("This file is encrypted. Please enter the password.");
    dialog.setContentText("Password: ");

    Button okButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
    TextField inputField = dialog.getEditor();

    AtomicReference<ProgramState> state = new AtomicReference<>();

    okButton.setOnAction(event -> {
      String password = inputField.getText();
      if (data.hash().equals(PasswordUtil.getHash(PasswordUtil.salted(password, data.salt())))) {
        try {
          state.set(FileLoader.parse(
              PasswordUtil.decrypt(PasswordUtil.keyFromPassSalt(password, data.salt()),
                  data.data())));
        } catch (Exception e) {
          System.err.println("Should not have reached this state.");
          e.printStackTrace();
          dialog.close();
        }
      }
    });
    dialog.showAndWait();
    return state.get();
  }
}
