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
public class PhotoSuppressionLocale {
    public String signature;
    public String dossier;
    public int idEcole;

    public PhotoSuppressionLocale() {
    }

    public PhotoSuppressionLocale(String signature, String dossier, int idEcole) {
        this.signature = signature;
        this.dossier = dossier;
        this.idEcole = idEcole;
    }

    public int getIdEcole() {
        return idEcole;
    }

    public void setIdEcole(int idEcole) {
        this.idEcole = idEcole;
    }

    

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getDossier() {
        return dossier;
    }

    public void setDossier(String dossier) {
        this.dossier = dossier;
    }

    @Override
    public String toString() {
        return "PhotoSuppressionLocale{" + "signature=" + signature + ", dossier=" + dossier + ", idEcole=" + idEcole + '}';
    }
}
