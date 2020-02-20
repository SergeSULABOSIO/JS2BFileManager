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
public class PhotoDonneePC {
    public int id;
    public long lastModified;
    public String dossier;
    public Object donnee;
    public int idEntreprise;

    public PhotoDonneePC() {
    }

    public PhotoDonneePC(int id, long lastModified, String dossier, Object donnee, int idEntreprise) {
        this.id = id;
        this.lastModified = lastModified;
        this.dossier = dossier;
        this.donnee = donnee;
        this.idEntreprise = idEntreprise;
    }

    public int getIdEntreprise() {
        return idEntreprise;
    }

    public void setIdEntreprise(int idEntreprise) {
        this.idEntreprise = idEntreprise;
    }
    
    public Object getDonnee() {
        return donnee;
    }

    public void setDonnee(Object donnee) {
        this.donnee = donnee;
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
        return "PhotoDonneePC{" + "id=" + id + ", lastModified=" + lastModified + ", dossier=" + dossier + ", donnee=" + donnee + ", idEntreprise=" + idEntreprise + '}';
    }
}
