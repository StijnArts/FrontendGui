package stijn.dev.util.javafx;

import javafx.fxml.*;
import stijn.dev.resource.*;

public class FXMLLoaderUtil {
    public static FXMLLoader createFMXLLoader(String resource){
        return new FXMLLoader(FrontEndApplication.class.getResource(resource));
    }

}
