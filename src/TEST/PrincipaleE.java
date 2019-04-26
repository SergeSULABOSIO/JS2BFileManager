/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TEST;

import SOURCES.Callback.EcouteurOuverture;
import SOURCES.Callback.EcouteurSuppression;
import SOURCES.Objets.FileManager;
import java.util.Vector;

/**
 *
 * @author user
 */
public class PrincipaleE {

    public static void main(String[] a) {

        FileManager fm = new FileManager();
        String dossierFrais = "ANNEE XX/FRAIS";

        fm.supprimerTout(dossierFrais, new EcouteurSuppression() {
            @Override
            public void onDone(String message, Object[] idsNonSupprimes) {
                
                for (Object obj : idsNonSupprimes) {
                    XX_Utilisateur oUtil = (XX_Utilisateur) obj;
                    System.out.println(" * " + oUtil.toString());
                }
            }

            @Override
            public void onError(String message) {
                System.out.println(message);
            }

            @Override
            public void onProcessing(String message) {
                System.out.println(message);
            }
        });
        
        
        /* 
        boolean rep = fm.supprimer(dossierFrais, 112);
        System.out.println("suppression: " + rep);
         */
        /*
        Ouverture de tous les elemensts du dossier
         */
        System.out.println("Taile: " + fm.getTaille(dossierFrais) + ", dossier " + dossierFrais);
        System.out.println("Liste:");
        fm.ouvrirTout(0, XX_Utilisateur.class, dossierFrais, new EcouteurOuverture() {
            @Override
            public void onDone(String message, Vector data) {
                for (Object obj : data) {
                    XX_Utilisateur oUtil = (XX_Utilisateur) obj;
                    System.out.println(" * " + oUtil.toString());
                }
            }

            @Override
            public void onError(String message) {
                System.out.println(message);
            }

            @Override
            public void onProcessing(String message) {
                System.out.println(message);
            }
        });

        /*
 Lister tous les élements du 

        String[] contenu = fm.getContenusDossier(dossierFrais);
        for (String elmt : contenu) {
            System.out.println(" * " + elmt);
        }
         */
 /*
            1. Enregistrement d'un Objet et attribution automatique de son ID
         
 
        XX_Utilisateur Uinput = new XX_Utilisateur(-1, 1, "SULA", "BOSIO", "Serge", "sulabosiog@gmail.com", "sulabosio", InterfaceUtilisateur.TYPE_ADMIN, (new Date().getTime()), InterfaceUtilisateur.DROIT_CONTROLER, InterfaceUtilisateur.DROIT_CONTROLER, InterfaceUtilisateur.DROIT_CONTROLER, InterfaceUtilisateur.DROIT_CONTROLER, InterfaceUtilisateur.DROIT_CONTROLER, InterfaceUtilisateur.DROIT_CONTROLER, InterfaceUtilisateur.DROIT_CONTROLER, InterfaceUtilisateur.BETA_EXISTANT);

        fm.enregistrer(Uinput, dossierFrais, new SaveListener() {
            @Override
            public void onDone(String message) {
                System.out.println(message);
                //3. Ouvrture d'un enregistrement unique à partir de son ID
                System.out.println("Objets enregistrés:");
                XX_Utilisateur Uoutput = (XX_Utilisateur) fm.ouvrir(XX_Utilisateur.class, dossierFrais, Uinput.getId());
                System.out.println(" * " + Uoutput.toString());
            }

            @Override
            public void onError(String message) {
                System.out.println(message);
            }

            @Override
            public void onProcessing(String message) {
                System.out.println(message);
            }
        });
         */
 /*
            2. Enregistrement d'une liste d'Objets et attribution automatique des leurs IDs
         
        Vector<XX_Utilisateur> Uinputs = new Vector();
        Uinputs.add(new XX_Utilisateur(-1, 1, "SULA1", "BOSIO1", "Serge", "sulabosiog@gmail.com", "sulabosio", InterfaceUtilisateur.TYPE_ADMIN, (new Date().getTime()), InterfaceUtilisateur.DROIT_CONTROLER, InterfaceUtilisateur.DROIT_CONTROLER, InterfaceUtilisateur.DROIT_CONTROLER, InterfaceUtilisateur.DROIT_CONTROLER, InterfaceUtilisateur.DROIT_CONTROLER, InterfaceUtilisateur.DROIT_CONTROLER, InterfaceUtilisateur.DROIT_CONTROLER, InterfaceUtilisateur.BETA_EXISTANT));
        Uinputs.add(new XX_Utilisateur(-1, 1, "SULA2", "BOSIO2", "Serge", "sulabosiog@gmail.com", "sulabosio", InterfaceUtilisateur.TYPE_ADMIN, (new Date().getTime()), InterfaceUtilisateur.DROIT_CONTROLER, InterfaceUtilisateur.DROIT_CONTROLER, InterfaceUtilisateur.DROIT_CONTROLER, InterfaceUtilisateur.DROIT_CONTROLER, InterfaceUtilisateur.DROIT_CONTROLER, InterfaceUtilisateur.DROIT_CONTROLER, InterfaceUtilisateur.DROIT_CONTROLER, InterfaceUtilisateur.BETA_EXISTANT));
        Uinputs.add(new XX_Utilisateur(-1, 1, "SULA3", "BOSIO3", "Serge", "sulabosiog@gmail.com", "sulabosio", InterfaceUtilisateur.TYPE_ADMIN, (new Date().getTime()), InterfaceUtilisateur.DROIT_CONTROLER, InterfaceUtilisateur.DROIT_CONTROLER, InterfaceUtilisateur.DROIT_CONTROLER, InterfaceUtilisateur.DROIT_CONTROLER, InterfaceUtilisateur.DROIT_CONTROLER, InterfaceUtilisateur.DROIT_CONTROLER, InterfaceUtilisateur.DROIT_CONTROLER, InterfaceUtilisateur.BETA_EXISTANT));
        
        fm.enregistrer(Uinputs, dossierFrais, new FileManagerListener() {
            @Override
            public void onDone(String message) {
                System.out.println(message);
                //3. Ouvrture d'un enregistrement unique à partir de son ID
                System.out.println("Objets enregistrés:");
                for (XX_Utilisateur Uinput : Uinputs) {
                    XX_Utilisateur Uoutput = (XX_Utilisateur) fm.ouvrir(XX_Utilisateur.class, dossierFrais, Uinput.getId());
                    System.out.println(" * " + Uoutput.toString());
                }
            }

            @Override
            public void onError(String message) {
                System.out.println(message);
            }

            @Override
            public void onProcessing(String message) {
                System.out.println(message);
            }
        });
         */
    }

}










