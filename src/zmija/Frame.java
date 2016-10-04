/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zmija;

import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

/**
 *
 * @author w7
 */
public class Frame extends JFrame{

    Board board = new Board();
    public Frame() throws HeadlessException {
        add(board);
        setJMenuBar(initMenu());
        pack();
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setTitle("Zmija");

        setVisible(true);

    }
    private JMenuBar initMenu() {
          
        JMenuBar menuBar = new JMenuBar();
        
        JMenu gameMenu = new JMenu("Igra");

       
        JMenuItem novaIgra = new JMenuItem("Nova igra");
        JMenuItem pomoc = new JMenuItem("Pomoc");
        JMenuItem rezultat = new JMenuItem("Rezultat");

        novaIgra.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                board.pocetakIgre();
            }
        });

        pomoc.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
            
                board.pomoc.setVisible(true);
            }
        });

              rezultat.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                board.readTextFileLineByLine();
            }
        });

        
        gameMenu.add(novaIgra);
        gameMenu.add(rezultat);
        
        menuBar.add(gameMenu);
        menuBar.add(pomoc);

        return menuBar;
    }
}
