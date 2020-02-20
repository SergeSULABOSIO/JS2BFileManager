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
public class PhotoDonneeEnligne {
    public int id;
    public long lastModified;
    public String dossier;
    public int idEntreprise;
    public int idExercice;

    public PhotoDonneeEnligne() {
    }

    public PhotoDonneeEnligne(int id, long lastModified, String dossier, int idEntreprise, int idExercice) {
        this.id = id;
        this.lastModified = lastModified;
        this.dossier = dossier;
        this.idEntreprise = idEntreprise;
        this.idExercice = idExercice;
    }

    public int getIdExercice() {
        return idExercice;
    }

    public void setIdExercice(int idExercice) {
        this.idExercice = idExercice;
    }

    

    public int getIdEntreprise() {
        return idEntreprise;
    }

    public void setIdEntreprise(int idEntreprise) {
        this.idEntreprise = idEntreprise;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getLastModified() {
        return lastModified;
    }

    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }

    public String getDossier() {
        return dossier;
    }

    public void setDossier(String dossier) {
        this.dossier = dossier;
    }

    @Override
    public String toString() {
        return "PhotoDonneeEnligne{" + "id=" + id + ", lastModified=" + lastModified + ", dossier=" + dossier + ", idEntreprise=" + idEntreprise + ", idExercice=" + idExercice + '}';
    }
}
