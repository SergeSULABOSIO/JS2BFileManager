/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.Objets;

/**
 *
 * @author HP Pavilion
 */
public class Preference {
    public double fenetre_x;
    public double fenetre_y;
    public double fenetre_w;
    public double fenetre_h;

    public Preference() {
    }

    public Preference(double fenetre_x, double fenetre_y, double fenetre_w, double fenetre_h) {
        this.fenetre_x = fenetre_x;
        this.fenetre_y = fenetre_y;
        this.fenetre_w = fenetre_w;
        this.fenetre_h = fenetre_h;
    }

    public double getFenetre_x() {
        return fenetre_x;
    }

    public void setFenetre_x(double fenetre_x) {
        this.fenetre_x = fenetre_x;
    }

    public double getFenetre_y() {
        return fenetre_y;
    }

    public void setFenetre_y(double fenetre_y) {
        this.fenetre_y = fenetre_y;
    }

    public double getFenetre_w() {
        return fenetre_w;
    }

    public void setFenetre_w(double fenetre_w) {
        this.fenetre_w = fenetre_w;
    }

    public double getFenetre_h() {
        return fenetre_h;
    }

    public void setFenetre_h(double fenetre_h) {
        this.fenetre_h = fenetre_h;
    }

    @Override
    public String toString() {
        return "Preference{" + "fenetre_x=" + fenetre_x + ", fenetre_y=" + fenetre_y + ", fenetre_w=" + fenetre_w + ", fenetre_h=" + fenetre_h + '}';
    }
}
