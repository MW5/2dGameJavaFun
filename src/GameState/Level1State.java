package GameState;

import Entity.*;
import Entity.Enemies.*;
import Main.GamePanel;
import TileMap.Background;
import TileMap.TileMap;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.awt.Point;

public class Level1State extends GameState {
    
    private TileMap tileMap;
    private Background bg;
    
    private Player player;
    private Hud hud;
    
    private ArrayList<Enemy> enemies;
    private ArrayList<EnemyDeath> enemyDeaths;
    
    
    public Level1State (GameStateManager gsm) {
        this.gsm = gsm;
        init();
    }
    
    public void init() {
        tileMap = new TileMap(30);
        tileMap.loadTiles("/Tilesets/lvl1_tileset.png");
        tileMap.loadMap("/Maps/level1-1.map");
        tileMap.setPosition(0, 0);
        tileMap.setTween(0.07);
        
        bg = new Background("/Backgrounds/lvl1_background.png", 0.1);
        
        player = new Player(tileMap);
        player.setPosition(50, 195);
        
        enemyDeaths = new ArrayList<EnemyDeath>();
        
        populateEnemies();
        
        hud = new Hud(player);
        
    }
    
    public void populateEnemies() {
        enemies = new ArrayList<Enemy>();
        
        Point[] points = new Point[] {
            new Point(180,195),
            new Point(240, 195),
            new Point(860, 200),
            new Point(1525, 200),
            new Point(1680, 200),
            new Point(1800, 200)
        };
        
        for (int i = 0; i<points.length; i++) {
            DickButt dB;
            dB = new DickButt(tileMap);
            dB.setPosition(points[i].x, points[i].y);
            enemies.add(dB);
        }
    }
    
    public void update() {
        //update player
        player.update();
        tileMap.setPosition(GamePanel.WIDTH/2-player.getX(), GamePanel.HEIGHT/2-player.getY());
        
        //update all enemies
        for (int i=0; i<enemies.size(); i++) {
                Enemy e = enemies.get(i);
                e.update();
                if (e.isDead()) {
                    enemyDeaths.add(new EnemyDeath(e.getX(), e.getY()));
                    enemies.remove(e);
                    i--;
                }
        }
        
        //update enemy deaths
        for (int i=0; i<enemyDeaths.size(); i++) {
            EnemyDeath e = enemyDeaths.get(i);
            if (!e.shouldRemove()) {
                e.update();
            } else {
               enemyDeaths.remove(e);
               i--;
            }
            
        }
        
        //attack enemies
        player.checkAttack(enemies);
        
        //update background
        bg.setPosition(tileMap.getX()/2, tileMap.getY());
    }
    
    public void draw(Graphics2D g) {
        //draw background
        bg.draw(g);
        //draw tilemap
        tileMap.draw(g);
        //draw player
        player.draw(g);
        //draw enemies
        for (int i=0;i<enemies.size();i++) {
            enemies.get(i).draw(g);
        }
        //draw enemy deaths
        for (EnemyDeath e : enemyDeaths) {
            e.setMapPosition((int)tileMap.getX(), (int)tileMap.getY());
            e.draw(g);
        }
        //draw hud
        hud.draw(g);
    }
    
    public void keyPressed(int k) {
        if (k == KeyEvent.VK_LEFT) {
            player.setLeft(true);
        }
        if (k == KeyEvent.VK_RIGHT) {
            player.setRight(true);
        }
        if (k == KeyEvent.VK_W) {
            player.setUp(true);
        }
        if (k == KeyEvent.VK_S) {
            player.setDown(true);
        }
        if (k == KeyEvent.VK_UP) {
            player.setJumping(true);
        }
        if (k == KeyEvent.VK_DOWN) {
            player.setGliding(true);
        }
        if (k == KeyEvent.VK_Z) {
            player.setScratching();
        }
        if (k == KeyEvent.VK_X) {
            player.setFiring();
        }
    }
    
    public void keyReleased(int k) {
        if (k == KeyEvent.VK_LEFT) {
            player.setLeft(false);
        }
        if (k == KeyEvent.VK_RIGHT) {
            player.setRight(false);
        }
        if (k == KeyEvent.VK_W) {
            player.setUp(false);
        }
        if (k == KeyEvent.VK_S) {
            player.setDown(false);
        }
        if (k == KeyEvent.VK_UP) {
            player.setJumping(false);
        }
        if (k == KeyEvent.VK_DOWN) {
            player.setGliding(false);
        }
    }
}
