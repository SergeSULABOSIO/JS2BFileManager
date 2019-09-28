/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.Objets;

/**
 *
 * @author HP Pavilion
 */
public class Dossier {
    private String nom;
    private Class classe;

    public Dossier(String nom, Class classe) {
        this.nom = nom;
        this.classe = classe;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Class getClasse() {
        return classe;
    }

    public void setClasse(Class classe) {
        this.classe = classe;
    }

    @Override
    public String toString() {
        return "Dossier{" + "nom=" + nom + ", classe=" + classe + '}';
    }
    
    
    
}
