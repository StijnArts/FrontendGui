package stijn.dev.resource.controllers;

import javafx.collections.*;
import javafx.collections.transformation.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import org.controlsfx.control.tableview2.*;
import org.controlsfx.control.tableview2.cell.*;
import stijn.dev.records.*;

import java.net.*;
import java.util.*;

public class ImportOverviewController implements Initializable{

    @FXML
    private FilteredTableView<RomImportRecord> overviewTable;
    @FXML
    private Button nextButton;
    @FXML
    private Button backButton;

    ObservableList<RomImportRecord> roms;


    public List<RomImportRecord> getRoms() {
        return roms;
    }

    public void setRoms(List<RomImportRecord> roms) {
        this.roms = FXCollections.observableList(roms);

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        overviewTable.setItems(roms);
        TableColumn2<RomImportRecord, String> filenameColumn = new FilteredTableColumn<>("Filename:");
        filenameColumn.setCellValueFactory(p->p.getValue().fullFilename());
        filenameColumn.setEditable(false);
        TableColumn2<RomImportRecord, String> fileExtensionColumn = new FilteredTableColumn<>("File Extension:");
        fileExtensionColumn.setCellValueFactory(p->p.getValue().fileExtension());
        fileExtensionColumn.setEditable(false);
        filenameColumn.setCellFactory(TextField2TableCell.forTableColumn());
        overviewTable.getColumns().setAll(filenameColumn,fileExtensionColumn);

        overviewTable.setRowHeaderVisible(true);
    }
}
