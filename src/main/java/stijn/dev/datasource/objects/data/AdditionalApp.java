package stijn.dev.datasource.objects.data;

import javafx.beans.property.*;

public class AdditionalApp {
    private StringProperty path;
    private StringProperty name;
    private StringProperty arguments;

    public AdditionalApp(String path, String name, String arguments) {
        this.path = new SimpleStringProperty(path);
        this.name = new SimpleStringProperty(name);
        this.arguments = new SimpleStringProperty(arguments);
    }

    public StringProperty getPath() {
        return path;
    }

    public void setPath(StringProperty path) {
        this.path = path;
    }

    public StringProperty getName() {
        return name;
    }

    public void setName(StringProperty name) {
        this.name = name;
    }

    public StringProperty getArguments() {
        return arguments;
    }

    public void setArguments(StringProperty arguments) {
        this.arguments = arguments;
    }
}
