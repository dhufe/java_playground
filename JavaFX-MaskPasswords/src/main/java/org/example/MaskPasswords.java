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
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MaskPasswords extends Application {
    private static final Random RNG = new Random();
    private final static Logger logger = LogManager.getLogger(MaskPasswords.class);
    private boolean bState = false;

    @Override
    public void start (Stage primaryStage) {
        TableView<ConfigModel> configTable = new TableView<>();
        ToggleButton extBtn = new ToggleButton();
        // standard column stuff:
        TableColumn<ConfigModel, String> KeyCol = new TableColumn<>("Key");
        KeyCol.setCellValueFactory(cellData -> cellData.getValue().keyProperty());

        TableColumn<ConfigModel, String> ValueCol = new TableColumn<>("Value");
        ValueCol.setCellValueFactory(cellData -> cellData.getValue().valueProperty());

        // which passwords are shown:
        ObservableSet<ConfigModel> configWithShownPasswords = FXCollections.observableSet();

        // cell factory for password column. Cells must show either the
        // real or masked password, and may
        // need to update if configWithShownPasswords changes:
        ValueCol.setCellFactory(c -> {
            // textfield cell for editing values:
            TableCell cell = new TableCell<ConfigModel, String>();
            // if the cell is reused for an item from a different row, update it:
            cell.indexProperty().addListener((obs, oldIndex, newIndex) -> updateCell(configWithShownPasswords, cell));
            // if the password changes, update:
            cell.itemProperty().addListener((obs, oldItem, newItem) -> updateCell(configWithShownPasswords, cell));
            // if the set of users with shown password changes, update the cell:
            configWithShownPasswords.addListener((Change<? extends ConfigModel> change) -> updateCell(configWithShownPasswords, cell));
            return cell;
        });

        configTable.setEditable(true);
        ValueCol.setEditable(true);

        ValueCol.setOnEditCommit(t -> {
            int iRow = t.getTablePosition().getRow();
            ConfigModel model = t.getTableView().getItems().get(iRow);
            model.setValue(t.getNewValue());
        });

        // keep text "Show" or "Hide" appropriately:
        extBtn.textProperty().bind(Bindings.when(extBtn.selectedProperty()).then("Hide").otherwise("Show"));
        extBtn.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
            if (!bState) {
                configWithShownPasswords.addAll(configTable.getItems());
                bState = true;
            } else {
                configWithShownPasswords.removeAll(configTable.getItems());
                bState = false;
            }
        });


        configTable.getColumns().addAll(Arrays.asList(KeyCol, ValueCol));
        configTable.getItems().addAll(createData());
        VBox pane = new VBox();
        pane.getChildren().add(configTable);
        pane.getChildren().add(extBtn);

        Scene scene = new Scene(pane, 800, 600);
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
            if (usersWithShownPasswords.contains(model) && model.getKey().toUpperCase().contains("PASS")) {
                cell.setText(model.getValue());
            } else if (!model.getKey().toUpperCase().contains("PASS")) {
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
