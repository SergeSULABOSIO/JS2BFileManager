/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.Synchronisateur;

/**
 *
 * @author user
 */
public class PhotoManifestePC {
    private int idEntreprise;
    private int idUtilisateur;
    private String dossier;
    private int dernierID;
    private long dateEnregistrement;

    public PhotoManifestePC() {
    }
    
    

    public PhotoManifestePC(int idEntreprise, int idUtilisateur, String dossier, int dernierID, long dateEnregistrement) {
        this.idEntreprise = idEntreprise;
        this.idUtilisateur = idUtilisateur;
        this.dossier = dossier;
        this.dernierID = dernierID;
        this.dateEnregistrement = dateEnregistrement;
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
        return "PhotoManifestePC{" + "idEntreprise=" + idEntreprise + ", idUtilisateur=" + idUtilisateur + ", dossier=" + dossier + ", dernierID=" + dernierID + ", dateEnregistrement=" + dateEnregistrement + '}';
    }
}















