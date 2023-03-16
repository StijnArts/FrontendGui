package stijn.dev.datasource.objects.items;

import java.util.*;

public class Developer {
    private String developerName;
    private Optional<Date> foundingDate = null;
    private Optional<String> description = null;
    private OptionalInt numberOfEmployees;
    private Optional<String> CEO = null;
    private Optional<String> website = null;

    public Developer(String developerName){
        this.developerName = developerName;
    }

    public String getDeveloperName() {
        return developerName;
    }

    public void setDeveloperName(String developerName) {
        this.developerName = developerName;
    }

    public Optional<Date> getFoundingDate() {
        return foundingDate;
    }

    public void setFoundingDate(Optional<Date> foundingDate) {
        this.foundingDate = foundingDate;
    }

    public Optional<String> getDescription() {
        return description;
    }

    public void setDescription(Optional<String> description) {
        this.description = description;
    }

    public OptionalInt getNumberOfEmployees() {
        return numberOfEmployees;
    }

    public void setNumberOfEmployees(OptionalInt numberOfEmployees) {
        this.numberOfEmployees = numberOfEmployees;
    }

    public Optional<String> getCEO() {
        return CEO;
    }

    public void setCEO(Optional<String> CEO) {
        this.CEO = CEO;
    }

    public Optional<String> getWebsite() {
        return website;
    }

    public void setWebsite(Optional<String> website) {
        this.website = website;
    }
}
