package org.example;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener.Change;
import javafx.beans.property.ReadOnlyObjectWrapper;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MaskPasswords extends Application {
    private static final Random RNG = new Random();
    private final static Logger logger = LogManager.getLogger(MaskPasswords.class);

    @Override
    public void start (Stage primaryStage) {
        TableView<ConfigModel> configTable = new TableView<>();

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
        ValueCol.setCellFactory( new ConfigTableCell<ConfigModel, ConfigModel>().forTableColumn() );
/*
            // textfield cell for editing values:
            ConfigTableCell cell = new ConfigTableCell<ConfigModel, ConfigModel>();
            // if the cell is reused for an item from a different row, update it:
            cell.indexProperty().addListener((obs, oldIndex, newIndex) -> updateCell(configWithShownPasswords, cell));
            // if the password changes, update:
            cell.itemProperty().addListener((obs, oldItem, newItem) -> updateCell(configWithShownPasswords, cell));
            // if the set of users with shown password changes, update the cell:
            configWithShownPasswords.addListener((Change<? extends ConfigModel> change) -> updateCell(configWithShownPasswords, cell));

            return cell;
        }*/



        //ValueCol.setCellValueFactory(new PropertyValueFactory<>("value"));

        configTable.setEditable(true);
        ValueCol.setEditable(true);

        ValueCol.setOnEditCommit( t -> {
            int iRow = t.getTablePosition().getRow();
            ConfigModel model = t.getTableView().getItems().get( iRow );
            model.setValue( t.getNewValue() );
        });


        //PropertyValue.setCellValueFactory(new PropertyValueFactory<>("value"));
        //PropertyValue.setCellFactory( TextFieldTableCell.forTableColumn());

        TableColumn<ConfigModel, ConfigModel> showHidePasswordCol = new TableColumn<>("Show/Hide password");
        // just use whole row (User) as data for cells in this column:
        showHidePasswordCol.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        // cell factory for toggle buttons:
        showHidePasswordCol.setCellFactory(c -> new ConfigTableCell() );
        showHidePasswordCol.setEditable(false);
        //showHidePasswordCol.setCellFactory(c -> new ConfigFieldCell() );
        configTable.getColumns().addAll(Arrays.asList(KeyCol, ValueCol, showHidePasswordCol));
        configTable.getItems().addAll(createData());
        BorderPane pane = new BorderPane(configTable);
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
            } else if (! model.getKey().toUpperCase().contains("PASS")) {
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
