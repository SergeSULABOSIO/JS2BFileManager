/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.Objets;

import BASE.ObjetNetWork;
import Callback.CallBackObjetNetWork;
import SOURCES.Callback.CallBackEcouteur;
import SOURCES.Callback.EcouteurLoginServeur;
import SOURCES.Callback.EcouteurLongin;
import SOURCES.Callback.EcouteurOuverture;
import SOURCES.Callback.EcouteurStandard;
import SOURCES.Callback.EcouteurSuppression;
import SOURCES.Utilitaires.Util;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.awt.Desktop;
import java.io.File;
import static java.lang.Thread.sleep;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;

/**
 *
 * @author user
 */
public class FileManager extends ObjetNetWork {

    private Registre registre = new Registre(0, new Date());
    private Session session = null;
    private String racine = "DataJ2BFees";
    private String pref = racine + "/PREF.man";
    private EcouteurFenetre ecouteurFenetre = null;

    public FileManager(String adresseServeur) {
        super(adresseServeur);
    }

    public Session fm_getSession() {
        return session;
    }
    
    public void fm_setEcouteurFenetre(JFrame fenetre){
        ecouteurFenetre = new EcouteurFenetre(fenetre, new CallBackEcouteur() {
            @Override
            public void onChange(Preference preference) {
                //System.out.println("Windows: " + preference.toString());
                ecrire(pref, preference);
            }
        });
        
        Preference savedPref = (Preference)Util.lire(pref, Preference.class);
        if(savedPref != null){
            fenetre.setBounds((int)savedPref.getFenetre_x(), (int)savedPref.getFenetre_y(), (int)savedPref.getFenetre_w(), (int)savedPref.getFenetre_h());
        }
    }
    

    private void loginToServer(Thread processus, String idEcole, String email, String motDePasse, EcouteurLoginServeur ecouteurLoginServeur) {
        try {
            if (ecouteurLoginServeur != null) {
                ecouteurLoginServeur.onProcessing("Connexion au serveur");
            }
            if (processus != null) {
                processus.sleep(100);
            }

            String parametres = "action=" + Util.ACTION_CONNEXION + "&id=" + idEcole + "&motDePasse=" + motDePasse + "&email=" + email;
            POST_CHARGER(parametres, new CallBackObjetNetWork() {
                @Override
                public void onDone(String jsonString) {
                    try {
                        ObjectMapper mapper = new ObjectMapper();
                        SessionWeb sessionWeb = mapper.readValue(jsonString.trim(), SessionWeb.class);
                        //System.out.println("Paiement: " + sessionWeb.getPaiement().toString());
                        if (sessionWeb != null && sessionWeb.getEntreprise() != null && sessionWeb.getUtilisateur() != null) {
                            if (ecouteurLoginServeur != null) {
                                Date today = new Date();
                                Date dateExpiry = Util.convertDatePaiement(sessionWeb.getPaiement().getDateExpiration());
                                if (dateExpiry != null) {
                                    if (today.after(dateExpiry)) {
                                        ecouteurLoginServeur.onError("Votre abonnement vient d'expirer ! Connectez vous à www.visiterlardc.com/s2b");
                                        fm_lancerPageWebAdmin("http://www.visiterlardc.com/s2b");
                                    } else {
                                        ecouteurLoginServeur.onDone("Connexion reussie.", sessionWeb.getEntreprise(), sessionWeb.getUtilisateur(), sessionWeb.getPaiement());
                                    }
                                } else {
                                    ecouteurLoginServeur.onError("Veuillez d'abord payer votre licence depuis www.visiterlardc.com/s2b");
                                    fm_lancerPageWebAdmin("http://www.visiterlardc.com/s2b");
                                }
                            }
                        } else {
                            if (ecouteurLoginServeur != null) {
                                ecouteurLoginServeur.onError("Identifiants non reconnus.");
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        if (ecouteurLoginServeur != null) {
                            ecouteurLoginServeur.onError(e.getMessage());
                        }
                    }

                }

                @Override
                public void onError(String message) {
                    if (ecouteurLoginServeur != null) {
                        ecouteurLoginServeur.onError(message);
                    }
                }

                @Override
                public void onProcessing(String message) {
                    if (ecouteurLoginServeur != null) {
                        ecouteurLoginServeur.onProcessing(message);
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            if (ecouteurLoginServeur != null) {
                ecouteurLoginServeur.onError("Serveur introuvable.");
            }
        }
    }

    public static boolean fm_lancerPageWebAdmin(String chemin) {
        Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
        if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
            try {
                desktop.browse(new URL(chemin).toURI());
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private void initSession(Thread processus, String idEcole, String email, String motDePasse, EcouteurLongin ecouteurLongin) {
        try {
            if (ecouteurLongin != null) {
                ecouteurLongin.onProcessing("Connexion au serveur...(" + getAdresseServeur() + ")");
            }
            processus.sleep(1000);
            loginToServer(processus, idEcole, email, motDePasse, new EcouteurLoginServeur() {
                @Override
                public void onDone(String message, Entreprise entreprise, Utilisateur utilisateur, Paiement paiement) {
                    if (Integer.parseInt(idEcole.trim()) == entreprise.getId() && motDePasse.trim().equals(utilisateur.getMotDePasse().trim())) {
                        if (ecouteurLongin != null) {
                            ecouteurLongin.onProcessing("Chargement des données...");
                        }

                        Date dateConnexion = new Date();
                        session = new Session(entreprise, utilisateur, paiement, dateConnexion.getTime() + "", dateConnexion);

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

    public void fm_login(String idEcole, String email, String motDePasse, EcouteurLongin ecouteurLongin) {
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
                    if (email.trim().length() == 0) {
                        if (ecouteurLongin != null) {
                            ecouteurLongin.onEchec("Désolé, aucun email.");
                        }
                    }
                    if (ecouteurLongin != null) {
                        ecouteurLongin.onProcessing("Authentification...");
                    }
                    initSession(this, idEcole, email, motDePasse, ecouteurLongin);
                } catch (Exception e) {
                    e.printStackTrace();
                    if (ecouteurLongin != null) {
                        ecouteurLongin.onEchec("Erreur de connexion.");
                    }
                }
            }

        }.start();
    }

    public void fm_logout(EcouteurStandard ecouteurStandard) {
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

    public void fm_loadSession(EcouteurLongin ecouteurLongin) {
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
                            Date today = new Date();
                            Date dateExpi = Util.convertDatePaiement(session.getPaiement().getDateExpiration());
                            if (dateExpi != null) {
                                if (today.after(dateExpi)) {
                                    ecouteurLongin.onEchec("Votre abonnement a expiré ! Merci de vous connecter sur votre page d'administration pour acheter une licence.");
                                } else {
                                    ecouteurLongin.onConnected("Connexion reussi!", session);
                                }
                            } else {
                                ecouteurLongin.onEchec("Licence introuvable. Veuillez vous connecter à votre page web d'administration.");
                            }
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
        if (!(new File(racine + "/" + session.getEntreprise().getId() + "/" + table + "/" + Registre.fichierRegistre)).exists()) {
            fm_reinitialiserRegistre(table);
        }
        chargerRegistreEnMemoire(table);
        return registre != null;
    }

    public boolean fm_reinitialiserRegistre(String table) {
        File ficRegistre = new File(racine + "/" + session.getEntreprise().getId() + "/" + table + "/" + Registre.fichierRegistre);
        return ecrire(ficRegistre.getAbsolutePath(), new Registre(0, new Date()));
    }

    private void chargerRegistreEnMemoire(String table) {
        //System.out.println("Le fichier " + fichierREGISTRE + " existe.");
        registre = (Registre) ouvrir(Registre.class, racine + "/" + session.getEntreprise().getId() + "/" + table + "/" + Registre.fichierRegistre);
    }
    
    private int getDernierID(String table) {
        //System.out.println("Le fichier " + fichierREGISTRE + " existe.");
        Registre reg = (Registre) ouvrir(Registre.class, racine + "/" + session.getEntreprise().getId() + "/" + table + "/" + Registre.fichierRegistre);
        registre = reg;
        return reg.getDernierID() + 1;
    }

    public Registre fm_getRegistre(String table) {
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

                boolean rep = ecrire(racine + "/" + session.getEntreprise().getId() + "/" + table + "/" + idNewObj, NewObj);
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

    public String[] fm_getContenusDossier(String table) {
        File dossier = new File(racine + "/" + session.getEntreprise().getId() + "/" + table);
        if (dossier.exists()) {
            if (dossier.isDirectory()) {
                return dossier.list(new FiltreDonnees(".man"));
            }
        }
        return new String[0];
    }

    public int fm_getTaille(String dossierDestination) {
        File dossier = new File(dossierDestination);
        if (dossier.exists()) {
            if (dossier.isDirectory()) {
                return dossier.list(new FiltreDonnees(".man")).length;
            }
        }
        return 0;
    }

    public void fm_enregistrer(int vitesse, Vector NewObjs, String table, EcouteurStandard ecouteur) {
        new Thread() {
            @Override
            public void run() {
                try {
                    int taille = NewObjs.size();
                    int index = 1;
                    for (Object NewO : NewObjs) {
                        sleep(vitesse);
                        fm_enregistrer(NewO, table, null);
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

    public void fm_enregistrer(Object NewObj, String table, EcouteurStandard ecouteur) {
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

    public Object fm_ouvrir(Class NomClasse, String table, int idObj) {
        return Util.lire(racine + "/" + session.getEntreprise().getId() + "/" + table + "/" + idObj, NomClasse);
    }

    public boolean fm_supprimer(String table, int idObj) {
        File fichObjet = new File(racine + "/" + session.getEntreprise().getId() + "/" + table + "/" + idObj);
        if (fichObjet.exists()) {
            return fichObjet.delete();
        }
        return false;
    }

    public void fm_supprimerTout(String table, EcouteurSuppression ecouteurSuppression) {
        new Thread() {
            @Override
            public void run() {
                try {
                    String[] tabIds = fm_getContenusDossier(table);
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

    public void fm_supprimerTout(String dossier, String[] tabIds, EcouteurSuppression ecouteurSuppression) {
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
            boolean deleted = fm_supprimer(table, Integer.parseInt(id.trim()));
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

    public void fm_ouvrirTout(int vitesseTraiement, Class NomClasse, String table, EcouteurOuverture ouvrirListener) {
        new Thread() {
            @Override
            public void run() {
                try {
                    String[] tabIDs = fm_getContenusDossier(table);
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
        return getDernierID(table);
    }

    private void saveRegistre(String table) {
        ecrire(new File(racine + "/" + session.getEntreprise().getId() + "/" + table + "/" + Registre.fichierRegistre).getAbsolutePath(), registre);
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


































































