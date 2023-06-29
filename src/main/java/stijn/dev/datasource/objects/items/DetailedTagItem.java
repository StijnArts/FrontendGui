package stijn.dev.datasource.objects.items;

import javafx.beans.property.*;

public class DetailedTagItem {
    private StringProperty name;
    private int id;
    private StringProperty description;
    private StringProperty sortingTitle;
    public DetailedTagItem(String id, String name, String description, String sortingTitle) {
        if(id.isBlank()){
            id = "0";
        }
        this.id = Integer.valueOf(id);
        this.name = new SimpleStringProperty(name);
        this.description = new SimpleStringProperty(description);
        this.sortingTitle = new SimpleStringProperty(sortingTitle);
    }

    public String getName() {
        return name.get();
    }

    public String getDescription() {
        return description.get();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public String getSortingTitle() {
        return sortingTitle.get();
    }

    public StringProperty sortingTitleProperty() {
        return sortingTitle;
    }

    public void setSortingTitle(String sortingTitle) {
        this.sortingTitle.set(sortingTitle);
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }
}
