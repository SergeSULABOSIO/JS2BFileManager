/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.DB;

import Source.Objet.UtilObjet;
import java.io.File;
import java.util.Date;
import java.util.Vector;

/**
 *
 * @author HP Pavilion
 */
public class PhotoDisqueLocal {

    private Vector<PhotoRubriqueLocal> rubriques;

    public PhotoDisqueLocal(Vector<PhotoRubriqueLocal> rubriques) {
        this.rubriques = rubriques;
    }

    public PhotoDisqueLocal() {
        this.rubriques = new Vector<PhotoRubriqueLocal>();
    }

    public StatusElement comparer(String nomRubrique, ElementDistant ed) {
        StatusElement stat = new StatusElement();
        stat.setIsNew(true);
        stat.setIsRecent(true);
        for (PhotoRubriqueLocal rl : rubriques) {
            if (rl.getNom().equals(nomRubrique)) {
                for (File elementLocal : rl.getContenus()) {
                    if (elementLocal.getName().equals(ed.getId() + "")) {
                        //System.out.println("Local: " + UtilObjet.getDateFrancais(new Date(elementLocal.lastModified())) +", Distant: " + UtilObjet.getDateFrancais(new Date(ed.lastModified())));
                        stat.setIsNew(false);
                        stat.setIsRecent((new Date(ed.lastModified())).after(new Date(elementLocal.lastModified())));
                        return stat;
                    }
                }
            }
        }
        return stat;
    }
    
    public Class getClasse(String nomDossier) {
        for (PhotoRubriqueLocal rl : rubriques) {
            if (rl.getNom().equals(nomDossier)) {
                return rl.getClasse();
            }
        }
        return null;
    }

    public void ajouterRubrique(PhotoRubriqueLocal rubrique) {
        if (!rubriques.contains(rubrique)) {
            rubriques.add(rubrique);
        }
    }

    public Vector<PhotoRubriqueLocal> getRubriques() {
        return rubriques;
    }

    public void setRubriques(Vector<PhotoRubriqueLocal> rubriques) {
        this.rubriques = rubriques;
    }

    @Override
    public String toString() {
        return "PhotoLocal{" + "rubriques=" + rubriques + '}';
    }
}





































