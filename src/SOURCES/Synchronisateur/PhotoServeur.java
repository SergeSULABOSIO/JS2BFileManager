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
public class PhotoServeur {

    public Vector<PhotoSuppressionDistante> listeSignaturesSupprimees;
    public Vector<PhotoManifesteServeur> listeManifestes;
    public Vector<PhotoDonneeEnligne> listeDonneeEnligne;

    public PhotoServeur() {
        listeSignaturesSupprimees = new Vector<>();
        listeManifestes = new Vector<>();
        listeDonneeEnligne = new Vector<>();
    }

    public PhotoServeur(Vector<PhotoSuppressionDistante> listeSignaturesSupprimees, Vector<PhotoManifesteServeur> listeManifestes, Vector<PhotoDonneeEnligne> listeRubriques) {
        this.listeSignaturesSupprimees = listeSignaturesSupprimees;
        this.listeManifestes = listeManifestes;
        this.listeDonneeEnligne = listeRubriques;
    }

    public Vector<PhotoSuppressionDistante> getListeSignaturesSupprimees() {
        return listeSignaturesSupprimees;
    }

    public void setListeSignaturesSupprimees(Vector<PhotoSuppressionDistante> listeSignaturesSupprimees) {
        this.listeSignaturesSupprimees = listeSignaturesSupprimees;
    }

    public Vector<PhotoManifesteServeur> getListeManifestes() {
        return listeManifestes;
    }

    public void setListeManifestes(Vector<PhotoManifesteServeur> listeManifestes) {
        this.listeManifestes = listeManifestes;
    }

    public Vector<PhotoDonneeEnligne> getListeDonneeEnligne() {
        return listeDonneeEnligne;
    }

    public void setListeDonneeEnligne(Vector<PhotoDonneeEnligne> listeDonneeEnligne) {
        this.listeDonneeEnligne = listeDonneeEnligne;
    }

    @Override
    public String toString() {
        return "PhotoServeur{" + "listeSignaturesSupprimees=" + listeSignaturesSupprimees + ", listeManifestes=" + listeManifestes + ", listeRubriques=" + listeDonneeEnligne + '}';
    }
}
