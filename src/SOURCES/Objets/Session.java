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
public class Session {
    private Entreprise entreprise;
    private Utilisateur utilisateur;
    private String token;
    private Date derniereConnexion;

    public Session() {
        
    }

    public Session(Entreprise entreprise, Utilisateur utilisateur, String token, Date derniereConnexion) {
        this.entreprise = entreprise;
        this.utilisateur = utilisateur;
        this.token = token;
        this.derniereConnexion = derniereConnexion;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getDerniereConnexion() {
        return derniereConnexion;
    }

    public void setDerniereConnexion(Date derniereConnexion) {
        this.derniereConnexion = derniereConnexion;
    }

    @Override
    public String toString() {
        return "Session{" + "entreprise=" + entreprise + ", utilisateur=" + utilisateur + ", token=" + token + ", derniereConnexion=" + derniereConnexion + '}';
    }
}
