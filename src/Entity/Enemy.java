package Entity;

import TileMap.TileMap;

public abstract class Enemy extends MapObject {
    
    protected int health;
    protected int maxHealth;
    protected boolean dead;
    protected int damage;
    
    public Enemy (TileMap tm) {
        super(tm);
        
    }
}
