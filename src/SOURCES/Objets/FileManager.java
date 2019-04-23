/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.Objets;

import SOURCES.Utilitaires.Util;
import java.io.File;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author user
 */
public class FileManager {

    private String dossier;
    private Registre registre = new Registre(0, new Date());
    private File fichierREGISTRE = null;

    public FileManager(String dossier) {
        this.dossier = dossier;
        init();
    }

    private void init() {
        //Initialisation
        this.fichierREGISTRE = new File(dossier + "/" + registre.getFichierRegistre());
        creerDossier(dossier);
        if (!fichierREGISTRE.exists()) {
            //System.out.println("Le fichier '" + fichierREGISTRE + "' n'existe pas !");
            //System.out.println("Création du fichier " + fichierREGISTRE + "...");
            if (ecrire(fichierREGISTRE.getAbsolutePath(), registre)) {
                //System.out.println(" - Succès !");
            }
        } else {
            updateRegistre();
        }
        //System.out.println("Reg: " + registre.toString());
    }

    private void updateRegistre() {
        //System.out.println("Le fichier " + fichierREGISTRE + " existe.");
        registre = (Registre) lire(fichierREGISTRE.getAbsolutePath(), Registre.class);
    }

    public File getFichierREGISTRE() {
        return fichierREGISTRE;
    }

    public boolean enregistrer(Object obj) {

        try {
            int idObj = getId(obj);
            boolean mustIncrement = false;
            if (idObj == -1) {
                setId(obj, getIdDisponible());
                idObj = getId(obj);
                mustIncrement = true;
            }

            //System.out.println("id of new Objetc = " + idObj);

            boolean rep = ecrire(dossier + "/" + idObj, obj);
            if (rep == true) {
                registre.incrementer(mustIncrement);
                saveRegistre();
                updateRegistre();
                return true; //registre.getDernierID();
            } else {
                return false;
            }

        } catch (Exception ex) {
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    private int getId(Object obj) throws Exception{
        return Integer.parseInt(obj.getClass().getDeclaredField("id").get(obj) + "");
    }
    
    private void setId(Object obj, int newid) throws Exception{
        obj.getClass().getDeclaredField("id").set(obj, newid);
    }

    public int getIdDisponible() {
        updateRegistre();
        return registre.getDernierID() + 1;
    }

    private void saveRegistre() {
        ecrire(fichierREGISTRE.getAbsolutePath(), registre);
    }

    private boolean ecrire(String chemin, Object obj) {
        return Util.ecrire(chemin, obj);
    }

    private Object lire(String chemin, Class NomClasse) {
        return Util.lire(chemin, NomClasse);
    }

    public boolean creerDossier(String nomEtChemin) {
        try {
            File dossier = new File(nomEtChemin);
            if (!dossier.exists()) {
                return dossier.mkdirs();
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
