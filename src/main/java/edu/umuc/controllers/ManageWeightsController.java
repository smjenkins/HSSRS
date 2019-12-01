package edu.umuc.controllers;

import edu.umuc.models.RankWeight;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

/**
 * This class is the controller for the Manage Weights page
 */
public class ManageWeightsController extends Controller implements Initializable {
    private static final Logger LOG = LoggerFactory.getLogger(ManageWeightsController.class);
    private static final String DEFAULT_RANK_WEIGHT_YAML ="defaultRankWeight.yaml";
    private static final String SAVED_RANK_WEIGHT_YAML ="savedRankWeight.yaml";

    /**
     * Declaring text field that correspond with the FXML objects
     */
    @FXML
    private TextField winLossWeight;
    
    @FXML
    private TextField oppWinsWeight;
    
    @FXML
    private TextField avgPtsDiffWeight;

    @FXML
    private TextField lastSeasonPercentCalc;

    /**
     * Default Constructor
     */
    public ManageWeightsController() { }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeTextFields();
    }

    /**
     * Initializes the rank weights text fields
     */
    private void initializeTextFields() {
        final RankWeight rankWeight = super.loadRankWeight(SAVED_RANK_WEIGHT_YAML);
        setRankWeight(rankWeight);
        winLossWeight.setText(DECIMAL_FORMAT.format(rankWeight.getWinLoss()));
        oppWinsWeight.setText(DECIMAL_FORMAT.format(rankWeight.getOppWins()));
        avgPtsDiffWeight.setText(DECIMAL_FORMAT.format(rankWeight.getAvgOppDifference()));
        lastSeasonPercentCalc.setText(DECIMAL_FORMAT.format(rankWeight.getLastSeasonPercentWeight()));
    }

    /**
     * Handles the save weights action
     */
    @FXML
    public void handleSaveWeights() {
        if (proceedSavingWeights()) {
            try{
                /**
                 * Convert textfield input to float and store new values
                 */
                final Float winLoss = Float.parseFloat(winLossWeight.getText());
                final Float oppWins = Float.parseFloat(oppWinsWeight.getText());
                final Float avgOppDifference = Float.parseFloat(avgPtsDiffWeight.getText());
                final Float lastSeason = Float.parseFloat(lastSeasonPercentCalc.getText());
                final RankWeight savedRankWeight = new RankWeight(winLoss, oppWins, avgOppDifference, lastSeason);
                setRankWeight(savedRankWeight);

                /**
                 * Write saved weight info to file
                 */
                saveWeights(savedRankWeight);
                reRankSchools();

                winLossWeight.setText(DECIMAL_FORMAT.format(savedRankWeight.getWinLoss()));
                oppWinsWeight.setText(DECIMAL_FORMAT.format(savedRankWeight.getOppWins()));
                avgPtsDiffWeight.setText(DECIMAL_FORMAT.format(savedRankWeight.getAvgOppDifference()));
                lastSeasonPercentCalc.setText(DECIMAL_FORMAT.format(savedRankWeight.getLastSeasonPercentWeight()));

            } catch (NumberFormatException e){
                /**
                 * Display alert window to the user if an invalid value is entered
                 */
                final Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Dialog");
                alert.setHeaderText("Invalid Value Detected");
                alert.setContentText("Please only enter numeric values.");

                alert.showAndWait();
            }
        }
    }

    /**
     * Handles the reset weights action
     */
    @FXML
    private void handleResetWeights() {
        if (proceedSavingWeights()) {
            final RankWeight rankWeight = super.loadRankWeight(DEFAULT_RANK_WEIGHT_YAML);
            setRankWeight(rankWeight);
            winLossWeight.setText(DECIMAL_FORMAT.format(rankWeight.getWinLoss()));
            oppWinsWeight.setText(DECIMAL_FORMAT.format(rankWeight.getOppWins()));
            avgPtsDiffWeight.setText(DECIMAL_FORMAT.format(rankWeight.getAvgOppDifference()));
            lastSeasonPercentCalc.setText(DECIMAL_FORMAT.format(rankWeight.getLastSeasonPercentWeight()));

            /**
             * "Autosave" default values on reset
             */
            saveWeights(rankWeight);
            reRankSchools();
        }
    }

    /**
     * Reusable method to save weights
     */
    private void saveWeights(RankWeight rankWeight){
        final DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        final Yaml yaml = new Yaml(options);

        try {
            FileWriter writer = new FileWriter(configPath + SAVED_RANK_WEIGHT_YAML);
            yaml.dump(rankWeight, writer);
        } catch (IOException ex) {
           LOG.error("Error occurred while saving the rank weights", ex);
        }
    }

    private boolean proceedSavingWeights() {
        final boolean retValue;

        /**
         * Only ask for confirmation if there are school results
         */
        if (isSchoolsRanked()) {
            final Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Dialog");
            alert.setHeaderText("Updating the weights will also update the School Ranking results");
            alert.setContentText("Do you want to proceed?");
            alert.showAndWait();

            /**
             * If the user did not click the cancel button, return true
             */
            retValue = !alert.getResult().getButtonData().getTypeCode().equals("C");
        } else {
            retValue = true;
        }

        return retValue;
    }
}

