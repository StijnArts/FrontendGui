package stijn.dev.resource.service;

import javafx.beans.value.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.stage.*;
import org.controlsfx.control.*;
import stijn.dev.datasource.importing.*;
import stijn.dev.resource.controllers.*;
import stijn.dev.util.javafx.*;

import java.io.*;
import java.util.*;

public class ImportPlatformSelectionService {
    private PlatformProcessor platformProcessor = new PlatformProcessor();
    public void next(Stage stage, Scene scene, String importingAsPlatform, String scrapeAsPlatform, List<File> files) {
        FXMLLoader loader = FXMLLoaderUtil.createFMXLLoader("importOverview.fxml");
        Parent root = RootUtil.createRoot(loader);
        Scene overviewScene = new Scene(root,scene.getWidth(),scene.getHeight());
        ImportOverviewController.create(loader, files, stage, overviewScene, importingAsPlatform, scrapeAsPlatform);
        stage.setScene(overviewScene);
        stage.show();
    }
    public void initialize(Button nextButton, PrefixSelectionComboBox<String> scrapeAsPlatformComboBox,
                           PrefixSelectionComboBox<String> platformComboBox, ImportPlatformSelectionController controller,
                           TextField customPlatformBox) {
        nextButton.setDisable(true);
        scrapeAsPlatformComboBox.setDisable(true);
        setPlatformComboBoxBehavior(platformComboBox, controller, scrapeAsPlatformComboBox);
        setCustomPlatformBoxBehavior(customPlatformBox, controller, scrapeAsPlatformComboBox, nextButton);
        setScrapeAsPlatformComboBoxBehavior(scrapeAsPlatformComboBox, controller, nextButton);
    }
    private void setScrapeAsPlatformComboBoxBehavior(PrefixSelectionComboBox<String> scrapeAsPlatformComboBox,
                                                     ImportPlatformSelectionController controller, Button nextButton) {
        scrapeAsPlatformComboBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                controller.setScrapeAsPlatform(scrapeAsPlatformComboBox.getValue());
                System.out.println("Scraping As Platform: "+ scrapeAsPlatformComboBox.getValue());
                if(null!= scrapeAsPlatformComboBox.getValue()&&!scrapeAsPlatformComboBox.getValue().isEmpty()) {
                    nextButton.setDisable(false);
                }
            }
        });
    }
    private void setCustomPlatformBoxBehavior(TextField customPlatformBox, ImportPlatformSelectionController controller,
                                              PrefixSelectionComboBox<String> scrapeAsPlatformComboBox, Button nextButton) {
        customPlatformBox.textProperty().addListener((observableValue, s, t1) -> {
            controller.setImportingAsPlatform(customPlatformBox.getText());
            scrapeAsPlatformComboBox.setDisable(false);
            nextButton.setDisable(false);
        });
    }
    private void setPlatformComboBoxBehavior(PrefixSelectionComboBox<String> platformComboBox,
                                             ImportPlatformSelectionController controller,
                                             PrefixSelectionComboBox<String> scrapeAsPlatformComboBox) {
        platformComboBox.valueProperty().addListener((observableValue, s, t1) -> {
            controller.setImportingAsPlatform(platformComboBox.getValue());
            scrapeAsPlatformComboBox.setDisable(false);
            scrapeAsPlatformComboBox.setValue(platformComboBox.getValue());
        });
    }
    public void setPlatformSelections(String importAs, String scrapeAs, PrefixSelectionComboBox<String> platformComboBox, PrefixSelectionComboBox<String> scrapeAsPlatformComboBox) {
        if(importAs!=null) {
            platformComboBox.setValue(importAs);
        }
        if(scrapeAs!=null) {
            scrapeAsPlatformComboBox.setValue(scrapeAs);
        }
    }

    public void processPlatforms(PrefixSelectionComboBox<String> platformComboBox, PrefixSelectionComboBox<String> scrapeAsPlatformComboBox, List<File> files) {
        platformProcessor.processPlatforms(platformComboBox,scrapeAsPlatformComboBox,files);
    }
}
