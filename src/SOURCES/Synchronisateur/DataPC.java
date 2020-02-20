/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.Synchronisateur;

import java.util.Vector;

/**
 *
 * @author HP Pavilion
 */
public class DataPC {
    public Vector<PhotoManifestePC> listeManifesteDuPC;
    public Vector<PhotoSuppressionLocale> listeSuppressionDuPC;
    public Vector<PhotoDonneePC> listeDonneeDuPC;
    public Vector<PhotoDonneeEnligne> listeDonneeATelecharger;

    public DataPC() {
    }

    public DataPC(Vector<PhotoManifestePC> listeManifesteDuPC, Vector<PhotoSuppressionLocale> listeSuppressionDuPC, Vector<PhotoDonneePC> listeDonneeDuPC, Vector<PhotoDonneeEnligne> listeDonneeATelecharger) {
        this.listeManifesteDuPC = listeManifesteDuPC;
        this.listeSuppressionDuPC = listeSuppressionDuPC;
        this.listeDonneeDuPC = listeDonneeDuPC;
        this.listeDonneeATelecharger = listeDonneeATelecharger;
    }

    public Vector<PhotoManifestePC> getListeManifesteDuPC() {
        return listeManifesteDuPC;
    }

    public void setListeManifesteDuPC(Vector<PhotoManifestePC> listeManifesteDuPC) {
        this.listeManifesteDuPC = listeManifesteDuPC;
    }

    public Vector<PhotoSuppressionLocale> getListeSuppressionDuPC() {
        return listeSuppressionDuPC;
    }

    public void setListeSuppressionDuPC(Vector<PhotoSuppressionLocale> listeSuppressionDuPC) {
        this.listeSuppressionDuPC = listeSuppressionDuPC;
    }

    public Vector<PhotoDonneePC> getListeDonneeDuPC() {
        return listeDonneeDuPC;
    }

    public void setListeDonneeDuPC(Vector<PhotoDonneePC> listeDonneeDuPC) {
        this.listeDonneeDuPC = listeDonneeDuPC;
    }

    public Vector<PhotoDonneeEnligne> getListeDonneeATelecharger() {
        return listeDonneeATelecharger;
    }

    public void setListeDonneeATelecharger(Vector<PhotoDonneeEnligne> listeDonneeATelecharger) {
        this.listeDonneeATelecharger = listeDonneeATelecharger;
    }

    @Override
    public String toString() {
        return "DataPC{" + "listeManifesteDuPC=" + listeManifesteDuPC + ", listeSuppressionDuPC=" + listeSuppressionDuPC + ", listeDonneeDuPC=" + listeDonneeDuPC + ", listeDonneeATelecharger=" + listeDonneeATelecharger + '}';
    }
    
    
    
    
}
