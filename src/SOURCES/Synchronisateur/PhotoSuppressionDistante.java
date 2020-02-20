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
public class PhotoSuppressionDistante {
    public String signature;
    public String dossier;
    public int idEcole;
    public int idExercice;

    public PhotoSuppressionDistante() {
    }

    public PhotoSuppressionDistante(String signature, String dossier, int idEcole, int idExercice) {
        this.signature = signature;
        this.dossier = dossier;
        this.idEcole = idEcole;
        this.idExercice = idExercice;
    }

    public int getIdExercice() {
        return idExercice;
    }

    public void setIdExercice(int idExercice) {
        this.idExercice = idExercice;
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
        return "PhotoSuppressionDistante{" + "signature=" + signature + ", dossier=" + dossier + ", idEcole=" + idEcole + ", idExercice=" + idExercice + '}';
    }
}
