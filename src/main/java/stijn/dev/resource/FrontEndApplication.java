package stijn.dev.resource;

import javafx.application.Application;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.image.*;
import javafx.scene.text.*;
import javafx.stage.Stage;
import stijn.dev.data.database.*;

import java.io.IOException;

public class FrontEndApplication extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        var databseHelper = new Neo4JDatabaseHelper();
//        databseHelper.runQuery("CREATE\n" +
//                "                (charlie:Person {name: 'Charlie Sheen'}),\n" +
//                "        (martin:Person {name: 'Martin Sheen'}),\n" +
//                "        (michael:Person {name: 'Michael Douglas'}),\n" +
//                "        (oliver:Person {name: 'Oliver Stone'}),\n" +
//                "        (rob:Person {name: 'Rob Reiner'}),\n" +
//                "        (wallStreet:Movie {title: 'Wall Street'}),\n" +
//                "        (charlie)-[:ACTED_IN {role: 'Bud Fox'}]->(wallStreet),\n" +
//                "                (martin)-[:ACTED_IN {role: 'Carl Fox'}]->(wallStreet),\n" +
//                "                (michael)-[:ACTED_IN {role: 'Gordon Gekko'}]->(wallStreet),\n" +
//                "                (oliver)-[:DIRECTED]->(wallStreet),\n" +
//                "                (thePresident:Movie {title: 'The American President'}),\n" +
//                "        (martin)-[:ACTED_IN {role: 'A.J. MacInerney'}]->(thePresident),\n" +
//                "                (michael)-[:ACTED_IN {role: 'President Andrew Shepherd'}]->(thePresident),\n" +
//                "                (rob)-[:DIRECTED]->(thePresident),\n" +
//                "                (martin)-[:FATHER_OF]->(charlie)");
//        databseHelper.runQuery("MATCH (n:Person)  return n.name").stream().forEach(record ->
//                record.fields().stream().forEach(pair-> System.out.println(pair.key()+pair.value())));
        //XMLParser.parseRoms(new String[]{"gex", "mario"});
        Parent root = FXMLLoader.load(getClass().getResource("demo.fxml"));
//        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
//        primaryStage.setTitle("Hello!");
//        primaryStage.setScene(scene);
        //Group root = new Group();
        Scene scene = new Scene(root);
        String css = this.getClass().getResource("demo.css").toExternalForm();
        scene.getStylesheets().add(css);
        Text text = new Text();
        text.setText("Text");
        text.setX(50);
        text.setY(50);
        //root.getChildren().add(text);

        Image icon = new Image("C:\\Users\\stijn\\Desktop\\Front End project\\FrontendGui\\src\\main\\resources\\icons\\library-icon.png");
        ImageView imageView = new ImageView(icon);
        imageView.setX(100);
        imageView.setY(100);
        //root.getChildren().add(imageView);
//        primaryStage.getIcons().add(icon);
//        primaryStage.setTitle("Frontend");

//        primaryStage.setFullScreen(true);
        primaryStage.setScene(scene);
        primaryStage.show();
        //Stage stage = new Stage();




    }

    public static void main(String[] args) {
        launch(args);
    }
}