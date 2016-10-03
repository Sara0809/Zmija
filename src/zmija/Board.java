package zmija;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import javax.swing.JPanel;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author w7
 */
class Board extends JPanel {
    
    public final int PANEL_WIDTH = 800;
    
    public final int PANEL_HEIGHT = 600;

    final Color BACKGROUND_COLOR = Color.BLUE;
    Boolean inGame;
    public Board() {
        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        setBackground(BACKGROUND_COLOR);
        setFocusable(true);

        setFont(getFont().deriveFont(Font.BOLD, 20f));
        setDoubleBuffered(true);
        inGame = false;
    }
    public void pocetakIgre() {
       
        inGame = true;

    }
    public void zaustaviIgru() {

    
        inGame = false;

    }
     @Override
    public void paint(Graphics g) {
        super.paint(g);

        Graphics2D g2 = (Graphics2D) g;

        if (inGame) {
            // Iscrtaj teren
            g2.drawRect(0, 0, PANEL_WIDTH, PANEL_HEIGHT);
            g2.setColor(Color.BLUE);

            
            // Iscrtaj sve objekte
            

            Font f = new Font("Helvetica", Font.PLAIN, 30);
            g2.setFont(f);
            g2.setColor(Color.WHITE);
           
            
            g.dispose();
         } else {
           
           Font f = new Font("Helvetica", Font.PLAIN, 30);
           g2.setFont(f);
        }
    }
}
