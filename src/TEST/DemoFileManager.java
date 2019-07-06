/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TEST;

import SOURCES.Callback.EcouteurLongin;
import SOURCES.Callback.EcouteurOuverture;
import SOURCES.Callback.EcouteurStandard;
import SOURCES.Callback.EcouteurSuppression;
import SOURCES.Interfaces.InterfaceUtilisateur;
import SOURCES.Objets.FileManager;
import SOURCES.Objets.Paiement;
import SOURCES.Objets.Registre;
import SOURCES.Objets.Session;
import SOURCES.Objets.Utilisateur;
import SOURCES.Utilitaires.Util;
import java.awt.event.KeyEvent;
import java.util.Date;
import java.util.Vector;
import javax.swing.JFrame;

/**
 *
 * @author user
 */
public class DemoFileManager extends javax.swing.JFrame {

    /**
     * Creates new form Principal
     */
    public FileManager fm = new FileManager("http://www.visiterlardc.com/s2b/processeurS2B.php");
    private JFrame moi = null;

    public DemoFileManager() {
        initComponents();
        tabPrincipal.removeAll();
        moi = this;
        
        moi.setTitle("Authentification...");
        
        fm.fm_setEcouteurFenetre(moi);  // On écoute désormais les mouvements de la fenetre
        
        
        fm.fm_loadSession(new EcouteurLongin() {
            @Override
            public void onConnected(String message, Session session) {
                if (session != null) {
                    moi.setTitle(session.getUtilisateur().getNom() + " | " + session.getEntreprise().getNom());
                    tabPrincipal.add("Bien venu " + session.getUtilisateur().getPrenom() + " ! - " + session.getEntreprise().getNom(), panUser);
                    tabPrincipal.remove(panLogin);
                    //chInfosLicence.setText(session.get);
                    showRegistre();
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

    private void showRegistre() {
        if (chUtilisateur.isSelected()) {
            updateInfosRegistre(dossierUtilisateur.getText());
        } else {
            updateInfosRegistre(dossierFrais.getText());
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
        int pauseMSecondes = Integer.parseInt(chVitesseTraitement.getText());
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

    private void listerDossier(Class NomClasse, String table) {
        ecran.setText("");
        progressUser.setIndeterminate(true);
        fm.fm_ouvrirTout(Integer.parseInt(chVitesseTraitement.getText().trim()), NomClasse, table, new EcouteurOuverture() {
            @Override
            public void onDone(String message, Vector data) {
                etat.setText(message);
                progressUser.setIndeterminate(false);
                if (data.size() != 0) {
                    ecran.append("Liste d'enregistrements:\n");
                    for (Object oRetrieved : data) {
                        ecran.append(" * " + oRetrieved.toString() + "\n");
                    }
                } else {
                    ecran.setText("Dossier vide !");
                }

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

    private void updateInfosRegistre(String dossier) {
        Registre registre = fm.fm_getRegistre(dossier);
        if (registre != null) {
            chLastId.setText("Last ID: " + registre.getDernierID() + " || " + registre.getDateEnregistrement().toLocaleString());
        } else {
            chLastId.setText("Dossier vide !");
        }

        if (fm.fm_getSession() != null) {
            Paiement paie = fm.fm_getSession().getPaiement();
            if (paie != null) {
                chInfosLicence.setText("Votre licence expire le " + Util.convertDatePaiement(paie.getDateExpiration()).toLocaleString());
                System.out.println("Echéance Licence: " + Util.convertDatePaiement(paie.getDateExpiration()).toLocaleString());
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

    private void supprimer(String table, String id) {
        ecran.setText("");

        boolean rep = fm.fm_supprimer(table, Integer.parseInt(id));
        if (rep == true) {
            ecran.append("Suppression de " + id + " dans " + table + " reussie !");
            etat.setText("Suppression effectuée  avec succès.");
        } else {
            ecran.append("Echec");
            etat.setText("Echec de suppression");
        }
    }

    private void supprimerGroupe(String table, String[] tabIDS) {
        ecran.setText("");
        progressUser.setIndeterminate(true);
        fm.fm_supprimerTout(table, tabIDS, new EcouteurSuppression() {
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

    private void viderTout(Class NomClasse, String table) {
        ecran.setText("");
        progressUser.setIndeterminate(true);
        fm.fm_supprimerTout(table, new EcouteurSuppression() {
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

                listerDossier(NomClasse, table);
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
        jPanel1 = new javax.swing.JPanel();
        chUtilisateur = new javax.swing.JRadioButton();
        chFrais = new javax.swing.JRadioButton();
        ecranObjet = new javax.swing.JLabel();
        dossierFrais = new javax.swing.JTextField();
        dossierUtilisateur = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        btSave = new javax.swing.JButton();
        btSaveGroup = new javax.swing.JButton();
        chNB = new javax.swing.JTextField();
        btDelete = new javax.swing.JButton();
        btDeleteGroup = new javax.swing.JButton();
        chIDS = new javax.swing.JTextField();
        btVider = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        btLister = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        chId = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        chVitesseTraitement = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        ecran = new javax.swing.JTextArea();
        jPanel3 = new javax.swing.JPanel();
        chLastId = new javax.swing.JLabel();
        etat = new javax.swing.JLabel();
        btReinitRegistre = new javax.swing.JButton();
        progressUser = new javax.swing.JProgressBar();
        chInfosLicence = new javax.swing.JLabel();
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

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Objets"));

        buttonGroup1.add(chUtilisateur);
        chUtilisateur.setSelected(true);
        chUtilisateur.setText("Utilisateur");
        chUtilisateur.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chUtilisateurItemStateChanged(evt);
            }
        });

        buttonGroup1.add(chFrais);
        chFrais.setText("Frais");
        chFrais.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chFraisItemStateChanged(evt);
            }
        });

        ecranObjet.setText("RAS");

        dossierFrais.setText("FRAIS");

        dossierUtilisateur.setText("UTILISATEUR");

        jButton3.setText("Deconnexion");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(chUtilisateur, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(chFrais, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(dossierFrais)
                            .addComponent(dossierUtilisateur, javax.swing.GroupLayout.DEFAULT_SIZE, 320, Short.MAX_VALUE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(ecranObjet, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(chUtilisateur)
                    .addComponent(dossierUtilisateur, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(chFrais)
                    .addComponent(dossierFrais, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, 13, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ecranObjet)
                    .addComponent(jButton3))
                .addContainerGap())
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Actions"));

        btSave.setText("Enregistrer");
        btSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btSaveActionPerformed(evt);
            }
        });

        btSaveGroup.setText("Enrgistrer en groupe");
        btSaveGroup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btSaveGroupActionPerformed(evt);
            }
        });

        chNB.setText("10");
        chNB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chNBActionPerformed(evt);
            }
        });

        btDelete.setText("Supprimer");
        btDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btDeleteActionPerformed(evt);
            }
        });

        btDeleteGroup.setText("Supprimer en groupe");
        btDeleteGroup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btDeleteGroupActionPerformed(evt);
            }
        });

        chIDS.setText("12,13,14,15,16");

        btVider.setText("Vider");
        btVider.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btViderActionPerformed(evt);
            }
        });

        jLabel1.setText("(objets).");

        btLister.setText("Lister dossier");
        btLister.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btListerActionPerformed(evt);
            }
        });

        jButton1.setText("Ouvrir");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        chId.setText("1");

        jLabel2.setText("(Id)");

        jLabel3.setText("(Ids)");

        jLabel4.setText("Vitesse de chargement de données (en Millisecondes)");

        chVitesseTraitement.setText("100");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(btVider, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btSave, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btDelete, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btSaveGroup, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btDeleteGroup, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btLister, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(chNB, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(chId, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(chIDS, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chVitesseTraitement, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btSave)
                    .addComponent(btSaveGroup)
                    .addComponent(chNB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btDelete)
                    .addComponent(btDeleteGroup)
                    .addComponent(chIDS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btVider)
                    .addComponent(btLister)
                    .addComponent(jButton1)
                    .addComponent(chId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(chVitesseTraitement, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        ecran.setColumns(20);
        ecran.setRows(5);
        jScrollPane1.setViewportView(ecran);

        chLastId.setText("Dernier ID: Null");

        etat.setText("Prêt.");

        btReinitRegistre.setText("Reinitialiser le compteur des IDs");
        btReinitRegistre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btReinitRegistreActionPerformed(evt);
            }
        });

        chInfosLicence.setText("Infos Licence");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(chInfosLicence, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(chLastId, javax.swing.GroupLayout.DEFAULT_SIZE, 269, Short.MAX_VALUE)
                            .addComponent(etat, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btReinitRegistre, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                .addComponent(progressUser, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())))))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(chInfosLicence)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(chLastId)
                    .addComponent(btReinitRegistre))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(etat)
                    .addComponent(progressUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout panUserLayout = new javax.swing.GroupLayout(panUser);
        panUser.setLayout(panUserLayout);
        panUserLayout.setHorizontalGroup(
            panUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panUserLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(panUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panUserLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(panUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jScrollPane1)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addContainerGap()))
        );
        panUserLayout.setVerticalGroup(
            panUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panUserLayout.createSequentialGroup()
                .addContainerGap(396, Short.MAX_VALUE)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(panUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panUserLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 113, Short.MAX_VALUE)
                    .addGap(98, 98, 98)))
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
                            .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 207, Short.MAX_VALUE)
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
                .addContainerGap(326, Short.MAX_VALUE))
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

    private void btSaveGroupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btSaveGroupActionPerformed
        // TODO add your handling code here:
        if (chUtilisateur.isSelected()) {
            Vector<Utilisateur> Uinputs = new Vector();
            int nbTour = Integer.parseInt(chNB.getText().trim());
            for (int i = 0; i < nbTour; i++) {
                Uinputs.add(new Utilisateur(-1, 1, "SULA", "BOSIO", "Serge", "sulabosiog@gmail.com", "sulabosio", InterfaceUtilisateur.TYPE_ADMIN, (new Date().getTime()), InterfaceUtilisateur.DROIT_CONTROLER, InterfaceUtilisateur.DROIT_CONTROLER, InterfaceUtilisateur.DROIT_CONTROLER, InterfaceUtilisateur.DROIT_CONTROLER, InterfaceUtilisateur.DROIT_CONTROLER, InterfaceUtilisateur.DROIT_CONTROLER, InterfaceUtilisateur.DROIT_CONTROLER, InterfaceUtilisateur.BETA_EXISTANT));
            }
            enreg_Groupe(Uinputs, dossierUtilisateur.getText());
        } else {
            Vector<XX_Frais> Uinputs = new Vector();
            int nbTour = Integer.parseInt(chNB.getText().trim());
            for (int i = 0; i < nbTour; i++) {
                Vector<LiaisonClasseFrais> lc = new Vector<>();
                lc.add(new LiaisonClasseFrais(1, "CM1", 100));
                lc.add(new LiaisonClasseFrais(2, "CM2", 100));

                Vector<LiaisonPeriodeFrais> lp = new Vector<>();
                lp.add(new LiaisonPeriodeFrais(1, "1ere Trime", 50));
                lp.add(new LiaisonPeriodeFrais(2, "2ème Trime", 50));

                Uinputs.add(new XX_Frais(-1, 1, 1, 1, 1, new Date().getTime(), "FRAISX", "$", 3, lc, lp, 100, InterfaceFrais.BETA_EXISTANT));
            }
            enreg_Groupe(Uinputs, dossierFrais.getText());
        }
    }//GEN-LAST:event_btSaveGroupActionPerformed

    private void chNBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chNBActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_chNBActionPerformed

    private void btSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btSaveActionPerformed
        // TODO add your handling code here:
        if (chUtilisateur.isSelected()) {
            Utilisateur Uinput = new Utilisateur(-1, 1, "SULA", "BOSIO", "Serge", "sulabosiog@gmail.com", "sulabosio", InterfaceUtilisateur.TYPE_ADMIN, (new Date().getTime()), InterfaceUtilisateur.DROIT_CONTROLER, InterfaceUtilisateur.DROIT_CONTROLER, InterfaceUtilisateur.DROIT_CONTROLER, InterfaceUtilisateur.DROIT_CONTROLER, InterfaceUtilisateur.DROIT_CONTROLER, InterfaceUtilisateur.DROIT_CONTROLER, InterfaceUtilisateur.DROIT_CONTROLER, InterfaceUtilisateur.BETA_EXISTANT);
            enreg_Objet(Uinput, dossierUtilisateur.getText());
        } else {
            Vector<LiaisonClasseFrais> lc = new Vector<>();
            lc.add(new LiaisonClasseFrais(1, "CM1", 100));
            lc.add(new LiaisonClasseFrais(2, "CM2", 100));

            Vector<LiaisonPeriodeFrais> lp = new Vector<>();
            lp.add(new LiaisonPeriodeFrais(1, "1ere Trime", 50));
            lp.add(new LiaisonPeriodeFrais(2, "2ème Trime", 50));

            XX_Frais newObj = new XX_Frais(-1, 1, 1, 1, 1, new Date().getTime(), "FRAISX", "$", 3, lc, lp, 100, InterfaceFrais.BETA_EXISTANT);

            enreg_Objet(newObj, dossierFrais.getText());
        }

    }//GEN-LAST:event_btSaveActionPerformed

    private void btListerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btListerActionPerformed
        // TODO add your handling code here:
        if (chUtilisateur.isSelected()) {
            listerDossier(Utilisateur.class, dossierUtilisateur.getText());
        } else {
            listerDossier(XX_Frais.class, dossierFrais.getText());
        }
    }//GEN-LAST:event_btListerActionPerformed

    private void btDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btDeleteActionPerformed
        // TODO add your handling code here:
        if (chUtilisateur.isSelected()) {
            supprimer(dossierUtilisateur.getText(), chIDS.getText().trim().split(",")[0]);
        } else {
            supprimer(dossierFrais.getText(), chIDS.getText().trim().split(",")[0]);
        }
    }//GEN-LAST:event_btDeleteActionPerformed

    private void btDeleteGroupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btDeleteGroupActionPerformed
        // TODO add your handling code here:
        if (chUtilisateur.isSelected()) {
            supprimerGroupe(dossierUtilisateur.getText(), chIDS.getText().trim().split(","));
        } else {
            supprimerGroupe(dossierFrais.getText(), chIDS.getText().trim().split(","));
        }
    }//GEN-LAST:event_btDeleteGroupActionPerformed

    private void btViderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btViderActionPerformed
        // TODO add your handling code here:
        if (chUtilisateur.isSelected()) {
            viderTout(Utilisateur.class, dossierUtilisateur.getText().trim());
        } else {
            viderTout(XX_Frais.class, dossierFrais.getText().trim());
        }
    }//GEN-LAST:event_btViderActionPerformed

    private void btReinitRegistreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btReinitRegistreActionPerformed
        // TODO add your handling code here:
        if (chUtilisateur.isSelected()) {
            updateInfosRegistre(dossierUtilisateur.getText());
            reinitRegistre(dossierUtilisateur.getText());
        } else {
            updateInfosRegistre(dossierFrais.getText());
            reinitRegistre(dossierFrais.getText());
        }

    }//GEN-LAST:event_btReinitRegistreActionPerformed

    private void chUtilisateurItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chUtilisateurItemStateChanged
        // TODO add your handling code here:
        ecran.setText("");
        if (chUtilisateur.isSelected()) {
            showRegistre();
        }
    }//GEN-LAST:event_chUtilisateurItemStateChanged

    private void chFraisItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chFraisItemStateChanged
        // TODO add your handling code here:
        ecran.setText("");
        if (chFrais.isSelected()) {
            showRegistre();
        }
    }//GEN-LAST:event_chFraisItemStateChanged

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        if (chUtilisateur.isSelected()) {
            ouvrirObjet(Utilisateur.class, dossierUtilisateur.getText(), chId.getText());
        } else {
            ouvrirObjet(XX_Frais.class, dossierFrais.getText(), chId.getText());
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        login();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        logout();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void chIdEcoleKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_chIdEcoleKeyReleased
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            login();
        }
    }//GEN-LAST:event_chIdEcoleKeyReleased

    private void chPassWordKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_chPassWordKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_chPassWordKeyReleased

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
    private javax.swing.JButton btDelete;
    private javax.swing.JButton btDeleteGroup;
    private javax.swing.JButton btLister;
    private javax.swing.JButton btReinitRegistre;
    private javax.swing.JButton btSave;
    private javax.swing.JButton btSaveGroup;
    private javax.swing.JButton btVider;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JTextField chEmail;
    private javax.swing.JRadioButton chFrais;
    private javax.swing.JTextField chIDS;
    private javax.swing.JTextField chId;
    private javax.swing.JTextField chIdEcole;
    private javax.swing.JLabel chInfosLicence;
    private javax.swing.JLabel chLastId;
    private javax.swing.JTextField chNB;
    private javax.swing.JPasswordField chPassWord;
    private javax.swing.JRadioButton chUtilisateur;
    private javax.swing.JTextField chVitesseTraitement;
    private javax.swing.JTextField dossierFrais;
    private javax.swing.JTextField dossierUtilisateur;
    private javax.swing.JTextArea ecran;
    private javax.swing.JLabel ecranObjet;
    private javax.swing.JLabel etat;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel labLogin;
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
                    moi.setTitle(session.getUtilisateur().getNom() + " | " + session.getEntreprise().getNom());
                    tabPrincipal.remove(panLogin);
                    tabPrincipal.add("Bien venu " + session.getUtilisateur().getPrenom() + " | " + session.getEntreprise().getNom(), panUser);
                    showRegistre();
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
}
