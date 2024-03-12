package org.example;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ConfigModel {
    private final StringProperty key = new SimpleStringProperty();
    private final StringProperty value = new SimpleStringProperty();

    public ConfigModel (String key, String value) {
        this.setKey(key);
        this.setValue(value);
    }

    public final void setKey (final String key) {
        this.key.set(key);
    }

    public final void setValue (final String value) {
        this.value.set(value);
    }

    public final StringProperty keyProperty() {
        return this.key;
    }

    public final String getKey() {
        return this.key.get();
    }

    public final StringProperty valueProperty() {
        return this.value;
    }

    public final String getValue() {
        return this.value.get();
    }

}
