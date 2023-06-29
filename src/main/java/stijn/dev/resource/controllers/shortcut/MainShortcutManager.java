package stijn.dev.resource.controllers.shortcut;

import javafx.scene.input.*;
import stijn.dev.resource.controllers.*;

public class MainShortcutManager {

    public static void configureShortcuts(MainController mainController){
        configureRefreshShortcut(mainController);



    }

    public static void configureRefreshShortcut(MainController mainController) {
        mainController.getScene().getAccelerators().put(
                new KeyCodeCombination(KeyCode.R, KeyCombination.SHORTCUT_DOWN),
                () -> mainController.refreshTable()
        );
    }
}
