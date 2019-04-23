/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TEST;

import SOURCES.Objets.FileManager;
import java.util.Date;
import java.util.Vector;

/**
 *
 * @author user
 */
public class Principale {
    public static void main(String[] a){
        /*
        XX_Utilisateur Uinput = new XX_Utilisateur(1, 1, "SULA", "BOSIO", "Serge", "sulabosiog@gmail.com", "sulabosio", InterfaceUtilisateur.TYPE_ADMIN, (new Date().getTime()), InterfaceUtilisateur.DROIT_CONTROLER, InterfaceUtilisateur.DROIT_CONTROLER, InterfaceUtilisateur.DROIT_CONTROLER, InterfaceUtilisateur.DROIT_CONTROLER, InterfaceUtilisateur.DROIT_CONTROLER, InterfaceUtilisateur.DROIT_CONTROLER, InterfaceUtilisateur.DROIT_CONTROLER, InterfaceUtilisateur.BETA_EXISTANT);
        System.out.println("Input - Noms: " + Uinput.getNom() + " " + Uinput.getPostnom() + " " + Uinput.getPrenom());
        boolean rep = FileManager.ecrireFichier(Uinput);
        System.out.println("Rep: " + rep);
        
        XX_Utilisateur Uouput = FileManager.lireFichier();
        System.out.println("Output - Noms: " + Uouput.getNom() + " " + Uouput.getPostnom() + " " + Uouput.getPrenom());
        */
        
        FileManager fm = new FileManager("dossierTest.js2b");
        
        Vector<LiaisonClasseFrais> liclasse = new Vector<>();
        liclasse.add(new LiaisonClasseFrais(1, "G1", 10));
        liclasse.add(new LiaisonClasseFrais(2, "G2", 8));
        liclasse.add(new LiaisonClasseFrais(3, "G3", 5));
        liclasse.add(new LiaisonClasseFrais(4, "L1", 0));
        
        Vector<LiaisonPeriodeFrais> liperiode = new Vector<>();
        liperiode.add(new LiaisonPeriodeFrais(1, "1ère Trimestre", 50));
        liperiode.add(new LiaisonPeriodeFrais(2, "2ème Trimestre", 50));
        
        XX_Frais Uinput = new XX_Frais(12, 1, 1, 1, 1, (new Date().getTime()), "INSCRIPTION", "$", 0, liclasse, liperiode, 100, InterfaceFrais.BETA_EXISTANT);
        System.out.println("Input - Noms: " + Uinput.getNom() + " " + Uinput.getMontantDefaut()+" "+Uinput.getMonnaie());
        boolean rep = fm.ecrire(Uinput);
        System.out.println("Rep: " + rep);
        
        XX_Frais Uouput = (XX_Frais)fm.lire(XX_Frais.class);
        System.out.println("Input - Noms: " + Uinput.getNom() + " " + Uinput.getMontantDefaut()+" "+Uinput.getMonnaie());
        System.out.println("Liaison - classes:");
        for(LiaisonClasseFrais lc: Uouput.getLiaisonsClasses()){
            System.out.println(" - " + lc.getNomClasse());
        }
        System.out.println("Liaison - périodes:");
        for(LiaisonPeriodeFrais lp: Uouput.getLiaisonsPeriodes()){
            System.out.println(" - " + lp.getNomPeriode());
        }
        
    }
    
}
























































































