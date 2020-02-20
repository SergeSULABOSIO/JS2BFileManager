/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.Synchronisateur;

/**
 *
 * @author HP Pavilion
 */
public class PhotoManifesteServeur {
    public int id;
    public int idEntreprise;
    public int idUtilisateur;
    public String dossier;
    public int dernierID;
    public long dateEnregistrement;

    public PhotoManifesteServeur() {
    }

    public PhotoManifesteServeur(int id, int idEntreprise, int idUtilisateur, String dossier, int dernierID, long dateEnregistrement) {
        this.id = id;
        this.idEntreprise = idEntreprise;
        this.idUtilisateur = idUtilisateur;
        this.dossier = dossier;
        this.dernierID = dernierID;
        this.dateEnregistrement = dateEnregistrement;
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

    public String getDossier() {
        return dossier;
    }

    public void setDossier(String dossier) {
        this.dossier = dossier;
    }

    public int getDernierID() {
        return dernierID;
    }

    public void setDernierID(int dernierID) {
        this.dernierID = dernierID;
    }

    public long getDateEnregistrement() {
        return dateEnregistrement;
    }

    public void setDateEnregistrement(long dateEnregistrement) {
        this.dateEnregistrement = dateEnregistrement;
    }

    @Override
    public String toString() {
        return "ManifesteDistant{" + "id=" + id + ", idEntreprise=" + idEntreprise + ", idUtilisateur=" + idUtilisateur + ", dossier=" + dossier + ", dernierID=" + dernierID + ", dateEnregistrement=" + dateEnregistrement + '}';
    }
    
}
