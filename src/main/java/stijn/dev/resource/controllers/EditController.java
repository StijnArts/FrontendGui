package stijn.dev.resource.controllers;

import javafx.collections.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import stijn.dev.datasource.database.dao.*;
import stijn.dev.datasource.importing.xml.*;
import stijn.dev.datasource.objects.data.*;
import stijn.dev.datasource.objects.items.*;
import stijn.dev.resource.controllers.components.*;
import stijn.dev.util.javafx.*;

import java.util.*;

public class EditController {
    @FXML
    ComboBox<ComboBoxItemWrap<String>> developerComboCheckBox;
    @FXML
    ComboBox<ComboBoxItemWrap<String>> publisherComboCheckBox;
    @FXML
    ComboBox<ComboBoxItemWrap<String>> playModesComboCheckbox;
    @FXML
    ComboBox<ComboBoxItemWrap<String>> ratingComboCheckBox;
    @FXML
    TextField titleTextField;
    @FXML
    ComboBox<String> platformComboBox;
    @FXML
    ComboBox<PriorityData> priorityComboBox;
    @FXML
    TextField gameIdTextField;
    @FXML
    TextField maxPlayersField;
    @FXML
    TextField defaultSortingTitleTextField;
    @FXML
    TextArea descriptionTextArea;
    private Game game;
    private GamesXMLParser gamesXMLParser = new GamesXMLParser();
    private DeveloperDAO developerDAO = new DeveloperDAO();
    private PublisherDAO publisherDAO = new PublisherDAO();
    private PriorityDataDAO priorityDataDAO = new PriorityDataDAO();
    private RatingDAO ratingDAO = new RatingDAO();
    private PlaymodeDAO playmodeDAO = new PlaymodeDAO();

    public static EditController create(FXMLLoader loader, Game gameImportItem){
        EditController ec = loader.getController();
        ec.configure(gameImportItem);
        return ec;
    }

    public void configure(Game gameImportItem){
        this.game = gameImportItem;
        configurePublisherComboCheckBox();
        configureDeveloperComboCheckBox();
        configureRatingComboCheckBox();
        configurePlaymodeComboCheckBox();
        configureTitleTextField();
        configurePlatformComboBox();
        configurePriorityComboBox();
        configureMaxPlayersField();
        configureDefaultSortingTitleField();
        configureDefaultSummaryField();
        configureDescriptionTextArea();
    }

    private void configureDefaultSummaryField() {
        if(!"null".equals(game.getSummary())){
            maxPlayersField.setText(game.getSummary());
        }
    }

    private void configurePlaymodeComboCheckBox() {
        ObservableList<ComboBoxItemWrap<String>> options = FXCollections.observableList(generateComboBoxItemWrappers(playmodeDAO.getPlaymodes()));
        String publisherValue = "";
        for (String publisher : game.getPlaymodes()) {
            publisherValue += publisher + "; ";
        }
        playModesComboCheckbox.setValue(new ComboBoxItemWrap<>(publisherValue));
        playModesComboCheckbox.setCellFactory(c->{
            ListCell<ComboBoxItemWrap<String>> cell = new ListCell<>(){
                boolean initialized = false;
                @Override
                protected void updateItem(ComboBoxItemWrap<String> item, boolean empty) {
                    super.updateItem(item, empty);
                    if(!empty){
                        final CheckBox checkBox = new CheckBox(item.toString());
                        checkBox.selectedProperty().bind(item.checkProperty());
                        if(!initialized){
                            if(game.getPlaymodes().contains(item.getItem())){
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
                playModesComboCheckbox.getItems().filtered(f->f.getCheck()).forEach(p->{
                    stringBuilder.append("; "+p.getItem());
                });
                final String string = stringBuilder.toString();
                playModesComboCheckbox.setValue(new ComboBoxItemWrap<String>(string.substring(Integer.min(2, string.length()))));
            });
            return cell;
        });
        playModesComboCheckbox.setItems(options);
    }

    private void configurePriorityComboBox() {
        ObservableList<PriorityData> options = FXCollections.observableList(priorityDataDAO.getPriorities());
        priorityComboBox.setItems(options);
        priorityComboBox.setValue(game.getPriority());
    }

    private void configureMaxPlayersField() {
        if(!"null".equals(game.getMaxPlayers())){
            maxPlayersField.setText(game.getMaxPlayers());
        }
    }

    private void configureDefaultSortingTitleField() {
        if(!"null".equals(game.getSortingTitle())){
            defaultSortingTitleTextField.setText(game.getSortingTitle());
        }
    }

    public void configureTitleTextField(){
        titleTextField.setText(game.getName());
    }

    private void configureDescriptionTextArea() {
        if(!"null".equals(game.getDescription())){
            descriptionTextArea.setText(game.getDescription());
        }
    }

    public void configurePlatformComboBox(){
        ObservableList<String> options = FXCollections.observableList(PlatformXMLParser.getPlatforms());
        platformComboBox.setItems(options);
        platformComboBox.setValue(game.getPlatform());
        ComboBoxAutocompleteUtil.autoCompleteComboBoxPlus(platformComboBox, (typedText, itemToCompare) -> itemToCompare.toLowerCase().contains(typedText.toLowerCase()));
    }

    public void configureRatingComboCheckBox(){
        ObservableList<ComboBoxItemWrap<String>> options = FXCollections.observableList(generateComboBoxItemWrappers(ratingDAO.getRatings()));
        String publisherValue = "";
        for (String publisher : game.getRatings()) {
            publisherValue += publisher + "; ";
        }
        ratingComboCheckBox.setValue(new ComboBoxItemWrap<>(publisherValue));
        ratingComboCheckBox.setCellFactory(c->{
            ListCell<ComboBoxItemWrap<String>> cell = new ListCell<>(){
                boolean initialized = false;
                @Override
                protected void updateItem(ComboBoxItemWrap<String> item, boolean empty) {
                    super.updateItem(item, empty);
                    if(!empty){
                        final CheckBox checkBox = new CheckBox(item.toString());
                        checkBox.selectedProperty().bind(item.checkProperty());
                        if(!initialized){
                            if(game.getRatings().contains(item.getItem())){
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
                ratingComboCheckBox.getItems().filtered(f->f.getCheck()).forEach(p->{
                    stringBuilder.append("; "+p.getItem());
                });
                final String string = stringBuilder.toString();
                ratingComboCheckBox.setValue(new ComboBoxItemWrap<String>(string.substring(Integer.min(2, string.length()))));
            });
            return cell;
        });
        ratingComboCheckBox.setItems(options);
    }

    public void configurePublisherComboCheckBox(){
        ObservableList<ComboBoxItemWrap<String>> options = FXCollections.observableList(generateComboBoxItemWrappers(publisherDAO.getPublishers()));
        String publisherValue = "";
        for (String publisher : game.getPublisher()) {
            publisherValue += publisher + "; ";
        }
        publisherComboCheckBox.setValue(new ComboBoxItemWrap<>(publisherValue));
        publisherComboCheckBox.setCellFactory(c->{
            ListCell<ComboBoxItemWrap<String>> cell = new ListCell<>(){
                boolean initialized = false;
                @Override
                protected void updateItem(ComboBoxItemWrap<String> item, boolean empty) {
                    super.updateItem(item, empty);
                    if(!empty){
                        final CheckBox checkBox = new CheckBox(item.toString());
                        checkBox.selectedProperty().bind(item.checkProperty());
                        if(!initialized){
                            if(game.getPublisher().contains(item.getItem())){
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
                publisherComboCheckBox.getItems().filtered(f->f.getCheck()).forEach(p->{
                    stringBuilder.append("; "+p.getItem());
                });
                final String string = stringBuilder.toString();
                publisherComboCheckBox.setValue(new ComboBoxItemWrap<String>(string.substring(Integer.min(2, string.length()))));
            });
            return cell;
        });
        publisherComboCheckBox.setItems(options);
    }

    public void configureDeveloperComboCheckBox(){
        ObservableList<ComboBoxItemWrap<String>> options = FXCollections.observableList(generateComboBoxItemWrappers(developerDAO.getDevelopers()));
        String publisherValue = "";
        for (String publisher : game.getDeveloper()) {
            publisherValue += publisher + "; ";
        }
        developerComboCheckBox.setValue(new ComboBoxItemWrap<>(publisherValue));
        developerComboCheckBox.setCellFactory(c->{
            ListCell<ComboBoxItemWrap<String>> cell = new ListCell<>(){
                boolean initialized = false;
                @Override
                protected void updateItem(ComboBoxItemWrap<String> item, boolean empty) {
                    super.updateItem(item, empty);
                    if(!empty){
                        final CheckBox checkBox = new CheckBox(item.toString());
                        checkBox.selectedProperty().bind(item.checkProperty());
                        if(!initialized){
                            if(game.getDeveloper().contains(item.getItem())){
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
                developerComboCheckBox.getItems().filtered(f->f.getCheck()).forEach(p->{
                    stringBuilder.append("; "+p.getItem());
                });
                final String string = stringBuilder.toString();
                developerComboCheckBox.setValue(new ComboBoxItemWrap<String>(string.substring(Integer.min(2, string.length()))));
            });
            return cell;
        });
        developerComboCheckBox.setItems(options);
    }

    public ArrayList<ComboBoxItemWrap<String>> generateComboBoxItemWrappers(List<String> strings){
        ArrayList<ComboBoxItemWrap<String>> comboBoxItemWraps = new ArrayList<>();
        for (String string :
                strings) {
            comboBoxItemWraps.add(new ComboBoxItemWrap<>(string));
        }
        return comboBoxItemWraps;
    }

}
