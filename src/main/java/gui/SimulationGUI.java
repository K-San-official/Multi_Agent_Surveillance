package gui;

import controller.GameController;
import gui.sceneLayouts.MainLayout;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Parent class of all GUI elements.
 * Central unit to control the graphics.
 */
public class SimulationGUI extends Application {

    // Variables
    int currentStep;
    MainLayout mainLayout;
    Scene mainScene;
    int simulationDelay;
    Timeline timeline;
    GameController controller;
    final int FPS = 25;
    final int WIDTH = 1200;
    final int HEIGHT = 800;
    public static final int CANVAS_OFFSET = 50; // Pushes the map a bit in the middle of the canvas (x and y).
    public static final double SCALING_FACTOR = 10;

    public void setController(GameController controller){
        this.controller = controller;
    }

    public void launchGUI() {
        String[] args = new String[0];
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        currentStep = 0;
        simulationDelay = 1;
        mainLayout = new MainLayout();
        mainLayout.setSimulationInstance(this);
        mainScene = new Scene(mainLayout, 1300, 1000);
        this.setController(new GameController(this));
        this.controller.drawFixedItems(mainLayout);
        // Timeline Animation
        this.timeline = new Timeline(new KeyFrame(Duration.millis(1000/FPS), actionEvent -> update()));
        this.timeline.setCycleCount(Timeline.INDEFINITE);
        // Display Window
        primaryStage.setTitle("Multi-Agent Simulation");
        primaryStage.setScene(mainScene);
        primaryStage.show();
    }

    public void update() {
        controller.update();
        updateGUI1step();
    }

    /**
     * Starts the simulation.
     */
    public void startSimulation() {
        this.timeline.play();
    }

    /**
     * Stops the simulation.
     */
    public void stopSimulation() {
        this.timeline.stop();
    }

    /**
     * Pauses the simulation
     */
    public void pauseSimulation() {
        this.timeline.pause();
    }

    /**
     * Updates the GUI one simulation step.
     */
    public void updateGUI1step() {
        this.controller.update();
        mainLayout.getStepCountLabel().setText("Current Step: " + currentStep);
        this.controller.drawMovingItems(mainLayout);
        currentStep++;
    }

    public Scene getMainScene() {
        return mainScene;
    }

    public MainLayout getMainLayout() {
        return mainLayout;
    }

    public void setMainScene(Scene mainScene) {
        this.mainScene = mainScene;
    }

    public int getCurrentStep() {
        return currentStep;
    }

    public void setCurrentStep(int currentStep) {
        this.currentStep = currentStep;
    }

}
