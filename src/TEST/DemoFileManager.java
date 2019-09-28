/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TEST;

import ICONES.Icones;
import SOURCES.Callback.EcouteurLongin;
import SOURCES.Callback.EcouteurOuverture;
import SOURCES.Callback.EcouteurSuppression;
import SOURCES.Callback.EcouteurSynchronisation;
import SOURCES.Objets.FileManager;
import SOURCES.Objets.PaiementLicence;
import SOURCES.Objets.Registre;
import SOURCES.Objets.Session;
import SOURCES.Utilitaires.UtilFileManager;
import Source.Callbacks.ConstructeurCriteres;
import Source.Callbacks.EcouteurNavigateurPages;
import Source.Callbacks.EcouteurStandard;
import Source.Interface.InterfaceCharge;
import Source.Interface.InterfaceRevenu;
import Source.Objet.Charge;
import Source.Objet.Revenu;
import Source.Objet.UtilObjet;
import Source.Objet.Utilisateur;
import Sources.CHAMP_LOCAL;
import Sources.PROPRIETE;
import Sources.UI.JS2BPanelPropriete;
import java.awt.event.KeyEvent;
import java.util.Date;
import java.util.Vector;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author user
 */
public class DemoFileManager extends javax.swing.JFrame {
    
    /**
     * Creates new form Principal
     */
    public FileManager fm = null;
    private JFrame moi = null;
    private Icones icones = null;
    public Utilisateur userConnected = null;
    public Session sessionConnected = null;

    public DemoFileManager() {
        initComponents();
        tabPrincipal.removeAll();
        moi = this;
        initIcones();
        fm = new FileManager("http://www.visiterlardc.com/s2b", "processeurS2B.php", btLogo);
        progressUser.setVisible(false);
        moi.setTitle("Authentification...");
        
        fm.fm_setEcouteurFenetre(moi);  // On écoute désormais les mouvements de la fenetre
        
        fm.fm_loadSession(new EcouteurLongin() {
            @Override
            public void onConnected(String message, Session session) {
                if (session != null) {
                    sessionConnected = session;
                    moi.setTitle(session.getUtilisateur().getNom() + " | " + session.getEntreprise().getNom());
                    tabPrincipal.add("Bien venu " + session.getUtilisateur().getPrenom() + " ! - " + session.getEntreprise().getNom(), panUser);
                    tabPrincipal.remove(panLogin);
                    //chInfosLicence.setText(session.get);
                    showRegistre();
                    ecouterOutilsDeNavigation();
                } else {
                    //On ne fait rien
                    labLogin.setText("Utilisateur non reconnu !, Merci de saisie vos identifiants.");
                }
                etat.setText(message);
            }

            @Override
            public void onEchec(String message) {
                //tabPrincipal.remove(panUser);
                etat.setText(message);
                tabPrincipal.add("Login", panLogin);
            }

            @Override
            public void onProcessing(String message) {
                //tabPrincipal.remove(panUser);
                etat.setText(message);
            }
        });

    }

    private void ecouterOutilsDeNavigation() {
        navigateurPages.initialiser(this, new EcouteurNavigateurPages() {
            @Override
            public void onRecharge(String motCle, int pageActuelle, int taillePage, JS2BPanelPropriete criteresAvances) {
                //System.out.println("reload...");
                progressUser.setIndeterminate(true);
                Class nomClasse = null;
                String table = "";
                if (chRevenu.isSelected()) {
                    nomClasse = Revenu.class;
                    table = UtilObjet.DOSSIER_REVENU;
                } else {
                    nomClasse = Charge.class;
                    table = UtilObjet.DOSSIER_CHARGE;
                }
                ouvrirTout(motCle, nomClasse, table, pageActuelle, taillePage, criteresAvances);
            }

        }, new ConstructeurCriteres() {
            @Override
            public JS2BPanelPropriete onInitialise() {

                Vector listeClasses = new Vector();
                listeClasses.add("TOUTES LES CLASSES");
                listeClasses.add("G1");
                listeClasses.add("G2");

                Vector listeEnseignant = new Vector();
                listeEnseignant.add("TOUS LES ENSEIGNANTS");
                listeEnseignant.add("Prof Serge SULA BOSIO");
                listeEnseignant.add("Prof Pavel SULA BOTOWAMUNGU");

                Vector listeSolvabilite = new Vector();
                listeSolvabilite.add("SOLVABLES & INSOLVABLES");
                listeSolvabilite.add("SOLVABLES");
                listeSolvabilite.add("INSOLVABLES");

                JS2BPanelPropriete panProp = new JS2BPanelPropriete(icones.getFiltrer_01(), "Critères avancés", true);
                panProp.viderListe();
                panProp.AjouterPropriete(new CHAMP_LOCAL(icones.getClasse_01(), "Classe", "cls", listeClasses, "", PROPRIETE.TYPE_CHOIX_LISTE), 0);
                panProp.AjouterPropriete(new CHAMP_LOCAL(icones.getClasse_01(), "Enseignant", "cls", listeEnseignant, "", PROPRIETE.TYPE_CHOIX_LISTE), 0);
                panProp.AjouterPropriete(new CHAMP_LOCAL(icones.getClasse_01(), "Solvabilité", "cls", listeSolvabilite, "", PROPRIETE.TYPE_CHOIX_LISTE), 0);
                panProp.AjouterPropriete(new CHAMP_LOCAL(icones.getClasse_01(), "Date début", "cls", null, new Date(), PROPRIETE.TYPE_CHOIX_DATE), 0);
                panProp.AjouterPropriete(new CHAMP_LOCAL(icones.getClasse_01(), "Date fin", "cls", null, new Date(), PROPRIETE.TYPE_CHOIX_DATE), 0);

                return panProp;
                //return null;
            }
        });
        navigateurPages.reload();
    }

    private void showRegistre() {
        if (chRevenu.isSelected()) {
            updateInfosRegistre(UtilObjet.DOSSIER_REVENU);
        } else {
            updateInfosRegistre(UtilObjet.DOSSIER_CHARGE);
        }
    }

    private void enreg_Objet(Object newObj, String dossier) {
        ecran.setText("");
        
        fm.fm_enregistrer(newObj, dossier, new EcouteurStandard() {
            @Override
            public void onDone(String message) {
                etat.setText(message);
                ecran.append("Objets enregistrés avec nouveau ID Automatique:\n");
                ecran.append(" * " + newObj.toString() + "\n");
                updateInfosRegistre(dossier);
            }

            @Override
            public void onError(String message) {
                etat.setText(message);
            }

            @Override
            public void onProcessing(String message) {
                etat.setText(message);
            }
        });

    }

    private void enreg_Groupe(Vector listeObjts, String table) {
        ecran.setText("");
        progressUser.setIndeterminate(true);
        int pauseMSecondes = Integer.parseInt(chVitesseTraitement.getValue()+"");
        fm.fm_enregistrer(pauseMSecondes, listeObjts, table, new EcouteurStandard() {
            @Override
            public void onDone(String message) {
                progressUser.setIndeterminate(false);
                etat.setText(message + "\n");
                ecran.append("Anciens Objets avec IDs modifiés automatiquement:\n");
                for (Object o : listeObjts) {
                    ecran.append(" -- " + o.toString() + "\n");
                }
                updateInfosRegistre(table);
            }

            @Override
            public void onError(String message) {
                etat.setText(message);
                progressUser.setIndeterminate(false);
            }

            @Override
            public void onProcessing(String message) {
                etat.setText(message);
            }
        });
    }

    private void ouvrirTout(String motCle, Class NomClasse, String table, int pageActuelle, int taillePage, JS2BPanelPropriete criteresAvances) {
        new Thread() {
            public void run() {
                ecran.setText("");
                Vector donnees = new Vector();
                //System.out.println("Mot clé: " + motCle);
                navigateurPages.patienter(true, "Chargement...");
                if (criteresAvances != null) {
                    System.out.println("Critères de filtrage approfondis:");
                    for (PROPRIETE prop : criteresAvances.getListePro()) {
                        System.out.println(" * " + prop.getNom() + ": " + prop.getValeurSelectionne());
                    }
                }
                fm.fm_ouvrirTout(Integer.parseInt(chVitesseTraitement.getValue()+""), NomClasse, table, pageActuelle, taillePage, new EcouteurOuverture() {

                    @Override
                    public boolean isCriteresRespectes(Object object) {
                        return true;
                    }

                    @Override
                    public void onElementLoaded(String message, Object data) {
                        ecran.append(" * " + data.toString() + "\n");
                        navigateurPages.patienter(true, data.toString());
                        donnees.add(data);
                    }

                    @Override
                    public void onDone(String message, int resultatTotal, Vector resultatTotalObjets) {
                        etat.setText(message);
                        progressUser.setIndeterminate(false);
                        navigateurPages.setInfos(resultatTotal, donnees.size());
                        navigateurPages.patienter(false, "Prêt");
                    }

                    @Override
                    public void onError(String message) {
                        etat.setText(message);
                        progressUser.setIndeterminate(false);
                        navigateurPages.patienter(false, "Prêt");
                    }

                    @Override
                    public void onProcessing(String message) {
                        etat.setText(message);
                    }
                });
            }
        }.start();
    }

    private void updateInfosRegistre(String dossier) {
        Registre registre = fm.fm_getRegistre(dossier);
        if (registre != null) {
            chLastId.setText("Last ID: " + registre.getDernierID() + " || " + registre.getDateEnregistrement().toLocaleString());
        } else {
            chLastId.setText("Dossier vide !");
        }

        if (fm.fm_getSession() != null) {
            PaiementLicence paie = fm.fm_getSession().getPaiement();
            if (paie != null) {
                chInfosLicence.setText("Votre licence expire le " + UtilFileManager.convertDatePaiement(paie.getDateExpiration()).toLocaleString());
                System.out.println("Echéance Licence: " + UtilFileManager.convertDatePaiement(paie.getDateExpiration()).toLocaleString());
            }
        }
    }

    private void reinitRegistre(String dossier) {
        if (fm.fm_reinitialiserRegistre(dossier)) {
            etat.setText("Registre réinitialisé !");
        } else {
            etat.setText("Erreur  lors de la réinitialisation!");
        }
        updateInfosRegistre(dossier);
    }

    private void supprimer(String table, String id, long signature) {
        ecran.setText("");
        
        boolean rep = fm.fm_supprimer(table, Integer.parseInt(id), signature);
        if (rep == true) {
            ecran.append("Suppression de " + id + " dans " + table + " reussie !");
            etat.setText("Suppression effectuée  avec succès.");
        } else {
            ecran.append("Echec");
            etat.setText("Echec de suppression");
        }
    }

    private void supprimerGroupe(String table, String[] tabIDS, Object[] tabSignatures) {
        ecran.setText("");
        progressUser.setIndeterminate(true);
        fm.fm_supprimerTout(table, tabIDS, tabSignatures, new EcouteurSuppression() {
            @Override
            public void onDone(String message, Object[] idsNonSupprimes) {
                ecran.setText(message);
                etat.setText(message);
                progressUser.setIndeterminate(false);

                if (idsNonSupprimes.length != 0) {
                    ecran.append("Ids des objets non supprimés:\n");
                    for (Object oId : idsNonSupprimes) {
                        ecran.append(" * " + oId + "\n");
                    }
                } else {
                    ecran.setText("les " + tabIDS.length + " ids sont tous supprimés !");
                }
            }

            @Override
            public void onError(String message) {
                ecran.setText(message);
                etat.setText(message);
                progressUser.setIndeterminate(false);
            }

            @Override
            public void onProcessing(String message) {
                ecran.setText(message);
                etat.setText(message);
            }
        });
    }

    private void viderTout(Class classe, String table) {
        ecran.setText("");
        progressUser.setIndeterminate(true);
        fm.fm_supprimerTout(classe, table, new EcouteurSuppression() {
            @Override
            public void onDone(String message, Object[] idsNonSupprimes) {
                etat.setText(message);
                progressUser.setIndeterminate(false);
                if (idsNonSupprimes.length != 0) {
                    ecran.setText("Objets non supprimés:\n");
                    for (Object oID : idsNonSupprimes) {
                        ecran.setText(" * " + oID + "\n");
                    }
                } else {
                    ecran.setText("Le dossier vidé!\n");
                }

                navigateurPages.reload();
            }

            @Override
            public void onError(String message) {
                etat.setText(message);
                progressUser.setIndeterminate(false);
            }

            @Override
            public void onProcessing(String message) {
                etat.setText(message);
            }
        });
    }

    private void ouvrirObjet(Class NomClasse, String table, String idObj) {
        ecran.setText("");
        Object obj = fm.fm_ouvrir(NomClasse, table, Integer.parseInt(idObj + ""));
        if (obj != null) {
            ecran.setText(obj.toString() + "\n");
        } else {
            ecran.setText("Objet introuvable !");
        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        tabPrincipal = new javax.swing.JTabbedPane();
        panUser = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        ecran = new javax.swing.JTextArea();
        jToolBar1 = new javax.swing.JToolBar();
        btLogo = new javax.swing.JButton();
        jSeparator6 = new javax.swing.JToolBar.Separator();
        btSynchroniser = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        btOuvrir = new javax.swing.JButton();
        btLister = new javax.swing.JButton();
        btAjouterUn = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JToolBar.Separator();
        btVider = new javax.swing.JButton();
        btSupprimer = new javax.swing.JButton();
        btReinitRegistre = new javax.swing.JButton();
        jSeparator7 = new javax.swing.JToolBar.Separator();
        btDeconnexion = new javax.swing.JButton();
        navigateurPages = new Source.UI.NavigateurPages();
        jToolBar2 = new javax.swing.JToolBar();
        chRevenu = new javax.swing.JRadioButton();
        chCharge = new javax.swing.JRadioButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        jLabel1 = new javax.swing.JLabel();
        chIdExercice = new javax.swing.JSpinner();
        jSeparator4 = new javax.swing.JToolBar.Separator();
        jLabel2 = new javax.swing.JLabel();
        chVitesseTraitement = new javax.swing.JSpinner();
        jSeparator5 = new javax.swing.JToolBar.Separator();
        ecranObjet = new javax.swing.JLabel();
        jToolBar3 = new javax.swing.JToolBar();
        chLastId = new javax.swing.JLabel();
        jSeparator8 = new javax.swing.JToolBar.Separator();
        chInfosLicence = new javax.swing.JLabel();
        jSeparator9 = new javax.swing.JToolBar.Separator();
        etat = new javax.swing.JLabel();
        progressUser = new javax.swing.JProgressBar();
        panLogin = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        chIdEcole = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        labLogin = new javax.swing.JLabel();
        progressLogin = new javax.swing.JProgressBar();
        jLabel8 = new javax.swing.JLabel();
        chPassWord = new javax.swing.JPasswordField();
        chEmail = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        ecran.setColumns(20);
        ecran.setRows(5);
        jScrollPane1.setViewportView(ecran);

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        btLogo.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btLogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMG/Facture03.png"))); // NOI18N
        btLogo.setText("Logo");
        btLogo.setFocusable(false);
        btLogo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btLogo.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(btLogo);
        jToolBar1.add(jSeparator6);

        btSynchroniser.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btSynchroniser.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMG/Facture03.png"))); // NOI18N
        btSynchroniser.setText("Synchroniser");
        btSynchroniser.setFocusable(false);
        btSynchroniser.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btSynchroniser.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btSynchroniser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btSynchroniserActionPerformed(evt);
            }
        });
        jToolBar1.add(btSynchroniser);
        jToolBar1.add(jSeparator2);

        btOuvrir.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btOuvrir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMG/Facture03.png"))); // NOI18N
        btOuvrir.setText("Ouvrir");
        btOuvrir.setFocusable(false);
        btOuvrir.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btOuvrir.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btOuvrir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btOuvrirActionPerformed(evt);
            }
        });
        jToolBar1.add(btOuvrir);

        btLister.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btLister.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMG/Facture03.png"))); // NOI18N
        btLister.setText("Lister");
        btLister.setFocusable(false);
        btLister.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btLister.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btLister.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btListerActionPerformed(evt);
            }
        });
        jToolBar1.add(btLister);

        btAjouterUn.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btAjouterUn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMG/Facture03.png"))); // NOI18N
        btAjouterUn.setText("Ajouter");
        btAjouterUn.setFocusable(false);
        btAjouterUn.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btAjouterUn.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btAjouterUn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btAjouterUnActionPerformed(evt);
            }
        });
        jToolBar1.add(btAjouterUn);
        jToolBar1.add(jSeparator3);

        btVider.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btVider.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMG/Facture03.png"))); // NOI18N
        btVider.setText("Vider");
        btVider.setFocusable(false);
        btVider.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btVider.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btVider.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btViderActionPerformed(evt);
            }
        });
        jToolBar1.add(btVider);

        btSupprimer.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btSupprimer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMG/Facture03.png"))); // NOI18N
        btSupprimer.setText("Supprimer");
        btSupprimer.setFocusable(false);
        btSupprimer.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btSupprimer.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btSupprimer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btSupprimerActionPerformed(evt);
            }
        });
        jToolBar1.add(btSupprimer);

        btReinitRegistre.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btReinitRegistre.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMG/Facture03.png"))); // NOI18N
        btReinitRegistre.setText("Reinitialiser");
        btReinitRegistre.setFocusable(false);
        btReinitRegistre.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btReinitRegistre.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btReinitRegistre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btReinitRegistreActionPerformed(evt);
            }
        });
        jToolBar1.add(btReinitRegistre);
        jToolBar1.add(jSeparator7);

        btDeconnexion.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btDeconnexion.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMG/Facture03.png"))); // NOI18N
        btDeconnexion.setText("Deconnexion");
        btDeconnexion.setFocusable(false);
        btDeconnexion.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btDeconnexion.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btDeconnexion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btDeconnexionActionPerformed(evt);
            }
        });
        jToolBar1.add(btDeconnexion);

        jToolBar2.setFloatable(false);
        jToolBar2.setRollover(true);

        buttonGroup1.add(chRevenu);
        chRevenu.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        chRevenu.setText("Revenu");
        chRevenu.setFocusable(false);
        chRevenu.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        chRevenu.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar2.add(chRevenu);

        buttonGroup1.add(chCharge);
        chCharge.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        chCharge.setSelected(true);
        chCharge.setText("Charge");
        chCharge.setFocusable(false);
        chCharge.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        chCharge.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        chCharge.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar2.add(chCharge);
        jToolBar2.add(jSeparator1);

        jLabel1.setText("ID Exercice:");
        jToolBar2.add(jLabel1);

        chIdExercice.setModel(new javax.swing.SpinnerNumberModel(2, null, null, 1));
        jToolBar2.add(chIdExercice);
        jToolBar2.add(jSeparator4);

        jLabel2.setText("Vitesse de chargement (en Millisecondes)");
        jToolBar2.add(jLabel2);

        chVitesseTraitement.setModel(new javax.swing.SpinnerNumberModel(100, null, null, 1));
        jToolBar2.add(chVitesseTraitement);
        jToolBar2.add(jSeparator5);

        ecranObjet.setText("RAS");
        jToolBar2.add(ecranObjet);

        jToolBar3.setFloatable(false);
        jToolBar3.setRollover(true);

        chLastId.setText("Dernier ID: Null");
        jToolBar3.add(chLastId);
        jToolBar3.add(jSeparator8);

        chInfosLicence.setText("Infos Licence");
        jToolBar3.add(chInfosLicence);
        jToolBar3.add(jSeparator9);

        etat.setText("Prêt.");
        jToolBar3.add(etat);
        jToolBar3.add(progressUser);

        javax.swing.GroupLayout panUserLayout = new javax.swing.GroupLayout(panUser);
        panUser.setLayout(panUserLayout);
        panUserLayout.setHorizontalGroup(
            panUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(panUserLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(navigateurPages, javax.swing.GroupLayout.DEFAULT_SIZE, 626, Short.MAX_VALUE)
                    .addComponent(jToolBar2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jToolBar3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        panUserLayout.setVerticalGroup(
            panUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panUserLayout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(navigateurPages, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jToolBar2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 237, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jToolBar3, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        tabPrincipal.addTab("Espace USER", panUser);

        jButton2.setText("Connexion");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("Email");

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("ID école");

        chIdEcole.setText("1");
        chIdEcole.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                chIdEcoleKeyReleased(evt);
            }
        });

        jLabel7.setText("Créer un nouveau compte");

        labLogin.setText("Prêt.");

        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText("Mot de passe utilisateur");

        chPassWord.setText("abc");
        chPassWord.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                chPassWordKeyReleased(evt);
            }
        });

        chEmail.setText("sulabosiog@gmail.com");

        javax.swing.GroupLayout panLoginLayout = new javax.swing.GroupLayout(panLogin);
        panLogin.setLayout(panLoginLayout);
        panLoginLayout.setHorizontalGroup(
            panLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panLoginLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(progressLogin, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panLoginLayout.createSequentialGroup()
                        .addGroup(panLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 349, Short.MAX_VALUE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(chIdEcole)
                            .addComponent(chPassWord, javax.swing.GroupLayout.DEFAULT_SIZE, 273, Short.MAX_VALUE)
                            .addComponent(chEmail)))
                    .addComponent(labLogin, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        panLoginLayout.setVerticalGroup(
            panLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panLoginLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(chEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(chPassWord, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(chIdEcole, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(progressLogin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labLogin)
                .addContainerGap(300, Short.MAX_VALUE))
        );

        tabPrincipal.addTab("Espace Login", panLogin);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(tabPrincipal)
                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabPrincipal)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        login();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void chIdEcoleKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_chIdEcoleKeyReleased
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            login();
        }
    }//GEN-LAST:event_chIdEcoleKeyReleased

    private void chPassWordKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_chPassWordKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_chPassWordKeyReleased

    private void btDeconnexionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btDeconnexionActionPerformed
        // TODO add your handling code here:
        logout();
    }//GEN-LAST:event_btDeconnexionActionPerformed

    private void btSynchroniserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btSynchroniserActionPerformed
        // TODO add your handling code here:
        synchroniser();
    }//GEN-LAST:event_btSynchroniserActionPerformed

    private void btAjouterUnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btAjouterUnActionPerformed
        // TODO add your handling code here:
        String rep = JOptionPane.showInputDialog(this, "Veuillez saisir le nombre d'element à créer", "ID", JOptionPane.YES_NO_OPTION);
        if (rep != null) {
            int nb = Integer.parseInt(rep.trim());
            if(nb == 1){
                ajouterUn();
            }else if(nb > 1){
                ajouterPlus(nb);
            }else{
                JOptionPane.showMessageDialog(this, "Valeur INVALIDE ! (" + rep + ").");
            }
        }
        
    }//GEN-LAST:event_btAjouterUnActionPerformed

    private void btViderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btViderActionPerformed
        // TODO add your handling code here:
        viderTout();
    }//GEN-LAST:event_btViderActionPerformed

    private void btListerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btListerActionPerformed
        // TODO add your handling code here:
        lister();
    }//GEN-LAST:event_btListerActionPerformed

    private void btOuvrirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btOuvrirActionPerformed
        // TODO add your handling code here:
        ouvrir();
    }//GEN-LAST:event_btOuvrirActionPerformed

    private void btSupprimerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btSupprimerActionPerformed
        // TODO add your handling code here:
        String rep = JOptionPane.showInputDialog(this, "Veuillez saisir l'ID (séparés par des virgules si plusieurs)", "ID", JOptionPane.YES_NO_OPTION);
        if (rep != null) {
            if (rep.contains(",")) {
                supprimerPlus(rep);
            } else {
                supprimerUn(rep);
            }
        }

    }//GEN-LAST:event_btSupprimerActionPerformed

    private void btReinitRegistreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btReinitRegistreActionPerformed
        // TODO add your handling code here:
        reinitRegistre();
    }//GEN-LAST:event_btReinitRegistreActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(DemoFileManager.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DemoFileManager.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DemoFileManager.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DemoFileManager.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new DemoFileManager().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btAjouterUn;
    private javax.swing.JButton btDeconnexion;
    private javax.swing.JButton btLister;
    private javax.swing.JButton btLogo;
    private javax.swing.JButton btOuvrir;
    private javax.swing.JButton btReinitRegistre;
    private javax.swing.JButton btSupprimer;
    private javax.swing.JButton btSynchroniser;
    private javax.swing.JButton btVider;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JRadioButton chCharge;
    private javax.swing.JTextField chEmail;
    private javax.swing.JTextField chIdEcole;
    private javax.swing.JSpinner chIdExercice;
    private javax.swing.JLabel chInfosLicence;
    private javax.swing.JLabel chLastId;
    private javax.swing.JPasswordField chPassWord;
    private javax.swing.JRadioButton chRevenu;
    private javax.swing.JSpinner chVitesseTraitement;
    private javax.swing.JTextArea ecran;
    private javax.swing.JLabel ecranObjet;
    private javax.swing.JLabel etat;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JToolBar.Separator jSeparator3;
    private javax.swing.JToolBar.Separator jSeparator4;
    private javax.swing.JToolBar.Separator jSeparator5;
    private javax.swing.JToolBar.Separator jSeparator6;
    private javax.swing.JToolBar.Separator jSeparator7;
    private javax.swing.JToolBar.Separator jSeparator8;
    private javax.swing.JToolBar.Separator jSeparator9;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToolBar jToolBar2;
    private javax.swing.JToolBar jToolBar3;
    private javax.swing.JLabel labLogin;
    private Source.UI.NavigateurPages navigateurPages;
    private javax.swing.JPanel panLogin;
    private javax.swing.JPanel panUser;
    private javax.swing.JProgressBar progressLogin;
    private javax.swing.JProgressBar progressUser;
    private javax.swing.JTabbedPane tabPrincipal;
    // End of variables declaration//GEN-END:variables

    private void login() {
        fm.fm_login(chIdEcole.getText().trim(), chEmail.getText().trim(), chPassWord.getText().trim(), new EcouteurLongin() {
            @Override
            public void onConnected(String message, Session session) {
                progressLogin.setIndeterminate(false);
                if (session != null) {
                    userConnected = session.getUtilisateur();
                    sessionConnected = session;
                    moi.setTitle(session.getUtilisateur().getNom() + " | " + session.getEntreprise().getNom());
                    tabPrincipal.remove(panLogin);
                    tabPrincipal.add("Bien venu " + session.getUtilisateur().getPrenom() + " | " + session.getEntreprise().getNom(), panUser);
                    showRegistre();
                    ecouterOutilsDeNavigation();
                }
            }

            @Override
            public void onEchec(String message) {
                labLogin.setText(message);
                progressLogin.setIndeterminate(false);
            }

            @Override
            public void onProcessing(String message) {
                labLogin.setText(message);
                progressLogin.setIndeterminate(true);
            }
        });
    }

    private void logout() {
        fm.fm_logout(new EcouteurStandard() {
            @Override
            public void onDone(String message) {
                tabPrincipal.removeAll();
                tabPrincipal.add("Authentification", panLogin);
                labLogin.setText(message);
            }

            @Override
            public void onError(String message) {
                etat.setText(message);
            }

            @Override
            public void onProcessing(String message) {
                etat.setText(message);
            }
        });
    }
    
    private void synchroniser() {
        int idExercice = Integer.parseInt(chIdExercice.getValue()+"");
        //ATTENTION!! Normalement, on ne devra synchroniser que les Dossier dont le User courant a le droit de CONTROLE
        etat.setText("");
        progressUser.setIndeterminate(true);
        
        fm.fm_synchroniser(sessionConnected.getUtilisateur(), idExercice, new EcouteurSynchronisation() {
            @Override
            public void onSuccess(String message) {
                etat.setText(message);
                progressUser.setIndeterminate(false);
                progressUser.setVisible(false);
            }

            @Override
            public void onEchec(String message) {
                etat.setText(message);
                progressUser.setIndeterminate(false);
                progressUser.setVisible(false);
            }

            @Override
            public void onProcessing(String message, int pourcentage) {
                etat.setText(message);
                //progressUser.setIndeterminate(true);
                progressUser.setValue(pourcentage);
                progressUser.setVisible(true);
            }
        });
    }

    private void ajouterUn() {
        int idExercice = Integer.parseInt(chIdExercice.getValue()+"");
        int idMonnaie = 1;
        long signatureMonnaie = 12450;
        if (chRevenu.isSelected()) {
            Revenu Urevenu = new Revenu(-1, sessionConnected.getEntreprise().getId(), sessionConnected.getUtilisateur().getId(), idExercice, idMonnaie, signatureMonnaie, "Frais Scolaire", "$", 100000, UtilObjet.getSignature(), InterfaceRevenu.BETA_EXISTANT);
            enreg_Objet(Urevenu, UtilObjet.DOSSIER_REVENU);
        } else {
            Charge Ucharge = new Charge(-1, sessionConnected.getEntreprise().getId(), sessionConnected.getUtilisateur().getId(), idExercice, "SALAIRE DU PERSONNEL", 100000, idMonnaie, signatureMonnaie, "$", UtilObjet.getSignature(), InterfaceCharge.BETA_EXISTANT);
            enreg_Objet(Ucharge, UtilObjet.DOSSIER_CHARGE);
        }
    }
    
    private void ajouterPlus(int nb){
        int idExercice = Integer.parseInt(chIdExercice.getValue()+"");
        int idMonnaie = 1;
        long signatureMonnaie = 12450;
        if (chRevenu.isSelected()) {
            Vector<Revenu> Uinputs = new Vector();
            for (int i = 0; i < nb; i++) {
                Revenu Urevenu = new Revenu(-1, sessionConnected.getEntreprise().getId(), sessionConnected.getUtilisateur().getId(), idExercice, idMonnaie, signatureMonnaie, "Revenu_" + i, "$", 100000, UtilObjet.getSignature(), InterfaceRevenu.BETA_EXISTANT);
                Uinputs.add(Urevenu);
            }
            enreg_Groupe(Uinputs, UtilObjet.DOSSIER_REVENU);
        } else {
            Vector<Charge> Uinputs = new Vector();
            for (int i = 0; i < nb; i++) {
                Charge Ucharge = new Charge(-1, sessionConnected.getEntreprise().getId(), sessionConnected.getUtilisateur().getId(), idExercice, "Charge_" + i, 100000, idMonnaie, signatureMonnaie, "$", UtilObjet.getSignature(), InterfaceCharge.BETA_EXISTANT);
                Uinputs.add(Ucharge);
            }
            enreg_Groupe(Uinputs, UtilObjet.DOSSIER_CHARGE);
        }
    }

    private void viderTout() {
        if (chRevenu.isSelected()) {
            viderTout(Revenu.class, UtilObjet.DOSSIER_REVENU);
        } else {
            viderTout(Charge.class, UtilObjet.DOSSIER_CHARGE);
        }
    }

    private void lister() {
        navigateurPages.reload();
    }

    private void initIcones() {
        icones = new Icones();
        btDeconnexion.setIcon(icones.getDéconnecté_03());
        btSynchroniser.setIcon(icones.getSynchroniser_03());
        btAjouterUn.setIcon(icones.getAjouter_03());
        btVider.setIcon(icones.getSupprimer_03());
        btSupprimer.setIcon(icones.getAnnuler_03());
        btLister.setIcon(icones.getListe_03());
        btOuvrir.setIcon(icones.getPortrait_03());
        btReinitRegistre.setIcon(icones.getFiltrer_03());
    }

    private void ouvrir() {
        String rep = JOptionPane.showInputDialog(this, "Veuillez saisir l'ID", "ID", JOptionPane.YES_NO_OPTION);
        if (rep != null) {
            if (chRevenu.isSelected()) {
                ouvrirObjet(Revenu.class, UtilObjet.DOSSIER_REVENU, rep);
            } else {
                ouvrirObjet(Charge.class, UtilObjet.DOSSIER_CHARGE, rep);
            }
        }
    }

    private void supprimerUn(String ID) {
        try {
            int idSupp = Integer.parseInt(ID.trim().split(",")[0]);
            if (chRevenu.isSelected()) {
                String dossier = UtilObjet.DOSSIER_REVENU;
                Revenu revSup = (Revenu) fm.fm_ouvrir(Revenu.class, dossier, idSupp);
                supprimer(dossier, idSupp + "", revSup.getSignature());
            } else {
                String dossier = UtilObjet.DOSSIER_CHARGE;
                Charge charSup = (Charge) fm.fm_ouvrir(Charge.class, dossier, idSupp);
                supprimer(dossier, idSupp + "", charSup.getSignature());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void supprimerPlus(String IDs) {
        String[] tabIDS = IDs.trim().split(",");
        Object[] tabSignatures;
        if (chRevenu.isSelected()) {
            String dossier = UtilObjet.DOSSIER_REVENU;
            tabSignatures = fm.fm_getSignatures(Revenu.class, dossier, tabIDS);
            supprimerGroupe(dossier, tabIDS, tabSignatures);
        } else {
            String dossier = UtilObjet.DOSSIER_CHARGE;
            tabSignatures = fm.fm_getSignatures(Charge.class, dossier, tabIDS);
            supprimerGroupe(dossier, tabIDS, tabSignatures);
        }

    }

    private void reinitRegistre() {
        if (chRevenu.isSelected()) {
            String dossier = UtilObjet.DOSSIER_REVENU;
            updateInfosRegistre(dossier);
            reinitRegistre(dossier);
        } else {
            String dossier = UtilObjet.DOSSIER_CHARGE;
            updateInfosRegistre(dossier);
            reinitRegistre(dossier);
        }
    }
}
