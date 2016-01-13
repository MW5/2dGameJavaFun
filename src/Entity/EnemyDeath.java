package Entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class EnemyDeath {
    private int x;
    private int y;
    private int xMap;
    private int yMap;
    
    private int width;
    private int height;
    
    private Animation animation;
    private BufferedImage[] sprites;
    private int numOfSprites = 6;
    
    private boolean remove;
    
    public EnemyDeath(int x, int y) {
        this.x = x;
        this.y = y;
        
        width = 30;
        height = 30;
        
        try {
            BufferedImage spritesheet = ImageIO.read(
                    getClass().getResourceAsStream("/Sprites/Enemies/enemyDeath.gif")
            );
            sprites = new BufferedImage[numOfSprites];
            for (int i=0; i<sprites.length; i++) {
                sprites[i] = spritesheet.getSubimage(width*i, 0, width, height); 
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        
        animation = new Animation();
        animation.setFrames(sprites);
        animation.setDelay(70);
    }
    
    public void update() {
        animation.update();
        if (animation.hasPlayedOnce()) {
            remove = true;
        }
    }
    
    public boolean shouldRemove() {
        return remove;
    }
    
    public void setMapPosition(int x, int y) {
        xMap = x;
        yMap = y;
    }
    
    public void draw(Graphics2D g) { //problem is here
        g.drawImage(animation.getImage(), x+xMap-width/2, y+yMap-height/2, null);
    }
}
