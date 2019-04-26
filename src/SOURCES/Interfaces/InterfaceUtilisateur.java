/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.Interfaces;

/**
 *
 * @author user
 */

public interface InterfaceUtilisateur {
    //Conatantes - DROIT D'ACCES
    public static final int DROIT_PAS_ACCES = 0;
    public static final int DROIT_VISUALISER = 1;
    public static final int DROIT_CONTROLER = 2;
    //Conatantes - TYPE
    public static final int TYPE_ADMIN = 0;
    public static final int TYPE_INVITE = 1;
    //Conatantes - BETA
    public static final int BETA_EXISTANT = 0;
    public static final int BETA_MODIFIE = 1;
    public static final int BETA_NOUVEAU = 2;
    
    //les getters
    public abstract int getId();
    public abstract int getIdEntreprise();
    public abstract String getEmail();
    public abstract String getMotDePasse();
    public abstract int getType();
    public abstract long getSignature();
    public abstract String getNom();
    public abstract String getPostnom();
    public abstract String getPrenom();
    //les droit d'access aux rubriques existantes
    public abstract int getDroitLitige();
    public abstract int getDroitExercice();
    public abstract int getDroitFacture();
    public abstract int getDroitInscription();
    public abstract int getDroitPaie();
    public abstract int getDroitTresorerie();
    public abstract int getDroitUtilisateur();
    public abstract int getBeta();  // 0 = Existant, 1 =  Modifié, 2 = Nouveau
    
    
    //Les setters
    public void setId(int id);
    public void setIdEntreprise(int idEntreprise);
    public void setEmail(String email);
    public void setMotDePasse(String motdepasse);
    public void setType(int typeUtilisateur);
    public void setSignature(long signature);
    public void setNom(String nom);
    public void setPostnom(String postnom);
    public void setPrenom(String prenom);
    //les droit d'access aux rubriques existantes
    public void setDroitLitige(int droit);
    public void setDroitExercice(int droit);
    public void setDroitFacture(int droit);
    public void setDroitInscription(int droit);
    public void setDroitPaie(int droit);
    public void setDroitTresorerie(int droit);
    public void setDroitUtilisateur(int droit);
    public void setBeta(int newbeta);  // 0 = Existant, 1 =  Modifié, 2 = Nouveau
}
