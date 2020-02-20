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
public class DataServeur {
    public Vector<EnregistrementServeur> listeEnregistrements;

    public DataServeur() {
    }

    public DataServeur(Vector<EnregistrementServeur> listeEnregistrements) {
        this.listeEnregistrements = listeEnregistrements;
    }

    public Vector<EnregistrementServeur> getListeEnregistrements() {
        return listeEnregistrements;
    }

    public void setListeEnregistrements(Vector<EnregistrementServeur> listeEnregistrements) {
        this.listeEnregistrements = listeEnregistrements;
    }

    @Override
    public String toString() {
        return "DataServeur{" + "listeEnregistrements=" + listeEnregistrements + '}';
    }
}
