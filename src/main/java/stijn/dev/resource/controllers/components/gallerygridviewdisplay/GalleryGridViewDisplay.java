package stijn.dev.resource.controllers.components.gallerygridviewdisplay;

import javafx.beans.property.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.stage.*;
import stijn.dev.datasource.database.dao.*;
import stijn.dev.datasource.objects.data.enums.*;
import stijn.dev.datasource.objects.items.*;
import stijn.dev.resource.controllers.*;
import stijn.dev.util.javafx.*;

public class GalleryGridViewDisplay extends AnchorPane {
    private Label fileNameLabel;
    private ImageView mediaImage;
    private GalleryItem galleryItem;
    private String imageLocation;
    private ContextMenu contextMenu = new ContextMenu();
    private int itemId;
    private BooleanProperty refreshTracker;

    public GalleryGridViewDisplay(GalleryItem galleryItem, Runnable onCloseRunnable, int itemId, BooleanProperty refreshTracker){
        this.galleryItem = galleryItem;
        this.itemId = itemId;
        this.refreshTracker = refreshTracker;
        if(galleryItem.getFile().exists()&&galleryItem.getMediaType()== MediaTypes.IMAGE){
            this.imageLocation = galleryItem.getFile().getAbsolutePath();
        } else{
            this.imageLocation = "C:\\Users\\stijn\\Desktop\\Front End project\\mediaFilePlaceholder.jpg";
        }
        initGraphics();
        initContextMenu(onCloseRunnable);
    }

    private void initContextMenu(Runnable onCloseRunnable) {
        MenuItem view = new MenuItem();
        view.setText("View");
        view.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FXMLLoader loader = FXMLLoaderUtil.createFMXLLoader("mediaDetailScreen.fxml");
                Parent root = RootUtil.createRoot(loader);
                Stage stage = new Stage();
                Scene scene = new Scene(root);
                //TODO create different detail screen for each common kind of media (PDF, video file, image files, music files)
                ImageDetailScreenController.create(loader, galleryItem, stage, onCloseRunnable);
                stage.setScene(scene);
                stage.show();
            }
        });
        MenuItem delete = new MenuItem();
        delete.setText("Delete");
        delete.setOnAction(actionEvent -> {
            deleteMedia();
        });
        contextMenu.getItems().addAll(view,delete);
        super.setOnContextMenuRequested(e->{
            contextMenu.show(this,e.getScreenX(),e.getScreenY());
        });
    }

    private void deleteMedia() {
        GalleryDAO galleryDAO = new GalleryDAO();
        galleryDAO.removeFromGallery(galleryItem.getId(), itemId);
        refreshTracker.set(true);
    }

    public void setWidth(double width){
        super.setWidth(width);
        fileNameLabel.setMaxWidth(width);
        fileNameLabel.setMinWidth(width);
    }

    public void setHeight(double height){
        super.setHeight(height);
        fileNameLabel.setMaxHeight(25);
        fileNameLabel.layoutYProperty().setValue(height-fileNameLabel.getMaxHeight());
    }

    private void initGraphics(){
        fileNameLabel = new Label(galleryItem.getName());

        fileNameLabel.layoutYProperty().setValue(0);
        fileNameLabel.setLabelFor(mediaImage);
        fileNameLabel.setFocusTraversable(false);
        fileNameLabel.setAlignment(Pos.CENTER);
        fileNameLabel.setText(galleryItem.getName());
        fileNameLabel.setTextFill(Color.WHITE);
        fileNameLabel.setBackground(new Background(new BackgroundFill(Color.BLACK,null,null)));
        fileNameLabel.setMaxHeight(25);
        mediaImage = new ImageView();
        mediaImage.setSmooth(true);
        mediaImage.setImage(new Image(imageLocation));
        mediaImage.setFocusTraversable(false);
        mediaImage.preserveRatioProperty().setValue(true);
        mediaImage.setSmooth(true);
        mediaImage.setCache(true);
        mediaImage.fitWidthProperty().bind(this.widthProperty());
        mediaImage.fitHeightProperty().bind(this.heightProperty());
        getChildren().addAll(mediaImage,fileNameLabel);
    }

    public GalleryItem getMedia(){
        return galleryItem;
    }
}
