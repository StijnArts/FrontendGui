package stijn.dev.resource.controllers.components.GameGridViewDisplay;

import javafx.event.*;
import javafx.fxml.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.stage.*;
import stijn.dev.datasource.objects.items.*;
import stijn.dev.resource.controllers.*;
import stijn.dev.util.javafx.*;

import java.io.*;

public class GameGridViewDisplay extends AnchorPane {
    private Label gameTitleLabel;
    private ImageView gameCoverImage;
    private Game game;
    private String imageLocation;
    private ContextMenu contextMenu = new ContextMenu();

    public GameGridViewDisplay(Game game, String imageLocation){
        this.game = game;
        //TODO make it take in the image location of the game
        File file = new File(imageLocation);
        if(file.exists()){
            this.imageLocation = imageLocation;
        } else{
            this.imageLocation = "C:\\Users\\stijn\\Desktop\\Front End project\\game-boy-grey-square-red-box-variation-front-1553417605-61.jpg";
        }
        initGraphics();
        MenuItem edit = new MenuItem();
        edit.setText("Edit");
        edit.acceleratorProperty().setValue(KeyCombination.valueOf("Ctrl+e"));
        edit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FXMLLoader loader = FXMLLoaderUtil.createFMXLLoader("edit.fxml");
                Parent root = RootUtil.createRoot(loader);
                Stage stage = new Stage();
                Scene scene = new Scene(root);
                EditController.create(loader,game);
                stage.setScene(scene);
                stage.show();
            }
        });
        contextMenu.getItems().add(edit);
        super.setOnContextMenuRequested(e->{
            contextMenu.show(this,e.getScreenX(),e.getScreenY());
        });
    }

    public Game getGame() {
        return game;
    }

    private void initGraphics(){
        gameTitleLabel = new Label(game.getName());

        gameTitleLabel.layoutYProperty().setValue(0);
        gameTitleLabel.setLabelFor(gameCoverImage);
        gameTitleLabel.setFocusTraversable(false);
        gameTitleLabel.setAlignment(Pos.CENTER);
        gameTitleLabel.setText(game.getName());
        gameTitleLabel.setTextFill(Color.WHITE);
        gameTitleLabel.setBackground(new Background(new BackgroundFill(Color.BLACK,null,null)));
        gameTitleLabel.setMaxHeight(25);
        gameCoverImage = new ImageView();
        gameCoverImage.setImage(new Image(imageLocation));
        gameCoverImage.setFocusTraversable(false);
        gameCoverImage.preserveRatioProperty().setValue(true);
        gameCoverImage.setSmooth(true);
        gameCoverImage.setCache(true);
        gameCoverImage.fitWidthProperty().bind(this.widthProperty());
        gameCoverImage.fitHeightProperty().bind(this.heightProperty());
        getChildren().addAll(gameCoverImage,gameTitleLabel);
    }

    public void setWidth(double width){
        super.setWidth(width);
        gameTitleLabel.setMaxWidth(width);
        gameTitleLabel.setMinWidth(width);
    }

    public void setHeight(double height){
        super.setHeight(height);
        gameTitleLabel.setMaxHeight(25);
        gameTitleLabel.layoutYProperty().setValue(height-gameTitleLabel.getMaxHeight());
    }
}
