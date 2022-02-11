package model;

import Enities.Entity;
import Enities.Guard;
import Enities.Intruder;
import controller.GameController;
import java.util.ArrayList;

public class GameMap {

    private GameController gameController;
    private int sizeX; //height
    private int sizeY; //width
    private ArrayList<MapItem> fixedItems = new ArrayList<>();
    private ArrayList<MapItem> movingItems = new ArrayList<>();

/* private double scaling;
     private double timeStep;
     private int gameMode;
     int[] teleport;
     List<int[]> shadedAreas;
     int[] texture; */

    public GameMap(GameController controller) {
        this.gameController = controller;
    }

    public GameMap(int sizeX, int sizeY) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
    }

    public GameMap(int sizeX, int sizeY, ArrayList<MapItem> fixedItems, ArrayList<MapItem> movingItems) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.fixedItems = fixedItems;
        this.movingItems = movingItems;
    }

    // TODO: move this
    // initializes map for testing purposes
    public void initTestGameMap() {
        sizeX = 120;
        sizeY = 80;
        createBorderWalls();
        fixedItems.add(new SpawnArea(2, 2, 20, 10));
        fixedItems.add(new Wall(50, 60, 55, 63));
        fixedItems.add(new Wall(70, 70, 75, 80));
        fixedItems.add(new Wall(60, 10, 75, 50));
//        movingItems.add(new Intruder(20,20));
        Guard remoteGuard = new Guard(40, 40);
        remoteGuard.map = this;
        remoteGuard.setRemote();
        movingItems.add(remoteGuard);
    }

    public void addToFixedItems(MapItem item) {
        fixedItems.add(item);
    }

    public void addToMovingItems(Entity entity) {
        movingItems.add(entity);
    }

    public void createBorderWalls() {
        // Corner positions
        Vector2D c1, c2, c3, c4;
        c1 = new Vector2D(0, 0);
        c2 = new Vector2D(sizeX, 0);
        c3 = new Vector2D(sizeX, sizeY);
        c4 = new Vector2D(0, sizeY);
        fixedItems.add(new Wall(new Vector2D(c1.getX(), c1.getY() - 2), new Vector2D(c2.getX(), c2.getY() - 2), c2, c1)); // Top wall
        fixedItems.add(new Wall(c2, new Vector2D(c2.getX() + 2, c2.getY()), new Vector2D(c3.getX() + 2, c3.getY()), c3)); // Right wall
        fixedItems.add(new Wall(c4, c3, new Vector2D(c3.getX(), c3.getY() + 2), new Vector2D(c4.getX(), c4.getY() + 2))); // Bottom wall
        fixedItems.add(new Wall(new Vector2D(c1.getX() - 2, c1.getY()), c1, c4, new Vector2D(c4.getX() - 2, c4.getY()))); // Left wall
    }

    public int getSizeX() {
        return sizeX;
    }

    public void setSizeX(int sizeX) {
        this.sizeX = sizeX;
    }

    public int getSizeY() {
        return sizeY;
    }

    public void setSizeY(int sizeY) {
        this.sizeY = sizeY;
    }

    public ArrayList<MapItem> getFixedItems() {
        return fixedItems;
    }

    public void setFixedItems(ArrayList<MapItem> fixedItems) {
        this.fixedItems = fixedItems;
    }

    public ArrayList<MapItem> getMovingItems() {
        return movingItems;
    }

    public void setMovingItems(ArrayList<MapItem> movingItems) {
        this.movingItems = movingItems;
    }

    public GameController getGameController() {
        return gameController;
    }

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }
}
