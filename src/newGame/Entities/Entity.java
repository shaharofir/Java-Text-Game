package newGame.Entities;

import newGame.IntPoint;
import newGame.MainGame;
import newGame.Mapping.Tile;

import java.util.List;
import java.util.ArrayList;

public abstract class Entity extends Representable {

    private int maxLevel = 30;

    private int floor;
    private int minX;
    private int maxX;
    private int x;
    private int minY;
    private int maxY;
    private int y;
    private int maxHealth;
    private int initialHealth;
    private int health;
    private int level;

    public Entity() {
        setName("Entity");
        setRepresentation('E');
        level = 1;
        health = 25;
        initialHealth = 25;
        maxHealth = 30;
        x = 0;
        y = 0;
    }

    public boolean inSight(Entity entity) {
        IntPoint p1 = entity.getPosition();
        IntPoint p2 = getPosition();

        // TODO: Change "MapBuffer" to "EntityBuffer" in here (This is broken for now)

        if(p1.getY() == p2.getY()) {
            int commonX = p1.getX();
            int maxY = Math.max(p1.getY(), p2.getY());
            int minY = Math.min(p1.getY(), p2.getY());
            for(int y = minY + 1; y < maxY; y++)
                if(MainGame.map.getTile(commonX, y).similar(Tile.WALL)/*MainGame.map.getCharacter(commonX, y) == 'X'*/)
                    return false;

            return true;
        }
        else if(p1.getX() == p2.getX()) {
            int commonY = p1.getY();
            int maxX = Math.max(p1.getX(), p2.getX());
            int minX = Math.min(p1.getX(), p2.getX());
            for(int x = minX + 1; x < maxX; x++)
                if(MainGame.map.getTile(x, commonY).similar(Tile.WALL)/*MainGame.map.getCharacter(x, commonY) == 'X'*/)
                    return false;

            return true;
        }
        else {
            double slope = (p1.getY() - p2.getY()) / (p1.getX() - p2.getX());
            double intercept = p1.getY() - (p1.getX() * slope);

            int maxX = Math.max(p1.getX(), p2.getX());
            int minX = Math.min(p1.getX(), p2.getX());
            for(int x = minX; x < maxX; x++)
                if(MainGame.map.getTile(x, (int) Math.ceil(x * slope + intercept)).similar(Tile.WALL)/*MainGame.map.getCharacter(x, (int) Math.ceil(x * slope + intercept)) == 'X'*/)
                    return false;

            return true;
        }
    }

    public void spawn(Tile onWhatTile) {
        final IntPoint rpos = MainGame.map.getMapBuffer().randomLoc(onWhatTile);
        setPosition(rpos.getX(), rpos.getY());

        if(!MainGame.map.getEntities().contains(this))
            MainGame.map.getEntities().add(this);
    }

    public List<Entity> withinProxy(double range) {
        List<Entity> withinRange = new ArrayList<>();
        for(Entity e : MainGame.map.getEntities())
            if(distance(e) <= range)
                withinRange.add(e);

        return withinRange;
    }

    public void adaptToMap() {
        setMinXY(MainGame.map.getMinX(), MainGame.map.getMinY());
        setMaxXY(MainGame.map.getMaxX(), MainGame.map.getMaxY());
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int ifloor) {
        floor = ifloor;
    }

    public double distance(double x, double y) {
        return Math.sqrt( Math.pow(x - this.x, 2) + Math.pow(y - this.y, 2) );
    }

    public double distance(IntPoint p) {
        return distance(p.getX(), p.getY());
    }

    public double distance(Entity e) {
        return distance(e.getX(), e.getY());
    }

    public boolean intersects(double x, double y) {
        return getX() == x && getY() == y;
    }

    public boolean intersects(IntPoint p) {
        return intersects(p.getX(), p.getY());
    }

    public boolean intersects(Entity e) {
        return intersects(e.getPosition());
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public void setMaxLevel(int maxLevel) {
        this.maxLevel = maxLevel;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setPosition(IntPoint p) {
        x = p.getX();
        y = p.getY();
    }

    public void setBounds(int minX, int minY, int maxX, int maxY) {
        setMaxXY(maxX, maxY);
        setMinXY(minX, minY);
    }

    public void setMinXY(int x, int y) {
        this.minX = x;
        this.minY = y;
    }

    public void setMaxXY(int x, int y) {
        this.maxX = x;
        this.maxY = y;
    }

    public int getMinX() {
        return minX;
    }

    public void setMinX(int minX) {
        this.minX = minX;
    }

    public int getMaxX() {
        return maxX;
    }

    public void setMaxX(int maxX) {
        this.maxX = maxX;
    }

    public int getMinY() {
        return minY;
    }

    public void setMinY(int minY) {
        this.minY = minY;
    }

    public int getMaxY() {
        return maxY;
    }

    public void setMaxY(int maxY) {
        this.maxY = maxY;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public IntPoint getPosition() {
        return new IntPoint(getX(), getY());
    }

    public void move(int deltaX, int deltaY) {
    	IntPoint p = previewMove(deltaX, deltaY);
    	final int x = p.getX();
    	final int y = p.getY();
    	if(!MainGame.map.isPassableTile(x, y)) {
            return;
        }

        setPosition(x, y);
    }

    public IntPoint previewMove(int deltaX, int deltaY) {
        int newX = getX() + deltaX;
        int newY = getY() + deltaY;
        /*
        if(newX < getMinX())
            newX = getMinX();
        else if(newX > getMaxX())
            newX = getMaxX();

        if(newY < getMinY())
            newY = getMinY();
        else if(newY > getMaxY())
            newY = getMaxY();
        */
        return new IntPoint(newX, newY);
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getHealth() {
        return this.health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getInitialHealth() {
        return this.initialHealth;
    }

    public void setInitialHealth(int initialHealth) {
        this.initialHealth = initialHealth;
    }

    public int getMaxHealth() {
        return this.maxHealth;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
        if(getHealth() > maxHealth)
            setHealth(maxHealth);
    }

    public void heal(int amount) {
        this.health = amount + this.health > this.maxHealth ? this.maxHealth : this.health + amount;
    }

    public void damage(int amount) {
        this.health = this.health - amount < 0 ? 0 : this.health - amount;
    }

    public boolean isDead() {
        return this.health <= 0;
    }

    public void removeAndClean() {
        if(this instanceof Character) {
            MainGame.requestEnd();
        }
        else {
            MainGame.map.getEntities().removeIf(entity -> entity.equals(this));
            MainGame.map.setTile(Tile.SPACE, getX() - 1, getY() - 1);//MainGame.map.setCharacter(prevCharOfMap, getX(), getY(), prevColorOfMap);

            if(this instanceof Character) {
                MainGame.requestEnd();
            }
        }
    }
}
