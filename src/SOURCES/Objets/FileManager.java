/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.Objets;

import BASE.ObjetNetWork;
import Callback.CallBackObjetNetWork;
import SOURCES.Callback.CallBackEcouteur;
import SOURCES.Callback.CritereSuppression;
import SOURCES.Callback.EcouteurContains;
import SOURCES.Callback.EcouteurLoginServeur;
import SOURCES.Callback.EcouteurLogo;
import SOURCES.Callback.EcouteurLongin;
import SOURCES.Callback.EcouteurOuverture;
import SOURCES.Callback.EcouteurParametreDecaissement;
import SOURCES.Callback.EcouteurParametreEncaissement;
import SOURCES.Callback.EcouteurPhotoDisqueDistant;
import SOURCES.Callback.EcouteurSuppression;
import SOURCES.Callback.EcouteurSynchronisation;
import SOURCES.DB.ElementDistant;
import SOURCES.DB.FMDataUploader;
import SOURCES.DB.InterpreteurSql;
import SOURCES.DB.MyDataSourceFactory;
import SOURCES.DB.PhotoDisqueDistant;
import SOURCES.DB.PhotoDisqueLocal;
import SOURCES.DB.PhotoRubriqueDistante;
import SOURCES.DB.PhotoRubriqueLocal;
import SOURCES.DB.StatusElement;
import SOURCES.Utilitaires.UtilFileManager;
import Source.Callbacks.EcouteurStandard;
import Source.Interface.InterfaceDecaissement;
import Source.Interface.InterfaceEncaissement;
import Source.Interface.InterfacePaiement;
import Source.Interface.InterfaceUtilisateur;
import Source.Objet.Agent;
import Source.Objet.Ayantdroit;
import Source.Objet.Charge;
import Source.Objet.Classe;
import Source.Objet.Cours;
import Source.Objet.Decaissement;
import Source.Objet.Eleve;
import Source.Objet.Encaissement;
import Source.Objet.Entreprise;
import Source.Objet.Annee;
import Source.Objet.Fiche;
import Source.Objet.Frais;
import Source.Objet.LiaisonFraisClasse;
import Source.Objet.LiaisonFraisEleve;
import Source.Objet.LiaisonFraisPeriode;
import Source.Objet.Monnaie;
import Source.Objet.Paiement;
import Source.Objet.Periode;
import Source.Objet.Revenu;
import Source.Objet.UtilObjet;
import Source.Objet.Utilisateur;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import static java.lang.Thread.sleep;
import java.lang.reflect.Field;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.sql.DataSource;
import javax.swing.ImageIcon;
import javax.swing.JButton;
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
    public static String MANIFESTE_DEL = "MANIFEST_DEL.man";
    private EcouteurFenetre ecouteurFenetre = null;
    private String adresseServeur;
    private EcouteurLogo ecouteurLogo;
    private JButton btLogo;

    public String server = "www.visiterlardc.com";
    public String port = "3306";
    public String dbName = "visiterl_s2b";
    public String dbUser = "visiterl_s2bUser";
    public String dbUserPwd = "ssula@s2b-simple.com";
    public Vector<ImageManifeste> listeManifestesDistants = new Vector<>();

    public FileManager(String adresseServeur, String pageProcesseur, JButton btLogo) {
        super(adresseServeur + "/" + pageProcesseur);

        File FicRacine = new File(racine);
        if (!FicRacine.exists()) {
            FicRacine.mkdir();
        }

        this.adresseServeur = adresseServeur;
        this.btLogo = btLogo;
        this.ecouteurLogo = new EcouteurLogo() {
            @Override
            public void onLogoReady(File fichierLocaleLogo) {
                try {
                    processRoundedImage(fichierLocaleLogo, 55);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onLogoDeleted() {
                //System.out.println("Logo deleted avec succès.");
            }

            @Override
            public void onLogoError(String message) {
                System.err.println(message);
            }

            @Override
            public void onLogoProcessing(String message) {
                System.out.println(message);
            }
        };

    }

    private void processRoundedImage(File fichierLocaleLogo, int cornerRadius) throws Exception {
        if (btLogo != null) {
            //System.out.println("Taille du logo : w=" + btLogo.getWidth() + ", h=" + btLogo.getHeight());
            int w = 56;
            int h = 56;

            BufferedImage image = ImageIO.read(fichierLocaleLogo);
            BufferedImage output = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = output.createGraphics();
            g2.setComposite(AlphaComposite.Src);
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(Color.WHITE);
            g2.fill(new RoundRectangle2D.Float(0, 0, w, h, cornerRadius, cornerRadius));
            g2.setComposite(AlphaComposite.SrcAtop);
            g2.drawImage(image.getScaledInstance(w, h, Image.SCALE_SMOOTH), 0, 0, null);
            g2.dispose();
            Image dimg = output.getScaledInstance(w, h, Image.SCALE_SMOOTH);
            ImageIcon imageIcon = new ImageIcon(dimg);
            btLogo.setIcon(imageIcon);
        }
    }

    public Session fm_getSession() {
        return session;
    }

    public void fm_setEcouteurFenetre(JFrame fenetre) {
        ecouteurFenetre = new EcouteurFenetre(fenetre, new CallBackEcouteur() {
            @Override
            public void onChange(Preference preference) {
                //System.out.println("Windows: " + preference.toString());
                File ficPreference = new File(pref);
                if (!ficPreference.exists()) {
                    System.out.println("Fichier " + ficPreference.getAbsolutePath() + " introuvable!");
                    System.out.println("Préférence actuelle : " + preference.toString());
                }
                ecrire(pref, preference);
            }
        });

        Preference savedPref = (Preference) UtilFileManager.lire(pref, Preference.class);
        if (savedPref != null) {
            fenetre.setBounds((int) savedPref.getFenetre_x(), (int) savedPref.getFenetre_y(), (int) savedPref.getFenetre_w(), (int) savedPref.getFenetre_h());
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

            String parametres = "action=" + UtilFileManager.ACTION_CONNEXION + "&id=" + idEcole + "&motDePasse=" + motDePasse + "&email=" + email;
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
                                Date dateExpiry = UtilFileManager.convertDatePaiement(sessionWeb.getPaiement().getDateExpiration());
                                if (dateExpiry != null) {
                                    if (today.after(dateExpiry)) {
                                        ecouteurLoginServeur.onError("Votre abonnement vient d'expirer ! Connectez vous à " + adresseServeur);
                                        fm_lancerPageWebAdmin(adresseServeur);
                                    } else {
                                        ecouteurLoginServeur.onDone("Connexion reussie.", sessionWeb.getEntreprise(), sessionWeb.getUtilisateur(), sessionWeb.getPaiement());
                                    }
                                } else {
                                    ecouteurLoginServeur.onError("Veuillez d'abord payer votre licence depuis " + adresseServeur);
                                    fm_lancerPageWebAdmin(adresseServeur);
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
                public void onDone(String message, Entreprise entreprise, Utilisateur utilisateur, PaiementLicence paiement) {
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
                                    downloadLogoEtablissement(session);
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

    private void downloadLogoEtablissement(Session session) {
        //Ici on démarre le téléchargement du logo de l'établissement auquel le USER est connecté
        //Exemple: http://www.visiterlardc.com/s2b/logo/2_279279.png

        String urlDuLogo = adresseServeur + "/" + session.getEntreprise().getLogo();
        File ficServer = new File(urlDuLogo);
        try {
            URL url = new URL(urlDuLogo);
            InputStream in = new BufferedInputStream(url.openStream());
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int n = 0;
            while (-1 != (n = in.read(buf))) {
                out.write(buf, 0, n);
            }
            out.close();
            in.close();
            byte[] response = out.toByteArray();
            FileOutputStream fos = new FileOutputStream(ficServer.getName());
            fos.write(response);
            fos.close();
            if (ecouteurLogo != null) {
                ecouteurLogo.onLogoProcessing(" *** Logo enregistré avec succès.");//onProcessing("Fichier téléchargé puis enregistré avec succès.");
                ecouteurLogo.onLogoReady(new File(ficServer.getName()));
            }

        } catch (Exception ex) {
            if (ecouteurLogo != null) {
                ecouteurLogo.onLogoError("Erreur: " + ex.getMessage());
            }
            ex.printStackTrace();
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

    private void deleteLogo() {
        if (session != null) {
            File fileToDelete = new File(new File(session.getEntreprise().getLogo()).getName());
            if (fileToDelete.exists()) {
                if (ecouteurLogo != null) {
                    ecouteurLogo.onLogoProcessing("Suppression du Logo: " + fileToDelete.getAbsolutePath() + "...");
                }
                boolean sup = fileToDelete.delete();
                if (sup == true) {
                    if (ecouteurLogo != null) {
                        ecouteurLogo.onLogoProcessing("Suppression réussie!");
                        ecouteurLogo.onLogoDeleted();
                    }
                } else {
                    ecouteurLogo.onLogoError("Désolé, nous n'avons pas pu supprimer le logo!");
                }
            } else {
                if (ecouteurLogo != null) {
                    ecouteurLogo.onLogoError("Impossible de supprimer le logo car il est introuvable.");
                }
            }
        }
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
                            deleteLogo();
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
                    session = (Session) UtilFileManager.lire(racine + "/" + Session.fichierSession, Session.class);
                    if (session != null) {
                        if (ecouteurLongin != null) {
                            Date today = new Date();
                            Date dateExpi = UtilFileManager.convertDatePaiement(session.getPaiement().getDateExpiration());
                            if (dateExpi != null) {
                                if (today.after(dateExpi)) {
                                    ecouteurLongin.onEchec("Votre abonnement a expiré ! Merci de vous connecter sur votre page d'administration pour acheter une licence.");
                                } else {
                                    ecouteurLongin.onConnected("Connexion reussi!", session);
                                    if (ecouteurLogo != null) {
                                        File fichierLogo = new File(new File(session.getEntreprise().getLogo()).getName());
                                        if (fichierLogo.exists()) {
                                            ecouteurLogo.onLogoReady(fichierLogo);
                                        }
                                    }
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
        registre = getRegistreEnMemoire(table);
    }

    private Registre getRegistreEnMemoire(String table) {
        //System.out.println("Le fichier " + fichierREGISTRE + " existe.");
        return (Registre) ouvrir(Registre.class, racine + "/" + session.getEntreprise().getId() + "/" + table + "/" + Registre.fichierRegistre);
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

    private boolean enregistrerDataFromServer(String JSONString, String table, String nomFichier, long lastModified, boolean ecraseAncien) {
        String dossier = racine + "/" + session.getEntreprise().getId() + "/" + table;
        File docc = new File(dossier);
        if (!docc.exists()) {
            docc.mkdirs();
        }
        boolean rep = UtilFileManager.ecrire_txt(dossier + "/" + nomFichier, JSONString, ecraseAncien);
        if (rep == true) {
            (new File(dossier + "/" + nomFichier)).setLastModified(lastModified);
        }
        return rep;
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

    public Object[] fm_getSignatures(Class classe, String table, String[] tabIds) {
        File dossier = new File(racine + "/" + session.getEntreprise().getId() + "/" + table);
        Vector<Long> Vsign = new Vector();
        try {
            if (dossier.exists()) {
                for (String ID : tabIds) {
                    Object oObj = fm_ouvrir(classe, table, Integer.parseInt(ID));
                    Field champSignature = classe.getDeclaredField("signature");
                    long signature = (long) champSignature.get(oObj);
                    Vsign.add(signature);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Vsign.toArray();
    }

    public File[] fm_getContenusDossier_(String table) {
        File dossier = new File(racine + "/" + session.getEntreprise().getId() + "/" + table);
        if (dossier.exists()) {
            if (dossier.isDirectory()) {
                return dossier.listFiles(new FiltreDonnees(".man"));
            }
        }
        return new File[0];
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

    public void fm_contains(Class NomClasse, String table, EcouteurContains ecc) {
        if (ecc != null) {
            try {
                boolean reponse = false;
                Vector TabObjsFound = new Vector();
                String[] tabIDs = fm_getContenusDossier(table);
                if (tabIDs.length != 0) {
                    for (String id : tabIDs) {
                        ecc.onProcessing("Vérification en encours...");
                        Object objetEncours = UtilFileManager.lire(racine + "/" + session.getEntreprise().getId() + "/" + table + "/" + id, NomClasse);
                        if (objetEncours != null) {
                            if (ecc.isOk(objetEncours) == true) {
                                if (!TabObjsFound.contains(objetEncours)) {
                                    TabObjsFound.add(objetEncours);
                                    reponse = true;
                                }
                            }
                        }
                    }
                }
                ecc.onSuccess("Done", reponse, TabObjsFound);
            } catch (Exception e) {
                e.printStackTrace();
                ecc.onError(e.getMessage());
            }
        }
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
        return UtilFileManager.lire(fichierSource, NomClasse);
    }

    public Object fm_ouvrir(Class NomClasse, String table, int idObj) {
        return UtilFileManager.lire(racine + "/" + session.getEntreprise().getId() + "/" + table + "/" + idObj, NomClasse);
    }

    public boolean fm_supprimer(String table, int idObj, Object signature) {
        String dossier = racine + "/" + session.getEntreprise().getId() + "/" + table + "/";
        File fichObjet = new File(dossier + idObj);
        if (fichObjet.exists()) {
            boolean rep = fichObjet.delete();
            if (rep == true) {
                //On actualise le fichier MANIFEST_DEL.man, utile pour la synchronisation par la suite avec le Serveur
                UtilFileManager.ecrire_txt(dossier + MANIFESTE_DEL, signature + ",", true);
            }
            return rep;
        }
        return false;
    }

    public Object[] fm_getSignaturesDeleted(String table) {
        Object[] res = null;
        String cheminDeletedSignatures = racine + "/" + session.getEntreprise().getId() + "/" + table + "/" + MANIFESTE_DEL;
        File fichObjet = new File(cheminDeletedSignatures);
        if (fichObjet.exists()) {
            res = UtilFileManager.lire_signaturesDeleted(cheminDeletedSignatures);
        }
        return res;
    }

    public boolean fm_detruireSignaturesDeleted(String table) {
        Object[] res = null;
        String cheminDeletedSignatures = racine + "/" + session.getEntreprise().getId() + "/" + table + "/" + MANIFESTE_DEL;
        File fichObjet = new File(cheminDeletedSignatures);
        if (fichObjet.exists()) {
            return fichObjet.delete();
        } else {
            return true;
        }
    }

    public boolean fm_detruireBasedOnSignature(String table, long signature) {
        String dossier = racine + "/" + session.getEntreprise().getId() + "/" + table;
        File fdossier = new File(dossier);
        if (fdossier.exists()) {
            String[] tabIDs = fm_getContenusDossier(table);
            if (tabIDs.length != 0) {
                for (String ID_master : tabIDs) {
                    boolean rep = UtilFileManager.containsSignature(dossier + "/" + ID_master, signature);
                    if (rep == true) {
                        return (new File(dossier + "/" + ID_master)).delete();
                    }
                }
            }
        }
        return false;
    }

    public void fm_supprimerTout(Class classe, String table, EcouteurSuppression ecouteurSuppression) {
        new Thread() {
            @Override
            public void run() {
                try {
                    String[] tabIds = fm_getContenusDossier(table);
                    Object[] tabbSignatures = fm_getSignatures(classe, table, tabIds);
                    deleteGroup(tabIds, tabbSignatures, table, ecouteurSuppression);
                } catch (Exception e) {
                    e.printStackTrace();
                    if (ecouteurSuppression != null) {
                        ecouteurSuppression.onError("Erreur lors de la suppression :" + e.getMessage());
                    }
                }
            }
        }.start();
    }

    public void fm_supprimerTout(String dossier, String[] tabIds, Object[] tabSignatures, EcouteurSuppression ecouteurSuppression) {
        new Thread() {
            @Override
            public void run() {
                try {
                    if (tabIds != null) {
                        deleteGroup(tabIds, tabSignatures, dossier, ecouteurSuppression);
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

    private void deleteGroup(String[] tabIds, Object[] tabSignatures, String table, EcouteurSuppression ecouteurSuppression) {
        Vector tabIdsNotDeleted = new Vector();
        int index = 1;
        for (String id : tabIds) {
            String status = "[ok]";
            boolean deleted = fm_supprimer(table, Integer.parseInt(id.trim()), (long) tabSignatures[index - 1]);
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

    public void fm_ouvrirEncaissements(int pageActuelle, int taillePage, EcouteurParametreEncaissement epe, EcouteurOuverture ouvrirListener) {
        if (ouvrirListener != null) {
            //Objets MASTER
            Vector objets_encaissements = new Vector();

            //ENCAISSEMENTS - ON CHARGE D'ABORD TOUT CE QUI RESPECTE LE CRITERE
            String[] tabIDs_encaissements = fm_getContenusDossier(UtilObjet.DOSSIER_ENCAISSEMENT);
            if (tabIDs_encaissements.length != 0) {
                for (String id : tabIDs_encaissements) {
                    ouvrirListener.onProcessing("Vérification en encours...");
                    Object objetEncours = UtilFileManager.lire(racine + "/" + session.getEntreprise().getId() + "/" + UtilObjet.DOSSIER_ENCAISSEMENT + "/" + id, Encaissement.class);
                    if (!objets_encaissements.contains(objetEncours)) {
                        if (ouvrirListener.isCriteresRespectes(objetEncours) == true) {
                            objets_encaissements.add(objetEncours);
                        }
                    }
                }
            }

            //FRAIS PAYES - ON CHARGE D'ABORD TOUT CE QUI RESPECTE LE CRITERE
            String[] tabIDs_paiement_frais = fm_getContenusDossier(UtilObjet.DOSSIER_PAIEMENT);
            if (tabIDs_paiement_frais.length != 0) {
                for (String id : tabIDs_paiement_frais) {
                    ouvrirListener.onProcessing("Vérification en encours...");

                    Paiement paiement = (Paiement) UtilFileManager.lire(racine + "/" + session.getEntreprise().getId() + "/" + UtilObjet.DOSSIER_PAIEMENT + "/" + id, Paiement.class);
                    Encaissement objetEncaissement = getEncaissement(paiement, epe);

                    if (!objets_encaissements.contains(objetEncaissement)) {
                        if (ouvrirListener.isCriteresRespectes(objetEncaissement) == true) {
                            objets_encaissements.add(objetEncaissement);
                        }
                    }
                }
            }

            //ENSUITE ON CHARGE LA PLAGE DEMANDEE
            int conteurElement = 0;
            int page = 1;
            Vector objetsTrouves = new Vector();
            for (Object objTrouve : objets_encaissements) {
                ouvrirListener.onProcessing("Chargement encours (" + (conteurElement + 1) + "/" + tabIDs_encaissements.length + ")...");

                if (objetsTrouves.size() < taillePage && page == pageActuelle) {//
                    if (!objetsTrouves.contains(objetsTrouves)) {
                        objetsTrouves.add(objTrouve);
                        ouvrirListener.onElementLoaded("Chargement en cours...", objTrouve);
                    }
                }
                if (((conteurElement + 1) % taillePage) == 0) {
                    page++;
                }
                conteurElement++;
            }

            ouvrirListener.onDone("Chargé!", conteurElement, objets_encaissements);
        }
    }

    public void fm_ouvrirDecaissements(int pageActuelle, int taillePage, EcouteurParametreDecaissement epe, EcouteurOuverture ouvrirListener) {
        if (ouvrirListener != null) {
            //Objets MASTER
            Vector objets_decaissements = new Vector();

            //DECAISSEMENTS - ON CHARGE D'ABORD TOUT CE QUI RESPECTE LE CRITERE
            String[] tabIDs_decaissements = fm_getContenusDossier(UtilObjet.DOSSIER_DECAISSEMENT);
            if (tabIDs_decaissements.length != 0) {
                for (String id : tabIDs_decaissements) {
                    ouvrirListener.onProcessing("Vérification en encours...");
                    Object objetEncours = UtilFileManager.lire(racine + "/" + session.getEntreprise().getId() + "/" + UtilObjet.DOSSIER_DECAISSEMENT + "/" + id, Decaissement.class);
                    if (!objets_decaissements.contains(objetEncours)) {
                        if (ouvrirListener.isCriteresRespectes(objetEncours) == true) {
                            objets_decaissements.add(objetEncours);
                        }
                    }
                }
            }

            //SALAIRES PAYES - ON CHARGE D'ABORD TOUT CE QUI RESPECTE LE CRITERE
            String[] tabIDs_paiement_salaires = fm_getContenusDossier(UtilObjet.DOSSIER_FICHE_DE_PAIE);
            if (tabIDs_paiement_salaires.length != 0) {
                for (String id : tabIDs_paiement_salaires) {
                    ouvrirListener.onProcessing("Vérification en encours...");

                    Fiche fiche = (Fiche) UtilFileManager.lire(racine + "/" + session.getEntreprise().getId() + "/" + UtilObjet.DOSSIER_FICHE_DE_PAIE + "/" + id, Fiche.class);
                    Decaissement objetDecaissement = getDecaissement(fiche, epe);

                    if (!objets_decaissements.contains(objetDecaissement)) {
                        if (ouvrirListener.isCriteresRespectes(objetDecaissement) == true) {
                            objets_decaissements.add(objetDecaissement);
                        }
                    }
                }
            }

            //ENSUITE ON CHARGE LA PAGE DEMANDEE
            int conteurElement = 0;
            int page = 1;
            Vector objetsTrouves = new Vector();
            for (Object objTrouve : objets_decaissements) {
                ouvrirListener.onProcessing("Chargement encours (" + (conteurElement + 1) + "/" + tabIDs_decaissements.length + ")...");

                if (objetsTrouves.size() < taillePage && page == pageActuelle) {//
                    if (!objetsTrouves.contains(objetsTrouves)) {
                        objetsTrouves.add(objTrouve);
                        ouvrirListener.onElementLoaded("Chargement en cours...", objTrouve);
                    }
                }
                if (((conteurElement + 1) % taillePage) == 0) {
                    page++;
                }
                conteurElement++;
            }

            ouvrirListener.onDone("Chargé!", conteurElement, objets_decaissements);
        }
    }

    private Encaissement getEncaissement(Paiement paiementFrais, EcouteurParametreEncaissement epe) {
        int destination = InterfaceEncaissement.DESTINATION_BANQUE;
        if (paiementFrais.getMode() == InterfacePaiement.MODE_CAISSE) {
            destination = InterfaceEncaissement.DESTINATION_CAISSE;
        }
        Frais fra = epe.getFrais(paiementFrais.getIdFrais());
        int idMonnaie = -1;
        String nomFrais = "";
        String codeMonnaie = "";
        if (fra != null) {
            idMonnaie = fra.getIdMonnaie();
            nomFrais = fra.getNom();
        }
        Monnaie mon = epe.getMonnaie(idMonnaie);
        if (mon != null) {
            codeMonnaie = mon.getCode();
        }
        int idUtilisateur = epe.getIdUtilisateur();
        String motif = nomFrais + " - " + paiementFrais.getNomEleve();
        int idRevenu = -1;
        String strRevenu = "";
        Revenu revenu = epe.getRevenu();
        if (revenu != null) {
            strRevenu = revenu.getNom();
            idRevenu = revenu.getId();
        }
        return new Encaissement(-100, destination, paiementFrais.getReference(), paiementFrais.getDate(), paiementFrais.getMontant(), idMonnaie, codeMonnaie, paiementFrais.getNomDepositaire(), motif, idRevenu, strRevenu, paiementFrais.getIdExercice(), idUtilisateur, UtilObjet.getSignature(), InterfaceEncaissement.BETA_EXISTANT);
    }

    private Decaissement getDecaissement(Fiche fichePaie, EcouteurParametreDecaissement epd) {
        int source = InterfaceEncaissement.DESTINATION_CAISSE;
        int idMonnaie = fichePaie.getIdMonnaie();
        String codeMonnaie = "";
        Monnaie mon = epd.getMonnaie(idMonnaie);
        if (mon != null) {
            codeMonnaie = mon.getCode();
        }
        String motif = "";
        String beneficiaire = "";
        Agent agentEncours = epd.getAgent(fichePaie.getIdAgent());
        if (agentEncours != null) {
            beneficiaire = agentEncours.getNom() + " " + agentEncours.getPostnom() + " " + agentEncours.getPrenom();
            motif = "Salaire " + fichePaie.getMois();
        }

        double avoire = 0;
        avoire += fichePaie.getAutresGains();
        avoire += fichePaie.getLogement();
        avoire += fichePaie.getSalaireBase();
        avoire += fichePaie.getTransport();

        double retenu = 0;
        retenu += fichePaie.getRetenu_ABSENCE();
        retenu += fichePaie.getRetenu_AVANCE_SALAIRE();
        retenu += fichePaie.getRetenu_CAFETARIAT();
        retenu += fichePaie.getRetenu_INSS();
        retenu += fichePaie.getRetenu_IPR();
        retenu += fichePaie.getRetenu_ORDINATEUR();
        retenu += fichePaie.getRetenu_SYNDICAT();

        double montant = avoire - retenu;

        int idCharge = -1;
        String nomCharge = "";
        Charge rr = epd.getCharge();
        if (rr != null) {
            nomCharge = rr.getNom();
            idCharge = rr.getId();
        }
        return new Decaissement(-100, source, fichePaie.getId() + "", fichePaie.getDateEnregistrement(), montant, idMonnaie, codeMonnaie, beneficiaire, motif, idCharge, nomCharge, fichePaie.getIdExercice(), fichePaie.getIdUtilisateur(), UtilObjet.getSignature(), InterfaceDecaissement.BETA_EXISTANT);
    }

    public PhotoDisqueLocal fm_getPhotoDisqueLocal(Vector<Dossier> dossiers) {
        //System.out.println("Parcours des dossiers:");
        PhotoDisqueLocal photoDisqueLocal = new PhotoDisqueLocal();
        for (Dossier dossier : dossiers) {
            //System.out.println("Dossier - " + dossier);
            PhotoRubriqueLocal photoRubriqueLocal = new PhotoRubriqueLocal(dossier.getClasse(), dossier.getNom());
            File[] contenus = fm_getContenusDossier_(dossier.getNom());
            for (File contenu : contenus) {
                //System.out.println("" + contenu + " - " + UtilObjet.getDateFrancais(new Date(contenu.lastModified())));
                photoRubriqueLocal.ajouterContenu(contenu);
            }
            photoDisqueLocal.ajouterRubrique(photoRubriqueLocal);
        }
        return photoDisqueLocal;
    }

    private void fm_loadPhotoRubriqueLeger(Statement stmt, ResultSet rs, int idExercice, PhotoDisqueDistant photoDisqueDistant) throws Exception {
        Entreprise ecole = session.getEntreprise();

        //Phase #0: Suppression en local de données supprimées du serveur distant
        System.out.println("Phase #0: Suppression en local de données supprimées du serveur distant");
        String sql = "SELECT * FROM BACKUP_DELETED_SIGNATURE;";
        rs = stmt.executeQuery(sql);
        while (rs.next()) {
            long signature = rs.getLong("signature");
            String dossier = rs.getString("dossier");
            boolean rep = fm_detruireBasedOnSignature(dossier, signature);
            System.out.println(" ** " + signature + ", " + dossier + ", detruit ? = " + rep);
        }

        //Récupération des MANIFESTES distants
        listeManifestesDistants.removeAllElements();
        System.out.println("MANIFESTES DISTANTS:");
        sql = "SELECT * FROM BACKUP_MANIFESTE WHERE idEntreprise = '" + ecole.getId() + "';";
        rs = stmt.executeQuery(sql);
        while (rs.next()) {
            int id = rs.getInt("id");
            int idEntreprise = rs.getInt("idEntreprise");
            int idUtilisateur = rs.getInt("idUtilisateur");
            String dossier = rs.getString("dossier");
            int dernierID = rs.getInt("dernierID");
            long dateEnregistrement = rs.getLong("dateEnregistrement");
            ImageManifeste imgMan = new ImageManifeste(id, idEntreprise, idUtilisateur, dossier, dernierID, dateEnregistrement);
            if (!listeManifestesDistants.contains(imgMan)) {
                listeManifestesDistants.add(imgMan);
                System.out.println("\t-" + imgMan.toString());
            }
        }
        System.out.println("..");

        //Parcours du tableau de tous les DOSSIERS EXISTANTS
        String[] tabDossiers = null;

        if (idExercice == -1) {
            tabDossiers = new String[]{UtilObjet.DOSSIER_ANNEE};
        } else {
            tabDossiers = new String[]{
                UtilObjet.DOSSIER_ANNEE,
                UtilObjet.DOSSIER_MONNAIE,
                UtilObjet.DOSSIER_CLASSE,
                UtilObjet.DOSSIER_PERIODE,
                UtilObjet.DOSSIER_AGENT,
                UtilObjet.DOSSIER_COURS,
                UtilObjet.DOSSIER_REVENU,
                UtilObjet.DOSSIER_CHARGE,
                UtilObjet.DOSSIER_FRAIS,
                UtilObjet.DOSSIER_ELEVE,
                UtilObjet.DOSSIER_AYANT_DROIT,
                UtilObjet.DOSSIER_PAIEMENT,
                UtilObjet.DOSSIER_ENCAISSEMENT,
                UtilObjet.DOSSIER_DECAISSEMENT,
                UtilObjet.DOSSIER_FICHE_DE_PAIE
            };
        }

        for (String DOSSIER : tabDossiers) {
            //On demande au serveur de supprimer les données qui ont été supprimées en local
            //En suite, on lui demande de garder en mémoire les signatures supprimées
            //Dans l'avenir il devra s'assurer que ces signatures (supprimées) ne puissent plus jamais apparaitre sur un poste local
            //Du côté de MySql, Table BACKUP_DELETED_SIGNATURE
            traiterSignaturesDeleted(DOSSIER, stmt, rs);

            //On intérroge le serveur distant pour avoir la liste de données actuelles
            PhotoRubriqueDistante photoRubriqueDistante = new PhotoRubriqueDistante();
            photoRubriqueDistante.setNom("BACKUP_" + DOSSIER);

            sql = "";
            if (DOSSIER.trim().equals(UtilObjet.DOSSIER_ANNEE)) {
                if(idExercice != -1){
                    sql = "select * from " + photoRubriqueDistante.getNom() + " WHERE idEntreprise = " + ecole.getId() + " AND id = " + idExercice + ";";
                }else{
                    sql = "select * from " + photoRubriqueDistante.getNom() + " WHERE idEntreprise = " + ecole.getId() + ";";
                }
            } else {
                sql = "select * from " + photoRubriqueDistante.getNom() + " WHERE idEntreprise = " + ecole.getId() + " AND idExercice = " + idExercice + ";";
            }
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                int id = rs.getInt("id");
                long lastModifiedDate = rs.getLong("lastModified");
                photoRubriqueDistante.ajouterContenu(new ElementDistant(id, ecole.getId(), idExercice, lastModifiedDate));
            }
            photoDisqueDistant.ajouterRubrique(photoRubriqueDistante);
        }
    }

    //"www.visiterlardc.com", "3306", "visiterl_s2b", "visiterl_s2bUser", "ssula@s2b-simple.com"
    public void fm_getPhotoDisqueDistant(int idExercice, String server, String port, String dbName, String dbUser, String userPwd, EcouteurPhotoDisqueDistant epd) {
        if (epd != null) {
            new Thread() {
                public void run() {
                    try {
                        epd.onProcessing("Synchronisation...");

                        DataSource ds = MyDataSourceFactory.getMySQLDataSource(server, port, dbName, dbUser, userPwd);
                        Connection con = null;
                        Statement stmt = null;
                        ResultSet rs = null;
                        try {
                            con = ds.getConnection();
                            stmt = con.createStatement();
                            PhotoDisqueDistant pdd = new PhotoDisqueDistant();
                            fm_loadPhotoRubriqueLeger(stmt, rs, idExercice, pdd);
                            epd.onDone(pdd);
                        } catch (SQLException e) {
                            e.printStackTrace();
                            epd.onEchec("Erreur: " + e.getMessage());
                        } finally {
                            try {
                                if (rs != null) {
                                    rs.close();
                                }
                                if (stmt != null) {
                                    stmt.close();
                                }
                                if (con != null) {
                                    con.close();
                                }
                            } catch (SQLException e) {
                                e.printStackTrace();
                                epd.onEchec("Erreur: " + e.getMessage());
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        epd.onEchec("Erreur: " + e.getMessage());
                    }
                }
            }.start();
        }
    }

    public void fm_synchroniser(Utilisateur currentUser, int idExerciceEncours, EcouteurSynchronisation ecouteurSynchronisation) {

        if (currentUser != null) {
            Vector<Dossier> dossiersControledByCurrentUser = new Vector<>();

            if (idExerciceEncours == -1) {
                dossiersControledByCurrentUser.add(new Dossier(UtilObjet.DOSSIER_ANNEE, Annee.class));
                System.out.println("SYNCHRONISATION D'EXERCICES UNIQUEMENT !");
            } else {
                if (currentUser.getDroitExercice() == InterfaceUtilisateur.DROIT_CONTROLER) {
                    dossiersControledByCurrentUser.add(new Dossier(UtilObjet.DOSSIER_ANNEE, Annee.class));
                    dossiersControledByCurrentUser.add(new Dossier(UtilObjet.DOSSIER_AGENT, Agent.class));
                    dossiersControledByCurrentUser.add(new Dossier(UtilObjet.DOSSIER_CHARGE, Charge.class));
                    dossiersControledByCurrentUser.add(new Dossier(UtilObjet.DOSSIER_CLASSE, Classe.class));
                    dossiersControledByCurrentUser.add(new Dossier(UtilObjet.DOSSIER_FRAIS, Frais.class));
                    dossiersControledByCurrentUser.add(new Dossier(UtilObjet.DOSSIER_PERIODE, Periode.class));
                    dossiersControledByCurrentUser.add(new Dossier(UtilObjet.DOSSIER_REVENU, Revenu.class));
                    dossiersControledByCurrentUser.add(new Dossier(UtilObjet.DOSSIER_COURS, Cours.class));
                    dossiersControledByCurrentUser.add(new Dossier(UtilObjet.DOSSIER_MONNAIE, Monnaie.class));
                } else {
                    System.out.println(currentUser.getNom() + " ne peut pas contôler les années scolaires");
                }
                if (currentUser.getDroitFacture() == InterfaceUtilisateur.DROIT_CONTROLER) {
                    dossiersControledByCurrentUser.add(new Dossier(UtilObjet.DOSSIER_PAIEMENT, Paiement.class));
                } else {
                    System.out.println(currentUser.getNom() + " ne peut pas contôler les factures");
                }
                if (currentUser.getDroitInscription() == InterfaceUtilisateur.DROIT_CONTROLER) {
                    dossiersControledByCurrentUser.add(new Dossier(UtilObjet.DOSSIER_ELEVE, Eleve.class));
                    dossiersControledByCurrentUser.add(new Dossier(UtilObjet.DOSSIER_AYANT_DROIT, Ayantdroit.class));
                } else {
                    System.out.println(currentUser.getNom() + " ne peut pas contôler les inscriptions");
                }
                if (currentUser.getDroitPaie() == InterfaceUtilisateur.DROIT_CONTROLER) {
                    dossiersControledByCurrentUser.add(new Dossier(UtilObjet.DOSSIER_FICHE_DE_PAIE, Fiche.class));
                } else {
                    System.out.println(currentUser.getNom() + " ne peut pas contôler la paie");
                }
                if (currentUser.getDroitTresorerie() == InterfaceUtilisateur.DROIT_CONTROLER) {
                    dossiersControledByCurrentUser.add(new Dossier(UtilObjet.DOSSIER_DECAISSEMENT, Decaissement.class));
                    dossiersControledByCurrentUser.add(new Dossier(UtilObjet.DOSSIER_ENCAISSEMENT, Encaissement.class));
                } else {
                    System.out.println(currentUser.getNom() + " ne peut pas contôler la trésorerie");
                }
            }

            System.out.println("Synchronisation en cours...");
            if (ecouteurSynchronisation != null) {
                ecouteurSynchronisation.onProcessing("Sync. en cours...", 5);
            }
            if (session != null) {
                //Photo du disque distant
                fm_getPhotoDisqueDistant(idExerciceEncours, server, port, dbName, dbUser, dbUserPwd, new EcouteurPhotoDisqueDistant() {
                    @Override
                    public void onDone(PhotoDisqueDistant photoDisqueDistant) {
                        //On lance la comparaison de ces deux disques
                        PhotoDisqueLocal photoDisqueLocal = fm_getPhotoDisqueLocal(dossiersControledByCurrentUser);
                        fm_comparerDisques(photoDisqueLocal, photoDisqueDistant, ecouteurSynchronisation);
                        
                        System.out.println("** Fin de la synchronisation **");

                        if (ecouteurSynchronisation != null) {
                            ecouteurSynchronisation.onProcessing("Done", 100);
                            ecouteurSynchronisation.onSuccess("Synchronisé !");
                        }
                    }

                    @Override
                    public void onEchec(String message) {
                        System.out.println(message);
                        if (ecouteurSynchronisation != null) {
                            ecouteurSynchronisation.onEchec(message);
                        }
                    }

                    @Override
                    public void onProcessing(String message) {
                        System.out.println(message);
                    }
                });
            }
        }
    }

    private void fm_comparerDisques(PhotoDisqueLocal photoDisqueLocal, PhotoDisqueDistant photoDisqueDistant, EcouteurSynchronisation ecouteurSynchronisation) {
        if (photoDisqueDistant != null & photoDisqueLocal != null) {
            FMDataUploader fMDataUploader = null;
            try {
                fMDataUploader = new FMDataUploader(server, port, dbName, dbUser, dbUserPwd);

                if (ecouteurSynchronisation != null) {
                    ecouteurSynchronisation.onProcessing("Phase #1...", 10);
                }
                //Phase #1: Téléchargement (Du Serveur au Poste local)
                //Etape #1-1: Données nouvelles
                //Etape #1-2: Donnés modifiées
                System.out.println();
                System.out.println("Phase #1: Téléchargement (Du Serveur au Poste local)");
                for (PhotoRubriqueDistante rubriqueDistante : photoDisqueDistant.getRubriques()) {
                    synchroniserDataVersPosteLocal(rubriqueDistante, photoDisqueLocal, fMDataUploader);
                }

                if (ecouteurSynchronisation != null) {
                    ecouteurSynchronisation.onProcessing("Phase #2...", 50);
                }
                //Phase #2: Chargement (Du Poste Local au Serveur)
                //Etape #2-1: Données nouvelles
                //Etape #2-2: Donnés modifiées
                System.out.println();
                System.out.println("Phase #2: Chargement (Du Poste Local au Serveur) - Uniquement pour les DOSSIERS DONT LE USER A LE DROIT D'ECRITURE");
                for (PhotoRubriqueLocal rubriqueLocale : photoDisqueLocal.getRubriques()) {
                    //Envoi de la mise à jour des données vers le serveur
                    synchroniserDataVersServeur(rubriqueLocale, photoDisqueDistant, fMDataUploader);
                    //Envoi de la mise à jour du manifeste vers le serveur
                    synchroniserManifeste(rubriqueLocale.getNom(), fMDataUploader);
                }

                fMDataUploader.close();
            } catch (Exception e) {
                e.printStackTrace();
                if (fMDataUploader != null) {
                    fMDataUploader.close();
                }
            }
        }
    }

    public void fm_ouvrirTout(int vitesseTraiement, Class NomClasseMaster, String table, int pageActuelle, int taillePage, EcouteurOuverture ouvrirListener) {
        if (ouvrirListener != null) {
            //Objets MASTER
            String[] tabIDs_master = fm_getContenusDossier(table);
            Vector objetsMaster = new Vector();

            //ON CHARGE D'ABORD TOUT CE QUI RESPECTE LE CRITERE
            if (tabIDs_master.length != 0) {
                for (String ID_master : tabIDs_master) {
                    ouvrirListener.onProcessing("Vérification en encours...");
                    Object objetEncours = UtilFileManager.lire(racine + "/" + session.getEntreprise().getId() + "/" + table + "/" + ID_master, NomClasseMaster);
                    if (!objetsMaster.contains(objetEncours)) {
                        if (ouvrirListener.isCriteresRespectes(objetEncours) == true) {
                            objetsMaster.add(objetEncours);
                        }
                    }
                }
            }

            //ENSUITE ON CHARGE LA PAGE DEMANDEE
            int conteurElement = 0;
            int page = 1;
            Vector objetsTrouves = new Vector();
            for (Object objTrouve : objetsMaster) {
                ouvrirListener.onProcessing("Chargement encours (" + (conteurElement + 1) + "/" + tabIDs_master.length + ")...");

                if (objetsTrouves.size() < taillePage && page == pageActuelle) {//
                    if (!objetsTrouves.contains(objetsTrouves)) {
                        objetsTrouves.add(objTrouve);
                        ouvrirListener.onElementLoaded("Chargement en cours...", objTrouve);
                    }
                }
                if (((conteurElement + 1) % taillePage) == 0) {
                    page++;
                }
                conteurElement++;
            }

            ouvrirListener.onDone("Chargé!", conteurElement, objetsMaster);
        }
    }

    public void fm_supprimerTout(Class NomClasseMaster, String table, EcouteurSuppression ecouteurSuppression, CritereSuppression critereSuppression) {
        try {
            String[] tabIDs_master = fm_getContenusDossier(table);
            Object[] tabSignatures = fm_getSignatures(NomClasseMaster, table, tabIDs_master);
            Vector tabIdsNotDeleted = new Vector();
            if (tabIDs_master.length != 0) {
                int index = 0;
                for (String ID_master : tabIDs_master) {
                    if (ecouteurSuppression != null) {
                        ecouteurSuppression.onProcessing("Vérification en encours...");
                    }
                    Object objetEncours = UtilFileManager.lire(racine + "/" + session.getEntreprise().getId() + "/" + table + "/" + ID_master, NomClasseMaster);
                    boolean canBeDeleted;
                    if (critereSuppression != null) {
                        canBeDeleted = critereSuppression.canBeDeleted(objetEncours);
                    } else {
                        canBeDeleted = true;
                    }
                    if (canBeDeleted == true) {
                        boolean deleted = fm_supprimer(table, Integer.parseInt(ID_master.trim()), tabSignatures[index]);
                        if (deleted == false) {
                            tabIdsNotDeleted.add(ID_master);
                        }
                    }
                    index++;
                }
            }
            if (ecouteurSuppression != null) {
                ecouteurSuppression.onDone("Ok", tabIdsNotDeleted.toArray());
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (ecouteurSuppression != null) {
                ecouteurSuppression.onError(e.getMessage());
            }
        }
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

    private boolean saveRegistre(String table, Registre newRegistre) {
        String dossier = racine + "/" + session.getEntreprise().getId() + "/" + table;
        File docc = new File(dossier);
        if (!docc.exists()) {
            docc.mkdirs();
        }
        return ecrire(new File(dossier + "/" + Registre.fichierRegistre).getAbsolutePath(), newRegistre);
    }

    private boolean ecrire(String chemin, Object obj) {
        return UtilFileManager.ecrire(chemin, obj);
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

    private void traiterSignaturesDeleted(String DOSSIER, Statement stmt, ResultSet rs) throws Exception {
        Object[] tabDeletedLocalSignatures = fm_getSignaturesDeleted(DOSSIER);
        if (tabDeletedLocalSignatures != null) {
            System.out.println("SIGNATURES LOCALES SUPPRIMEES (A SUPPRIMER AUSSI DEPUIS LE SERVEUR:");
            for (Object signature : tabDeletedLocalSignatures) {
                System.out.println(" - " + signature + " - Dossier: " + DOSSIER);
                //On charge cette signature sur le serveur (BACKUP_DELETED_SIGNATURE)
                String sql = "INSERT INTO `BACKUP_DELETED_SIGNATURE` (`signature`, `dossier`) VALUES ('" + signature + "', '" + DOSSIER + "');";
                int repUpdate = -1;
                try {
                    repUpdate = stmt.executeUpdate(sql);
                } catch (Exception e) {
                    e.printStackTrace();
                    repUpdate = 1;
                    System.out.println("Mais je continue...");
                }

                if (repUpdate == 1) {
                    //On supprime du serveur
                    sql = "DELETE FROM `BACKUP_" + DOSSIER + "` WHERE `signature` = '" + signature + "';";
                    int repDelete = stmt.executeUpdate(sql);

                    //S'il s'agit de Tables qui ont des liaison telles que FRAIS, CLASSE, ELEVE & PERIODE
                    //Alors il faut aussi supprimer leurs liaisons respectives
                    if (repDelete == 1 && DOSSIER.equals(UtilObjet.DOSSIER_CLASSE)) {
                        sql = "DELETE FROM `BACKUP_LiaisonFraisClasse` WHERE `signatureClasse` = '" + signature + "';";
                        stmt.executeUpdate(sql);
                    }
                    if (repDelete == 1 && DOSSIER.equals(UtilObjet.DOSSIER_ELEVE)) {
                        sql = "DELETE FROM `BACKUP_LiaisonFraisEleve` WHERE `signatureEleve` = '" + signature + "';";
                        stmt.executeUpdate(sql);
                    }
                    if (repDelete == 1 && DOSSIER.equals(UtilObjet.DOSSIER_FRAIS)) {
                        sql = "DELETE FROM `BACKUP_LiaisonFraisEleve` WHERE `signatureFrais` = '" + signature + "';";
                        stmt.executeUpdate(sql);
                    }
                    if (repDelete == 1 && DOSSIER.equals(UtilObjet.DOSSIER_PERIODE)) {
                        sql = "DELETE FROM `BACKUP_LiaisonFraisPeriode` WHERE `signaturePeriode` = '" + signature + "';";
                        stmt.executeUpdate(sql);
                    }
                }
            }
        }

        boolean repDestructionMANIFESTE_DEL = fm_detruireSignaturesDeleted(DOSSIER);
        System.out.println("MANFEST_DEL.man du dossier " + DOSSIER + " supprimé = " + repDestructionMANIFESTE_DEL);
    }

    private void synchroniserManifeste(String dossier, FMDataUploader fMDataUploader) {
        ImageManifeste imgManOnLine = null;
        for (ImageManifeste imgMan : listeManifestesDistants) {
            if (imgMan.getDossier().equals(dossier)) {
                imgManOnLine = imgMan;
            }
        }
        Utilisateur user = session.getUtilisateur();
        Entreprise ecole = session.getEntreprise();
        Registre regLoc = getRegistreEnMemoire(dossier);
        if (regLoc != null) {
            if (imgManOnLine == null) {
                System.out.println("\t\tMan Local: " + regLoc.getDernierID() + ", " + regLoc.getDateEnregistrement());
                System.out.println("\t\tMan Distant: NULL");

                String sql = InterpreteurSql.getInsertManifeste(ecole.getId(), user.getId(), dossier, regLoc.getDernierID(), regLoc.getDateEnregistrement().getTime() + "");
                int rep = fMDataUploader.executerUpdate(sql);
                System.out.println("\t\t\tChargement du manifeste = " + rep);
            } else {
                System.out.println("\t\tMan Local: " + regLoc.getDernierID() + ", " + regLoc.getDateEnregistrement());
                System.out.println("\t\tMan Distant: " + imgManOnLine.getDernierID() + ", " + new Date(imgManOnLine.getDateEnregistrement()));
                if (regLoc.getDateEnregistrement().after(new Date(imgManOnLine.getDateEnregistrement()))) {
                    System.out.println("\t\tManifeste local plus récent que celui du serveur! (On actualise le serveur)");
                    String sql = InterpreteurSql.getUpdateManifeste(ecole.getId(), user.getId(), dossier, regLoc.getDernierID(), regLoc.getDateEnregistrement().getTime() + "");
                    int rep = fMDataUploader.executerUpdate(sql);
                    System.out.println("\t\t\tChargement du manifeste = " + rep);
                } else if (regLoc.getDateEnregistrement().before(new Date(imgManOnLine.getDateEnregistrement()))) {
                    System.out.println("\t\tManifeste local plus ancien que celui du serveur! (On actualise le poste local)");
                    boolean rep = saveRegistre(dossier, new Registre(imgManOnLine.getDernierID(), new Date(imgManOnLine.getDateEnregistrement())));
                    System.out.println("\t\t\tEnregistrement du manifeste = " + rep);
                } else {
                    System.out.println("\t\tManifeste local le même que celui du serveur (On ne fait rien)");
                }
            }
        } else {
            if (imgManOnLine != null) {
                System.out.println("\t\tMan Local: NULL");
                System.out.println("\t\tMan Distant: " + imgManOnLine.getDernierID() + ", " + new Date(imgManOnLine.getDateEnregistrement()));
                boolean rep = saveRegistre(dossier, new Registre(imgManOnLine.getDernierID(), new Date(imgManOnLine.getDateEnregistrement())));
                System.out.println("\t\t\tEnregistrement du manifeste = " + rep);
            }
        }
    }

    private void synchroniserDataVersServeur(PhotoRubriqueLocal rubriqueLocale, PhotoDisqueDistant photoDisqueDistant, FMDataUploader fMDataUploader) {
        System.out.println(rubriqueLocale.getNom() + ":");
        for (File el : rubriqueLocale.getContenus()) {
            String nomRub = rubriqueLocale.getNom();
            StatusElement ste = photoDisqueDistant.comparer(nomRub, el);
            System.out.println("\t" + el.getName() + ", lastMidified: " + el.lastModified() + " - " + ste.toString());
            
            if (ste.isIsNew() == true) {
                //CHARGEMENT SUR LE SERVEUR
                Object oObjet = fm_ouvrir(rubriqueLocale.getClasse(), nomRub, Integer.parseInt(el.getName()));
                String sql = InterpreteurSql.getInsert(oObjet, el.lastModified());
                int rep = fMDataUploader.executerUpdate(sql);
                System.out.println("\t\tChargement - Nouvelle données... = " + rep);

            } else if (ste.isIsNew() == false && ste.isIsRecent() == true) {
                //CHARGEMENT SUR LE SERVEUR
                Object oObjet = fm_ouvrir(rubriqueLocale.getClasse(), nomRub, Integer.parseInt(el.getName()));
                String sql = InterpreteurSql.getUpdate(oObjet, el.lastModified());
                int rep = fMDataUploader.executerUpdate(sql);
                System.out.println("\t\tChargement - Données modifiées... = " + rep);

            }
        }
    }

    private void synchroniserDataVersPosteLocal(PhotoRubriqueDistante rubriqueDistante, PhotoDisqueLocal photoDisqueLocal, FMDataUploader fMDataUploader) throws Exception {
        System.out.println(rubriqueDistante.getNom() + ":");
        for (ElementDistant ed : rubriqueDistante.getContenus()) {
            String nomRub = rubriqueDistante.getNom().replaceFirst("BACKUP_", "");
            StatusElement ste = photoDisqueLocal.comparer(nomRub, ed);
            System.out.println("\t" + ed.getId() + ", lastModified: " + ed.lastModified() + " - " + ste.toString());

            //TELECHARGEMENT SUR LE POSTE LOCAL
            if (ste.isIsNew() == true) {
                saveNewDate(true, rubriqueDistante.getNom(), nomRub, photoDisqueLocal, fMDataUploader, ed);
            } else if (ste.isIsNew() == false && ste.isIsRecent() == true) {
                saveNewDate(false, rubriqueDistante.getNom(), nomRub, photoDisqueLocal, fMDataUploader, ed);
            }
        }
    }

    private void saveNewDate(boolean ecraseAncien, String nomTable, String dossier, PhotoDisqueLocal photoDisqueLocal, FMDataUploader fMDataUploader, ElementDistant ed) throws Exception {
        String sql = "";
        if(nomTable.equals("BACKUP_ANNEE")){
            sql = "SELECT * FROM " + nomTable + " WHERE idEntreprise = " + ed.getIdEntreprise() + " AND id = " + ed.getId() + ";";
        }else{
            sql = "SELECT * FROM " + nomTable + " WHERE idEntreprise = " + ed.getIdEntreprise() + " AND id = " + ed.getId() + " AND idExercice = " + ed.getIdExercice() + ";";
        }
        
        
        ResultSet rsObjet = fMDataUploader.executerQuery(sql);

        //Note: il faut prendre en compte les liaisons et savoir les reconstituer en local car sur la base, les liaisons sont séparées de leurs responsables
        String strJSON = "";
        while (rsObjet.next()) {
            Class classe = photoDisqueLocal.getClasse(dossier);
            System.out.println("Classe " + classe);
            strJSON += "{";
            String fileName = null;
            for (Field champClasse : classe.getDeclaredFields()) {
                if (!champClasse.getName().toLowerCase().equals("beta")) {
                    if (champClasse.getName().toLowerCase().equals("id")) {
                        fileName = "" + rsObjet.getInt(champClasse.getName());
                    }
                    strJSON += "\"" + champClasse.getName() + "\" : ";
                    if (champClasse.getType() == String.class) {
                        strJSON += "\"" + rsObjet.getObject(champClasse.getName()) + "\",";
                    } else {
                        strJSON += rsObjet.getObject(champClasse.getName()) + ",";
                    }
                    System.out.println(" ** champ " + champClasse.getName() + " = " + rsObjet.getObject(champClasse.getName()));
                }
            }

            strJSON += "\"beta\" : 0";
            strJSON += "}";
            System.out.println(strJSON);
            long lastModified = rsObjet.getLong("lastModified");
            if (fileName != null) {
                boolean repSaveData = enregistrerDataFromServer(strJSON, dossier, fileName, lastModified, ecraseAncien);
                System.out.println(" **** SAVED = " + repSaveData + " !");
            }
            System.out.println(" **** ");
        }
    }
}








