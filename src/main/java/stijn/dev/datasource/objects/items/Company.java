package stijn.dev.datasource.objects.items;

import java.util.*;

public class Company {
    private String publisherName;
    private Optional<Date> foundingDate = null;
    private Optional<String> description = null;
    private OptionalInt numberOfEmployees;
    private Optional<String> CEO = null;
    private Optional<String> website = null;

    public Company(String publisherName){
        this.publisherName = publisherName;
    }

    public String getPublisherName() {
        return publisherName;
    }

    public Optional<Date> getFoundingDate() {
        return foundingDate;
    }

    public Optional<String> getDescription() {
        return description;
    }

    public OptionalInt getNumberOfEmployees() {
        return numberOfEmployees;
    }

    public Optional<String> getCEO() {
        return CEO;
    }

    public Optional<String> getWebsite() {
        return website;
    }

    public void setPublisherName(String publisherName) {
        this.publisherName = publisherName;
    }

    public void setFoundingDate(Optional<Date> foundingDate) {
        this.foundingDate = foundingDate;
    }

    public void setDescription(Optional<String> description) {
        this.description = description;
    }

    public void setNumberOfEmployees(OptionalInt numberOfEmployees) {
        this.numberOfEmployees = numberOfEmployees;
    }

    public void setCEO(Optional<String> CEO) {
        this.CEO = CEO;
    }

    public void setWebsite(Optional<String> website) {
        this.website = website;
    }
}
