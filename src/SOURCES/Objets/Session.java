/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.Objets;

import Source.Objet.Entreprise;
import Source.Objet.Utilisateur;
import java.util.Date;

/**
 *
 * @author HP Pavilion
 */
public class Session {
    public Entreprise entreprise;
    public Utilisateur utilisateur;
    public PaiementLicence paiement;
    public String token;
    public Date derniereConnexion;
    public final static String fichierSession = "SESSION.man";

    public Session() {
        
    }

    public Session(Entreprise entreprise, Utilisateur utilisateur, PaiementLicence paiement, String token, Date derniereConnexion) {
        this.entreprise = entreprise;
        this.utilisateur = utilisateur;
        this.paiement = paiement;
        this.token = token;
        this.derniereConnexion = derniereConnexion;
    }

    public Entreprise getEntreprise() {
        return entreprise;
    }

    public void setEntreprise(Entreprise entreprise) {
        this.entreprise = entreprise;
    }

    public PaiementLicence getPaiement() {
        return paiement;
    }

    public void setPaiement(PaiementLicence paiement) {
        this.paiement = paiement;
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
        return "Session{" + "entreprise=" + entreprise + ", utilisateur=" + utilisateur + ", paiement=" + paiement + ", token=" + token + ", derniereConnexion=" + derniereConnexion + '}';
    }
}

















