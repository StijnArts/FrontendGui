package stijn.dev.datasource.objects.data;

import javafx.beans.property.*;
import javafx.collections.*;

import java.time.*;

public class ReleaseDate {
    private SimpleStringProperty territory;
    private SimpleObjectProperty<LocalDate> date;
    public ReleaseDate(String territory, LocalDate date){
        this.territory = new SimpleStringProperty(territory);
        this.date = new SimpleObjectProperty(date);
    }

    public SimpleStringProperty getTerritory() {
        return territory;
    }

    public void setTerritory(SimpleStringProperty territory) {
        this.territory = territory;
    }

    public SimpleObjectProperty<LocalDate> getDate() {
        return date;
    }


    public void setDate(SimpleObjectProperty<LocalDate> date) {
        this.date = date;
    }
}
