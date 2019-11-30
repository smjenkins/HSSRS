package edu.umuc.controllers;

import edu.umuc.models.School;
import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;

import javax.net.ssl.SSLHandshakeException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class retrieves the data for a single school and loads the data into the 
 * school object.
 */
class GetDataThread implements Runnable {
    private static final Logger LOG = LoggerFactory.getLogger(GetDataThread.class);
	
    /**
     * The school object to store the scraped data
     */
    private final School school;

    /**
     * The thread performing the scrape
     */
    private Thread thread;

    /**
     * The year to scrape
     */
    private final String year;

    /**
     * The season to scrape
     */
    private final String season;

    /**
     * The sport to scrape
     */
    private final String sport;
	
    public GetDataThread (School school, String year, String season, String sport){
            this.school = school;
            this.year = year;
            this.season = season;
            this.sport = sport;
    }

    @Override
    public void run() {
        try {
            setSchoolData();
        } catch (IOException ex) {
            LOG.error("Exception with school: " + school.getSchoolName(), ex);
        }
    }

    /**
     * Gets the data and stores it in the school object
     * @throws IOException 
     */
    private void setSchoolData() throws IOException {
        boolean isError;
        int errorCount = 0;

        do {
            try {
                isError = false;
                int winCount = 0;
                int lossCount = 0;
                int pointsDifference = 0;
                int gameCount = 0;
                float averagePointDifference;
                String schoolPath;
                
                /**
                 * The URL to the school's sport data for the specified year
                 */
                String url = "https://www.washingtonpost.com/allmetsports/" + year + "-" + season + "/" + school.getUrlPath() + "/" + sport + "/";

                System.out.println("URL!!! - " + url);

                /**
                 * Get the data within the specified timeout period
                 */
                Document dataPage = Jsoup.connect(url).timeout(60 * 1000).get();  // 60 Seconds
                String title = dataPage.title();
                if (title == null || title.startsWith("404")) {
                    LOG.warn("Missing page for school: " + school.getSchoolName() + " using URL: " + url);    
                    return;
                }

                int recordWins = 0;
                int recordLosses = 0;
                
                /**
                 * Gets the record of Wins and Losses from the page
                 */
                Elements recordElements = dataPage.getElementsByClass("record");
                if (recordElements.size() == 1) {
                    Element record = recordElements.get(0);
                    String recordText = record.text();
                    int colonLocation = recordText.indexOf(":");
                    if (colonLocation > 0) {
                        recordText = recordText.substring(colonLocation+1);
                        int dashLocation = recordText.indexOf("-");
                        String winsText = recordText.substring(0, dashLocation).trim();
                        String lossesText = recordText.substring(dashLocation+1).trim();
                        recordWins = cleanScore(winsText);
                        recordLosses = cleanScore(lossesText);
                    }
                }

                /**
                 * Get the data for each win and loss and calculate total wins and losses
                 */
                Elements scheduleElements = dataPage.getElementsByClass("weekly-schedule");
                if (scheduleElements.size() > 0) {
                    Element schedule = scheduleElements.get(0);
                    Elements games = schedule.getElementsByTag("tr");
                    for (Element game : games) {
                        Elements data = game.getElementsByTag("td");
                        if (!data.isEmpty()) {
                            Elements linkElements = data.get(1).getElementsByTag("a");
                            Element link;
                            if (linkElements.size() > 0) {
                                link = linkElements.get(0);
                                String href = link.attr("href");
                                int startLocation = href.indexOf("/", 20)+1;
                                int endLocation = href.indexOf("/", startLocation);
                                schoolPath = href.substring(startLocation, endLocation);
                            } else {
                                schoolPath=""; 
                            }
                            String text = data.get(2).text();
                            char winOrLoss = text.trim().charAt(1);
                            int dashLocation = text.indexOf("-");

                            if ((winOrLoss == 'W' || winOrLoss == 'L') && dashLocation != -1) {
                                int score;
                                int opponentScore;
                                int firstScore = cleanScore(text.substring(3, dashLocation).trim());
                                String secondHalf = text.substring(dashLocation+1).trim();
                                int spaceLocation = secondHalf.indexOf(" ");
                                if (spaceLocation != -1) {
                                        secondHalf = secondHalf.substring(0, spaceLocation).trim();
                                }

                                int secondScore = cleanScore(secondHalf);

                                if (winOrLoss == 'W') {
                                    winCount++;
                                    score = firstScore;
                                    opponentScore = secondScore;
                                } else {
                                    lossCount++;
                                    score = secondScore;
                                    opponentScore = firstScore;
                                }
                                pointsDifference += score;
                                pointsDifference -= opponentScore;
                                gameCount++;
                                if (schoolPath != null && !schoolPath.isEmpty()) {
                                    school.addOpponent(schoolPath);
                                }
                            }
                        }
                    }

                    school.setWins(winCount);
                    school.setLosses(lossCount);

                    if (recordWins != winCount || recordLosses != lossCount) {
                        school.setWinLossRecordIncorrect(true);
                    }
                    averagePointDifference = gameCount == 0 ? 0 : pointsDifference / (float)gameCount;
                    school.setAvgPointDifference(averagePointDifference);
                }
            } catch (SocketTimeoutException | SSLHandshakeException | ConnectException e) {
                isError = true;
                errorCount++;
            } catch (Exception ex) {
                isError = true;
                errorCount++;
                LOG.error("Exception with school: " + school.getSchoolName(), ex);
            }
        } while (isError && errorCount < 3);
    }

    /**
     * Takes a string and returns the integer in the string removing known invalid characters
     * @param scoreString   The record listed on the website that needs cleaning
     * @return the integer value in the string passed to the method
     */
    private int cleanScore (String scoreString) {
        String cleanString = scoreString;
        int findRightParentheses = cleanString.indexOf(")");
        if (findRightParentheses > 0) {
            cleanString = cleanString.substring(findRightParentheses + 1);
        }
        int findAdditionalHyphen = cleanString.indexOf("-");
        if (findAdditionalHyphen > 0) {
            cleanString = cleanString.substring(findAdditionalHyphen + 1);
        }
        return Integer.parseInt(cleanString.trim());
    }
}