/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TEST;

import TEST.LiaisonClasseFrais;
import TEST.LiaisonPeriodeFrais;
import java.util.Vector;

/**
 *
 * @author HP Pavilion
 */
public interface InterfaceFrais {
    //Conatantes - BETA
    public static final int BETA_EXISTANT = 0;
    public static final int BETA_MODIFIE = 1;
    public static final int BETA_NOUVEAU = 2;
    
    public abstract int getId();
    public abstract int getIdEntreprise();
    public abstract int getIdUtilisateur();
    public abstract int getIdExercice();
    public abstract int getIdMonnaie();
    public abstract double getMontantDefaut();
    public abstract String getNom();
    public abstract long getSignatureMonnaie();
    public abstract Vector<LiaisonPeriodeFrais> getLiaisonsPeriodes();
    public abstract LiaisonPeriodeFrais getLiaisonPeriodes(InterfacePeriode periode);
    public abstract Vector<LiaisonClasseFrais> getLiaisonsClasses();
    public abstract LiaisonClasseFrais getLiaisonClasses(InterfaceClasse classe);
    public abstract int getBeta();  // 0 = Existant, 1 =  Modifié, 2 = Nouveau
    
    public abstract void setId(int id);
    public abstract void setIdEntreprise(int idEntreprise);
    public abstract void setIdUtilisateur(int idUtilisateur);
    public abstract void setIdExercice(int idExercice);
    public abstract void setIdMonnaie(int idMonnaie);
    public abstract void setMontantDefaut(double montantDefaut);
    public abstract void setNom(String nom);
    public abstract void setSignatureMonnaie(long signatureMonnaie);
    public abstract void ajouterLiaisonsPeriode(LiaisonPeriodeFrais liaisons);
    public abstract void setLiaisonsPeriodes(Vector<LiaisonPeriodeFrais> liaisons);
    public abstract void setLiaisonsClasses(Vector<LiaisonClasseFrais> liaisons);
    public abstract void ajouterLiaisonsClasse(LiaisonClasseFrais liaison);
    public abstract void viderLiaisonsClasses();
    public abstract void viderLiaisonsPeriodes();
    
    public abstract void setBeta(int newbeta);
}
