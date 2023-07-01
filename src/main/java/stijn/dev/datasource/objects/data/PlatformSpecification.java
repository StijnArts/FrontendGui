package stijn.dev.datasource.objects.data;

import javafx.beans.property.*;

public class PlatformSpecification {
    StringProperty specificationType;
    StringProperty specification;
    public PlatformSpecification(String specificationType, String specification){
        this.specificationType = new SimpleStringProperty(specificationType);
        this.specification = new SimpleStringProperty(specification);
    }

    public String getSpecificationType() {
        return specificationType.get();
    }

    public StringProperty specificationTypeProperty() {
        return specificationType;
    }

    public void setSpecificationType(String specificationType) {
        this.specificationType.set(specificationType);
    }

    public String getSpecification() {
        return specification.get();
    }

    public StringProperty specificationProperty() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification.set(specification);
    }
}
