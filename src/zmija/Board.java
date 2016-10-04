package zmija;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
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
class Board extends JPanel implements Runnable{
    
    public final int PANEL_SIRINA = 800;
    
    public final int PANEL_VISINA = 600;

    final Color BACKGROUND_COLOR = Color.BLUE;
    
    private final ImageIcon grass;
    private final ImageIcon background;

    final Thread runner;

    Random random;
    TijeloZmije tijeloZmije;
    hrana hrana;
    HelpFrame pomoc;

    private String imeIgraca;
    int brzina;
    int RezultatIgre = 0;
    
    int pojedenoHrane = 0;
    
    Boolean inGame;
    public Board() {
    setPreferredSize(new Dimension(PANEL_SIRINA, PANEL_VISINA));
        setBackground(BACKGROUND_COLOR);
        setFocusable(true);

        setFont(getFont().deriveFont(Font.BOLD, 18f));
        setDoubleBuffered(true);

        inGame = false;
        brzina = 150;

        grass = new ImageIcon(getClass().getResource("grass.jpg"));
        background = new ImageIcon(getClass().getResource("zmijica.sl.jpg"));

        random = new Random();
        tijeloZmije = new TijeloZmije(this);
        hrana = new hrana(random.nextInt(30), random.nextInt(30));
        pomoc = new HelpFrame(this);

        addKeyListener(new KeyAdapter() 
        {
             @Override
        public void keyPressed(KeyEvent e) {
            int keyCode = e.getKeyCode();

            if (keyCode == KeyEvent.VK_LEFT) {
                tijeloZmije.kretanjeLijevo();
            } else if (keyCode == KeyEvent.VK_RIGHT) {
                tijeloZmije.kretanjDesno();
            } else if (keyCode == KeyEvent.VK_UP) {
                tijeloZmije.kretanjeGore();
            } else if (keyCode == KeyEvent.VK_DOWN) {
                tijeloZmije.kretanjeDole();
            }
        }
        });

        runner = new Thread(this);
        runner.start();
    }
    public void pocetakIgre() {
       pojedenoHrane = 0;
        tijeloZmije = new TijeloZmije(this);
       
        inGame = true;

    }
    public void zaustaviIgru() {
        imeIgraca = JOptionPane.showInputDialog(null, "Molim vas, unesite ime:", "Vas rezultat " + RezultatIgre , JOptionPane.INFORMATION_MESSAGE);
        try {

            List<String> scores = load("src/zmija/rezultat.txt");
            scores.add(imeIgraca + " - " + RezultatIgre);
            save_file("src/zmija/rezultat.txt", scores); 
            readTextFileLineByLine();
        } catch (IOException ex) { 
            System.out.println("Error : " + ex);
        }
       
        inGame = false;

    }
     @Override
    public void paint(Graphics g) {
       super.paint(g);

        Graphics2D g2 = (Graphics2D) g;

        if (inGame) {
            

            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            // Iscrtaj teren
            g2.drawRect(0, 0, PANEL_SIRINA, PANEL_VISINA);
            g2.setColor(Color.BLUE);

            g2.drawImage(grass.getImage(), 0, 0, PANEL_SIRINA, PANEL_VISINA, null);
            // Iscrtaj sve objekte
            tijeloZmije.draw(g2);
            hrana.draw(g2);

            Font f = new Font("Helvetica", Font.PLAIN, 30);
            g2.setFont(f);
            g2.setColor(Color.WHITE);
            g2.drawString("REZULTAT: " + RezultatIgre, 10, 30);

            Toolkit.getDefaultToolkit().sync();

            // Optimizacija upotrebe RAM-a, 
            g.dispose();
        } else {
            g2.drawImage(background.getImage(), 0, 0, PANEL_SIRINA, PANEL_VISINA, null);
            Font f = new Font("Helvetica", Font.PLAIN, 30);
            g2.setFont(f);
        }
    }
    
        public void update() {
        tijeloZmije.kretanje();

    }
    private boolean foodIntersectsSnake() {
        for (int i = 0; i < tijeloZmije.zmija.size(); i++) {
            if (tijeloZmije.zmija.get(i).intersects(hrana.getBounds())) {
                return true;
            }
        }
        return false;
    }
        
    private void detectCollision() {
        if (tijeloZmije.getBoundsHead().intersects(hrana.getBounds())) {
            tijeloZmije.rast();

            while (foodIntersectsSnake()) {
                hrana = new hrana(random.nextInt(30), random.nextInt(30));
            }
            pojedenoHrane++;
            RezultatIgre += 10;
            if (pojedenoHrane % 5 == 0 && brzina > 50) {
                brzina = brzina - 20;
            }
        }
        tijeloZmije.udarilaUsvojRep();

    }

    
     @Override
    public void run() {

        while (true) {

            if (inGame) {
                update();
                detectCollision();
                repaint();
            }

            try {

                Thread.sleep(brzina);

            } catch (InterruptedException ex) {
                System.out.println(ex.toString());
            }

        }

    }
    
    //Čuvanje razultata u datoteci
    private void save_file(String name_fale, List<String> scores) throws IOException {

        File file = new File(name_fale);
        if (!file.exists()) { //Ako ne postoji datoteka, kreirati je
            System.out.println("ispisi");
            file.createNewFile();
        }
        try (PrintWriter writer = new PrintWriter(file, "UTF-8")) {
            for (String score : scores) {
                writer.println(score);
            }
        }
    }

    // postaviti u listu rezultate(punjenje liste)
    private List<String> load(String file_name) throws FileNotFoundException {
        File file = new File(file_name);

        if (!file.exists()) {
            //ako ne postoji datotetka, izbaci izuzetak
            throw new FileNotFoundException();
        }

        List<String> scores = new ArrayList<>();

        try (Scanner scanner = new Scanner(file)) { //kreiranje citaoca koji unosi podatke
            while (scanner.hasNextLine()) {
                scores.add(scanner.nextLine()); //postavljanje rezultata u listu

            }
        }

        return scores;
    }

    public static void readTextFileLineByLine() {
        FileReader in = null;
        //BufferedReader dozvoljava čitanje većeg "komada" datoteke odjednom.
        BufferedReader bin = null;

        try {

            File file = new File("src/zmija/rezultat.txt");

            in = new FileReader(file);
            // Za inicijalizaciju, BufferedReader zahtjeva otvoren FileReader tok
            bin = new BufferedReader(in);

            String data;
            ArrayList<String> rijeci = new ArrayList<>();

            /*
             * Metoda readLine klase BufferedReader učitava jedan red teksta iz
             * datoteke. Vraća null ukoliko dođe do kraja datoteke.
             */
            while ((data = bin.readLine()) != null) {
                rijeci.add(data);
            }

            int d = rijeci.size();

            String strLine = "";

            for (int i = 0; i < d; i++) {
                strLine += (i + 1) + ". " + rijeci.get(i) + "\n";
            }
            JOptionPane.showMessageDialog(null, strLine, "Rezultat", JOptionPane.INFORMATION_MESSAGE);

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, ex.toString());
        } finally {
            if (bin != null) {
                try {
                    bin.close();
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, ex.toString());
                }
            }

            if (in != null) {
                try {
                    in.close();
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, ex.toString());
                }
            }
        }
    }
}
