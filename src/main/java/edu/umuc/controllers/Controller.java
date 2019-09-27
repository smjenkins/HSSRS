package edu.umuc.controllers;

import edu.umuc.SportsRankingApp;
import edu.umuc.models.GeneralProperties;
import edu.umuc.models.League;
import edu.umuc.models.RankWeight;
import edu.umuc.models.School;
import edu.umuc.models.Sport;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.*;
import java.util.logging.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.stream.Collectors;

/**
 * This class is the controller for all pages without a specific controller
 */
public class Controller {
    /**
     * Location of the school ranking fxml page
     */
    public static final String SCHOOL_RANKING_FXML = "/SchoolRanking.fxml";

    /**
     * Location of the leagues fxml page
     */
    public static final String LEAGUES_FXML = "/Leagues.fxml";
    
    /**
     * Location of the home fxml page
     */
    public static final String HOME_PAGE_FXML = "/HomePage.fxml";
    
     /**
     * Location of the about fxml page
     */
    public static final String ABOUT_PAGE_FXML = "/About.fxml";

    /**
     * Location of the rank calculation fxml page
     */
    public static final String RANK_CALC_PAGE_FXML = "/RankCalculation.fxml";

    /**
     * Location of the manage weights fxml page
     */
    private static final String MANAGE_WEIGHTS_FXML = "/ManageWeights.fxml";

    /**
     * Location of the config properties file
     */
    private static final String CONFIG_PROPERTIES = "/config.properties";

    /**
     * Initialize logger
     */
    private static final Logger LOG = LoggerFactory.getLogger(Controller.class);

    /**
     * Default league weight
     */
    private final static Float DEFAULT_LEAGUE_WEIGHT = 1F;

    /**
     * Default league name
     */
    private final static String DEFAULT_LEAGUE_NAME = "NONE";

    /**
     * General properties filename
     */
    private static final String GENERAL_PROPERTIES_YAML = "generalProperties.yaml";

    /**
     * leagues yaml filename
     */
    private final static String LEAGUES_YAML = "leagues.yaml";

    /**
     * schools yaml filename
     */
    private final static String SCHOOLS_YAML = "schools.yaml";
    
    /**
     * sports yaml filename
     */
    private final static String SPORTS_YAML = "sports.yaml";

    /**
     * List of sport objects to be used by all controller objects
     */
    private List<Sport> sports = new ArrayList<>();

    /**
     * List of league objects to be used by all controller objects
     */
    private static final List<League> leagues = new ArrayList<>();

    /**
     * List of school objects to be used by all controller objects
     */
    public static final List<School> schools = new ArrayList<>();

    /**
     * RankWeight object to be used by all controller objects
     */
    private static RankWeight rankWeight;

    /**
     * Stores the school that was selected on the School Ranking page for use by RankCalculationController
     */
    private static School selectedSchool;

    /**
     * Stores the league of the school that was selected on the School Ranking page for use by RankCalculationController
     */
    private static League selectedLeague;

    /**
     * Stores the sport selected on the School Ranking page to reload when returning to the school ranking page
     */
    private static Sport sportSelected;
    
    /**
     * Stores the year that was selected in the School Ranking page to reload when returning to the school ranking page
     */
    private static String yearSelected;
    
    private static League leagueSelected;
    
    
    /**
     * Stores the general properties
     */
    private static GeneralProperties generalProperties;
    
    /**
     * Marked true when the schools have already been ranked
     */
    private static boolean schoolsRanked = false;

    public static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat( "0.00" );
    
    /**
     * Stores the path for the location of the config and yaml files
     */
    protected String configPath;

    /**
     * Constructor 
     * This application depends on successfully reading the contents of the YAML files. 
     * If the system is not able to read these YAML files for any reason, the
     * application will exit.
     */
    public Controller() {
        try {
            final Properties properties = new Properties();
            properties.load(this.getClass().getResourceAsStream(CONFIG_PROPERTIES));
            configPath = properties.getProperty("configpath");
            if (configPath == null) {
                LOG.error("No config path found in the properties file.");
                System.exit(-1);
            }
        } catch (IOException ex) {
            LOG.error("Unable to read read the config.properties file", ex);
            System.exit(-1);
        }

        if (generalProperties == null) {
            loadGeneralPropertiesData();
            if (generalProperties == null) {
                LOG.error("General Properties not loaded");
                System.exit(-1);
            }
        }
    
        if (leagues.isEmpty()) {
            loadLeaguesData();
            if (leagues.isEmpty()) {
                LOG.error("Leagues not loaded");
                System.exit(-1);
            }
        }
        
        if (schools.isEmpty()) {
            loadSchoolsData();
            if (schools.isEmpty()) {
                LOG.error("Schools not loaded");
                System.exit(-1);
            }
        }
        
        if (sports.isEmpty()) {
            loadSportsData();
            if (sports.isEmpty()) {
                LOG.error("Sports not loaded");
                System.exit(-1);
            }
        }
    }

    @FXML
    private Button btnSchoolsRanking;

    @FXML
    private Button btnLeagues;

    @FXML
    private Button btnHome;

    @FXML
    private Button btnAbout;
    
    @FXML
    private Button rankSchools;

    @FXML
    private Button rankCalc;
    
    @FXML
    private Button btnManageWeights;
    
    @FXML
    private Button btnResetWeights;
    
    @FXML
    private Button btnSaveWeights;
    
    @FXML
    private Button btnExportCSV;

    /**
     * Handles all button clicks not handled by other controllers
     * @param event 
     */
    @FXML
    public void processButtonClickEvents(ActionEvent event){
        if (event.getSource() == btnSchoolsRanking) {
            loadPage(SCHOOL_RANKING_FXML);
        } else if (event.getSource() == btnLeagues){
            loadPage(LEAGUES_FXML);
        } else if (event.getSource() == btnHome){
            loadPage(HOME_PAGE_FXML);
        } else if (event.getSource() == btnAbout){
            loadPage(ABOUT_PAGE_FXML);               
        } else if (event.getSource() == btnManageWeights){
            loadPage(MANAGE_WEIGHTS_FXML);            
        }
        else if (event.getSource() == btnExportCSV){
            try {
                SchoolRankingController.exportToCSV();
            } catch (Exception ex) {
                java.util.logging.Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Common method to load the corresponding FXML page
     * @param fxmlUrl   The location of the fxml page to load
     */
    protected void loadPage(String fxmlUrl) {
        try {
            final FXMLLoader fxmlLoader = new FXMLLoader();
            final Parent schoolRankingPage = fxmlLoader.load(getClass().getResourceAsStream(fxmlUrl));
            final Stage stage = SportsRankingApp.getPrimaryStage();
            stage.getScene().setRoot(schoolRankingPage);
        } catch (IOException ex) {
            LOG.error("Exception in loadPage", ex);
        }
    }

    /**
     * Added method for re-usability purposes in child classes
     * @param yamlName  The file location of the RankWeights yaml to open
     * @return  The RankWeight object created from the yaml file
     */
    protected RankWeight loadRankWeight(String yamlName) {
        RankWeight returnRankWeight = new RankWeight();

        /**
         * Loads the saved weights from the corresponding YAML file
         */
        final Yaml yaml = new Yaml(new Constructor(RankWeight.class));
        final File file = new File(configPath, yamlName);
        try {
            returnRankWeight = yaml.load(new FileInputStream(file));
            rankWeight = returnRankWeight;
        } catch (FileNotFoundException ex) {
            LOG.error("Exception in loadRankWeight", ex);
        }
        return returnRankWeight;
    }

    /**
     * Re-Rank schools with the current weights
     */
    protected void reRankSchools() {
        if (isSchoolsRanked()) {
            ScrapeData.calculateTotalPoints(getRankWeight(), getSchools());
            ScrapeData.sortSchools(getSchools());
        }
    }

    /**
     * Loads the general properties file
     */
    private void loadGeneralPropertiesData() {
        final File generalPropertiesFile = new File(configPath, GENERAL_PROPERTIES_YAML);
        try (InputStream inputStream = new FileInputStream(generalPropertiesFile)) {
            Yaml yaml = new Yaml();
            generalProperties = yaml.loadAs(inputStream, GeneralProperties.class);
        } catch (Exception ex) {
            LOG.error("Exception loading general properties data YAML file.", ex);
        }
    }
        
    public static GeneralProperties getGeneralProperties() {
        return generalProperties;
    }
   
    /**
     * Loads the league data from the league yaml file
     */
    private void loadLeaguesData(){
        File file = new File(configPath, LEAGUES_YAML);
        try (InputStream inputStream = new FileInputStream(file)) {
            final Yaml yaml = new Yaml();
            Iterable<Object> iterableLeagues = yaml.loadAll(inputStream);
            
            iterableLeagues.forEach(iterableLeague -> {
                final Map<String, Object> map = ((HashMap<String, Object>) iterableLeague);
                final League league = new League((String)map.get("leagueId"), (String)map.get("name"), ((Double)map.get("weight")).floatValue());
                final List<HashMap<String, String>> leagueSchools = (List)(map.get("schools"));

                league.setSchools(leagueSchools.stream().map(school -> (school).get("name")).collect(Collectors.toList()));
                leagues.add(league);
            });

        } catch (Exception ex) {
            LOG.error("Exception loading Leagues Yaml file.", ex);
            System.exit(-1);
        }
    }
    
    /**
     * Loads the school data from the school yaml file
     */
    private void loadSchoolsData(){
        File file = new File(configPath, SCHOOLS_YAML);
        try (InputStream inputStream = new FileInputStream(file)) {
            final Yaml yaml = new Yaml();
            Iterable<Object> iterableSchools = yaml.loadAll(inputStream);

            iterableSchools.forEach(iterableSchool -> {
                final Map<String, String> map = ((HashMap<String, String>) iterableSchool);
                final School school = new School(map.get("name"), map.get("path"));
                schools.add(school);
            });

        } catch (Exception ex) {
            LOG.error("Exception loading Schools Yaml file.", ex);
            System.exit(-1);
        }
    }
    
    /**
     * Loads the sports data from the sports yaml file
     */
    private void loadSportsData() {
        File file = new File(configPath, SPORTS_YAML);
        try (InputStream inputStream = new FileInputStream(file)) {
            final Yaml yaml = new Yaml();
            Iterable<Object> itrSports = yaml.loadAll(inputStream);
        
            itrSports.forEach(itr -> {
                final Map<String, String> map = ((HashMap<String, String>) itr);
                final Sport sport = new Sport(map.get("name"), map.get("season"), map.get("path"));
                sports.add(sport);
            });
            Collections.sort(sports);
        } catch (Exception ex) {
            LOG.error("Exception loading Sports Yaml file.", ex);
            System.exit(-1);
        }
    }
    
    public static List<League> getLeagues() {
        return leagues;
    }

    public static List<School> getSchools() {
        return schools;
    }
    
    public List<Sport> getSports() {
        return sports;
    }

    /**
     * Gets the league weight for a specific school
     * @param schoolName    The school to get a league weight for
     * @return              The league weight for the specific school
     */
    public static Float getLeagueWeightForSchool (String schoolName){
        return leagues.stream()
                .filter(league -> league.containsSchool(schoolName))
                .map(League::getWeight)
                .findAny()
                .orElse(DEFAULT_LEAGUE_WEIGHT);
    }

    /**
     * Gets the league name for a specific school
     * @param schoolName    The school to find the league name for
     * @return              The league name for the specific school
     */
    public static String getLeagueNameForSchool (String schoolName){
        return leagues.stream()
                .filter(league -> league.containsSchool(schoolName))
                .map(League::getName)
                .findAny()
                .orElse(DEFAULT_LEAGUE_NAME);
    }

    public static RankWeight getRankWeight() {
        return rankWeight;
    }

    public static void setRankWeight(RankWeight rankWeight) {
        Controller.rankWeight = rankWeight;
    }

    /**
     * Gets the League object for a specific school
     * @param schoolName    The school to get the league object for
     * @return              The league for the specific school
     */
    public static League getLeagueForSchool (String schoolName){
        for (League singleLeague : leagues) {
            if (singleLeague.getSchools().contains(schoolName)) {
                return singleLeague;
            }
        }
        return null;
    }

    /**
     * Resets the ranking data for all schools prior to another ranking
     */
    public static void initializeSchools() {
        for (School school : schools) {
            school.initialize();
        }
    }

    public static School getSelectedSchool() {
        return selectedSchool;
    }

    public static void setSelectedSchool(School selectedSchool) {
        Controller.selectedSchool = selectedSchool;
    }

    public static League getSelectedLeague() {
        return selectedLeague;
    }

    public static void setSelectedLeague(League selectedLeague) {
        Controller.selectedLeague = selectedLeague;
    }

    public static boolean isSchoolsRanked() {
        return schoolsRanked;
    }

    public static void setSchoolsRanked(boolean schoolsRanked) {
        Controller.schoolsRanked = schoolsRanked;
    }

    public static Sport getSportSelected() {
        return sportSelected;
    }

    public static void setSportSelected(Sport sportSelected) {
        Controller.sportSelected = sportSelected;
    }

    public static String getYearSelected() {
        return yearSelected;
    }

    public static void setYearSelected(String yearSelected) {
        Controller.yearSelected = yearSelected;
    }
    public static void setLeagueSelected(League leagueSelected){
        Controller.leagueSelected = leagueSelected;
    }
    public static League getLeagueSelected(){
     return leagueSelected;   
    }
}
