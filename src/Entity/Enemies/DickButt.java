package Entity.Enemies;

import Entity.Enemy;
import Entity.Animation;
import TileMap.TileMap;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.awt.Graphics2D;

public class DickButt extends Enemy {
    
    private BufferedImage[] sprites;
    public DickButt (TileMap tm) {
        super(tm);
        
        moveSpeed = 0.3;
        maxSpeed = 0.3;
        fallSpeed = 0.2;
        maxFallSpeed = 10;
        
        width = 30;
        height = 30;
        cWidth = 20;
        cHeight = 20;
        
        health = maxHealth = 2;
        damage = 1;
        
        //load sprites
        try {
            BufferedImage spritesheet = ImageIO.read(
                    getClass().getResourceAsStream("/Sprites/Enemies/dickButt.gif"));
            //num of frames
            sprites = new BufferedImage[3];
            for (int i=0; i<sprites.length; i++) {
                sprites[i] = spritesheet.getSubimage(i*width,0,width,height);
            }
            
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        animation = new Animation();
        animation.setFrames(sprites);
        animation.setDelay(300);
        
        right = true;
    }
    
    private void getNextPosition() {
        
    }
    
    public void update() {
        //update position
        getNextPosition();
        checkTileMapCollision();
        setPosition(xTemp, yTemp);
    }
    
    public void draw(Graphics2D g) {
        setMapPosition();
        super.draw(g);
    }
}