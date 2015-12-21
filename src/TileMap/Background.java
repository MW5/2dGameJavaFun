package TileMap;

import Main.GamePanel;
import TileMap.Background;
import java.awt.image.*;
import javax.imageio.ImageIO;
import java.awt.*;

//prepares background class
public class Background {
    
    private BufferedImage image;
    
    private double x;
    private double y;
    private double dx;
    private double dy;
    
    private int width;
    private int height;
    
    private double moveScale;
    
    public Background(String s, double ms) {
        try {
            image = ImageIO.read(getClass().getResourceAsStream(s));
            width = image.getWidth();
            height = image.getHeight();
            moveScale = ms;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    //constructor for stars
    public Background(String s) {
        try {
            image = ImageIO.read(getClass().getResourceAsStream(s));
            width = image.getWidth();
            height = image.getHeight();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void setPosition(double x, double y) {
        this.x = x * moveScale % GamePanel.WIDTH;
        this.y = y * moveScale % GamePanel.HEIGHT;
    }
    
    public void setVector(double dx, double dy) {
        this.dx = dx;
        this.dy = dy;
    }
    
    public void update() {
        x += dx;
        while(x <= -width) {
            x += width;
        }
        while(x >= width) {
            x -= width;
        }
        y += dy;
        while(y <= -height) {
            y += height;
        }
        while(y >= height) {
            y -= height;
        }
    }
    
    public void draw(Graphics2D g) {
        g.drawImage(image, (int)x, (int)y, null);
        if (x < 0) {
            g.drawImage(image, (int)x + GamePanel.WIDTH, (int)y, null);
        }
        if (x > 0) {
            g.drawImage(image, (int)x - GamePanel.WIDTH, (int)y, null);
        }
    }
}
