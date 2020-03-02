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
public class EnregistrementServeur {
    public String dossier;
    public int id;
    public long lastModified;
    public Object donnee;

    public EnregistrementServeur() {
    }

    public EnregistrementServeur(String dossier, int id, long lastModified, Object donnee) {
        this.dossier = dossier;
        this.id = id;
        this.lastModified = lastModified;
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

    public Object getDonnee() {
        return donnee;
    }

    public void setDonnee(Object donnee) {
        this.donnee = donnee;
    }

    @Override
    public String toString() {
        return "EnregistrementServeur{" + "dossier=" + dossier + ", id=" + id + ", lastModified=" + lastModified + ", donnee=" + donnee + '}';
    }

    
}
