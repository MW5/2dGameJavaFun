package Entity;

import TileMap.*;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class Projectile extends MapObject {
    
    private boolean hit;
    private boolean remove;
    private BufferedImage[] sprites;
    //num of regular sprites
    private int numOfSprites = 1;
    private BufferedImage[] hitSprites;
    //num of hitSprites
    private int numOfHitSprites = 3;
    
    public Projectile(TileMap tm, boolean right) {
        super(tm);
        
        facingRight = right;
        moveSpeed = 3.8;
        if (right) {
            dx = moveSpeed;
        }
        if (!right) {
            dx = -moveSpeed;
        }
        width = 30;
        height = 30;
        cWidth = 14;
        cWidth = 14;
        
        //load sprites
        try {
            //load file
            BufferedImage spriteSheet = ImageIO.read(
                getClass().getResourceAsStream("/Sprites/Player/projectile.png"));
            //get array with sprites subimages
            sprites = new BufferedImage[numOfSprites];
            for (int i=0; i<sprites.length; i++) {
                sprites[i] = spriteSheet.getSubimage(
                        i*width,
                        0,
                        width,
                        height);
            }
            //get array with hitSprites subimages
            hitSprites = new BufferedImage[numOfHitSprites];
            for (int i=0; i<hitSprites.length; i++) {
                hitSprites[i] = spriteSheet.getSubimage(
                        i*width,
                        height,
                        width,
                        height);
            }
            
            animation = new Animation();
            animation.setFrames(sprites);
            animation.setDelay(70);
            
            
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void setHit() {
        if (hit) {
            return;
        }
        hit = true;
        animation.setFrames(hitSprites);
        animation.setDelay(70);
        dx = 0;
    }
    
    public boolean shouldRemove() {
        return remove;
    }
    
    public void update() {
        checkTileMapCollision();
        setPosition(xTemp, yTemp);
        
        if (dx == 0 && !hit) {
            setHit();
        }
        
        animation.update();
        if (hit && animation.hasPlayedOnce()) {
            remove = true;
        }
    }
    
    public void draw(Graphics2D g) {
        setMapPosition();
        super.draw(g);
        
    }
}
