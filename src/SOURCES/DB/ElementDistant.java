/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.DB;

import java.util.Date;

/**
 *
 * @author HP Pavilion
 */
public class ElementDistant {
    private int id;
    private int idEntreprise;
    private int idExercice;
    private long lastModified;

    public ElementDistant(int id, int idEntreprise, int idExercice, long lastModified) {
        this.id = id;
        this.idEntreprise = idEntreprise;
        this.idExercice = idExercice;
        this.lastModified = lastModified;
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

    public int getIdExercice() {
        return idExercice;
    }

    public void setIdExercice(int idExercice) {
        this.idExercice = idExercice;
    }

    public long lastModified() {
        return lastModified;
    }

    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }

    @Override
    public String toString() {
        return "ElementDistant{" + "id=" + id + ", idEntreprise=" + idEntreprise + ", idExercice=" + idExercice + ", lastModified=" + new Date(lastModified) + '}';
    }
}


