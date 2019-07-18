/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.Objets;

import Source.Objet.Entreprise;
import Source.Objet.Utilisateur;


/**
 *
 * @author HP Pavilion
 */
public class SessionWeb {
    public Entreprise entreprise;
    public Utilisateur utilisateur;
    public PaiementLicence paiement;

    public SessionWeb() {
        
    }

    public SessionWeb(Entreprise entreprise, Utilisateur utilisateur, PaiementLicence paiement) {
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

    public PaiementLicence getPaiement() {
        return paiement;
    }

    public void setPaiement(PaiementLicence paiement) {
        this.paiement = paiement;
    }

    @Override
    public String toString() {
        return "SessionWeb{" + "entreprise=" + entreprise + ", utilisateur=" + utilisateur + ", paiement=" + paiement + '}';
    }
}


















