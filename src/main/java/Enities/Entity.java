package Enities;

import model.MapItem;
import model.SpawnArea;
import model.Vector2D;

import java.util.ArrayList;
import java.util.Random;

/**
 * Abstract class of an entity on the map.
 */
public abstract class Entity extends MapItem {
    double explorationFactor;
    double fovAngle = 40;
    double fovDepth = 30;
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
        this.direction = new Vector2D(300, 300);
        velocity = 0;
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
        for (double i = -0.5*fovAngle; i <= 0.5*fovAngle; i+=0.4){
            System.out.println(i);
            Vector2D direction = new Vector2D(
                    getDirection().getX()*Math.cos(Math.toRadians(i))- getDirection().getY()*Math.sin(Math.toRadians(i)),
                    getDirection().getX()*Math.sin(Math.toRadians(i)) + getDirection().getY()*Math.cos(Math.toRadians(i))
            );
            rays.add(new Ray(getPosition(), direction));
        }
        return rays;
    }
}
