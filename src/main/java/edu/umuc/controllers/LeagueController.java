package edu.umuc.controllers;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.*;

/**
 * This class is the controller for the League page
 */
public class LeagueController extends Controller implements Initializable {

    /**
     * The table for the league data
     */
    @FXML
    private TableView<FXLeagueTable> tbLeague;

    /**
     * School name column for the league data table
     */
    @FXML
    public TableColumn<FXLeagueTable, String> lgSchoolName;

    /**
     * League name column for the league data table
     */
    @FXML
    public TableColumn<FXLeagueTable, String> lgLeagueName;

    /**
     * League weight column for the league data table
     */
    @FXML
    public TableColumn<FXLeagueTable, Float> lgLeagueFactor;

    /**
     * Default Constructor
     */
    public LeagueController() {
    }

    /**
     * Initializes the form on load
     * @param location
     * @param resources 
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadLeagueInformation();
    }

    /**
     * Loads the league information on the UI tbLeague table
     */
    private void loadLeagueInformation(){
        lgSchoolName.setCellValueFactory(new PropertyValueFactory<>("lgSchoolName"));
        lgLeagueName.setCellValueFactory(new PropertyValueFactory<>("lgLeagueName"));
        lgLeagueFactor.setCellValueFactory(new PropertyValueFactory<>("lgLeagueFactor"));

        final ObservableList<FXLeagueTable> studentsModels = FXCollections.observableArrayList();

        getLeagues().forEach(league -> league.getSchools().forEach(school -> {
            studentsModels.add(new FXLeagueTable(school, league.getName(), league.getWeight()));
        }));

        tbLeague.setItems(studentsModels);
    }

    /**
     * In order to load information on the UI, we need to create a class with the JavaFX
     * bean properties ex: SimpleStringProperty and SimpleFloatProperty.
     *
     * Created a local class FXLeagueTable for this purpose
     */
    public class FXLeagueTable {
        private final SimpleStringProperty lgSchoolName;
        private final SimpleStringProperty lgLeagueName;
        private final SimpleFloatProperty lgLeagueFactor;

        private FXLeagueTable(String lgSchoolName, String lgLeagueName, Float lgLeagueFactor) {
            this.lgSchoolName = new SimpleStringProperty(lgSchoolName);
            this.lgLeagueName = new SimpleStringProperty(lgLeagueName);
            this.lgLeagueFactor = new SimpleFloatProperty(lgLeagueFactor);
        }

        public String getLgSchoolName() {
            return lgSchoolName.get();
        }

        public String getLgLeagueName() {
            return lgLeagueName.get();
        }

        public float getLgLeagueFactor() {
            return lgLeagueFactor.get();
        }
    }
}
