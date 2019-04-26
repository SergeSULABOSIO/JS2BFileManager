/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TEST;

import SOURCES.Callback.EcouteurOuverture;
import SOURCES.Callback.EcouteurStandard;
import SOURCES.Callback.EcouteurSuppression;
import SOURCES.Objets.FileManager;
import SOURCES.Objets.Registre;
import java.util.Date;
import java.util.Vector;

/**
 *
 * @author user
 */
public class Principal extends javax.swing.JFrame {

    /**
     * Creates new form Principal
     */
    public FileManager fm = new FileManager();

    public Principal() {
        initComponents();
        showRegistre();
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

        fm.enregistrer(newObj, dossier, new EcouteurStandard() {
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

    private void enreg_Groupe(Vector listeObjts, String dossier) {
        ecran.setText("");

        int pauseMSecondes = Integer.parseInt(chVitesseTraitement.getText());
        fm.enregistrer(pauseMSecondes, listeObjts, dossier, new EcouteurStandard() {
            @Override
            public void onDone(String message) {
                etat.setText(message + "\n");
                ecran.append("Anciens Objets avec IDs modifiés automatiquement:\n");
                for (Object o : listeObjts) {
                    ecran.append(" -- " + o.toString() + "\n");
                }
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

    private void listerDossier(Class NomClasse, String dossier) {
        ecran.setText("");

        fm.ouvrirTout(Integer.parseInt(chVitesseTraitement.getText().trim()), NomClasse, dossier, new EcouteurOuverture() {
            @Override
            public void onDone(String message, Vector data) {
                etat.setText(message);
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
            }

            @Override
            public void onProcessing(String message) {
                etat.setText(message);
            }
        });
    }

    private void updateInfosRegistre(String cheminDossier) {
        Registre registre = fm.getRegistre(cheminDossier);
        if (registre != null) {
            chLastId.setText("Last ID: " + registre.getDernierID() + " || " + registre.getDateEnregistrement().toLocaleString());
        } else {
            chLastId.setText("Dossier vide !");
        }
    }

    private void reinitRegistre(String cheminDossier) {
        if (fm.reinitialiserRegistre(cheminDossier)) {
            etat.setText("Registre réinitialisé !");
        } else {
            etat.setText("Erreur  lors de la réinitialisation!");
        }
    }

    private void supprimer(String dossier, String id) {
        ecran.setText("");

        boolean rep = fm.supprimer(dossier, Integer.parseInt(id));
        if (rep == true) {
            ecran.append("Suppression de " + id + " dans " + dossier + " reussie !");
            etat.setText("Suppression effectuée  avec succès.");
        } else {
            ecran.append("Echec");
            etat.setText("Echec de suppression");
        }
    }

    private void supprimerGroupe(String dossier, String[] tabIDS) {
        ecran.setText("");

        fm.supprimerTout(dossier, tabIDS, new EcouteurSuppression() {
            @Override
            public void onDone(String message, Object[] idsNonSupprimes) {
                ecran.setText(message);
                etat.setText(message);
                
                if(idsNonSupprimes.length != 0){
                    ecran.append("Ids des objets non supprimés:");
                    for(Object oId: idsNonSupprimes){
                        ecran.append(" * " + oId+"\n");
                    }
                }else{
                    ecran.setText("les " + tabIDS.length + " ids sont tous supprimés !");
                }
            }

            @Override
            public void onError(String message) {
                ecran.setText(message);
                etat.setText(message);
            }

            @Override
            public void onProcessing(String message) {
                ecran.setText(message);
                etat.setText(message);
            }
        });
    }

    private void viderTout(Class NomClasse, String dossier) {
        ecran.setText("");

        fm.supprimerTout(dossier, new EcouteurSuppression() {
            @Override
            public void onDone(String message, Object[] idsNonSupprimes) {
                etat.setText(message);
                if (idsNonSupprimes.length != 0) {
                    ecran.setText("Objets non supprimés:\n");
                    for (Object oID : idsNonSupprimes) {
                        ecran.setText(" * " + oID + "\n");
                    }
                } else {
                    ecran.setText("Le dossier vidé!\n");
                }

                listerDossier(NomClasse, dossier);
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

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        chUtilisateur = new javax.swing.JRadioButton();
        chFrais = new javax.swing.JRadioButton();
        ecranObjet = new javax.swing.JLabel();
        dossierFrais = new javax.swing.JTextField();
        dossierUtilisateur = new javax.swing.JTextField();
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
        jTextField1 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        chVitesseTraitement = new javax.swing.JTextField();
        etat = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        ecran = new javax.swing.JTextArea();
        chLastId = new javax.swing.JLabel();
        btReinitRegistre = new javax.swing.JButton();

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

        dossierFrais.setText("ANNEE XX/FRAIS");

        dossierUtilisateur.setText("ANNEE XX/UTILISATEUR");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(ecranObjet, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(chUtilisateur, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(chFrais, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(dossierFrais)
                            .addComponent(dossierUtilisateur))))
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 7, Short.MAX_VALUE)
                .addComponent(ecranObjet)
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

        jTextField1.setText("1");

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
                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(chVitesseTraitement, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        etat.setText("Prêt.");

        ecran.setColumns(20);
        ecran.setRows(5);
        jScrollPane1.setViewportView(ecran);

        chLastId.setText("Dernier ID: Null");

        btReinitRegistre.setText("Reinitialiser le compteur des IDs");
        btReinitRegistre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btReinitRegistreActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(etat, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane1)
            .addGroup(layout.createSequentialGroup()
                .addComponent(chLastId, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btReinitRegistre, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 109, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(chLastId)
                    .addComponent(btReinitRegistre))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(etat))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btSaveGroupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btSaveGroupActionPerformed
        // TODO add your handling code here:
        if (chUtilisateur.isSelected()) {
            Vector<XX_Utilisateur> Uinputs = new Vector();
            int nbTour = Integer.parseInt(chNB.getText().trim());
            for (int i = 0; i < nbTour; i++) {
                Uinputs.add(new XX_Utilisateur(-1, 1, "SULA", "BOSIO", "Serge", "sulabosiog@gmail.com", "sulabosio", InterfaceUtilisateur.TYPE_ADMIN, (new Date().getTime()), InterfaceUtilisateur.DROIT_CONTROLER, InterfaceUtilisateur.DROIT_CONTROLER, InterfaceUtilisateur.DROIT_CONTROLER, InterfaceUtilisateur.DROIT_CONTROLER, InterfaceUtilisateur.DROIT_CONTROLER, InterfaceUtilisateur.DROIT_CONTROLER, InterfaceUtilisateur.DROIT_CONTROLER, InterfaceUtilisateur.BETA_EXISTANT));
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
            XX_Utilisateur Uinput = new XX_Utilisateur(-1, 1, "SULA", "BOSIO", "Serge", "sulabosiog@gmail.com", "sulabosio", InterfaceUtilisateur.TYPE_ADMIN, (new Date().getTime()), InterfaceUtilisateur.DROIT_CONTROLER, InterfaceUtilisateur.DROIT_CONTROLER, InterfaceUtilisateur.DROIT_CONTROLER, InterfaceUtilisateur.DROIT_CONTROLER, InterfaceUtilisateur.DROIT_CONTROLER, InterfaceUtilisateur.DROIT_CONTROLER, InterfaceUtilisateur.DROIT_CONTROLER, InterfaceUtilisateur.BETA_EXISTANT);
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
            listerDossier(XX_Utilisateur.class, dossierUtilisateur.getText());
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
            viderTout(XX_Utilisateur.class, dossierUtilisateur.getText().trim());
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
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Principal().setVisible(true);
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
    private javax.swing.JRadioButton chFrais;
    private javax.swing.JTextField chIDS;
    private javax.swing.JLabel chLastId;
    private javax.swing.JTextField chNB;
    private javax.swing.JRadioButton chUtilisateur;
    private javax.swing.JTextField chVitesseTraitement;
    private javax.swing.JTextField dossierFrais;
    private javax.swing.JTextField dossierUtilisateur;
    private javax.swing.JTextArea ecran;
    private javax.swing.JLabel ecranObjet;
    private javax.swing.JLabel etat;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
