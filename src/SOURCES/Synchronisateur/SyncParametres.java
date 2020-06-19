/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.Synchronisateur;

import Source.Objet.UtilObjet;

/**
 *
 * @author HP Pavilion
 */
public class SyncParametres {
    public int idEcole;
    public int idExercice;
    public String motDePasse;
    public String email;
    public String adresseServeur = UtilObjet.pageWeb + "/mon-compte/sync/processeur.php"; //"www.visiterlardc.com/s2b/sync/processeur.php";

    public SyncParametres() {
    }

    public SyncParametres(int idEcole, int idExercice, String motDePasse, String email) {
        this.idEcole = idEcole;
        this.idExercice = idExercice;
        this.motDePasse = motDePasse;
        this.email = email;
        adresseServeur = UtilObjet.pageWeb + "/mon-compte/sync/processeur.php";
    }

    public int getIdEcole() {
        return idEcole;
    }

    public void setIdEcole(int idEcole) {
        this.idEcole = idEcole;
    }

    public int getIdExercice() {
        return idExercice;
    }

    public void setIdExercice(int idExercice) {
        this.idExercice = idExercice;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAdresseServeur() {
        return adresseServeur;
    }

    public void setAdresseServeur(String adresseServeur) {
        this.adresseServeur = adresseServeur;
    }
    
    public String getTexte(){
        return "&idEcole=" + idEcole + "&motDePasse=" + motDePasse + "&email=" + email + "&idExercice=" + idExercice;
    }
    
}
