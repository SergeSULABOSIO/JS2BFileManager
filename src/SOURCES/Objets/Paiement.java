/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.Objets;

import SOURCES.Utilitaires.UtilFileManager;
import java.util.Date;

/**
 *
 * @author user
 */
public class Paiement {

    public int id;
    public int idEntreprise;
    public int idUtilisateur;
    public int plan;                  //-1 = Ofelé	 0 = Plan A ($ 100 pour 6 mois) 	 1= Plan B ($180 pour 12 mois) 	 2 = Plan C ($ 340 pour 24 mois) 
    public int canal;                 //0 = MPESA	 1=Orange Money 
    public String reference;
    public String dateReception;
    public double montant;
    public String dateActivation;
    public String dateExpiration;

    public Paiement() {
        
    }

    public Paiement(int id, int idEntreprise, int idUtilisateur, int plan, int canal, String reference, String dateReception, double montant, String dateActivation, String dateExpiration) {
        this.id = id;
        this.idEntreprise = idEntreprise;
        this.idUtilisateur = idUtilisateur;
        this.plan = plan;
        this.canal = canal;
        this.reference = reference;
        this.dateReception = dateReception;
        this.montant = montant;
        this.dateActivation = dateActivation;
        this.dateExpiration = dateExpiration;
    }

    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdEntreprise() {
        return idEntreprise;
    }

    public void setIdEntreprise(int idEntreprise) {
        this.idEntreprise = idEntreprise;
    }

    public int getIdUtilisateur() {
        return idUtilisateur;
    }

    public void setIdUtilisateur(int idUtilisateur) {
        this.idUtilisateur = idUtilisateur;
    }

    public int getPlan() {
        return plan;
    }

    public void setPlan(int plan) {
        this.plan = plan;
    }

    public int getCanal() {
        return canal;
    }

    public void setCanal(int canal) {
        this.canal = canal;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getDateReception() {
        return dateReception;
    }

    public void setDateReception(String dateReception) {
        this.dateReception = dateReception;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public String getDateActivation() {
        return dateActivation;
    }

    public void setDateActivation(String dateActivation) {
        this.dateActivation = dateActivation;
    }
    
    public String getDateExpiration() {
        return dateExpiration;
    }

    public void setDateExpiration(String dateExpiration) {
        this.dateExpiration = dateExpiration;
    }
    
    @Override
    public String toString() {
        return "Paiement{" + "id=" + id + ", idEntreprise=" + idEntreprise + ", idUtilisateur=" + idUtilisateur + ", plan=" + plan + ", canal=" + canal + ", reference=" + reference + ", dateReception=" + dateReception + ", montant=" + montant + ", dateActivation=" + dateActivation + ", dateExpiration=" + dateExpiration + '}';
    }
}




























