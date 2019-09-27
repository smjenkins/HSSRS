package edu.umuc;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import static edu.umuc.controllers.Controller.HOME_PAGE_FXML;
import javafx.scene.image.Image;

/**
 * The main class for the High School Sports Ranking System application
 */
public class SportsRankingApp extends Application {
    /**
     * Static variables associated with the SCENE
     */
    private static final int DEFAULT_SCENE_WIDTH = 1100;
    private static final int DEFAULT_SCENE_HEIGHT = 600;

    /**
     * The primary stage refers to the active window. In our design, we always want
     * to close the previous windows and open a new one. If this design were to
     * change, the developer would need to create a new stage.
     */
    private static Stage primaryStage;

    /**
     * Sets the primary stage
     * @param stage     The stage to be saved in the class
     */
    private void setPrimaryStage(Stage stage) {
        SportsRankingApp.primaryStage = stage;
    }

    /**
     * Gets the primary stage
     * @return The primary stage
     */
    static public Stage getPrimaryStage() {
        return SportsRankingApp.primaryStage;
    }

    /**
     * Starts the application and loads the main start page
     * @param primaryStage  The stage to load the main page to
     * @throws Exception 
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        setPrimaryStage(primaryStage);

        final FXMLLoader fxmlLoader = new FXMLLoader();
        final Parent homePage = fxmlLoader.load(getClass().getResourceAsStream(HOME_PAGE_FXML));
        primaryStage.setScene(new Scene(homePage, DEFAULT_SCENE_WIDTH, DEFAULT_SCENE_HEIGHT));
                primaryStage.setTitle("High School Sports Ranking System (HSSRS) Version 2.0");
        Image icon = new Image("/icons/wpost-icon.png");
        primaryStage.getIcons().add(icon);
        primaryStage.show();
    }

    /**
     * Main class of the application
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }
}
