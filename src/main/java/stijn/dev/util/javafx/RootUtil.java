package stijn.dev.util.javafx;

import javafx.fxml.*;
import javafx.scene.*;

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
