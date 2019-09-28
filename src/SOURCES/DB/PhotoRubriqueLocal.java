/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.DB;

import java.io.File;
import java.util.Vector;

/**
 *
 * @author HP Pavilion
 */
public class PhotoRubriqueLocal {
    private String nom;
    private Class classe;
    private Vector<File> contenus;

    public PhotoRubriqueLocal(Class classe, String nom) {
        this.classe = classe;
        this.nom = nom;
        this.contenus = new Vector<File>();
    }
    
    public PhotoRubriqueLocal() {
        this.nom = "";
        this.contenus = new Vector<File>();
    }
    
    public void ajouterContenu(File contenu){
        if(!contenus.contains(contenu)){
            contenus.add(contenu);
        }
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Vector<File> getContenus() {
        return contenus;
    }

    public void setContenus(Vector<File> contenus) {
        this.contenus = contenus;
    }

    public Class getClasse() {
        return classe;
    }

    public void setClasse(Class classe) {
        this.classe = classe;
    }

    @Override
    public String toString() {
        return "PhotoRubriqueLocal{" + "nom=" + nom + ", classe=" + classe + ", contenus=" + contenus + '}';
    }
}
