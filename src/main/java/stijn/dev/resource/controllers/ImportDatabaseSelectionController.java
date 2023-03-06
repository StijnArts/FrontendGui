package stijn.dev.resource.controllers;

import javafx.beans.binding.*;
import javafx.beans.property.*;
import javafx.collections.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import org.controlsfx.control.*;
import org.controlsfx.control.tableview2.*;
import org.controlsfx.control.tableview2.cell.*;
import stijn.dev.data.database.*;
import stijn.dev.records.*;

import java.net.*;
import java.util.*;

public class ImportDatabaseSelectionController implements Initializable {
    @FXML
    TableView2 IDSelectionTable;
    @FXML
    Button nextButton;
    @FXML
    Button backButton;

    private ObservableList<RomDatabaseComparingRecord> romDatabaseComparingRecords;

    public void onNextButtonPressed(ActionEvent event){
        List<RomDatabaseRecord> romDatabaseRecords = makeRomDatabaseRecords();
        DatabaseHelper.importRoms(romDatabaseRecords);
    }

    private List<RomDatabaseRecord> makeRomDatabaseRecords() {
        return null;
    }

    public ObservableList<RomDatabaseComparingRecord> getRomDatabaseComparingRecords() {
        return romDatabaseComparingRecords;
    }

    public void setRomDatabaseComparingRecords(ObservableList<RomDatabaseComparingRecord> romDatabaseComparingRecords) {
        this.romDatabaseComparingRecords = romDatabaseComparingRecords;
        setTable();
    }

    private void setTable() {
        IDSelectionTable.setItems(romDatabaseComparingRecords);
        IDSelectionTable.setEditable(true);
        TableColumn2<RomDatabaseComparingRecord, String> filenameColumn = new FilteredTableColumn<>("Filename:");
        filenameColumn.setCellValueFactory(p->p.getValue().romImportRecord().fullFilename());
        filenameColumn.setEditable(false);
        TableColumn2<RomDatabaseComparingRecord, StringProperty> databaseTitleColumn = new FilteredTableColumn<>("Database Results:");
        databaseTitleColumn.setCellValueFactory(i-> {
            for (StringProperty key : i.getValue().databaseResults().keySet()
            ) {
                final StringProperty value = i.getValue().databaseResults().get(key);
                return Bindings.createObjectBinding(()->value);
            }
            return Bindings.createObjectBinding(()->new SimpleStringProperty("Not Found"));
        });
        databaseTitleColumn.setCellFactory(col -> {
            TableCell<RomDatabaseComparingRecord, StringProperty> cell = new TableCell<>();
            final PrefixSelectionComboBox<String> comboBox = new PrefixSelectionComboBox<>();
            cell.itemProperty().addListener((observable, oldValue,newValue)->{
                if(oldValue != null){
                    comboBox.valueProperty().unbindBidirectional(oldValue);
                }
                if(newValue != null){
                    comboBox.valueProperty().bindBidirectional(newValue);
                }
            });
            cell.graphicProperty().bind(Bindings.when(cell.emptyProperty()).then((Node) null).otherwise(comboBox));
            return cell;
        });
        databaseTitleColumn.setEditable(true);
        IDSelectionTable.getColumns().addAll(filenameColumn,databaseTitleColumn);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }


}
