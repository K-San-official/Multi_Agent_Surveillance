package Enities;

import agents.AbstractAgent;
import agents.RandomAgent;
import agents.RemoteAgent;
import model.*;

import java.util.ArrayList;
import java.util.Random;

/**
 * Abstract class of an entity on the map.
 */
public abstract class Entity extends MapItem {
    double fovAngle = 30;
    double fovDepth = 20;
    protected Vector2D direction;
    boolean isIntruder;
    double sprintMovementFactor;//number by which the movement speed needs to be increased when sprinting
    double sprintRotationFactor;//number by which the rotation speed needs to be decreased when sprinting
    boolean isSprinting = true;
    ArrayList<Ray> fov;
    double turnSpeed;//rotation in degrees/sec
    public double radius = 1;//width of the entity
    int ID;
    HitBox hitBox;
    protected double velocity;
    protected AbstractAgent agent;
    protected Vector2D prevPos;

    public Entity(double x, double y, GameMap currentMap) {
        setMap(currentMap);
        this.setPosition(new Vector2D(x,y));
        this.direction = Vector2D.randomVector();
        velocity = 0;
        Vector2D c1 = Vector2D.add(getPosition(), new Vector2D(-radius,-radius));
        Vector2D c2 = Vector2D.add(getPosition(), new Vector2D(radius,-radius));
        Vector2D c3 = Vector2D.add(getPosition(), new Vector2D(-radius,radius));
        Vector2D c4 = Vector2D.add(getPosition(), new Vector2D(radius,radius));
        hitBox = new HitBox(c1,c2,c3,c4);
    }

    public void setMap(GameMap map){
        this.map = map;
    }

    /**
     * Gives the Entity a new position, based on the agent.
     */
    public void update(ArrayList<MapItem> items) {
        if (this.agent != null) {
            agent.changeMovement(items);
        }
        // Check collision detection
        boolean inSpecialArea = false;
        for(MapItem item : items) {
            if (((Area) item).isAgentInsideArea(this)){
                Area areaItem = (Area) item;
                areaItem.onAgentCollision(this);
                inSpecialArea = true;
            }
        }
        if(!inSpecialArea){
            resetEntityParam();
        }
        // Update HitBox
        this.getHitBox().transform(this);
    }

    /**
     * Sets the agent of a guard.
     * 0 = random agent
     * 1 = remote agent
     * @param type
     */
    public void setAgent(int type) {
        switch (type) {
            case 0: {
                agent = new RandomAgent();
                agent.setEntityInstance(this);
                break;
            }
            case 1: {
                agent = new RemoteAgent();
                agent.setEntityInstance(this);
                agent.addControls();
                break;
            }
            default: {
                System.out.println("No agent defined!!!");
            }
        }
    }

    public double getRadius() { return radius; }

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

    public double getFovAngle() {
        return fovAngle;
    }

    public void setFovAngle(double fovAngle) {
        this.fovAngle = fovAngle;
    }

    public double getFovDepth() {
        return fovDepth;
    }

    public void setFovDepth(double fovDepth) {
        this.fovDepth = fovDepth;
    }

    public void resetEntityParam(){
        this.setVelocity(0.1);
        this.setFovAngle(30);
        this.setFovDepth(20);
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

    public abstract boolean isIntruder();

    /**
     * Creates a field of view for an entity.
     * @return
     */
    public ArrayList<Ray> FOV() {
        ArrayList<Ray> rays = new ArrayList<>();
        // Create all the rays
        for (double i = -0.5 * fovAngle; i <= 0.5 * fovAngle; i+= 1){
            Vector2D direction = new Vector2D(
                    getDirection().getX()*Math.cos(Math.toRadians(i)) - getDirection().getY()*Math.sin(Math.toRadians(i)),
                    getDirection().getX()*Math.sin(Math.toRadians(i)) + getDirection().getY()*Math.cos(Math.toRadians(i))
            );
            Ray ray = new Ray(getPosition(), Vector2D.resize(direction, fovDepth));
            GameMap map = this.map;
            double minDistance = fovDepth;
            // Scan all fixed items on the map
            for (MapItem item: map.getSolidBodies()) {
                Area area = (Area) item;
                // Find the closest object to avoid "seeing through walls"
                for (int j = 0; j < area.getCornerPoints().length; j++){
                    double currentDistance = Vector2D.distance(ray.getOrigin(), ray.getPoint(), area.getCornerPoints()[j], area.getCornerPoints()[(j + 1) % 4]);
                    if (currentDistance < minDistance && currentDistance > 0) {
                        minDistance = currentDistance;
                    }
                }
            }
            // Set the length of the ray accordingly.
            ray.setDirection(Vector2D.resize(ray.getDirection(), minDistance));
            rays.add(ray);
        }
        return rays;
    }

    @Override
    public boolean isSolidBody() {
        return false;
    }

    @Override
    public boolean isDynamicObject() {
        return true;
    }

    @Override
    public boolean isTransparentObject() {
        return false;
    }

    public HitBox getHitBox() {
        return hitBox;
    }

    public void setHitBox(HitBox hitBox) {
        this.hitBox = hitBox;
    }

    public Vector2D getPrevPos() {
        return prevPos;
    }

    public void setPrevPos(Vector2D pos) {
        prevPos = pos;
    }
}
