package stijn.dev.resource.controllers;

import javafx.fxml.*;
import javafx.stage.*;
import stijn.dev.datasource.objects.items.*;

public class ImageDetailScreenController {
    private static double cellHeight = 150;
    private static double cellWidth = 150;

    public static void create(FXMLLoader loader, GalleryItem galleryItem, Stage stage, Runnable onCloseRunnable) {

    }

    public static double getCellHeight() {
        return cellHeight;
    }

    public static void setCellHeight(double cellHeight) {
        ImageDetailScreenController.cellHeight = cellHeight;
    }

    public static double getCellWidth() {
        return cellWidth;
    }

    public static void setCellWidth(double cellWidth) {
        ImageDetailScreenController.cellWidth = cellWidth;
    }
}
