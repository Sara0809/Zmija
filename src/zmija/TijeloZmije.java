package zmija;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.util.LinkedList;

public class TijeloZmije{

    private Board board;

    int xStrana;
    int yStrana;
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
        this.tijlo = 2;
        this.board = board;
        x = 160;
        y = 160;
        xStrana = x;
        yStrana = y;
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

        Rectangle head = new Rectangle(zmija.getFirst());

        if (realMovingState == StranaKretanja.KRETANJE_DESNO) {
            head.x = head.x + s;
            xStrana = head.x;
            yStrana = head.y;
            zmija.add(0, head);
            if (this.zmija.size() > tijlo) {
                zmija.removeLast();
            }

            if (head.x >= 600) {
                head.x = 0;
            }
        } else if (realMovingState == StranaKretanja.KRETANJE_LIJEVO) {
            head.x = head.x - s;
            xStrana = head.x;
            yStrana = head.y;
            zmija.add(0, head);

            if (this.zmija.size() > tijlo) {
                zmija.removeLast();
            }

            if (head.x < 0) {
                head.x = 600 - s;
            }
        } else if (realMovingState == StranaKretanja.KRETANJE_GORE) {
            head.y = head.y - s;
            xStrana = head.x;
            yStrana = head.y;
            zmija.add(0, head);

            if (this.zmija.size() > tijlo) {
                zmija.removeLast();
            }

            if (head.y < 0) {
                head.y = 600 - s;
            }

        } else if (realMovingState == StranaKretanja.KRETSNJE_DOLE) {
            head.y = head.y + s;
            xStrana = head.x;
            yStrana = head.y;
            zmija.add(0, head);
            if (this.zmija.size() >= tijlo) {
                zmija.removeLast();
            }

            if (head.y >= 600) {
                head.y = 0;
            }

        }

    }

    /**
     * Metoda koja vraća pravugaonik koji predstavlja glavu zmije.
     */
    public Rectangle2D getBoundsHead() {
        return new Rectangle2D.Double(xStrana, yStrana, s, s);
    }

    /**
     * Metoda koja ispituje da li je zmija udarila u svoj rep.
     */
    void udarilaUsvojRep() {
        for (int i = 1; i < zmija.size(); i++) {
            if (zmija.getFirst().intersects(zmija.get(i))) {
                board.zaustaviIgru();
            }
        }
    }
}