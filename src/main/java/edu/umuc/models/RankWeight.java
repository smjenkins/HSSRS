package edu.umuc.models;

/**
 * Stores all of the information of a set of rank weights to
 * be used in the calculation of the rank points for a school
 */
public class RankWeight {
    
    /**
     * The rank weight for a schools wins and losses
     */
    private float winLoss;
 
    /**
     * The rank weight for the opponent wins
     */
    private float oppWins;
    
    /**
     * The rank weight for average opponent point differences
     */
    private float avgOppDifference;

    /**
     * The weight given to last season's average in this season's calculation.
     */
    private float lastSeasonPercentWeight;

    /**
     * Default Constructor
     */
    public RankWeight() {
    }

    /**
     * Constructor
     * @param winLoss           Weight assigned to wins and losses
     * @param oppWins           Weight assigned to opponent wins
     * @param avgOppDifference  Weight assigned to average opponent point difference
     */
    public RankWeight(float winLoss, float oppWins, float avgOppDifference) {
        this.winLoss = winLoss;
        this.oppWins = oppWins;
        this.avgOppDifference = avgOppDifference;
    }

    /**
     * Constructor
     * @param winLoss                   Weight assigned to wins and losses
     * @param oppWins                   Weight assigned to opponent wins
     * @param avgOppDifference          Weight assigned to average opponent point difference
     * @param lastSeasonPercentWeight   Weight given to a team's previous season average.
     */
    public RankWeight(float winLoss, float oppWins, float avgOppDifference, float lastSeasonPercentWeight) {
        this.winLoss = winLoss;
        this.oppWins = oppWins;
        this.avgOppDifference = avgOppDifference;
        this.lastSeasonPercentWeight = lastSeasonPercentWeight;
    }

    public float getWinLoss() {
        return winLoss;
    }

    public void setWinLoss(float winLoss) {
        this.winLoss = winLoss;
    }

    public float getOppWins() {
        return oppWins;
    }

    public void setOppWins(float oppWins) {
        this.oppWins = oppWins;
    }

    public float getAvgOppDifference() {
        return avgOppDifference;
    }

    public void setAvgOppDifference(float avgOppDifference) {
        this.avgOppDifference = avgOppDifference;
    }

    public float getLastSeasonPercentWeight() {
        return lastSeasonPercentWeight;
    }

    public void setLastSeasonPercentWeight(float lastSeasonPercentWeight) {
        this.lastSeasonPercentWeight = lastSeasonPercentWeight;
    }
}
