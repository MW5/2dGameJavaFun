package Main;

import javax.swing.JFrame;

public class Game {
    public static void main (String[] args) {
        //creates window
        JFrame window = new JFrame("Madzioszka w kosmosie");
        //sets window properties
        window.setContentPane(new GamePanel());
        
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.pack();
        window.setVisible(true);
    }
    
}
