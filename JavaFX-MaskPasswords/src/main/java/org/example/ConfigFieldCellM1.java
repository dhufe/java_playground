package org.example;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javafx.util.converter.DefaultStringConverter;

public class ConfigFieldCellM1<ConfigModel> extends TableCell<ConfigModel, ConfigModel> {

    private final ToggleButton button = new ToggleButton();
    private ObservableSet<ConfigModel> configWithShownPasswords;
    private ObjectProperty<StringConverter> converter = new SimpleObjectProperty<>(this, "converter");

    private TextField txtField;

    public static <ConfigModel> Callback<TableColumn<String, String>, TableCell<String, String>> forTableColumn () {
        return forTableColumn( new DefaultStringConverter());
    }

    public static <ConfigModel> Callback<TableColumn<ConfigModel, ConfigModel>, TableCell<ConfigModel, ConfigModel>> forTableColumn (final StringConverter<ConfigModel> converter) {
        return list -> (TableCell<ConfigModel, ConfigModel>) new ConfigFieldCellM1<ConfigModel>(converter);
    }

    private static <ConfigModel> String getItemText (Cell<ConfigModel> cell, StringConverter<ConfigModel> converter) {
        return converter == null ? cell.getItem() == null ? "" : cell.getItem()
                .toString() : converter.toString(cell.getItem());
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
                    throw new IllegalStateException(
                            "Attempting to convert text input into Object, but provided "
                                    + "StringConverter is null. Be sure to set a StringConverter "
                                    + "in your cell factory.");
                }
                cell.commitEdit(converter.fromString(txtFld.getText()));
                t.consume();
            }
        });
        return txtFld;
    }

    private void startEdit (final Cell<ConfigModel> cell, final StringConverter<ConfigModel> converter) {
        txtField.setText(getItemText(cell, converter));

        cell.setText(null);
        cell.setGraphic(txtField);

        // Make sure the text area stays the right size:
        /* The following solution is courtesy of James_D: https://stackoverflow.com/a/22733264/5432315 */
        // Perform a lookup for an element with a css class of "text"
        // This will give the Node that actually renders the text inside the
        // TextArea
        Node text = txtField.lookup(".text");
        // Bind the preferred height of the text area to the actual height of the text
        // This will make the text area the height of the text, plus some padding
        // of 20 pixels, as long as that height is between the text area's minHeight
        // and maxHeight. The max height will be the height of its parent (usually).
        txtField.prefHeightProperty().bind(Bindings.createDoubleBinding(() ->
                text.getBoundsInLocal().getHeight(), text.boundsInLocalProperty()).add(20)
        );


        txtField.selectAll();
        txtField.requestFocus();
    }

    private static <T> void cancelEdit (Cell<T> cell, final StringConverter<T> converter) {
        cell.setText(getItemText(cell, converter));
        cell.setGraphic(null);
    }

    private void updateItem (final Cell<ConfigModel> cell, final StringConverter<ConfigModel> converter) {

        if (cell.isEmpty()) {
            cell.setText(null);
            cell.setGraphic(null);
            cell.setTooltip(null);

        } else {
            if (cell.isEditing()) {
                if (txtField != null) {
                    txtField.setText(getItemText(cell, converter));
                }
                cell.setText(null);
                cell.setGraphic(txtField);
                cell.setTooltip(null);
            } else {
                ConfigModel item = cell.getItem();
                button.setSelected(this.configWithShownPasswords.contains(item));
                //if ( ! item.getKey().toUpperCase().contains("PASS") ) {
                    setGraphic(null);
                    cell.setText(getItemText(cell, converter));
                    cell.setGraphic(null);

                    //Add text as tooltip so that user can read text without editing it.
                    Tooltip tooltip = new Tooltip(getItemText(cell, converter));
                    tooltip.setWrapText(true);
                    tooltip.prefWidthProperty().bind(cell.widthProperty());
                    cell.setTooltip(tooltip);
                /*}
                else {
                    setGraphic(button);
                }*/
            }
        }
    }

    public ConfigFieldCellM1 (ObservableSet<ConfigModel> configWithShownPasswords) {
        this.configWithShownPasswords = configWithShownPasswords;
        // update toggle button state if configWithShownPasswords changes:
        this.configWithShownPasswords.addListener((SetChangeListener.Change<? extends ConfigModel> change) -> {
            button.setSelected(configWithShownPasswords.contains(getItem()));
        });

        // update configWithShownPasswords if toggle selection changes:
        button.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
            if (isNowSelected) {
                this.configWithShownPasswords.add( getItem() );
            } else {
                this.configWithShownPasswords.remove(getItem());
            }
        });

        // keep text "Show" or "Hide" appropriately:
        button.textProperty().bind(Bindings.when(button.selectedProperty()).then("Hide").otherwise("Show"));
        button.setAlignment(Pos.CENTER);
    }

    // Just update graphic as needed:
    @Override
    public void updateItem (ConfigModel item, boolean empty) {
        super.updateItem(item, empty);
        updateItem(this, getConverter());
    }


    public ConfigFieldCellM1 (StringConverter converter) {
        this.getStyleClass().add("text-area-table-cell");
        setConverter(converter);
    }

    public final ObjectProperty<StringConverter> converterProperty () {
        return converter;
    }

    public final void setConverter (StringConverter value) {
        converterProperty().set(value);
    }

    public final StringConverter getConverter () {
        return converterProperty().get();
    }

    @Override
    public void startEdit () {
        if (!isEditable() || !getTableView().isEditable() || !getTableColumn().isEditable()) {
            return;
        }

        super.startEdit();

        if (isEditing()) {
            if (txtField == null) {
                txtField = createTextField(this, getConverter());
            }

            startEdit(this, getConverter());
        }
    }

    @Override
    public void cancelEdit () {
        super.cancelEdit();
        cancelEdit(this, getConverter());
    }
}
