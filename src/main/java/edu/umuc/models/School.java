package edu.umuc.models;

import java.util.ArrayList;

/**
 * Represents a school and stores all of the required information to retrieve and store the data to calculate total rank points.
 * A single school object can be reused each time a different sport or year is selected by using the initialize method.
 */
public class School {

    /**
     * The schools name to be displayed
     */
    private String schoolName;

    /**
     * The path used by the Washington Post website to find the school
     * example: paint-branch is the path in https://www.washingtonpost.com/allmetsports/2018-fall/paint-branch/football/
     */
    private String urlPath;

    /**
     * Stores the calculated rankPoints
     */
    private float rankPoints = 0f;

    /**
     * Stores the number of wins for the school
     */
    private int wins = 0;

    /**
     * Stores the number of losses for the school
     */
    private int losses = 0;

    /**
     * Stores the average point difference for the school
     */
    private float avgPointDifference = 0f;

    /**
     * Stores the list of opponents for the school
     */
    private ArrayList<String> opponents = new ArrayList<>();

    /**
     * Stores the total wins from the schools opponents
     */
    private float opponentsTotalWins = 0;

    /**
     * Flag to indicate the record on the Washington Post website does not match the total Wins and Losses found in the results
     */
    private boolean winLossRecordIncorrect = false;
    /**
     * Average number of opponent wins
     */
    private float avgOpponentWins = 0f;

    private float previousYearPoints = 0f;

    public School() {

    }

    public School(String schoolName, String urlPath) {
        this.schoolName = schoolName;
        this.urlPath = urlPath;
    }

    /**
     * Used to reinitialize a school before a new sport or year is scraped
     */
    public void initialize() {
        rankPoints = 0f;
        wins = 0;
        losses = 0;
        avgPointDifference = 0f;
        opponents = new ArrayList<>();
        opponentsTotalWins = 0;
        winLossRecordIncorrect = false;
        avgOpponentWins = 0f;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getUrlPath() {
        return urlPath;
    }

    public void setUrlPath(String urlPath) {
        this.urlPath = urlPath;
    }

    public float getRankPoints() {
        return rankPoints;
    }

    public void setRankPoints(float rankPoints) {
        this.rankPoints = rankPoints;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getLosses() {
        return losses;
    }

    public void setLosses(int losses) {
        this.losses = losses;
    }

    public float getAvgPointDifference() {
        return avgPointDifference;
    }

    public void setAvgPointDifference(float avgPointDifference) {
        this.avgPointDifference = avgPointDifference;
    }

    public ArrayList<String> getOpponents() {
        return opponents;
    }

    public void setOpponents(ArrayList<String> opponents) {
        this.opponents = opponents;
    }

    public float getOpponentsTotalWins() {
        return opponentsTotalWins;
    }

    public void setOpponentsTotalWins(float opponentsTotalWins) {
        this.opponentsTotalWins = opponentsTotalWins;
    }

    public void addOpponent(String opponent) {
        this.opponents.add(opponent);
    }

    public boolean isWinLossRecordIncorrect() {
        return winLossRecordIncorrect;
    }

    public void setWinLossRecordIncorrect(boolean winLossRecordIncorrect) {
        this.winLossRecordIncorrect = winLossRecordIncorrect;
    }

    /**
     * Adds wins from an opponent to the opponent total wins count
     *
     * @param opponentsTotalWins
     */
    public void addOpponentWins(int opponentsTotalWins) {
        this.opponentsTotalWins += opponentsTotalWins;
    }

    public float getAvgOpponentWins() {
        return avgOpponentWins;
    }

    public void setAvgOpponentWins() {
        if (opponents == null || opponents.size() == 0) {
            this.avgOpponentWins = 0;
        } else {
            this.avgOpponentWins = opponentsTotalWins / opponents.size();
        }
    }

    /**
     * Calculates the points for the schools wins by multiplying wins by the league weight
     *
     * @param leagueWeight
     * @return points for school wins
     */
    public float getPointsForWins(float leagueWeight) {
        return wins * leagueWeight;
    }

    /**
     * Calculates the points for the schools losses by multiplying losses by the league weight
     *
     * @param leagueWeight
     * @return points for school losses
     */
    public float getPointsForLosses(float leagueWeight) {
        return (losses * (1 / leagueWeight)) * -1;
    }

    /**
     * Calculates the total points for wins and losses by calling getPointsForWins and getPointsForLosses
     *
     * @param rankWeight
     * @param leagueWeight
     * @return total points for wins and losses
     */
    public float getSumOfPoints(RankWeight rankWeight, float leagueWeight) {
        return (getPointsForWins(leagueWeight) + getPointsForLosses(leagueWeight)) * rankWeight.getWinLoss();
    }

    /**
     * Calculates points from opponent wins by multiplying the count of opponent wins by the rank weight for opponent wins
     *
     * @param rankWeight
     * @return total points from opponent wins
     */
    public float getPointsFromOpponentWins(RankWeight rankWeight) {
        return (opponents == null || opponents.size() == 0) ? 0 : opponentsTotalWins * rankWeight.getOppWins() / opponents.size();
    }

    /**
     * Calculates points from average point differential by multiplying the average point difference by the weight for the average opponent difference
     *
     * @param rankWeight
     * @return total points from average point differential
     */
    public float getPointsFromAveragePointDifferential(RankWeight rankWeight) {
        return (avgPointDifference * rankWeight.getAvgOppDifference());
    }

    /**
     * Calculates the total points by using getSumOfPoints, getPointsFromOpponentWins, and getPointsFromAveragePointDifferential
     *
     * @param rankWeight
     * @param leagueWeight
     * @return total points
     */
    public float getTotalPoints(RankWeight rankWeight, float leagueWeight) {
        rankPoints = (getPreviousYearPoints() * rankWeight.getLastSeasonPercentWeight()) + getSumOfPoints(rankWeight, leagueWeight) + getPointsFromOpponentWins(rankWeight) + getPointsFromAveragePointDifferential(rankWeight);
        return rankPoints;
    }

    public float getPreviousYearPoints() {
        return previousYearPoints;
    }

    public void setPreviousYearPoints(float previousYearPoints) {
        this.previousYearPoints = previousYearPoints;
    }
}
