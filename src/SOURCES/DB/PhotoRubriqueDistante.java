/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.DB;


import java.io.File;
import java.util.Date;
import java.util.Vector;

/**
 *
 * @author HP Pavilion
 */
public class PhotoRubriqueDistante {
    private String nom;
    private Vector<ElementDistant> contenus;

    public PhotoRubriqueDistante(String nom, Vector<ElementDistant> contenus) {
        this.nom = nom;
        this.contenus = contenus;
    }
    
    public PhotoRubriqueDistante() {
        this.nom = "";
        this.contenus = new Vector<ElementDistant>();
    }
    
    public void ajouterContenu(ElementDistant contenu){
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

    public Vector<ElementDistant> getContenus() {
        return contenus;
    }

    public void setContenus(Vector<ElementDistant> contenus) {
        this.contenus = contenus;
    }

    @Override
    public String toString() {
        return "Rubrique{" + "nom=" + nom + ", contenus=" + contenus + '}';
    }
}





