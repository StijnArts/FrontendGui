package stijn.dev.service.javafx;

import javafx.fxml.*;
import javafx.scene.*;
import stijn.dev.resource.*;

import java.io.*;

public class RootUtil {
    public static Parent createRoot(FXMLLoader loader){
        Parent root;
        try {
            root = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return root;
    }
}
