package edu.umuc;

import edu.umuc.models.RankWeight;
import edu.umuc.models.School;

import static edu.umuc.controllers.Controller.DECIMAL_FORMAT;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TestSuite {

    private School school;
    private RankWeight rankWeight;

    @Before
    public void setup() {
        System.out.println("Initializing Test Suite.");
        school = new School();
        school.initialize();
        rankWeight = new RankWeight(1, 1, 1);
    }

    @Test
    public void divideByZeroSchool() {
        System.out.println("Testing School class divide by zero functions.");
        school.setAvgOpponentWins();//Potential DBZ function
        school.getPointsFromOpponentWins(rankWeight);//Potential DBZ function
        assertEquals((int)school.getPointsFromOpponentWins(rankWeight), 0);
    }

    @Test
    /**
     * This test is not very clean, but tests something in the controller that is also not very clean.
     * Essentially ripping line-for-line from the controller since much of the data required to fully test it
     * will not be available at test time.  Using our test class school, we know that if divide by zeros exist,
     * they should appear here since the school will be 0 wins / 0 losses / 0 opponents at initialization.
     * Writing the resultant text to log.  An error would be thrown if the test was unsuccessful simply because
     * of the divide by zero.
     */
    public void divideByZeroController() {
        System.out.println("Testing controller class divide by zero functions.");
        System.out.println("Average Opponent Wins is: Opponents Wins (" + this.school.getOpponentsTotalWins() +
                ") divided by the Opponent size (" + this.school.getOpponents().size() + ") equaling " +
                String.valueOf(DECIMAL_FORMAT.format(this.school.getOpponents().size() == 0 ? 0 : this.school.getOpponentsTotalWins() / this.school.getOpponents().size())) + "\n" +
                "Sum of Points is: Team wins (" + this.school.getWins() + ") minus team losses (" + this.school.getLosses() +
                ") times the Win-Loss Weight (" + this.rankWeight.getWinLoss() + ") equaling " +
                String.valueOf(DECIMAL_FORMAT.format((this.school.getWins() - this.school.getLosses()) * this.rankWeight.getWinLoss())) + "\n" +
                "Points from Opponents Wins is: Average Opponent Wins (" + DECIMAL_FORMAT.format(this.school.getOpponents().size() == 0 ? 0 : this.school.getOpponentsTotalWins() / this.school.getOpponents().size()) +
                ") times the Opponent Points Weight (" + this.rankWeight.getOppWins() + ") equaling " +
                DECIMAL_FORMAT.format((this.school.getOpponents().size() == 0 ? 0 : this.school.getOpponentsTotalWins() / this.school.getOpponents().size()) * this.rankWeight.getOppWins()) + "\n" +
                "Points from Avg. Point Differential is: Average point difference (" + DECIMAL_FORMAT.format(this.school.getAvgPointDifference()) + ") times the Avg Point Difference (" +
                this.rankWeight.getAvgOppDifference() + ") equaling " + DECIMAL_FORMAT.format(this.school.getAvgPointDifference() * this.rankWeight.getAvgOppDifference()) + "\n" +
                "Total Points is: Sum of Points (" + DECIMAL_FORMAT.format((this.school.getWins() - this.school.getLosses()) * this.rankWeight.getWinLoss()) +
                ") plus the Pts from Opponents Wins (" + DECIMAL_FORMAT.format((this.school.getOpponents().size() == 0 ? 0 : this.school.getOpponentsTotalWins() / this.school.getOpponents().size()) * this.rankWeight.getOppWins()) +
                ") plus the Pts from Avg Point Differential (" + DECIMAL_FORMAT.format(this.school.getAvgPointDifference() * this.rankWeight.getAvgOppDifference()) + ") equaling " +
                DECIMAL_FORMAT.format((((this.school.getWins() - this.school.getLosses()) * this.rankWeight.getWinLoss()) +
                        (this.school.getOpponents().size() == 0 ? 0 : (this.school.getOpponentsTotalWins() / this.school.getOpponents().size()) * this.rankWeight.getOppWins()) +
                        (this.school.getAvgPointDifference() * this.rankWeight.getAvgOppDifference()))) + "\n\n\n\n\n\n\n\n");
    }
}
