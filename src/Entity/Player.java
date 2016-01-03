package Entity;

import TileMap.*;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Player extends MapObject {
    
    //player stuff
    private int health;
    private int maxHealth;
    private int projectile;
    private int ammo;
    private boolean dead;
    private boolean flinching;
    private long flinchTimer;
    
    //projectile
    private boolean firing;
    private int ammoCost;
    private int projectileDamage;
    private ArrayList<Projectile> projectiles;
    
    //scratch
    private boolean scratching;
    private int scratchDamage;
    private int scratchRange;
    
    //gliding
    private boolean gliding;
    
    //animations
    private ArrayList<BufferedImage[]> sprites;
    //number of animation frames for each of the animation actions
    private final int[] numFrames = {
        2, 8, 1, 2, 4, 2, 5
    };
    //animation actions
    private static final int IDLE = 0;
    private static final int WALKING = 1;
    private static final int JUMPING = 2;
    private static final int FALLING = 3;
    private static final int GLIDING = 4;
    private static final int PROJECTILE = 5;
    private static final int SCRATCHING = 6;
    
    public Player(TileMap tm) {
        super(tm);
        width = 30;
        height = 30;
        cWidth = 20;
        cHeight = 20;
        
        moveSpeed = 0.3;
        maxSpeed = 1.6;
        stopSpeed = 0.4;
        fallSpeed = 0.15;
        maxFallSpeed = 4.0;
        jumpStart = -4.8;
        stopJumpSpeed = 0.3;
        
        facingRight = true;
        
        health = maxHealth = 5;
        projectile = ammo = 2500;
        ammoCost = 200;
        projectileDamage = 5;
        projectiles = new ArrayList<Projectile>();
        
        scratchDamage = 8;
        scratchRange = 40;
        
        //load sprites
        try {
            BufferedImage spriteSheet = ImageIO.read(
                    getClass().getResourceAsStream("/Sprites/Player/player_sprites.png"));
            sprites = new ArrayList<BufferedImage[]>();
            for (int i=0; i <numFrames.length; i++) {
                BufferedImage[] bi = new BufferedImage[numFrames[i]];
                for (int j=0; j<numFrames[i]; j++) {
                    //the last animation has different size
                    if (i!=6) {
                        bi[j] = spriteSheet.getSubimage(j*width, i*height, width, height);
                    } else {
                        bi[j] = spriteSheet.getSubimage(j*width*2, i*height, width*2, height);
                    }
                }
                sprites.add(bi);
            }
            
            
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        
        animation = new Animation();
        currentAction = IDLE;
        animation.setFrames(sprites.get(IDLE));
        animation.setDelay(400);
    }
    public int getHealth() {
        return health;
    }
    public int getMaxHealth() {
        return maxHealth;
    }
    public int getProjectile() {
        return projectile;
    }
    public int getAmmo() {
        return ammo;
    }
    
    public void setFiring() {
        firing = true;
    }
    public void setScratching() {
        scratching = true;
    }
    public void setGliding(boolean b) {
        gliding = b;
    }
    
    private void getNextPosition() {
        if (left) {
            dx -= moveSpeed;
            if (dx < -maxSpeed) {
                dx = -maxSpeed;
            }
        } else if (right) {
            dx += moveSpeed;
            if (dx > maxSpeed) {
                dx = maxSpeed;
            }
        } else {
            if (dx > 0) {
                dx -= stopSpeed;
                if (dx < 0) {
                    dx = 0;
                }
            } else if (dx < 0) {
                dx += stopSpeed;
                if (dx > 0) {
                    dx = 0;
                }
            }
        }
        
        //cannot move while attacking, except in air
        if ((currentAction == SCRATCHING || currentAction == PROJECTILE) &&
                (!jumping || falling)) {
            dx = 0;
        }
        
        //jumping
        if (jumping && !falling) {
            dy = jumpStart;
            falling = true;
        }
        
        //falling
        if (falling) {
            if (dy > 0 && gliding) {
                dy += fallSpeed * 0.1;
            } else {
                dy += fallSpeed;
            }
            if (dy > 0) {
                jumping = false;
            }
            if (dy < 0 && !jumping) {
                dy += stopJumpSpeed;
            }
            if (dy > maxFallSpeed) {
                dy = maxFallSpeed;
            }
        }
    }
    
    public void update() {
        //update position
        getNextPosition();
        checkTileMapCollision();
        setPosition(xTemp, yTemp);
        
        //check if attacks has stopped
        if (currentAction == SCRATCHING) {
            if (animation.hasPlayedOnce()) {
                scratching = false;
            }
        }
        if (currentAction == PROJECTILE) {
            if (animation.hasPlayedOnce()) {
                firing = false;
            }
        }
        
        //projectile attack
        projectile += 1;
        if (projectile > ammo) {
            projectile = ammo;
        }
        if(firing && currentAction != PROJECTILE) {
            if (projectile > ammoCost) {
                projectile -= ammoCost;
                Projectile pr = new Projectile(tileMap, facingRight);
                pr.setPosition(x, y);
                projectiles.add(pr);
            }
        }
        
        //update projectiles
        for (int i=0; i<projectiles.size(); i++) {
            projectiles.get(i).update();
            if (projectiles.get(i).shouldRemove()){
                projectiles.remove(i);
                i--;
            }
        }
        
        //set animation
        if (scratching) {
            if (currentAction != SCRATCHING) {
                currentAction = SCRATCHING;
                animation.setFrames(sprites.get(SCRATCHING));
                animation.setDelay(50);
                width = 60;
                
            }
        } else if (firing) {
            if (currentAction != PROJECTILE) {
                currentAction = PROJECTILE;
                animation.setFrames(sprites.get(PROJECTILE));
                animation.setDelay(100);
                width = 30;
            }
        } else if (dy > 0) {
            if (gliding) {
                if (currentAction != GLIDING) {
                    currentAction = GLIDING;
                    animation.setFrames(sprites.get(GLIDING));
                    animation.setDelay(100);
                    width = 30;
                }
            } else if (currentAction != FALLING) {
                if (currentAction != FALLING) {
                    currentAction = FALLING;
                    animation.setFrames(sprites.get(FALLING));
                    animation.setDelay(100);
                    width = 30;
                }
            }
        } else if (dy < 0) {
            if (currentAction != JUMPING) {
                currentAction = JUMPING;
                animation.setFrames(sprites.get(JUMPING));
                animation.setDelay(-1);
                width = 30;
            }
        } else if (left || right) {
            if (currentAction != WALKING) {
                currentAction = WALKING;
                animation.setFrames(sprites.get(WALKING));
                animation.setDelay(40);
                width = 30;
            }
        } else {
            if (currentAction != IDLE) {
                currentAction = IDLE;
                animation.setFrames(sprites.get(IDLE));
                animation.setDelay(400);
                width = 30;
            }
        }
        animation.update();
        
        //set direction
        if (currentAction != SCRATCHING && currentAction != PROJECTILE) {
            if (right) {
                facingRight = true;
            } 
            if (left) {
                facingRight = false;
            }
        }
    }
    
    public void draw(Graphics2D g) {
        setMapPosition();
        
        //draw projectiles
        for (int i=0; i<projectiles.size(); i++) {
            projectiles.get(i).draw(g);
        }
        
        
        //draw player
        if (flinching) {
            long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
            if (elapsed / 10 % 0 == 0) {
                return;
            }
        }
        if (facingRight) {
            g.drawImage(animation.getImage(),
                    (int)(x+xMap-width/2),
                    (int)(y+yMap-height/2),
                    null);
        }
        if (!facingRight) {
            g.drawImage(animation.getImage(),
                    (int)(x+xMap-width/2+width),
                    (int)(y+yMap-height/2),
                    -width,
                    height,
                    null);
        }
    }
    
}
