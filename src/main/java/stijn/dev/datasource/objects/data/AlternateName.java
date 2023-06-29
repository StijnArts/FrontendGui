package stijn.dev.datasource.objects.data;

import javafx.beans.property.*;

public class AlternateName {
    private StringProperty alternateName;
    private StringProperty alternateNameID;
    private StringProperty region;
    public AlternateName(String alternateNameID,String alternateName){
        this.alternateNameID = new SimpleStringProperty(alternateNameID);
        this.alternateName = new SimpleStringProperty(alternateName);
    }

    public AlternateName(String alternateNameId, String alternateName, String region) {
        this.alternateNameID = new SimpleStringProperty(alternateNameId);
        this.alternateName = new SimpleStringProperty(alternateName);
        this.region = new SimpleStringProperty(region);
    }

    public AlternateName(){

    }

    public StringProperty getAlternateName() {
        return alternateName;
    }

    public void setAlternateName(String alternateName) {
        this.alternateName = new SimpleStringProperty(alternateName);
    }

    public StringProperty getAlternateNameID() {
        return alternateNameID;
    }

    public void setAlternateNameID(String alternateNameID) {
        this.alternateNameID = new SimpleStringProperty(alternateNameID);
    }

    public StringProperty getReason() {
        return region;
    }

    public void setRegion(String region) {
        this.region = new SimpleStringProperty(region);
    }
}
