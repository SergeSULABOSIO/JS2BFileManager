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

    public SessionWeb() {
        
    }

    public SessionWeb(Entreprise entreprise, Utilisateur utilisateur) {
        this.entreprise = entreprise;
        this.utilisateur = utilisateur;
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

    @Override
    public String toString() {
        return "SessionWeb{" + "entreprise=" + entreprise + ", utilisateur=" + utilisateur + '}';
    }
}










