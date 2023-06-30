package stijn.dev.resource.controllers;

import javafx.beans.binding.*;
import javafx.beans.property.*;
import javafx.collections.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import org.controlsfx.control.tableview2.*;
import org.controlsfx.control.tableview2.cell.*;
import stijn.dev.datasource.database.dao.*;
import stijn.dev.datasource.objects.items.*;
import stijn.dev.util.javafx.*;

import java.util.*;

public class TagConfigurationScreenController {
    private Stage stage;
    private List<DetailedTagItem> tags;
    @FXML
    private TableView2<DetailedTagItem> tagTable;
    private static TagDAO tagDAO = new TagDAO();
    private EditController editController;
    private int tempTagCounter = 0;
    public static TagConfigurationScreenController create(FXMLLoader loader, Stage stage, EditController editController){
        TagConfigurationScreenController controller = loader.getController();

        controller.configure(tagDAO.getDetailedTags(), stage, editController);
        return controller;
    }

    public static void openTagConfigurationScreen(Stage stage){
        FXMLLoader loader = FXMLLoaderUtil.createFMXLLoader("tagConfigurationScreen.fxml");
        Parent root = RootUtil.createRoot(loader);
        Stage tagStage = new Stage();
        tagStage.initOwner(stage);
        Scene scene = new Scene(root);
        TagConfigurationScreenController.create(loader, tagStage);
        tagStage.setScene(scene);
        tagStage.show();
    }

    public static TagConfigurationScreenController create(FXMLLoader loader, Stage stage){
        TagConfigurationScreenController controller = loader.getController();
        controller.configure(tagDAO.getDetailedTags(), stage);
        return controller;
    }

    public void saveTags(){
        for(Object tag : tagTable.getItems()){
            if(tag instanceof DetailedTagItem){
                if(!((DetailedTagItem) tag).getName().isEmpty()){
                    HashMap<String, Object> tagParameters = new HashMap<>();
                    tagParameters.put("id", ((DetailedTagItem) tag).getId());
                    tagParameters.put("tag",((DetailedTagItem) tag).getName());
                    tagParameters.put("description",((DetailedTagItem) tag).getDescription());
                    tagParameters.put("sortingTitle",((DetailedTagItem) tag).getSortingTitle());
                    tagDAO.createTag(tagParameters);
                }
            }
        }
        if(editController != null){
            editController.updateTags();
        }
        this.stage.close();
    }

    public void closeDialog(){
        this.stage.close();
    }

    public void configure(List<DetailedTagItem> tags, Stage stage){
        this.tags = tags;
        this.stage = stage;
        configureTagTable();
    }

    public void configure(List<DetailedTagItem> tags, Stage stage, EditController editController){
        configure(tags, stage);
        this.editController = editController;
    }

    private void configureTagTable() {
        tagTable.setEditable(true);
        ObservableList<DetailedTagItem> tagObservable = FXCollections.observableList(tags);
        tagTable.setItems(tagObservable);
        tagTable.getItems().add(new DetailedTagItem(generateNewTagID(),"","",""));
        TableColumn2<DetailedTagItem,String> tagNameColumn = new FilteredTableColumn<>("Tag Name");
        tagNameColumn.setEditable(true);
        tagNameColumn.setCellValueFactory(p->p.getValue().nameProperty());
        tagNameColumn.setCellFactory(TextField2TableCell.forTableColumn());
        tagNameColumn.setOnEditCommit(event -> {
            if(!event.getNewValue().isBlank()){
                if(event.getTableView().getItems().size()-1==event.getTablePosition().getRow()){
                    event.getTableView().getItems().add(new DetailedTagItem(generateNewTagID(),"","",""));
                }
            }
            event.getTableView().getItems().get(event.getTablePosition().getRow()).setName(event.getNewValue());
        });
        TableColumn2<DetailedTagItem,String> descriptionColumn = new FilteredTableColumn<>("Tag Description");
        descriptionColumn.setEditable(true);
        descriptionColumn.setCellValueFactory(p->p.getValue().descriptionProperty());
        descriptionColumn.setCellFactory(TextField2TableCell.forTableColumn());
        descriptionColumn.setOnEditCommit(event -> {
            if(!event.getNewValue().isBlank()){
                if(event.getTableView().getItems().size()-1==event.getTablePosition().getRow()){
                    event.getTableView().getItems().add(new DetailedTagItem(generateNewTagID(),"","",""));
                }
            }
            event.getTableView().getItems().get(event.getTablePosition().getRow()).setDescription(event.getNewValue());
        });
        TableColumn2<DetailedTagItem,String> sortingTitle = new FilteredTableColumn<>("Sorting Title");
        sortingTitle.setEditable(true);
        sortingTitle.setCellValueFactory(p->p.getValue().sortingTitleProperty());
        sortingTitle.setCellFactory(TextField2TableCell.forTableColumn());
        sortingTitle.setOnEditCommit(event -> {
            if (!event.getNewValue().isBlank()) {
                if (event.getTableView().getItems().size() - 1 == event.getTablePosition().getRow()) {
                    event.getTableView().getItems().add(new DetailedTagItem(generateNewTagID(),"", "", ""));
                }
            }
            event.getTableView().getItems().get(event.getTablePosition().getRow()).setSortingTitle(event.getNewValue());
        });
        TableColumn2<DetailedTagItem,Integer> deleteColumn = new FilteredTableColumn<>("Remove Tag");
        deleteColumn.setCellValueFactory(p->new SimpleObjectProperty<>(p.getValue().getId()));
        deleteColumn.setCellFactory(col -> {
            TableCell<DetailedTagItem,Integer> c = new TableCell<>();
            final Button button = new Button("Delete");
            final BorderPane borderPane = new BorderPane();
            final HBox hBox = new HBox(5, button);
            borderPane.centerProperty().setValue(hBox);

            button.setOnAction(event -> {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirm Delete");
                alert.setHeaderText("Deleting a tag is permanent and removes the tag from all the associated games.");
                alert.setContentText("Are you sure you want to delete the tag?");

                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK){

                    tagDAO.deleteTag(c.getTableView().getItems().get(c.getIndex()).getId());
                    tags.removeIf(detailedTagItem -> detailedTagItem.getId()==c.getTableView().getItems().get(c.getIndex()).getId());
                } else {
                    alert.close();
                }
            });
            c.graphicProperty().bind(Bindings.when(c.emptyProperty()).then((Node)null).otherwise(hBox));
            return c;
        });
        tagTable.getColumns().setAll(tagNameColumn, descriptionColumn, sortingTitle, deleteColumn);
    }

    private String generateNewTagID() {
        int number = 1000000000+tempTagCounter;
        tempTagCounter++;
        return ""+number;
    }
}
