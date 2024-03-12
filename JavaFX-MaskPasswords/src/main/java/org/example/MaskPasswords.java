package org.example;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener.Change;
import javafx.beans.property.ReadOnlyObjectWrapper;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MaskPasswords extends Application {
    private static final Random RNG = new Random();
    private final static Logger logger = LogManager.getLogger(MaskPasswords.class);

    @Override
    public void start (Stage primaryStage) {
        TableView<ConfigModel> userTable = new TableView<>();

        // standard column stuff:
        TableColumn<ConfigModel, String> KeyCol = new TableColumn<>("Key");
        KeyCol.setCellValueFactory(cellData -> cellData.getValue().keyProperty());

        TableColumn<ConfigModel, String> ValueCol = new TableColumn<>("Value");
        ValueCol.setCellValueFactory(cellData -> cellData.getValue().valueProperty());

        // which passwords are shown:
        ObservableSet<ConfigModel> usersWithShownPasswords = FXCollections.observableSet();

        // cell factory for password column. Cells must show either the
        // real or masked password, and may
        // need to update if usersWithShownPasswords changes:
        ValueCol.setCellFactory(c -> {

            // plain old cell:
            TableCell<ConfigModel, String> cell = new TableCell<>();

            // if the cell is reused for an item from a different row, update it:
            cell.indexProperty().addListener((obs, oldIndex, newIndex) -> updateCell(usersWithShownPasswords, cell));

            // if the password changes, update:
            cell.itemProperty().addListener((obs, oldItem, newItem) -> updateCell(usersWithShownPasswords, cell));

            // if the set of users with shown password changes, update the cell:
            usersWithShownPasswords.addListener((Change<? extends ConfigModel> change) -> updateCell(usersWithShownPasswords, cell));

            return cell;
        });

        TableColumn<ConfigModel, ConfigModel> showHidePasswordCol = new TableColumn<>("Show/Hide password");

        // just use whole row (User) as data for cells in this column:
        showHidePasswordCol.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));

        // cell factory for toggle buttons:
        showHidePasswordCol.setCellFactory(c -> new TableCell<ConfigModel, ConfigModel>() {

            // create toggle button once for cell:
            private final ToggleButton button = new ToggleButton();

            // anonymous constructor:
            {
                // update toggle button state if usersWithShownPasswords changes:
                usersWithShownPasswords.addListener((Change<? extends ConfigModel> change) -> {
                    button.setSelected(usersWithShownPasswords.contains(getItem()));
                });

                // update usersWithShownPasswords if toggle selection changes:
                button.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
                    if (isNowSelected) {
                        usersWithShownPasswords.add(getItem());
                    } else {
                        usersWithShownPasswords.remove(getItem());
                    }
                });

                // keep text "Show" or "Hide" appropriately:
                button.textProperty().bind(Bindings.when(button.selectedProperty()).then("Hide").otherwise("Show"));
                setAlignment(Pos.CENTER);
            }

            // Just update graphic as needed:
            @Override
            public void updateItem (ConfigModel item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    button.setSelected(usersWithShownPasswords.contains(item));
                    setGraphic(button);
                }
            }
        });

        userTable.getColumns().addAll(Arrays.asList(KeyCol, ValueCol, showHidePasswordCol));

        userTable.getItems().addAll(createData());

        Scene scene = new Scene(new BorderPane(userTable), 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private String maskPassword (String text) {
        char[] chars = new char[text.length()];
        Arrays.fill(chars, '*');
        return new String(chars);
    }

    private List<ConfigModel> createData () {
        return IntStream.rangeClosed(1, 10).mapToObj(i -> {
            ConfigModel model;
            if (i % 2 == 0) model = new ConfigModel("User " + i, randomPassword());
            else model = new ConfigModel("UserPass" + i, randomPassword());
            return model;
        }).collect(Collectors.toList());
    }

    private String randomPassword () {
        int pwSize = 6 + RNG.nextInt(5);
        char[] chars = new char[pwSize];
        for (int i = 0; i < chars.length; i++) {
            chars[i] = (char) ('a' + RNG.nextInt(26));
        }
        return new String(chars);
    }


    private void updateCell (ObservableSet<ConfigModel> usersWithShownPasswords, TableCell<ConfigModel, String> cell) {
        int index = cell.getIndex();
        TableView<ConfigModel> table = cell.getTableView();
        if (index < 0 || index >= table.getItems().size()) {
            cell.setText("");
        } else {
            ConfigModel model = table.getItems().get(index);
            // if current config parameter is shown or the according key contains pass
            if (usersWithShownPasswords.contains(model) || model.getKey().toUpperCase().contains("PASS")) {
                cell.setText(model.getValue());
            } else {
                cell.setText(maskPassword(model.getValue()));
            }
        }
    }

    public static void main (String[] args) {
        launch(args);
    }
}
