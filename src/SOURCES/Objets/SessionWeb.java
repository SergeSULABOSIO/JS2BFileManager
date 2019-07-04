/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.Objets;

import java.util.Date;

/**
 *
 * @author HP Pavilion
 */
public class SessionWeb {
    public Entreprise entreprise;
    public Utilisateur utilisateur;
    public Paiement paiement;

    public SessionWeb() {
        
    }

    public SessionWeb(Entreprise entreprise, Utilisateur utilisateur, Paiement paiement) {
        this.entreprise = entreprise;
        this.utilisateur = utilisateur;
        this.paiement = paiement;
    }

    public Entreprise getEntreprise() {
        return entreprise;
    }

    public void setEntreprise(Entreprise entreprise) {
        this.entreprise = entreprise;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public Paiement getPaiement() {
        return paiement;
    }

    public void setPaiement(Paiement paiement) {
        this.paiement = paiement;
    }

    @Override
    public String toString() {
        return "SessionWeb{" + "entreprise=" + entreprise + ", utilisateur=" + utilisateur + ", paiement=" + paiement + '}';
    }
}


















