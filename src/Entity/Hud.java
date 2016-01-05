package Entity;

import Entity.Player;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class Hud {
    
    private Player player;
    private BufferedImage hudImage;
    private Font font;
    
    public Hud(Player p) {
        player = p;
        try {
            hudImage = ImageIO.read(
                getClass().getResourceAsStream("/HUD/hud.gif"));
            font = new Font("Arial", Font.PLAIN, 14);
            
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void draw(Graphics2D g) {
        g.drawImage(hudImage, 0, 10, null);
        g.setFont(font);
        g.setColor(Color.white);
        g.drawString(player.getHealth()+"/"+player.getMaxHealth(),30,25);
        g.drawString((int)player.getAmmoLeft()+"/"+player.getMaxAmmo(), 30,45);
    }
}
