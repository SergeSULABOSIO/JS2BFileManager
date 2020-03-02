/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.Synchronisateur;

import BASE.ObjetNetWork;
import Callback.CallBackObjetNetWork;
import Callback.CallBackReponse;
import SOURCES.DB.PhotoDisqueLocal;
import SOURCES.DB.PhotoRubriqueLocal;
import SOURCES.Objets.SessionWeb;
import Source.Objet.UtilObjet;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.util.Date;
import java.util.Vector;

/**
 *
 * @author HP Pavilion
 */
public class Synchronisateur extends ObjetNetWork {

    public static int ACTION_SYNCHRO_GET_PHOTO_SERVEUR = 2020;
    public static int ACTION_SYNCHRO_GET_DATA_SERVEUR = 2021;
    public String Str_Etape = "";

    public SyncParametres params = null;

    //LE PC LOCAL
    public Vector<PhotoManifestePC> listeManifesteDuPC = null;
    public Vector<PhotoManifestePC> A_Charger_listeManifesteDuPC = new Vector<>();
    public Vector<PhotoSuppressionLocale> A_Charger_listeSuppressionDuPC = new Vector<>();
    public Vector<PhotoDonneePC> A_Charger_listeDonneeAChargerSurServeur = new Vector<>();
    public PhotoDisqueLocal photoPC = null;

    //LE SERVEUR DISTANT
    public PhotoServeur photoServeur = null;
    public SynchronisateurListener eps;
    public Vector<PhotoDonneeEnligne> A_Charger_listeDonneeATelecharger = new Vector<>();
    public DataServeur dataServeur = null;

    public Synchronisateur(SyncParametres parametres, SynchronisateurListener eps) {
        super(parametres.getAdresseServeur());
        this.params = parametres;
        this.eps = eps;
    }

    public void demarrer() {
        new Thread() {
            @Override
            public void run() {
                synchro_getCopieServeur();
            }

        }.start();
    }

    private void synchro_getDataServeur() {
        Str_Etape = "Etape 2/2";
        if (eps != null) {
            eps.onAfficheProgression(100, 45, Str_Etape + ": Téléchargement des données...");
        }
        DataPC dataPc = new DataPC(A_Charger_listeManifesteDuPC, A_Charger_listeSuppressionDuPC, A_Charger_listeDonneeAChargerSurServeur, A_Charger_listeDonneeATelecharger);
        POST_AJOUTER("action=" + ACTION_SYNCHRO_GET_DATA_SERVEUR + params.getTexte(), dataPc, new CallBackReponse() {
            @Override
            public void onSucess(Object object) {
                try {
                    if (eps != null) {
                        eps.onAfficheProgression(100, 100, Str_Etape + ": Téléchargement réussi!");
                    }
                    //System.out.println("DataServer: " + object);
                    ObjectMapper mapper = new ObjectMapper();
                    dataServeur = mapper.readValue(object + "", DataServeur.class);

                    //1. On décompose le packet reçu du serveur et on enregistre les données une après l'autre sur le PC
                    H_ecritureDataSurPC();

                    if (eps != null) {
                        eps.onSuccess("Synchronisation à 100% effectuée!");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    if (eps != null) {
                        eps.onAfficheProgression(100, 100, Str_Etape + ": " + e.getMessage());
                        eps.onEchec(Str_Etape + ": " + e.getMessage());
                    }
                }
            }

            @Override
            public void onErreur(String message) {
                //System.out.println(message);
                if (eps != null) {
                    eps.onAfficheProgression(100, 100, Str_Etape + ": " + message);
                    eps.onEchec(Str_Etape + ": " + message);
                }
            }

            @Override
            public void onProcessing(String message) {
                //System.out.println(message);
                if (eps != null) {
                    eps.onAfficheProgression(100, 80, Str_Etape + ": " + message);
                }
            }
        });
    }

    private void synchro_getCopieServeur() {
        Str_Etape = "Etape 1/2";
        if (eps != null) {
            eps.onAfficheProgression(100, 45, Str_Etape + ": Copie de l'image du serveur...");
        }
        POST_CHARGER("action=" + ACTION_SYNCHRO_GET_PHOTO_SERVEUR + params.getTexte(), new CallBackObjetNetWork() {
            @Override
            public void onDone(String JsonObject) {
                try {
                    if (eps != null) {
                        eps.onAfficheProgression(100, 100, Str_Etape + ": Image du serveur reçue!");
                    }
                    //System.out.println("JsonObject: " + JsonObject);
                    ObjectMapper mapper = new ObjectMapper();
                    photoServeur = mapper.readValue(JsonObject.trim(), PhotoServeur.class);

                    //1. On supprime ce qui a été supprimé au serveur
                    //Résultat: Ce qui est supprimé au serveur doit aussi être supprimé localement
                    A_suppression_donnees_supprimees_depuis_serveur();

                    //2. On cherche les signature supprimées du PC à charger au Serveur
                    //Résultat: On a la liste des signatures supprimées localement à envoyer sur le serveur
                    B_chargement_donnees_supprimees_du_pc();

                    //3. On Recherche les Manifestes à enregistrer sur le Poste Local (Nouveau & Récemment modifiés) en povénance du serveur
                    //Résultat: on actualise les manifestes locaux du PC
                    C_actualisation_manifestes_du_pc_selon_serveur();

                    //4. On Recherche les Manifestes du PC actuel, chargeables sur le Serveur (Nouveau & Récemment modifiés)
                    //Résultat: on a la liste des manifestes à charger sur le serveur distant
                    D_recherche_nouveaux_manifestes_a_charger_au_serveur();

                    //5. On va aussi capturer la photo du PC local (du disque local)
                    //Résultat: on connait les données du PC local
                    E_chargement_donnees_enregistrees_sur_pc();

                    //6. On Recherche les données Téléchargeables (Du Serveur au poste Local)
                    //Résulalt: on a la liste des données nouvelles et modifiées à faire vénir sur le PC (Téléchargement)
                    F_recherche_donnees_serveur_telechargeables_sur_pc();

                    //7. On Recherche les données Chargeables (Du Poste Local au Serveur)
                    //Résulalt: on a la liste des données nouvelles et modifiées à faire partir sur le Serveur (Chargement)
                    G_recherche_donnees_pc_chargeables_sur_serveur();

                    //On passe maintenant à la deuxième étape de la synchronisation: le téléchargement de données sur le PC
                    synchro_getDataServeur();

                } catch (Exception e) {
                    e.printStackTrace();
                    if (eps != null) {
                        eps.onAfficheProgression(100, 100, Str_Etape + ": " + e.getMessage());
                        eps.onEchec(Str_Etape + ": " + e.getMessage());
                    }
                }
            }

            @Override
            public void onError(String message) {
                //System.out.println(message);
                if (eps != null) {
                    eps.onAfficheProgression(100, 100, Str_Etape + ": " + message);
                    eps.onEchec(Str_Etape + ": " + message);
                }
            }

            @Override
            public void onProcessing(String message) {
                //System.out.println(message);
                if (eps != null) {
                    eps.onAfficheProgression(100, 80, Str_Etape + ": " + message);
                }
            }
        });
    }

    private void A_suppression_donnees_supprimees_depuis_serveur() {
        System.out.println("****** A_suppression_donnees_supprimees_depuis_serveur");
        int total = photoServeur.getListeSignaturesSupprimees().size();
        int actuel = 1;
        for (PhotoSuppressionDistante ps : photoServeur.getListeSignaturesSupprimees()) {
            if (eps != null) {
                boolean rep = false;
                if (params.getIdExercice() == -1) { //Synchronisation légère: on travaille uniquement sur le dossier ANNEE
                    if (ps.getDossier().equals(UtilObjet.DOSSIER_ANNEE)) {
                        rep = eps.onDeleteSignature(ps);
                        if (rep == true) {
                            System.out.println(" - " + ps);
                        }
                    }
                } else {
                    rep = eps.onDeleteSignature(ps);
                    if (rep == true) {
                        System.out.println(" - " + ps);
                    }
                }
                eps.onAfficheProgression(total, actuel, Str_Etape + ": Atualisation - Suppression au PC (" + rep + "): " + ps.getDossier() + " - " + ps.getSignature());
                actuel++;
            }
        }
    }

    private void B_chargement_donnees_supprimees_du_pc() {
        System.out.println("****** B_chargement_donnees_supprimees_du_pc");
        if (eps != null) {
            eps.onAfficheProgression(100, 50, Str_Etape + ": Chargement - Signature supprimées du PC...");
            A_Charger_listeSuppressionDuPC = eps.onCopieSuppressionsDuPC();
            //System.out.println(A_Charger_listeSuppressionDuPC);
            eps.onAfficheProgression(100, 100, Str_Etape + ": Chargement effectué!");
        }
    }

    private void C_actualisation_manifestes_du_pc_selon_serveur() {
        System.out.println("****** C_actualisation_manifestes_du_pc_selon_serveur");
        if (eps != null) {
            int actuel = 1;
            listeManifesteDuPC = eps.onCopieManifestesDuPC();
            int total = photoServeur.getListeManifestes().size();
            boolean rep = false;
            for (PhotoManifesteServeur manifesteServeur : photoServeur.getListeManifestes()) {

                boolean isNew = true;
                boolean isRecent = false;

                for (PhotoManifestePC manifestePC : listeManifesteDuPC) {
                    if (manifesteServeur.getDossier().equals(manifestePC.getDossier())) {
                        isNew = false;
                        if (new Date(manifesteServeur.getDateEnregistrement()).after(new Date(manifestePC.getDateEnregistrement()))) {
                            isRecent = true;
                        }
                    }
                }
                if (isNew == true || isRecent == true) {  //Nouveau Manifeste à enregistrer sur le poste ou PC local
                    if (params.getIdExercice() == -1) { // Synchronisation légère : on ne synchronise que le dossier ANNEE
                        if (manifesteServeur.getDossier().equals(UtilObjet.DOSSIER_ANNEE)) {
                            rep = eps.onSaveManifeste(manifesteServeur);
                            System.out.println(" - " + manifesteServeur.toString());
                        }
                    } else {
                        rep = eps.onSaveManifeste(manifesteServeur);
                        System.out.println(" - " + manifesteServeur.toString());
                    }

                }
                eps.onAfficheProgression(total, actuel, Str_Etape + ": Atualisation - Manifestes au PC (" + rep + "): " + manifesteServeur.getDossier() + " - " + manifesteServeur.getDateEnregistrement());
                actuel++;
            }
        }
    }

    private void D_recherche_nouveaux_manifestes_a_charger_au_serveur() {
        System.out.println("****** D_recherche_nouveaux_manifestes_a_charger_au_serveur");
        if (eps != null) {
            int actuel = 1;
            listeManifesteDuPC = eps.onCopieManifestesDuPC();
            int total = listeManifesteDuPC.size();
            A_Charger_listeManifesteDuPC.removeAllElements();
            boolean rep = false;
            for (PhotoManifestePC manifestePC : listeManifesteDuPC) {

                boolean isRecent = false;
                boolean isNew = true;

                for (PhotoManifesteServeur manifesteServeur : photoServeur.getListeManifestes()) {
                    if (manifestePC.getDossier().equals(manifesteServeur.getDossier())) {
                        isNew = false;
                        if (new Date(manifestePC.getDateEnregistrement()).after(new Date(manifesteServeur.getDateEnregistrement()))) {
                            isRecent = true;
                        }
                    }
                }
                if (isNew == true || isRecent == true) {  //Nouveau Manifeste à charger sur le serveur
                    if (params.getIdExercice() == -1) {   //Synchronisation légère: on travaille seulement sur le dossier ANNEE
                        if (manifestePC.getDossier().equals(UtilObjet.DOSSIER_ANNEE)) {
                            if (!A_Charger_listeManifesteDuPC.contains(manifestePC)) {
                                A_Charger_listeManifesteDuPC.add(manifestePC);
                                System.out.println(" - " + manifestePC.toString());
                                rep = true;
                            }
                        }
                    } else {
                        if (!A_Charger_listeManifesteDuPC.contains(manifestePC)) {
                            A_Charger_listeManifesteDuPC.add(manifestePC);
                            System.out.println(" - " + manifestePC.toString());
                            rep = true;
                        }
                    }

                }
                eps.onAfficheProgression(total, actuel, Str_Etape + ": Tri - Manifestes à charger au Serveur (" + rep + "): " + manifestePC.getDossier() + " - " + manifestePC.getDateEnregistrement());
                actuel++;
            }
        }
    }

    private void E_chargement_donnees_enregistrees_sur_pc() {
        System.out.println("****** E_chargement_donnees_enregistrees_sur_pc");
        if (eps != null) {
            eps.onAfficheProgression(100, 50, Str_Etape + ": Copie de l'image du PC Local...");
            photoPC = eps.onCopieLePC();
            //System.out.println(photoPC);
            eps.onAfficheProgression(100, 100, Str_Etape + ": Copie du PC Local effectuée!");
        }
    }

    public void F_recherche_donnees_serveur_telechargeables_sur_pc() {
        System.out.println("****** F_recherche_donnees_serveur_telechargeables_sur_pc");
        if (eps != null) {
            int actuel = 1;
            int total = photoServeur.getListeDonneeEnligne().size();
            boolean rep = false;
            A_Charger_listeDonneeATelecharger.removeAllElements();
            for (PhotoDonneeEnligne donneeOnline : photoServeur.getListeDonneeEnligne()) {

                boolean isRecent = false;
                boolean isNew = true;
                long lastModifiedLocal = 0;

                for (PhotoRubriqueLocal rubriqueLocale : photoPC.getRubriques()) {
                    if (donneeOnline.getDossier().equals(rubriqueLocale.getNom())) {
                        for (File donneeLocal : rubriqueLocale.getContenus()) {
                            if (donneeLocal.getName().equals(donneeOnline.getId() + "")) {
                                isNew = false;
                                if (new Date(donneeOnline.getLastModified()).after(new Date(donneeLocal.lastModified()))) {
                                    lastModifiedLocal = donneeLocal.lastModified();
                                    isRecent = true;
                                }
                            }
                        }
                    }
                }
                if (isNew == true | isRecent == true) {
                    if (params.getIdExercice() == -1) {   //Synchronisation légère: on ne travaille que sur le dossier ANNEE
                        if (donneeOnline.getDossier().equals(UtilObjet.DOSSIER_ANNEE)) {
                            if (!A_Charger_listeDonneeATelecharger.contains(donneeOnline)) {
                                A_Charger_listeDonneeATelecharger.add(donneeOnline);
                                System.out.println(" - (" + lastModifiedLocal + "|" + donneeOnline.getLastModified() + ") " + donneeOnline.toString());
                                rep = true;
                            }
                        }
                    } else {
                        if (!A_Charger_listeDonneeATelecharger.contains(donneeOnline)) {
                            A_Charger_listeDonneeATelecharger.add(donneeOnline);
                            System.out.println(" - (" + lastModifiedLocal + "|" + donneeOnline.getLastModified() + ") " + donneeOnline.toString());
                            rep = true;
                        }
                    }

                }
                eps.onAfficheProgression(total, actuel, Str_Etape + ": Recherche - Téléchargeables sur PC (" + rep + "): " + donneeOnline.getDossier() + " - " + donneeOnline.getLastModified());
                actuel++;
            }
        }
    }

    public void G_recherche_donnees_pc_chargeables_sur_serveur() throws NumberFormatException {
        System.out.println("****** G_recherche_donnees_pc_chargeables_sur_serveur");
        if (eps != null) {
            A_Charger_listeDonneeAChargerSurServeur.removeAllElements();
            for (PhotoRubriqueLocal rubriquePC : photoPC.getRubriques()) {
                int actuel = 1;
                int total = rubriquePC.getContenus().size();
                boolean rep = false;
                for (File donneeLocal : rubriquePC.getContenus()) {
                    boolean isRecent = false;
                    boolean isNew = true;
                    Date dateLocal = null;
                    Date dateDistante = null;
                    for (PhotoDonneeEnligne donneeOnline : photoServeur.getListeDonneeEnligne()) {
                        if (donneeOnline.getDossier().equals(rubriquePC.getNom())) {
                            dateLocal = new Date(Long.parseLong(donneeLocal.lastModified() + ""));
                            dateDistante = new Date(Long.parseLong(donneeOnline.getLastModified() + ""));

                            if (donneeLocal.getName().equals(donneeOnline.getId() + "")) {
                                isNew = false;
                                if (dateLocal.after(dateDistante)) {
                                    isRecent = true;
                                }
                            }
                        }

                    }

                    if (isNew == true || isRecent == true) {
                        if (params.getIdExercice() == -1) {   //Synchronisation légère: on ne travaille que sur le dossier ANNEE
                            if (rubriquePC.getNom().equals(UtilObjet.DOSSIER_ANNEE)) {
                                Object donneeLocal_brutte = eps.onLoadData(rubriquePC.getClasse(), rubriquePC.getNom(), Integer.parseInt(donneeLocal.getName().trim()));
                                PhotoDonneePC rawData = new PhotoDonneePC(Integer.parseInt(donneeLocal.getName().trim()), donneeLocal.lastModified(), rubriquePC.getNom(), donneeLocal_brutte, params.getIdEcole());
                                if (!A_Charger_listeDonneeAChargerSurServeur.contains(rawData)) {
                                    A_Charger_listeDonneeAChargerSurServeur.add(rawData);
                                    System.out.println(" - " + rawData.toString());
                                    rep = true;
                                }
                            }
                        } else {
                            Object donneeLocal_brutte = eps.onLoadData(rubriquePC.getClasse(), rubriquePC.getNom(), Integer.parseInt(donneeLocal.getName().trim()));
                            PhotoDonneePC rawData = new PhotoDonneePC(Integer.parseInt(donneeLocal.getName().trim()), donneeLocal.lastModified(), rubriquePC.getNom(), donneeLocal_brutte, params.getIdEcole());
                            if (!A_Charger_listeDonneeAChargerSurServeur.contains(rawData)) {
                                A_Charger_listeDonneeAChargerSurServeur.add(rawData);
                                System.out.println(" - " + rawData.toString());
                                rep = true;
                            }
                        }
                    }
                    eps.onAfficheProgression(total, actuel, Str_Etape + ": Recherche - Chargeables sur Serveur (" + rep + "): " + rubriquePC.getNom() + "/" + donneeLocal.getName() + " - " + donneeLocal.lastModified());
                    actuel++;
                }
            }
        }
    }

    public void H_ecritureDataSurPC() {
        System.out.println("****** H_ecritureDataSurPC");
        int total = dataServeur.getListeEnregistrements().size();
        int actuel = 1;
        for (EnregistrementServeur enregistrement : dataServeur.getListeEnregistrements()) {
            if (eps != null) {
                boolean rep = false;
                if (params.getIdExercice() == -1) { //Synchronisation légère: on travaille uniquement sur le dossier ANNEE
                    if (enregistrement.getDossier().equals(UtilObjet.DOSSIER_ANNEE)) {
                        rep = eps.onSaveData(enregistrement);
                        if (rep == true) {
                            System.out.println(" - (" + enregistrement.getLastModified() + " ou " + new Date(enregistrement.getLastModified()) + ") " + enregistrement.toString());
                        }
                    }
                } else {
                    rep = eps.onSaveData(enregistrement);
                    if (rep == true) {
                        System.out.println(" - (" + enregistrement.getLastModified() + " ou " + new Date(enregistrement.getLastModified()) + ") " + enregistrement.toString());
                    }
                }
                eps.onAfficheProgression(total, actuel, Str_Etape + ": Enregistrement sur PC (" + rep + "): " + enregistrement.getDossier() + " - " + enregistrement.getDonnee());
                actuel++;
            }
        }
    }

    public static void main(String[] a) {
        String email = "sulabosiog@gmail.com";
        String motDePasse = "abc";
        int idEcole = 2;
        int idExercice = 1;
        SyncParametres params = new SyncParametres(idEcole, idExercice, motDePasse, email);

        new Synchronisateur(params, new SynchronisateurListener() {
            @Override
            public boolean onDeleteSignature(PhotoSuppressionDistante photoSignature) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public boolean onSaveManifeste(PhotoManifesteServeur photoManifeste) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public Vector<PhotoSuppressionLocale> onCopieSuppressionsDuPC() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public Vector<PhotoManifestePC> onCopieManifestesDuPC() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public PhotoDisqueLocal onCopieLePC() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public Object onLoadData(Class classe, String dossier, int id) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public boolean onSaveData(EnregistrementServeur enregistrementServeur) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void onAfficheProgression(int total, int actuel, String message) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void onSuccess(String message) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void onEchec(String message) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        }).demarrer();
    }
}
