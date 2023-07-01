package stijn.dev.resource.service;

import javafx.collections.*;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.util.*;
import stijn.dev.datasource.database.dao.*;
import stijn.dev.resource.controllers.*;
import stijn.dev.resource.controllers.components.*;

import java.util.*;

import static stijn.dev.resource.controllers.components.ComboBoxItemWrap.generateComboBoxItemWrappers;

public class PlatformEditPrepper {
    public void configure(PlatformEditScreenController platformEditScreenController) {

        //General
        configurePlatformNameField(platformEditScreenController);
        configurePublisherCheckComboBox(platformEditScreenController);
        configureManufacturerCheckComboBox(platformEditScreenController);
        configureMediaCheckComboBox(platformEditScreenController);
//        configureMediaTypeCheckComboBox(platformEditScreenController);
//        configureProductFamilyComboBox(platformEditScreenController);
//        configureGenerationField(platformEditScreenController);
//        configureUnitsSoldField(platformEditScreenController);
//        configureDiscontinuedOnDatePicker(platformEditScreenController);
//        configureDefaultSummaryTextArea(platformEditScreenController);
//
//        configureDescriptionTextArea(platformEditScreenController);
//        configureAlternateNamesTable(platformEditScreenController);
//        configureRelatedPlatformsTable(platformEditScreenController);
//        configureSpecificationsTable(platformEditScreenController);
//        configureTriviaTable(platformEditScreenController);
//        configureReleaseDatesTable(platformEditScreenController);

    }

    private void configureMediaCheckComboBox(PlatformEditScreenController platformEditScreenController) {
        MediaDAO mediaDAO = new MediaDAO();
        ObservableList<ComboBoxItemWrap<String>> options = FXCollections.observableList(generateComboBoxItemWrappers(
                mediaDAO.getMedia().stream().filter(publisher->!publisher.isEmpty()).toList()));
        String publisherValue = "";
        for (String manufacturer : platformEditScreenController.getPlatform().getManufacturers()) {
            publisherValue += manufacturer + "; ";
        }
        platformEditScreenController.getMediaComboBox().setValue(new ComboBoxItemWrap<>(publisherValue));
        platformEditScreenController.getMediaComboBox().setConverter(new StringConverter<ComboBoxItemWrap<String>>() {
            @Override
            public String toString(ComboBoxItemWrap<String> stringComboBoxItemWrap) {
                return stringComboBoxItemWrap.itemProperty().getValue();
            }

            @Override
            public ComboBoxItemWrap<String> fromString(String s) {
                return new ComboBoxItemWrap<String>(s);
            }
        });
        platformEditScreenController.getMediaComboBox().setCellFactory(c->{
            ListCell<ComboBoxItemWrap<String>> cell = new ListCell<>(){
                boolean initialized = false;
                @Override
                protected void updateItem(ComboBoxItemWrap<String> item, boolean empty) {
                    super.updateItem(item, empty);
                    if(!empty){
                        final CheckBox checkBox = new CheckBox(item.toString());
                        checkBox.selectedProperty().bind(item.checkProperty());
                        if(!initialized){
                            if(platformEditScreenController.getPlatform().getMedia().contains(item.getItem())){
                                item.checkProperty().set(true);
                            }
                            initialized = true;
                        }
                        setGraphic(checkBox);
                    }

                }
            };

            cell.addEventFilter(MouseEvent.MOUSE_RELEASED, mouseEvent -> {
                cell.getItem().checkProperty().set(!cell.getItem().checkProperty().get());
                StringBuilder stringBuilder = new StringBuilder();
                platformEditScreenController.getMediaComboBox().getItems().filtered(f->f.getCheck()).forEach(p->{
                    stringBuilder.append("; "+p.getItem());
                });
                final String string = stringBuilder.toString();
                platformEditScreenController.getMediaComboBox().setValue(new ComboBoxItemWrap<String>(string.substring(Integer.min(2, string.length()))));
                platformEditScreenController.getMediaComboBox().tooltipProperty().setValue(new Tooltip(string.substring(Integer.min(2, string.length()))));
            });
            return cell;
        });
        platformEditScreenController.getMediaComboBox().valueProperty().addListener((observableValue, stringComboBoxItemWrap, t1) -> {
            platformEditScreenController.getMediaComboBox().show();
            List<String> selectedOptions = Arrays.stream(platformEditScreenController.getMediaComboBox().getValue().getItem().split("; ")).toList();
            options.forEach(option -> {
                for (String selectedOption : selectedOptions) {
                    if(selectedOption.trim().toLowerCase().contains(option.getItem().toLowerCase())){
                        option.setCheck(true);
                        return;
                    }
                }
                option.setCheck(false);

            });
        });
        platformEditScreenController.getMediaComboBox().focusedProperty().addListener(((observableValue, aBoolean, t1) -> {
            platformEditScreenController.getMediaComboBox().setValue(new ComboBoxItemWrap<>(platformEditScreenController.getMediaComboBox().getValue().itemProperty().getValue()));
        }));
        platformEditScreenController.getMediaComboBox().setItems(options);
    }

    private void configureManufacturerCheckComboBox(PlatformEditScreenController platformEditScreenController) {
        ManufacturerDAO manufacturerDAO = new ManufacturerDAO();
        ObservableList<ComboBoxItemWrap<String>> options = FXCollections.observableList(generateComboBoxItemWrappers(
                manufacturerDAO.getManufacturers().stream().filter(publisher->!publisher.isEmpty()).toList()));
        String publisherValue = "";
        for (String manufacturer : platformEditScreenController.getPlatform().getManufacturers()) {
            publisherValue += manufacturer + "; ";
        }
        platformEditScreenController.getManufacturerComboBox().setValue(new ComboBoxItemWrap<>(publisherValue));
        platformEditScreenController.getManufacturerComboBox().setConverter(new StringConverter<ComboBoxItemWrap<String>>() {
            @Override
            public String toString(ComboBoxItemWrap<String> stringComboBoxItemWrap) {
                return stringComboBoxItemWrap.itemProperty().getValue();
            }

            @Override
            public ComboBoxItemWrap<String> fromString(String s) {
                return new ComboBoxItemWrap<String>(s);
            }
        });
        platformEditScreenController.getManufacturerComboBox().setCellFactory(c->{
            ListCell<ComboBoxItemWrap<String>> cell = new ListCell<>(){
                boolean initialized = false;
                @Override
                protected void updateItem(ComboBoxItemWrap<String> item, boolean empty) {
                    super.updateItem(item, empty);
                    if(!empty){
                        final CheckBox checkBox = new CheckBox(item.toString());
                        checkBox.selectedProperty().bind(item.checkProperty());
                        if(!initialized){
                            if(platformEditScreenController.getPlatform().getManufacturers().contains(item.getItem())){
                                item.checkProperty().set(true);
                            }
                            initialized = true;
                        }
                        setGraphic(checkBox);
                    }

                }
            };

            cell.addEventFilter(MouseEvent.MOUSE_RELEASED, mouseEvent -> {
                cell.getItem().checkProperty().set(!cell.getItem().checkProperty().get());
                StringBuilder stringBuilder = new StringBuilder();
                platformEditScreenController.getManufacturerComboBox().getItems().filtered(f->f.getCheck()).forEach(p->{
                    stringBuilder.append("; "+p.getItem());
                });
                final String string = stringBuilder.toString();
                platformEditScreenController.getManufacturerComboBox().setValue(new ComboBoxItemWrap<String>(string.substring(Integer.min(2, string.length()))));
                platformEditScreenController.getManufacturerComboBox().tooltipProperty().setValue(new Tooltip(string.substring(Integer.min(2, string.length()))));
            });
            return cell;
        });
        platformEditScreenController.getManufacturerComboBox().valueProperty().addListener((observableValue, stringComboBoxItemWrap, t1) -> {
            platformEditScreenController.getManufacturerComboBox().show();
            List<String> selectedOptions = Arrays.stream(platformEditScreenController.getManufacturerComboBox().getValue().getItem().split("; ")).toList();
            options.forEach(option -> {
                for (String selectedOption : selectedOptions) {
                    if(selectedOption.trim().toLowerCase().contains(option.getItem().toLowerCase())){
                        option.setCheck(true);
                        return;
                    }
                }
                option.setCheck(false);

            });
        });
        platformEditScreenController.getManufacturerComboBox().focusedProperty().addListener(((observableValue, aBoolean, t1) -> {
            platformEditScreenController.getManufacturerComboBox().setValue(new ComboBoxItemWrap<>(platformEditScreenController.getManufacturerComboBox().getValue().itemProperty().getValue()));
        }));
        platformEditScreenController.getManufacturerComboBox().setItems(options);
    }

    private void configurePlatformNameField(PlatformEditScreenController platformEditScreenController) {
        if(!"null".equals(platformEditScreenController.getPlatform().getPlatformName())){
            platformEditScreenController.getPlatformNameField().setText(platformEditScreenController.getPlatform().getPlatformName());
        }
    }

    private void configurePublisherCheckComboBox(PlatformEditScreenController platformEditScreenController) {
        PublisherDAO publisherDAO = new PublisherDAO();
        ObservableList<ComboBoxItemWrap<String>> options = FXCollections.observableList(generateComboBoxItemWrappers(
                publisherDAO.getPublishers().stream().filter(publisher->!publisher.isEmpty()).toList()));
        String publisherValue = "";
        for (String publisher : platformEditScreenController.getPlatform().getPublishers()) {
            publisherValue += publisher + "; ";
        }
        platformEditScreenController.getPublisherComboBox().setValue(new ComboBoxItemWrap<>(publisherValue));
        platformEditScreenController.getPublisherComboBox().setConverter(new StringConverter<ComboBoxItemWrap<String>>() {
            @Override
            public String toString(ComboBoxItemWrap<String> stringComboBoxItemWrap) {
                return stringComboBoxItemWrap.itemProperty().getValue();
            }

            @Override
            public ComboBoxItemWrap<String> fromString(String s) {
                return new ComboBoxItemWrap<String>(s);
            }
        });
        platformEditScreenController.getPublisherComboBox().setCellFactory(c->{
            ListCell<ComboBoxItemWrap<String>> cell = new ListCell<>(){
                boolean initialized = false;
                @Override
                protected void updateItem(ComboBoxItemWrap<String> item, boolean empty) {
                    super.updateItem(item, empty);
                    if(!empty){
                        final CheckBox checkBox = new CheckBox(item.toString());
                        checkBox.selectedProperty().bind(item.checkProperty());
                        if(!initialized){
                            if(platformEditScreenController.getPlatform().getPublishers().contains(item.getItem())){
                                item.checkProperty().set(true);
                            }
                            initialized = true;
                        }
                        setGraphic(checkBox);
                    }

                }
            };

            cell.addEventFilter(MouseEvent.MOUSE_RELEASED, mouseEvent -> {
                cell.getItem().checkProperty().set(!cell.getItem().checkProperty().get());
                StringBuilder stringBuilder = new StringBuilder();
                platformEditScreenController.getPublisherComboBox().getItems().filtered(f->f.getCheck()).forEach(p->{
                    stringBuilder.append("; "+p.getItem());
                });
                final String string = stringBuilder.toString();
                platformEditScreenController.getPublisherComboBox().setValue(new ComboBoxItemWrap<String>(string.substring(Integer.min(2, string.length()))));
                platformEditScreenController.getPublisherComboBox().tooltipProperty().setValue(new Tooltip(string.substring(Integer.min(2, string.length()))));
            });
            return cell;
        });
        platformEditScreenController.getPublisherComboBox().valueProperty().addListener((observableValue, stringComboBoxItemWrap, t1) -> {
            platformEditScreenController.getPublisherComboBox().show();
            List<String> selectedOptions = Arrays.stream(platformEditScreenController.getPublisherComboBox().getValue().getItem().split("; ")).toList();
            options.forEach(option -> {
                for (String selectedOption : selectedOptions) {
                    if(selectedOption.trim().toLowerCase().contains(option.getItem().toLowerCase())){
                        option.setCheck(true);
                        return;
                    }
                }
                option.setCheck(false);

            });
        });
        platformEditScreenController.getPublisherComboBox().focusedProperty().addListener(((observableValue, aBoolean, t1) -> {
            platformEditScreenController.getPublisherComboBox().setValue(new ComboBoxItemWrap<>(platformEditScreenController.getPublisherComboBox().getValue().itemProperty().getValue()));
        }));
        platformEditScreenController.getPublisherComboBox().setItems(options);
    }
}
