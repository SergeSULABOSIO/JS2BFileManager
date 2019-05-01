/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.Objets;

import SOURCES.Callback.EcouteurLoginServeur;
import SOURCES.Callback.EcouteurLongin;
import SOURCES.Callback.EcouteurOuverture;
import SOURCES.Callback.EcouteurStandard;
import SOURCES.Callback.EcouteurSuppression;
import SOURCES.Interfaces.InterfaceUtilisateur;
import SOURCES.Utilitaires.Util;
import java.io.File;
import static java.lang.Thread.sleep;
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
    private Session session = null;
    private String racine = "DataJ2BFees";

    public FileManager() {

    }

    private void loginToServer(Thread processus, String idEcole, String userPassWord, EcouteurLoginServeur ecouteurLoginServeur) {
        try {
            if (ecouteurLoginServeur != null) {
                ecouteurLoginServeur.onProcessing("Connexion au serveur");
            }
            if (processus != null) {
                processus.sleep(100);
            }
            Entreprise entreprise = new Entreprise(10, "ECOLE CARTESIENNE DE KINSHASA", "Limeté - Kinshasa/RDC", "+243844803514", "info@cartesien.org", "www.cartesien.org", "EquityBank RDC", "CARTESIEN DE KINSHASA", "0123654100001248", "IBAN0145400", "WFTCDKIN", "logo.png", "RCCM00BT45", "ID00145", "IP4551220");
            Utilisateur utilisateur = new Utilisateur(1, entreprise.getId(), "sulabosiog@gmail.com", "abc", InterfaceUtilisateur.TYPE_ADMIN, new Date().getTime(), "SULA", "BOSIO", "Serge", InterfaceUtilisateur.DROIT_CONTROLER, InterfaceUtilisateur.DROIT_CONTROLER, InterfaceUtilisateur.DROIT_CONTROLER, InterfaceUtilisateur.DROIT_CONTROLER, InterfaceUtilisateur.DROIT_CONTROLER, InterfaceUtilisateur.DROIT_CONTROLER, InterfaceUtilisateur.DROIT_CONTROLER, InterfaceUtilisateur.BETA_EXISTANT);
            
            ecouteurLoginServeur.onDone("Connexion reussie.", entreprise, utilisateur);
        } catch (Exception e) {
            e.printStackTrace();
            if (ecouteurLoginServeur != null) {
                ecouteurLoginServeur.onError("Serveur introuvable.");
            }
        }
    }

    private void initSession(Thread processus, String idEcole, String motDePasse, EcouteurLongin ecouteurLongin) {
        try {
            if (ecouteurLongin != null) {
                ecouteurLongin.onProcessing("Connexion au serveur...");
            }
            processus.sleep(1000);
            loginToServer(processus, idEcole, motDePasse, new EcouteurLoginServeur() {
                @Override
                public void onDone(String message, Entreprise entreprise, Utilisateur utilisateur) {
                    if (Integer.parseInt(idEcole.trim()) == entreprise.getId() && motDePasse.trim().equals(utilisateur.getMotDePasse().trim())) {
                        if (ecouteurLongin != null) {
                            ecouteurLongin.onProcessing("Chargement des données...");
                        }
                        Date dateConnexion = new Date();
                        session = new Session(entreprise, utilisateur, dateConnexion.getTime() + "", dateConnexion);

                        //Enregistrement de la session dans le disque local
                        if (ecouteurLongin != null) {
                            ecouteurLongin.onProcessing("Initialisation de la session...");
                        }

                        String sessionFolder = racine;
                        creerDossierSiNExistePas(sessionFolder);
                        if (new File(sessionFolder).exists() == true) {
                            boolean isSessionCreated = ecrire(sessionFolder + "/" + Session.fichierSession, session);
                            if (isSessionCreated == true) {
                                if (ecouteurLongin != null) {
                                    ecouteurLongin.onConnected(message, session);
                                }
                            } else {
                                if (ecouteurLongin != null) {
                                    ecouteurLongin.onEchec("Impossible d'initialiser la session Utilisateur!");
                                }
                            }
                        } else {
                            if (ecouteurLongin != null) {
                                ecouteurLongin.onEchec("sessionFolder introuvable!");
                            }
                        }
                    } else {
                        if (ecouteurLongin != null) {
                            ecouteurLongin.onEchec("Coordonnées incorrectes!");
                        }
                    }
                }

                @Override
                public void onError(String message) {
                    if (ecouteurLongin != null) {
                        ecouteurLongin.onEchec(message);
                    }
                }

                @Override
                public void onProcessing(String message) {
                    if (ecouteurLongin != null) {
                        ecouteurLongin.onProcessing(message);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            if (ecouteurLongin != null) {
                ecouteurLongin.onEchec("Erreur !");
            }
        }
    }

    public void login(String idEcole, String motDePasse, EcouteurLongin ecouteurLongin) {
        new Thread() {
            @Override
            public void run() {
                try {
                    if (motDePasse.trim().length() == 0) {
                        if (ecouteurLongin != null) {
                            ecouteurLongin.onEchec("Désolé, aucun mot de passe.");
                        }
                    }
                    if (idEcole.trim().length() == 0) {
                        if (ecouteurLongin != null) {
                            ecouteurLongin.onEchec("Désolé, aucun ID de l'école.");
                        }
                    }
                    if (ecouteurLongin != null) {
                        ecouteurLongin.onProcessing("Authentification...");
                    }
                    initSession(this, idEcole, motDePasse, ecouteurLongin);
                } catch (Exception e) {
                    e.printStackTrace();
                    if (ecouteurLongin != null) {
                        ecouteurLongin.onEchec("Erreur de connexion.");
                    }
                }
            }

        }.start();
    }

    public void logout(EcouteurStandard ecouteurStandard) {
        new Thread() {
            @Override
            public void run() {
                try {
                    if (ecouteurStandard != null) {
                        ecouteurStandard.onProcessing("Deconnexion...");
                    }
                    sleep(1000);
                    File sessionFile = new File(racine + "/" + Session.fichierSession);
                    boolean sessionDeleted = sessionFile.delete();
                    if (sessionDeleted == true && !sessionFile.exists()) {
                        if (ecouteurStandard != null) {
                            ecouteurStandard.onDone("Déconnecté!");
                        }
                    } else {
                        if (ecouteurStandard != null) {
                            ecouteurStandard.onError("Impossible de supprimer votre session!");
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    if (ecouteurStandard != null) {
                        ecouteurStandard.onError("Erreur!");
                    }
                }
            }

        }.start();
    }

    public void loadSession(EcouteurLongin ecouteurLongin) {
        new Thread() {
            @Override
            public void run() {
                try {
                    //C'est ici que l'on lire le contenu de l'objet Session renregistré
                    //Si l'objet est nul, c'est que le USER S'était déconnecté
                    if (ecouteurLongin != null) {
                        ecouteurLongin.onProcessing("Vérification de la session...");
                    }
                    sleep(1000);
                    session = (Session) Util.lire(racine + "/" + Session.fichierSession, Session.class);
                    if (session != null) {
                        if (ecouteurLongin != null) {
                            ecouteurLongin.onConnected("Connexion reussi!", session);
                        }
                    } else {
                        if (ecouteurLongin != null) {
                            ecouteurLongin.onEchec("Session expirée!");
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    if (ecouteurLongin != null) {
                        ecouteurLongin.onEchec("Erreur de connexion.");
                    }
                }
            }

        }.start();
    }

    private boolean initDataFolder(String table) {
        //Initialisation
        creerDossierSiNExistePas(racine + "/" + session.getEntreprise().getId() + "/" + table);
        if (!(new File(racine+"/"+session.getEntreprise().getId()+"/"+table + "/" + Registre.fichierRegistre)).exists()) {
            reinitialiserRegistre(table);
        }
        chargerRegistreEnMemoire(table);
        return registre != null;
    }

    public boolean reinitialiserRegistre(String table) {
        File ficRegistre = new File(racine+"/"+session.getEntreprise().getId()+"/"+table + "/" + Registre.fichierRegistre);
        return ecrire(ficRegistre.getAbsolutePath(), new Registre(0, new Date()));
    }

    private void chargerRegistreEnMemoire(String table) {
        //System.out.println("Le fichier " + fichierREGISTRE + " existe.");
        registre = (Registre) ouvrir(Registre.class, racine+"/"+session.getEntreprise().getId()+"/"+table + "/" + Registre.fichierRegistre);
    }

    public Registre getRegistre(String table) {
        chargerRegistreEnMemoire(table);
        return registre;
    }

    private void enregistrer_NoThread(Object NewObj, String table, EcouteurStandard ecouteur) {
        try {
            //On doit initialiser le dossier des données avant toute chose 
            boolean canSave = initDataFolder(table);
            if (canSave == true) {
                //En suite on procède à l'enregistrement
                int idNewObj = getIdObjet(NewObj);
                boolean mustIncrement = false;
                if (idNewObj == -1) {
                    setIdToNewObject(NewObj, getIdDisponible(table));
                    idNewObj = getIdObjet(NewObj);
                    mustIncrement = true;
                }
                //System.out.println("id of new Objetc = " + idObj);
                if (ecouteur != null) {
                    ecouteur.onProcessing("Enregistrement...");
                }

                boolean rep = ecrire(racine+"/"+session.getEntreprise().getId() + "/" + table + "/" + idNewObj, NewObj);
                if (rep == true) {
                    registre.incrementer(mustIncrement);
                    saveRegistre(table);
                    chargerRegistreEnMemoire(table);

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

    public String[] getContenusDossier(String table) {
        File dossier = new File(racine + "/" + session.getEntreprise().getId() + "/" + table);
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

    public void enregistrer(int vitesse, Vector NewObjs, String table, EcouteurStandard ecouteur) {
        new Thread() {
            @Override
            public void run() {
                try {
                    int taille = NewObjs.size();
                    int index = 1;
                    for (Object NewO : NewObjs) {
                        sleep(vitesse);
                        enregistrer(NewO, table, null);
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

    public void enregistrer(Object NewObj, String table, EcouteurStandard ecouteur) {
        if (ecouteur == null) {//Ici la méthode parent tourne déjà dans un Thread léger
            enregistrer_NoThread(NewObj, table, ecouteur);
        } else {
            new Thread() {
                @Override
                public void run() {
                    enregistrer_NoThread(NewObj, table, ecouteur);
                }

            }.start();
        }
    }

    private Object ouvrir(Class NomClasse, String fichierSource) {
        return Util.lire(fichierSource, NomClasse);
    }

    public Object ouvrir(Class NomClasse, String table, int idObj) {
        return Util.lire(racine + "/" + session.getEntreprise().getId() + "/" + table + "/" + idObj, NomClasse);
    }

    public boolean supprimer(String table, int idObj) {
        File fichObjet = new File(racine + "/" + session.getEntreprise().getId() + "/" + table + "/" + idObj);
        if (fichObjet.exists()) {
            return fichObjet.delete();
        }
        return false;
    }

    public void supprimerTout(String table, EcouteurSuppression ecouteurSuppression) {
        new Thread() {
            @Override
            public void run() {
                try {
                    String[] tabIds = getContenusDossier(table);
                    deleteGroup(tabIds, table, ecouteurSuppression);
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

    private void deleteGroup(String[] tabIds, String table, EcouteurSuppression ecouteurSuppression) {
        Vector tabIdsNotDeleted = new Vector();
        int index = 1;
        for (String id : tabIds) {
            String status = "[ok]";
            boolean deleted = supprimer(table, Integer.parseInt(id.trim()));
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

    public void ouvrirTout(int vitesseTraiement, Class NomClasse, String table, EcouteurOuverture ouvrirListener) {
        new Thread() {
            @Override
            public void run() {
                try {
                    String[] tabIDs = getContenusDossier(table);
                    Vector data = new Vector();
                    int index = 1;
                    if (tabIDs.length != 0) {
                        for (String ID : tabIDs) {
                            sleep(vitesseTraiement);
                            if (ouvrirListener != null) {
                                ouvrirListener.onProcessing("Chargement encours (" + index + "/" + tabIDs.length + ")...");
                            }
                            Object temp = Util.lire(racine + "/" + session.getEntreprise().getId() + "/" + table + "/" + ID, NomClasse);
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

    private int getIdDisponible(String table) {
        chargerRegistreEnMemoire(table);
        if (registre != null) {
            return registre.getDernierID() + 1;
        } else {
            return -1;
        }
    }

    private void saveRegistre(String table) {
        ecrire(new File(racine + "/"+ session.getEntreprise().getId() + "/" + table + "/" + Registre.fichierRegistre).getAbsolutePath(), registre);
    }

    private boolean ecrire(String chemin, Object obj) {
        return Util.ecrire(chemin, obj);
    }

    private boolean creerDossierSiNExistePas(String cheminComplet) {
        try {
            File dossier = new File(cheminComplet);
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










