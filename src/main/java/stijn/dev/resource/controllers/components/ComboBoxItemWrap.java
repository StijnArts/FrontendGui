package stijn.dev.resource.controllers.components;

import javafx.beans.property.*;

import java.util.*;

public class ComboBoxItemWrap<T> {
    private BooleanProperty check = new SimpleBooleanProperty(false);
    private ObjectProperty<T> item = new SimpleObjectProperty<>();

    public ComboBoxItemWrap(T item) {
        this.item.set(item);
    }

    public ComboBoxItemWrap(T item, Boolean check) {
        this.item.set(item);
        this.check.set(check);
    }

    public BooleanProperty checkProperty() {
        return check;
    }

    public Boolean getCheck() {
        return check.getValue();
    }

    public void setCheck(Boolean value) {
        check.set(value);
    }

    public ObjectProperty<T> itemProperty() {
        return item;
    }

    public T getItem() {
        return item.getValue();
    }

    public void setItem(T value) {
        item.setValue(value);
    }

    @Override
    public String toString() {
        return item.getValue().toString();
    }

    public static ArrayList<ComboBoxItemWrap<String>> generateComboBoxItemWrappers(List<String> strings){
        ArrayList<ComboBoxItemWrap<String>> comboBoxItemWraps = new ArrayList<>();
        for (String string :
                strings) {
            comboBoxItemWraps.add(new ComboBoxItemWrap<>(string));
        }
        return comboBoxItemWraps;
    }
}
