package GameState;

import java.awt.*;
import TileMap.Background;
import java.awt.event.KeyEvent;

//prepares menustate
public class MenuState extends GameState {
    
    private Background bg;
    private Background bgStars;
    
    private int currentChoice = 0;
    private String[] options = {
        "Start","Help","Quit"
    };
    
    private Color titleColor;
    private Font titleFont;
    private Font titleSmallerFont;
    
    private Font font;
    
    public MenuState(GameStateManager gsm) {
        this.gsm = gsm;
        
        try {
            //prepares background object for menu
            bg = new Background("/Backgrounds/menubg.png", 1);
            bg.setVector(-0.1, 0);
            
            bgStars = new Background ("/Backgrounds/stars.png");
            
            titleColor = new Color(172, 0 ,191);
            titleFont = new Font("Century Gothic", Font.PLAIN, 28);
            titleSmallerFont = new Font("Century Gothic", Font.PLAIN, 20);
            font = new Font("Arial", Font.PLAIN, 12);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void init() {
        
    };
    
    public void update() {
        bg.update();
    };
    
    public void draw(Graphics2D g) {
        //draw bg
        bgStars.draw(g);
        bg.draw(g);
        
        //draw title
        g.setColor(titleColor);
        g.setFont(titleFont);
        g.drawString("Madzioszka", 80, 70);
        g.setColor(Color.WHITE);
        g.setFont(titleSmallerFont);
        g.drawString("w kosmosie", 120, 90);
        
        //draw menu options
        g.setFont(font);
        for (int i=0; i<options.length; i++) {
            if (i == currentChoice) {
                g.setColor(Color.WHITE);
            } else {
                g.setColor(new Color(172, 0 ,191));
            }
            g.drawString(options[i], 145, 140 + i * 15);
        }
    };
    
    private void select() {
        if (currentChoice == 0) {
            gsm.setState(GameStateManager.LEVEL1STATE);
        }
        if (currentChoice == 1) {
            //help
        }
        if (currentChoice == 2) {
            System.exit(0);
        }
    }
    
    public void keyPressed(int k) {
        if (k == KeyEvent.VK_ENTER){
            select();
        }
        if (k == KeyEvent.VK_UP) {
            currentChoice--;
            if (currentChoice == -1) {
                currentChoice = options.length-1;
            }
        }
        if (k == KeyEvent.VK_DOWN) {
            currentChoice++;
            if (currentChoice == 3) {
                currentChoice = 0;
            }
        }
    };
    
    public void keyReleased(int k) {
        
    };
}
