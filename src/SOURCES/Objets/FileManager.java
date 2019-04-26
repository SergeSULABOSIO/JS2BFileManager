/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.Objets;

import SOURCES.Callback.EcouteurOuverture;
import SOURCES.Callback.EcouteurStandard;
import SOURCES.Callback.EcouteurSuppression;
import SOURCES.Utilitaires.Util;
import java.io.File;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author user
 */
public class FileManager {
    
    private Registre registre = new Registre(0, new Date());

    public FileManager() {
        
    }

    private boolean initDataFolder(String dossier) {
        //Initialisation
        creerDossierSiNExistePas(dossier);
        if (!(new File(dossier + "/" + Registre.fichierRegistre)).exists()) {
            reinitialiserRegistre(dossier);
        }
        chargerRegistreEnMemoire(dossier);
        return registre != null;
    }

    public boolean reinitialiserRegistre(String dossier) {
        File ficRegistre = new File(dossier + "/" + Registre.fichierRegistre);
        return ecrire(ficRegistre.getAbsolutePath(), new Registre(0, new Date()));
    }

    private void chargerRegistreEnMemoire(String nomDossier) {
        //System.out.println("Le fichier " + fichierREGISTRE + " existe.");
        registre = (Registre) ouvrir(Registre.class, nomDossier + "/" + Registre.fichierRegistre);
    }

    public Registre getRegistre(String nomDossier) {
        chargerRegistreEnMemoire(nomDossier);
        return registre;
    }

    private void enregistrer_NoThread(Object NewObj, String nomDossier, EcouteurStandard ecouteur) {
        try {
            //On doit initialiser le dossier des données avant toute chose
            boolean canSave = initDataFolder(nomDossier);
            if (canSave == true) {
                //En suite on procède à l'enregistrement
                int idNewObj = getIdObjet(NewObj);
                boolean mustIncrement = false;
                if (idNewObj == -1) {
                    setIdToNewObject(NewObj, getIdDisponible(nomDossier));
                    idNewObj = getIdObjet(NewObj);
                    mustIncrement = true;
                }
                //System.out.println("id of new Objetc = " + idObj);
                if (ecouteur != null) {
                    ecouteur.onProcessing("Enregistrement...");
                }

                boolean rep = ecrire(nomDossier + "/" + idNewObj, NewObj);
                if (rep == true) {
                    registre.incrementer(mustIncrement);
                    saveRegistre(nomDossier);
                    chargerRegistreEnMemoire(nomDossier);

                    if (ecouteur != null) {
                        ecouteur.onDone("Enregistré avec succès.");
                    }
                } else {
                    if (ecouteur != null) {
                        ecouteur.onError("Désolé, erreur!");
                    }
                }
            } else {
                if (ecouteur != null) {
                    ecouteur.onError("Erreur: dossier introuvable!");
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
            if (ecouteur != null) {
                ecouteur.onError("Impossible d'enregistrer.");
            }
        }
    }

    public String[] getContenusDossier(String dossierDestination) {
        File dossier = new File(dossierDestination);
        if (dossier.exists()) {
            if (dossier.isDirectory()) {
                return dossier.list(new FiltreDonnees(".man"));
            }
        }
        return new String[0];
    }

    public int getTaille(String dossierDestination) {
        File dossier = new File(dossierDestination);
        if (dossier.exists()) {
            if (dossier.isDirectory()) {
                return dossier.list(new FiltreDonnees(".man")).length;
            }
        }
        return 0;
    }

    public void enregistrer(int vitesse, Vector NewObjs, String dossierDestination, EcouteurStandard ecouteur) {
        new Thread() {
            @Override
            public void run() {
                try {
                    int taille = NewObjs.size();
                    int index = 1;
                    for (Object NewO : NewObjs) {
                        sleep(vitesse);
                        enregistrer(NewO, dossierDestination, null);
                        if (ecouteur != null) {
                            ecouteur.onProcessing("Enregistrement en cours (" + index + "/" + taille + ")...");
                        }
                        index++;
                    }
                    if (ecouteur != null) {
                        ecouteur.onDone("Enregistré avec succès.");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    if (ecouteur != null) {
                        ecouteur.onError("Erreur ! (" + e.getMessage() + ")");
                    }
                }
            }
        }.start();
    }

    public void enregistrer(Object NewObj, String dossierDestination, EcouteurStandard ecouteur) {
        if (ecouteur == null) {//Ici la méthode parent tourne déjà dans un Thread léger
            enregistrer_NoThread(NewObj, dossierDestination, ecouteur);
        } else {
            new Thread() {
                @Override
                public void run() {
                    enregistrer_NoThread(NewObj, dossierDestination, ecouteur);
                }

            }.start();
        }
    }

    private Object ouvrir(Class NomClasse, String fichierSource) {
        return Util.lire(fichierSource, NomClasse);
    }

    public Object ouvrir(Class NomClasse, String dossierSource, int idObj) {
        return Util.lire(dossierSource + "/" + idObj, NomClasse);
    }

    public boolean supprimer(String dossierSource, int idObj) {
        File fichObjet = new File(dossierSource + "/" + idObj);
        if (fichObjet.exists()) {
            return fichObjet.delete();
        }
        return false;
    }

    public void supprimerTout(String dossier, EcouteurSuppression ecouteurSuppression) {
        new Thread() {
            @Override
            public void run() {
                try {
                    String[] tabIds = getContenusDossier(dossier);
                    deleteGroup(tabIds, dossier, ecouteurSuppression);
                } catch (Exception e) {
                    e.printStackTrace();
                    if (ecouteurSuppression != null) {
                        ecouteurSuppression.onError("Erreur lors de la suppression :" + e.getMessage());
                    }
                }
            }
        }.start();
    }

    public void supprimerTout(String dossier, String[] tabIds, EcouteurSuppression ecouteurSuppression) {
        new Thread() {
            @Override
            public void run() {
                try {
                    if (tabIds != null) {
                        deleteGroup(tabIds, dossier, ecouteurSuppression);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    if (ecouteurSuppression != null) {
                        ecouteurSuppression.onError("Erreur lors de la suppression :" + e.getMessage());
                    }
                }
            }
        }.start();
    }

    private void deleteGroup(String[] tabIds, String dossierSource, EcouteurSuppression ecouteurSuppression) {
        Vector tabIdsNotDeleted = new Vector();
        int index = 1;
        for (String id : tabIds) {
            String status = "[ok]";
            boolean deleted = supprimer(dossierSource, Integer.parseInt(id.trim()));
            if (deleted == false) {
                tabIdsNotDeleted.add(id);
                status = "[Erreur]";
            }
            if (ecouteurSuppression != null) {
                ecouteurSuppression.onProcessing("Suppression en cours (" + index + "/" + tabIds.length + " " + status + ")...");
            }
            index++;
        }
        if (ecouteurSuppression != null) {
            if (tabIds.length != 0) {
                ecouteurSuppression.onDone("Suppression effectué (" + index + "/" + tabIds.length + ").", tabIdsNotDeleted.toArray());
            } else {
                ecouteurSuppression.onDone("Aucun élement à supprimer.", new Object[0]);
            }
        }
    }

    public void ouvrirTout(int vitesseTraiement, Class NomClasse, String dossierSource, EcouteurOuverture ouvrirListener) {
        new Thread() {
            @Override
            public void run() {
                try {
                    String[] tabIDs = getContenusDossier(dossierSource);
                    Vector data = new Vector();
                    int index = 1;
                    if (tabIDs.length != 0) {
                        for (String ID : tabIDs) {
                            sleep(vitesseTraiement);
                            if (ouvrirListener != null) {
                                ouvrirListener.onProcessing("Chargement encours (" + index + "/" + tabIDs.length + ")...");
                            }
                            Object temp = Util.lire(dossierSource + "/" + ID, NomClasse);
                            if (!data.contains(temp)) {
                                data.add(temp);
                            }
                            index++;
                        }

                    }
                    if (ouvrirListener != null) {
                        ouvrirListener.onDone("Données chargées avec succès.", data);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    if (ouvrirListener != null) {
                        ouvrirListener.onProcessing("Erreur lors du chargement: " + e.getMessage());
                    }
                }
            }

        }.start();
    }

    private int getIdObjet(Object obj) throws Exception {
        Field champID = obj.getClass().getDeclaredField("id");
        if (champID != null) {
            return Integer.parseInt(champID.get(obj) + "");
        } else {
            return -1;
        }
    }

    private void setIdToNewObject(Object obj, int newid) throws Exception {
        Field champID = obj.getClass().getDeclaredField("id");
        if (champID != null) {
            champID.set(obj, newid);
        }
    }

    private int getIdDisponible(String dossier) {
        chargerRegistreEnMemoire(dossier);
        if(registre != null){
            return registre.getDernierID() + 1;
        }else{
            return -1;
        }
    }

    private void saveRegistre(String dossier) {
        ecrire(new File(dossier+"/"+Registre.fichierRegistre).getAbsolutePath(), registre);
    }

    private boolean ecrire(String chemin, Object obj) {
        return Util.ecrire(chemin, obj);
    }

    public boolean creerDossierSiNExistePas(String nomEtChemin) {
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







































