package edu.umuc.controllers;
import edu.umuc.models.RankWeight;
import edu.umuc.models.School;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class creates the threads to pull the data for a certain sport and year 
 * from the Washington Post website.
 */
public class ScrapeData {
    private static final Logger LOG = LoggerFactory.getLogger(ScrapeData.class);

    public List<School> scrapeData(String year, String season, String sport, RankWeight rankWeight) throws InterruptedException, TimeoutException {
        /**
         * Retrieves the schools from the controller object
         */
        final List<School> schools = Controller.getSchools();

        /**
         * Limits the maximum number of threads
         */
        final ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(300);

        /**
         * Create a thread for each school to scrape data and add the information to the school object
         */
        schools.forEach(school -> {
            final GetDataThread task = new GetDataThread(school, year, season, sport);
            executor.execute(task);
        });

        executor.shutdown();

        /**
         * Wait for threads to complete before continuing. 
         * threadsCompletedBeforeTimeout is set to true if all threads complete before the timeout
         */
        boolean threadsCompletedBeforeTimeout = executor.awaitTermination(5, TimeUnit.MINUTES);

        /**
         * For each opponent faced, add the opponent wins to the school object oppWins
         */
        if (threadsCompletedBeforeTimeout) {
            addOppWins(schools);
            calculateTotalPoints(rankWeight, schools);
            sortSchools(schools);
        } else {
            throw new TimeoutException("Data scrape operation timed out. Check connection and Washington Post website for proper operation.");
        }

        return schools;
    }

    /**
     * Adds up all opponent wins for each school
     */
    private static void addOppWins(List<School> schools){
        schools.forEach(school -> {
            school.getOpponents().forEach(opponent -> {
                final School oppSchool = schools.stream()
                        .filter(school1 -> school1.getUrlPath().equals(opponent))
                        .findFirst()
                        .orElse(null);

                if (oppSchool == null) {
                    LOG.warn("Unable to find school path for opponent: " + opponent);
                } else {
                    school.addOpponentWins(oppSchool.getWins());
                }
            });
        });
    }

    /**
     * Calculates total rank points per school
     * @param rankWeight    The rankweight objects to use for the calculation
     * @param schools       The schools to rank
     */
    public static void calculateTotalPoints(RankWeight rankWeight, List<School> schools) {
        schools.stream()
               .filter(school -> school.getWins() != 0 || school.getLosses() != 0)
               .forEach(school -> school.getTotalPoints(rankWeight, Controller.getLeagueWeightForSchool(school.getSchoolName())));
    }

    /**
     * Sorts schools list by rank points
     * @param schools The schools to sort
     */
    public static void sortSchools(List<School> schools) {
        schools.sort((school1, school2) -> (int) ((school2.getRankPoints() * 100) - (school1.getRankPoints() * 100)));
    }
}