package zmija;


import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author w7
 */
public class hrana {
    private int x;
    private int y;
    
    private final int s = 20;
    
     hrana(int x, int y) {
        this.x = x * s;
        this.y = y * s;
    }
     public void draw(Graphics2D g2) {
        g2.fillRect(x, y, s, s);
    }
     public Rectangle2D getBounds() {
        return new Rectangle2D.Double(x, y, s, s);
    }
    
}
