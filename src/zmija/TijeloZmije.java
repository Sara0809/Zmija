package zmija;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.util.LinkedList;

public class TijeloZmije{

    private Board board;

    int xGlava;
    int yGlava;
    int x;
    int y;
    int s = 20;
    int tijlo;

    LinkedList<Rectangle> zmija = new LinkedList();

    StranaKretanja realMovingState;
    StranaKretanja usersLastMovingState;

    enum StranaKretanja {

        KRETANJE_LIJEVO, KRETANJE_DESNO, KRETANJE_GORE, KRETSNJE_DOLE
    }

    public TijeloZmije(Board board) {
        this.tijlo = 5;
        this.board = board;
        x = 160;
        y = 160;
        xGlava = x;
        yGlava = y;
        for (int i = 0; i < tijlo; i++) {
            Rectangle r = new Rectangle(x, y, s, s);
            zmija.add(r);
        }
        this.realMovingState = StranaKretanja.KRETANJE_DESNO;
        this.usersLastMovingState = StranaKretanja.KRETANJE_DESNO;
    }

    void rast() {
        tijlo++;
    }

    public void kretanjDesno() {
        usersLastMovingState = StranaKretanja.KRETANJE_DESNO;
    }

    public void kretanjeLijevo() {
        usersLastMovingState = StranaKretanja.KRETANJE_LIJEVO;
    }

    public void kretanjeGore() {
        usersLastMovingState = StranaKretanja.KRETANJE_GORE;
    }

    public void kretanjeDole() {
        usersLastMovingState = StranaKretanja.KRETSNJE_DOLE;
    }

    public void draw(Graphics2D g2) {
        for (int i = 0; i < zmija.size(); i++) {
            g2.setColor(Color.CYAN);
            Rectangle r = zmija.get(i);
            g2.fillRect(r.x, r.y, r.width, r.height);
        }
    }

    public void kretanje() {

        if (realMovingState == StranaKretanja.KRETANJE_GORE && usersLastMovingState != StranaKretanja.KRETSNJE_DOLE
                || realMovingState == StranaKretanja.KRETSNJE_DOLE && usersLastMovingState != StranaKretanja.KRETANJE_GORE
                || realMovingState == StranaKretanja.KRETANJE_LIJEVO && usersLastMovingState != StranaKretanja.KRETANJE_DESNO
                || realMovingState == StranaKretanja.KRETANJE_DESNO && usersLastMovingState != StranaKretanja.KRETANJE_LIJEVO) {
            realMovingState = usersLastMovingState;
        }

        Rectangle glava = new Rectangle(zmija.getFirst());

        if (realMovingState == StranaKretanja.KRETANJE_DESNO) {
            glava.x = glava.x + s;
            xGlava = glava.x;
            yGlava = glava.y;
            zmija.add(0, glava);
            if (this.zmija.size() > tijlo) {
                zmija.removeLast();
            }

            if (glava.x >= 600) {
                glava.x = 0;
            }
        } else if (realMovingState == StranaKretanja.KRETANJE_LIJEVO) {
            glava.x = glava.x - s;
            xGlava = glava.x;
            yGlava = glava.y;
            zmija.add(0, glava);

            if (this.zmija.size() > tijlo) {
                zmija.removeLast();
            }

            if (glava.x < 0) {
                glava.x = 600 - s;
            }
        } else if (realMovingState == StranaKretanja.KRETANJE_GORE) {
            glava.y = glava.y - s;
            xGlava = glava.x;
            yGlava = glava.y;
            zmija.add(0, glava);

            if (this.zmija.size() > tijlo) {
                zmija.removeLast();
            }

            if (glava.y < 0) {
                glava.y = 600 - s;
            }

        } else if (realMovingState == StranaKretanja.KRETSNJE_DOLE) {
            glava.y = glava.y + s;
            xGlava = glava.x;
            yGlava = glava.y;
            zmija.add(0, glava);
            if (this.zmija.size() >= tijlo) {
                zmija.removeLast();
            }

            if (glava.y >= 600) {
                glava.y = 0;
            }

        }

    }

    
     
    public Rectangle2D getBoundsHead() {
        return new Rectangle2D.Double(xGlava, yGlava, s, s);
    }
    
    void udarilaUsvojRep() {
        for (int i = 1; i < zmija.size(); i++) {
            if (zmija.getFirst().intersects(zmija.get(i))) {
                board.zaustaviIgru();
            }
        }
    }
}