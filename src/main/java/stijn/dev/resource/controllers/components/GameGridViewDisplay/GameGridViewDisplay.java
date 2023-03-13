package stijn.dev.resource.controllers.components.GameGridViewDisplay;

import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.effect.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;

public class GameGridViewDisplay extends AnchorPane {
    private Label gameTitleLabel;
    private ImageView gameCoverImage;
    private String gameTitle;
    private String imageLocation;

    public GameGridViewDisplay(String title, String imageLocation){
        this.gameTitle = title;
        //TODO make it take in the image location of the game
        this.imageLocation = "C:\\Users\\stijn\\Desktop\\Front End project\\game-boy-grey-square-red-box-variation-front-1553417605-61.jpg";
        initGraphics();
    }

    public String getGameTitle() {
        return gameTitle;
    }

    private void initGraphics(){
        gameTitleLabel = new Label(gameTitle);

        gameTitleLabel.layoutYProperty().setValue(0);
        gameTitleLabel.setLabelFor(gameCoverImage);
        gameTitleLabel.setFocusTraversable(false);
        gameTitleLabel.setAlignment(Pos.CENTER);
        gameTitleLabel.setText(gameTitle);
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
