package org.example;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;
import javafx.event.Event;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
// import javafx.scene.control.cell.CellUtils;
import javafx.scene.input.KeyCode;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javafx.util.converter.DefaultStringConverter;

import java.util.Arrays;

public class ConfigTableCell<S, T> extends TableCell<S, T> {
    private TextField textField;
    private final ToggleButton button = new ToggleButton();
    private ObservableSet<T> configWithShownPasswords = FXCollections.observableSet();
    private ObjectProperty<StringConverter<T>> converter;

    public <S> Callback<TableColumn<S, String>, TableCell<S, String>> forTableColumn () {
        return forTableColumn(new DefaultStringConverter() );
    }

    public <S, T> Callback<TableColumn<S, T>, TableCell<S, T>> forTableColumn (StringConverter<T> var0 ) {
        return (var2) -> {

            ConfigTableCell cell = new ConfigTableCell<S, T>(var0);

            cell.indexProperty().addListener((obs, oldIndex, newIndex) -> cell.updateCell(this.configWithShownPasswords, cell));
            // if the password changes, update:
            cell.itemProperty().addListener((obs, oldItem, newItem) -> cell.updateCell(configWithShownPasswords, cell));
            // if the set of users with shown password changes, update the cell:
            //this.configWithShownPasswords.addListener((SetChangeListener.Change<? extends T> change) -> cell.updateCell(configWithShownPasswords, cell));
            return cell;
        };
    }

    private static <T> String getItemText (Cell<T> cell, StringConverter<T> converter) {
        return converter == null ? cell.getItem() == null ? "" : cell.getItem().toString() : converter.toString(cell.getItem());
    }

    private static <T> TextField createTextField (final Cell<T> cell, final StringConverter<T> converter) {
        TextField txtFld = new TextField(getItemText(cell, converter));

        txtFld.setOnKeyPressed(t -> {
            if (t.getCode() == KeyCode.ESCAPE) {
                cell.cancelEdit();
                t.consume();
            } else if (t.getCode() == KeyCode.ENTER && t.isShiftDown()) {
                t.consume();
                txtFld.insertText(txtFld.getCaretPosition(), "\n");
            } else if (t.getCode() == KeyCode.ENTER) {
                if (converter == null) {
                    throw new IllegalStateException("Attempting to convert text input into Object, but provided " + "StringConverter is null. Be sure to set a StringConverter " + "in your cell factory.");
                }
                cell.commitEdit(converter.fromString(txtFld.getText()));
                t.consume();
            }
        });
        return txtFld;
    }

    public ConfigTableCell () {
        this((StringConverter) null);

        // update toggle button state if configWithShownPasswords changes:
        this.configWithShownPasswords.addListener((SetChangeListener.Change<? extends T> change) -> {
            button.setSelected(configWithShownPasswords.contains(getItem()));
        });

        // update configWithShownPasswords if toggle selection changes:
        button.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
            if (isNowSelected) {
                this.configWithShownPasswords.add(getItem());
            } else {
                this.configWithShownPasswords.remove(getItem());
            }
        });

        // keep text "Show" or "Hide" appropriately:
        button.textProperty().bind(Bindings.when(button.selectedProperty()).then("Hide").otherwise("Show"));
        button.setAlignment(Pos.CENTER);
    }

    public ConfigTableCell (StringConverter<T> var1) {

        // update toggle button state if configWithShownPasswords changes:
        this.configWithShownPasswords.addListener((SetChangeListener.Change<? extends T> change) -> {
            button.setSelected(configWithShownPasswords.contains(getItem()));
        });

        // update configWithShownPasswords if toggle selection changes:
        button.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
            if (isNowSelected) {
                this.configWithShownPasswords.add(getItem());
            } else {
                this.configWithShownPasswords.remove(getItem());
            }
        });

        // keep text "Show" or "Hide" appropriately:
        button.textProperty().bind(Bindings.when(button.selectedProperty()).then("Hide").otherwise("Show"));
        button.setAlignment(Pos.CENTER);

        this.converter = new SimpleObjectProperty(this, "converter");
        this.getStyleClass().add("text-field-table-cell");
        this.setConverter(var1);
    }

    public final ObjectProperty<StringConverter<T>> converterProperty () {
        return this.converter;
    }

    public final void setConverter (StringConverter<T> var1) {
        this.converterProperty().set(var1);
    }

    public final StringConverter<T> getConverter () {
        return (StringConverter) this.converterProperty().get();
    }

    private <T> void startEdit (final Cell<T> cell, final StringConverter<T> converter) {
        textField.setText(getItemText(cell, converter));

        cell.setText(null);
        cell.setGraphic(textField);

        // Make sure the text area stays the right size:
        /* The following solution is courtesy of James_D: https://stackoverflow.com/a/22733264/5432315 */
        // Perform a lookup for an element with a css class of "text"
        // This will give the Node that actually renders the text inside the
        // TextArea
        Node text = textField.lookup(".text");
        // Bind the preferred height of the text area to the actual height of the text
        // This will make the text area the height of the text, plus some padding
        // of 20 pixels, as long as that height is between the text area's minHeight
        // and maxHeight. The max height will be the height of its parent (usually).
        //textField.prefHeightProperty().bind(Bindings.createDoubleBinding(() -> text.getBoundsInLocal().getHeight(), text.boundsInLocalProperty()).add(20));


        textField.selectAll();
        textField.requestFocus();
    }

    @Override
    public void startEdit () {

        if (!isEditable() || !getTableView().isEditable() || !getTableColumn().isEditable()) {
            return;
        }
        super.startEdit();

        if (isEditing()) {
            if (textField == null) {
                textField = createTextField(this, getConverter());
            }

            startEdit(this, getConverter());
        }
    }

    private void updateItem (final Cell<T> cell, final StringConverter<T> converter) {
        if (cell.isEmpty()) {
            cell.setText(null);
            cell.setGraphic(null);
            cell.setTooltip(null);

        } else {
            if (cell.isEditing()) {
                if (textField != null) {
                    textField.setText(getItemText(cell, converter));
                }
                cell.setText(null);
                cell.setGraphic(textField);
                cell.setTooltip(null);
            } else {

                if (cell.getItem() instanceof ConfigModel) {
                    ConfigModel item = (ConfigModel) cell.getItem();
                    if (!item.getKey().toUpperCase().contains("PASS")) {
                        setGraphic(null);
                        cell.setText(getItemText(cell, converter));
                        cell.setGraphic(null);
                    } else {
                        setGraphic(button);
                    }
                } else {
                    cell.setText(getItemText(cell, converter));
                    cell.setGraphic(null);
                }

                //button.setSelected(this.configWithShownPasswords.contains(item));

                //Add text as tooltip so that user can read text without editing it.
                Tooltip tooltip = new Tooltip(getItemText(cell, converter));
                tooltip.setWrapText(true);
                tooltip.prefWidthProperty().bind(cell.widthProperty());
                cell.setTooltip(tooltip);
            }
        }
    }

    private static <T> void cancelEdit (Cell<T> cell, final StringConverter<T> converter) {
        cell.setText(getItemText(cell, converter));
        cell.setGraphic(null);
    }

    @Override
    public void cancelEdit () {
        super.cancelEdit();
        cancelEdit(this, getConverter());
    }

    @Override
    public void updateItem (T item, boolean empty) {
        super.updateItem(item, empty);
        updateItem(this, getConverter());
    }

    @Override
    public void commitEdit (T item) {

        // This block is necessary to support commit on losing focus, because the baked-in mechanism
        // sets our editing state to false before we can intercept the loss of focus.
        // The default commitEdit(...) method simply bails if we are not editing...
        if (!isEditing() && !item.equals(getItem())) {
            TableView<S> table = getTableView();
            if (table != null) {
                TableColumn<S, T> column = getTableColumn();
                TableColumn.CellEditEvent<S, T> event = new TableColumn.CellEditEvent<>(table,
                        new TablePosition<>(table, getIndex(), column),
                        TableColumn.editCommitEvent(), item);
                Event.fireEvent(column, event);
            }
        }

        super.commitEdit(item);

        setContentDisplay(ContentDisplay.TEXT_ONLY);
    }

    private String maskPassword (String text) {
        char[] chars = new char[text.length()];
        Arrays.fill(chars, '*');
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

}
