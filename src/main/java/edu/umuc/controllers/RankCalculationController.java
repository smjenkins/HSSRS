package edu.umuc.controllers;

import edu.umuc.models.League;
import edu.umuc.models.RankWeight;
import edu.umuc.models.School;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * This class is the controller for the Rank Calculation page
 */
public class RankCalculationController extends Controller implements Initializable {

    /**
     * The School Results TableView and associated columns
     */
    @FXML
    private TableView<RankCalculationController.FXResultsTable> tbSchoolResults;

    @FXML
    private TableColumn<RankCalculationController.FXResultsTable, Integer> wins;

    @FXML
    private TableColumn<RankCalculationController.FXResultsTable, Integer> losses;

    @FXML
    private TableColumn<RankCalculationController.FXResultsTable, String> leagueName;

    @FXML
    private TableColumn<RankCalculationController.FXResultsTable, String> leagueFactor;

    @FXML
    private TableColumn<RankCalculationController.FXResultsTable, Float> weightWinLoss;

    @FXML
    private TableColumn<RankCalculationController.FXResultsTable, Float> weightOppPoints;

    @FXML
    private TableColumn<RankCalculationController.FXResultsTable, Float> weightAvgPointsScored;

    @FXML
    private TableColumn<RankCalculationController.FXResultsTable, Float> oppWins;

    @FXML
    private TableColumn<RankCalculationController.FXResultsTable, String> avgPointDifferential;

    /**
     * The Calculation TableView and associated columns
     */
    @FXML
    private TableView<RankCalculationController.FXCalculationTable> tbCalculation;

    @FXML
    private TableColumn<RankCalculationController.FXCalculationTable, String> pointsForWin;

    @FXML
    private TableColumn<RankCalculationController.FXCalculationTable, String> pointsForLosses;

    @FXML
    private TableColumn<RankCalculationController.FXCalculationTable, String> sumOfPoints;

    @FXML
    private TableColumn<RankCalculationController.FXCalculationTable, String> pointsFromOppWins;

    @FXML
    private TableColumn<RankCalculationController.FXCalculationTable, String> pointsFromAvgOppDiff;

    @FXML
    private TableColumn<RankCalculationController.FXCalculationTable, String> totalPoints;

    @FXML
    private Label textArea;

    @FXML
    private Button btnHome;

    @FXML
    private Button btnSchoolsRanking;

    @FXML
    private Label lblSchoolName;

    /**
     * Default constructor
     */
    public RankCalculationController() {
    }

    /**
     * Sets up the tables and populates the data
     *
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        wins.setCellValueFactory(new PropertyValueFactory<>("wins"));
        losses.setCellValueFactory(new PropertyValueFactory<>("losses"));
        leagueName.setCellValueFactory(new PropertyValueFactory<>("leagueName"));
        leagueFactor.setCellValueFactory(new PropertyValueFactory<>("leagueWeight"));
        weightWinLoss.setCellValueFactory(new PropertyValueFactory<>("winLossWeight"));
        weightOppPoints.setCellValueFactory(new PropertyValueFactory<>("oppWinWeight"));
        weightAvgPointsScored.setCellValueFactory(new PropertyValueFactory<>("avgPointDiffWeight"));
        oppWins.setCellValueFactory(new PropertyValueFactory<>("oppWins"));
        avgPointDifferential.setCellValueFactory(new PropertyValueFactory<>("avgPointDiff"));

        pointsForWin.setCellValueFactory(new PropertyValueFactory<>("pointsForWins"));
        pointsForLosses.setCellValueFactory(new PropertyValueFactory<>("pointsForLosses"));
        sumOfPoints.setCellValueFactory(new PropertyValueFactory<>("sumOfPoints"));
        pointsFromOppWins.setCellValueFactory(new PropertyValueFactory<>("pointsFromOpponentWins"));
        pointsFromAvgOppDiff.setCellValueFactory(new PropertyValueFactory<>("pointsFromAveragePointDiff"));
        totalPoints.setCellValueFactory(new PropertyValueFactory<>("totalPoints"));

        final ObservableList<RankCalculationController.FXResultsTable> result = FXCollections.observableArrayList();
        final ObservableList<RankCalculationController.FXCalculationTable> calculation = FXCollections.observableArrayList();

        if (getSelectedSchool() != null) {
            lblSchoolName.setText(getSelectedSchool().getSchoolName() + " Results");
            result.add(new FXResultsTable(getSelectedLeague(), getSelectedSchool(), getRankWeight()));
            calculation.add(new FXCalculationTable(getSelectedLeague(), getSelectedSchool(), getRankWeight()));
        } else {
            lblSchoolName.setText("Results");
        }
        tbSchoolResults.setItems(result);
        tbCalculation.setItems(calculation);

        //Added for readability
        float opponentTotalWins = getSelectedSchool().getOpponentsTotalWins();
        float opponentSize = getSelectedSchool().getOpponents().size();
        float wins = getSelectedSchool().getWins();
        float losses = getSelectedSchool().getLosses();
        float winLossWeight = getRankWeight().getWinLoss();
        float opponentWinWeight = getRankWeight().getOppWins();
        float averagePointDifference = getSelectedSchool().getAvgPointDifference();
        float averagePointDifferenceWeight = getRankWeight().getAvgOppDifference();
        float lastSeasonPercentWeight = getRankWeight().getLastSeasonPercentWeight();
        float previousYearPoints = getSelectedSchool().getPreviousYearPoints();
        float valAverageOpponentWins = opponentSize == 0 ? 0 : opponentTotalWins / opponentSize;
        float valPoints = (wins - losses) * winLossWeight;
        float valPointsOpponentWins = opponentSize == 0 ? 0 : (opponentTotalWins / opponentSize) * opponentWinWeight;
        float valDifferential = averagePointDifference * averagePointDifferenceWeight;
        float valLastSeason = lastSeasonPercentWeight == 0 ? 0 : previousYearPoints * lastSeasonPercentWeight;
        float valTotal = valAverageOpponentWins + valPoints + valPointsOpponentWins + valDifferential + valLastSeason;

        textArea.setText("Average Opponent Wins: Opponents Wins (" + opponentTotalWins + ") " + " divided by the Opponent size (" + opponentSize + ") equaling " + DECIMAL_FORMAT.format(valAverageOpponentWins) + "\n" +
                "Sum of Points: Team wins (" + wins + ") minus team losses (" + losses + ") times the Win-Loss Weight (" + winLossWeight + ") " + " equaling " + DECIMAL_FORMAT.format(valPoints) + "\n" +
                "Points from Opponents Wins: Average Opponent Wins (" + DECIMAL_FORMAT.format(opponentSize == 0 ? 0 : opponentTotalWins / opponentSize) + ") times the Opponent Points Weight (" + opponentWinWeight + ") equaling " + DECIMAL_FORMAT.format(valPointsOpponentWins) + "\n" +
                "Points from Average Point Differential: Average point difference (" + DECIMAL_FORMAT.format(averagePointDifference) + ") times the Avg Point Difference (" + averagePointDifferenceWeight + ") equaling " + DECIMAL_FORMAT.format(valDifferential) + "\n" +
                (lastSeasonPercentWeight > 0 ? "Points from Last Season: Total Points (" + DECIMAL_FORMAT.format(previousYearPoints) + ") times Last Season Weight (" + DECIMAL_FORMAT.format(lastSeasonPercentWeight) + ") equaling  " + DECIMAL_FORMAT.format(valLastSeason) + "\n" : "") +
                "Total Points: " + DECIMAL_FORMAT.format(valAverageOpponentWins) + " + " + DECIMAL_FORMAT.format(valPoints) + " + " + DECIMAL_FORMAT.format(valPointsOpponentWins) + " + " + DECIMAL_FORMAT.format(valDifferential) + " + " + (lastSeasonPercentWeight > 0 ? DECIMAL_FORMAT.format(valLastSeason) : "") + " = " + DECIMAL_FORMAT.format(valTotal) + "\n\n\n\n\n\n\n\n");
    }

    /**
     * Class created for the Results table
     */
    public class FXResultsTable {
        private final SimpleIntegerProperty wins;
        private final SimpleIntegerProperty losses;
        private final SimpleStringProperty leagueName;
        private final SimpleStringProperty leagueWeight;
        private final SimpleFloatProperty winLossWeight;
        private final SimpleFloatProperty oppWinWeight;
        private final SimpleFloatProperty avgPointDiffWeight;
        private final SimpleStringProperty oppWins;
        private final SimpleStringProperty avgPointDiff;


        private FXResultsTable(League league, School school, RankWeight rankWeight) {
            this.wins = new SimpleIntegerProperty(school.getWins());
            this.losses = new SimpleIntegerProperty(school.getLosses());
            this.leagueName = new SimpleStringProperty(league.getName());
            this.leagueWeight = new SimpleStringProperty(DECIMAL_FORMAT.format(league.getWeight()));
            this.winLossWeight = new SimpleFloatProperty(rankWeight.getWinLoss());
            this.oppWinWeight = new SimpleFloatProperty(rankWeight.getOppWins());
            this.avgPointDiffWeight = new SimpleFloatProperty(rankWeight.getAvgOppDifference());
            this.oppWins = new SimpleStringProperty(String.valueOf(DECIMAL_FORMAT.format(school.getOpponents().size() == 0 ? 0 : school.getOpponentsTotalWins() / school.getOpponents().size())));
            this.avgPointDiff = new SimpleStringProperty(DECIMAL_FORMAT.format(school.getAvgPointDifference()));
        }

        public SimpleIntegerProperty winsProperty() {
            return wins;
        }

        public SimpleIntegerProperty lossesProperty() {
            return losses;
        }

        public SimpleStringProperty leagueNameProperty() {
            return leagueName;
        }

        public SimpleStringProperty leagueWeightProperty() {
            return leagueWeight;
        }

        public SimpleFloatProperty winLossWeightProperty() {
            return winLossWeight;
        }

        public SimpleFloatProperty oppWinWeightProperty() {
            return oppWinWeight;
        }

        public SimpleFloatProperty avgPointDiffWeightProperty() {
            return avgPointDiffWeight;
        }

        public SimpleStringProperty oppWinsProperty() {
            return oppWins;
        }

        public SimpleStringProperty avgPointDiffProperty() {
            return avgPointDiff;
        }

        public int getWins() {
            return wins.get();
        }

        public int getLosses() {
            return losses.get();
        }

        public String getLeagueName() {
            return leagueName.get();
        }

        public String getLeagueWeight() {
            return leagueWeight.get();
        }

        public float getWinLossWeight() {
            return winLossWeight.get();
        }

        public float getOppWinWeight() {
            return oppWinWeight.get();
        }

        public float getAvgPointDiffWeight() {
            return avgPointDiffWeight.get();
        }

        public String getOppWins() {
            return oppWins.get();
        }

        public String getAvgPointDiff() {
            return avgPointDiff.get();
        }

    }

    /**
     * Class created for the Calculation table
     */
    public class FXCalculationTable {
        private final SimpleStringProperty pointsForWins;
        private final SimpleStringProperty pointsForLosses;
        private final SimpleStringProperty sumOfPoints;
        private final SimpleStringProperty pointsFromOpponentWins;
        private final SimpleStringProperty pointsFromAveragePointDiff;
        private final SimpleStringProperty totalPoints;

        private FXCalculationTable(League league, School school, RankWeight rankWeight) {
            this.pointsForWins = new SimpleStringProperty(DECIMAL_FORMAT.format(school.getPointsForWins(league.getWeight())));
            this.pointsForLosses = new SimpleStringProperty(DECIMAL_FORMAT.format(school.getPointsForLosses(league.getWeight())));
            this.sumOfPoints = new SimpleStringProperty(DECIMAL_FORMAT.format(school.getSumOfPoints(rankWeight, league.getWeight())));
            this.pointsFromOpponentWins = new SimpleStringProperty(DECIMAL_FORMAT.format(school.getPointsFromOpponentWins(rankWeight)));
            this.pointsFromAveragePointDiff = new SimpleStringProperty(DECIMAL_FORMAT.format(school.getPointsFromAveragePointDifferential(rankWeight)));
            this.totalPoints = new SimpleStringProperty(DECIMAL_FORMAT.format(school.getTotalPoints(rankWeight, league.getWeight())));
        }

        public SimpleStringProperty pointsForWinsProperty() {
            return pointsForWins;
        }

        public String getPointsForWins() {
            return pointsForWins.get();
        }

        public SimpleStringProperty pointsForLossesProperty() {
            return pointsForLosses;
        }

        public String getPointsForLosses() {
            return pointsForLosses.get();
        }

        public SimpleStringProperty sumOfPointsProperty() {
            return sumOfPoints;
        }

        public String getSumOfPoints() {
            return sumOfPoints.get();
        }

        public SimpleStringProperty pointsFromOpponentWinsProperty() {
            return pointsFromOpponentWins;
        }

        public String getPointsFromOpponentWins() {
            return pointsFromOpponentWins.get();
        }

        public SimpleStringProperty pointsFromAveragePointDiffProperty() {
            return pointsFromAveragePointDiff;
        }

        public String getPointsFromAveragePointDiff() {
            return pointsFromAveragePointDiff.get();
        }

        public SimpleStringProperty totalPointsProperty() {
            return totalPoints;
        }

        public String getTotalPoints() {
            return totalPoints.get();
        }
    }
}
