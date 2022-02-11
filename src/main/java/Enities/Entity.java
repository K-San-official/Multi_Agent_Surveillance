package Enities;

import model.*;

import java.util.ArrayList;
import java.util.Random;

/**
 * Abstract class of an entity on the map.
 */
public abstract class Entity extends MapItem {
    double explorationFactor;
    double fovAngle = 80;
    double fovDepth = 60;
    //Vector2D fovDirection;
    Vector2D direction;
    boolean isIntruder;
    double sprintMovementFactor;//number by which the movement speed needs to be increased when sprinting
    double sprintRotationFactor;//number by which the rotation speed needs to be decreased when sprinting
    boolean isSprinting = true;
    ArrayList<Ray> fov;
    double turnSpeed;//rotation in degrees/sec
    double radius;//width of the entity

    protected double velocity;

    public Entity(double x, double y) {
        this.setPosition(new Vector2D(x,y));
        this.direction = Vector2D.randomVector();
        velocity = 0;
    }

    public void setMap(GameMap map){
        this.map = map;
    }

    /**
     * Gives the Entity a new position, based on the direction the Entity is looking at and the
     * current velocity.
     */
    public void update() {
        this.setPosition(Vector2D.add(getPosition(), Vector2D.scalar(direction, velocity)));
    }

    public Vector2D getDirection() {
        return direction;
    }

    public void setDirection(Vector2D direction) {
        this.direction = direction;
    }

    public double getVelocity() {
        return velocity;
    }

    public void setVelocity(double velocity) {
        this.velocity = velocity;
    }

    public Entity(SpawnArea spawnArea) {
        Random rand = new Random();

        //TODO: Place entity in spawn area. Since it is no perfect rectangle, we need to choose a different
        // algorithm.
        /*
        double x = rand.nextDouble()*spawnArea.getWidth() + spawnArea.getPosition().getX();
        double y = rand.nextDouble()*spawnArea.getWidth() + spawnArea.getPosition().getX();
         */
    }

    public ArrayList<Ray> FOV(){
        ArrayList<Ray> rays = new ArrayList<>();
        for (double i = -0.5*fovAngle; i <= 0.5*fovAngle; i+=1){
            Vector2D direction = new Vector2D(
                    getDirection().getX()*Math.cos(Math.toRadians(i))- getDirection().getY()*Math.sin(Math.toRadians(i)),
                    getDirection().getX()*Math.sin(Math.toRadians(i)) + getDirection().getY()*Math.cos(Math.toRadians(i))
            );
            Ray ray = new Ray(getPosition(), Vector2D.resize(direction, fovDepth));
            GameMap map = this.map;
            for (MapItem item: map.getFixedItems()){
                Area area = (Area) item;
                for (int j = 0; j < area.getCornerPoints().length - 1;j++){
                    ray.cast(area.getCornerPoints()[j],area.getCornerPoints()[j+ 1]);
                }
                ray.cast(area.getCornerPoints()[0],area.getCornerPoints()[3]);
            }
            rays.add(ray);

        }
        return rays;
    }
}
