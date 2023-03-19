package stijn.dev.datasource.objects.items;

import javafx.beans.property.*;

public class Tag {
    private StringProperty name;

    public Tag(String name) {
        this.name = new SimpleStringProperty(name);
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }
}
