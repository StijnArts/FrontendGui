package stijn.dev.resource;

import javafx.application.Application;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.image.*;
import javafx.scene.text.*;
import javafx.stage.Stage;

import java.io.IOException;
//TODO refactor code (Extract methods and classes)
//TODO add dependency Injection
//TODO add interfaces
public class FrontEndApplication extends Application {
    public static boolean importProcessIsRunning;
    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("demo.fxml"));
        Scene scene = new Scene(root);
        String css = this.getClass().getResource("demo.css").toExternalForm();
        scene.getStylesheets().add(css);
        Text text = new Text();
        text.setText("Text");
        text.setX(50);
        text.setY(50);

        Image icon = new Image("C:\\Users\\stijn\\Desktop\\Front End project\\FrontendGui\\src\\main\\resources\\icons\\library-icon.png");
        ImageView imageView = new ImageView(icon);
        imageView.setX(100);
        imageView.setY(100);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}