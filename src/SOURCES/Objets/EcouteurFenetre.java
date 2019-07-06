/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.Objets;

import SOURCES.Callback.CallBackEcouteur;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import javax.swing.JFrame;

/**
 *
 * @author HP Pavilion
 */
public class EcouteurFenetre {

    private JFrame fenetre;
    private CallBackEcouteur callBackEcouteur;
    private Preference oldPref = new Preference(0, 0, 0, 0);

    public EcouteurFenetre(JFrame fenetre, CallBackEcouteur callBackEcouteur) {
        this.fenetre = fenetre;
        this.callBackEcouteur = callBackEcouteur;
        init();
    }
    

    private void init() {
        this.fenetre.addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {
                getWindowDetails();
            }

            @Override
            public void componentMoved(ComponentEvent e) {
                getWindowDetails();
            }

            @Override
            public void componentShown(ComponentEvent e) {
                
            }

            @Override
            public void componentHidden(ComponentEvent e) {
                
            }
        });
    }
    
    private void getWindowDetails(){
        Rectangle rectangle = this.fenetre.getBounds();
        Preference newPref = new Preference(rectangle.getX(), rectangle.getY(), rectangle.getWidth(), rectangle.getHeight());
        if(!newPref.toString().equals(oldPref.toString())){
            oldPref = newPref;
            callBackEcouteur.onChange(oldPref);
        }
    }

}
