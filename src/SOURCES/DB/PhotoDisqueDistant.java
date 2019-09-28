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
public class PhotoDisqueDistant {
    private Vector<PhotoRubriqueDistante> rubriques;

    public PhotoDisqueDistant(Vector<PhotoRubriqueDistante> rubriques) {
        this.rubriques = rubriques;
    }
    
    public PhotoDisqueDistant() {
        this.rubriques = new Vector<PhotoRubriqueDistante>();
    }
    
    public void ajouterRubrique(PhotoRubriqueDistante rubrique){
        if(!rubriques.contains(rubrique)){
            rubriques.add(rubrique);
        }
    }

    public Vector<PhotoRubriqueDistante> getRubriques() {
        return rubriques;
    }

    public void setRubriques(Vector<PhotoRubriqueDistante> rubriques) {
        this.rubriques = rubriques;
    }
    
    public StatusElement comparer(String nomRubrique, File el) {
        StatusElement stat = new StatusElement();
        stat.setIsNew(true);
        stat.setIsRecent(true);
        for (PhotoRubriqueDistante rd : rubriques) {
            if ((rd.getNom().replaceFirst("BACKUP_", "")).equals(nomRubrique)) {
                for (ElementDistant elementDistant : rd.getContenus()) {
                    if ((elementDistant.getId()+"").equals(el.getName())) {
                        stat.setIsNew(false);
                        stat.setIsRecent((new Date(el.lastModified())).after(new Date(elementDistant.lastModified())));
                        return stat;
                    }
                }
            }
        }
        return stat;
    }

    @Override
    public String toString() {
        return "PhotoLocal{" + "rubriques=" + rubriques + '}';
    }
}























