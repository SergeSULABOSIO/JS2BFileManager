/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.DB;

import SOURCES.Objets.FileManager;
import SOURCES.Utilitaires.UtilFileManager;
import Source.Interface.InterfaceCharge;
import Source.Objet.Charge;
import Source.Objet.UtilObjet;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.util.Date;

/**
 *
 * @author user
 */
public class InterpreteurSql {

    public InterpreteurSql() {

    }

    public static String getInsertManifeste(int idEntreprise, int idUtilisateur, String dossier, int dernierID, String dateEnregistrement) {
        //INSERT INTO `BACKUP_MANIFESTE` (`idEntreprise`, `idUtilisateur`, `dossier`, `dernierID`, `dateEnregistrement`) VALUES ('2', '3', 'REVENU1', '40', '478784512200')
        return "INSERT INTO `BACKUP_MANIFESTE` (`idEntreprise`, `idUtilisateur`, `dossier`, `dernierID`, `dateEnregistrement`) VALUES ('" + idEntreprise + "', '" + idUtilisateur + "', '" + dossier + "', '" + dernierID + "', '" + dateEnregistrement + "')";
    }

    public static String getUpdateManifeste(int idEntreprise, int idUtilisateur, String dossier, int dernierID, String dateEnregistrement) {
        //UPDATE `BACKUP_MANIFESTE` SET `dernierID` = '11', `dateEnregistrement` = '45511220097' WHERE `BACKUP_MANIFESTE`.`id` = 6;
        return "UPDATE `BACKUP_MANIFESTE` SET `dernierID` = " + dernierID + ", `dateEnregistrement` = '" + dateEnregistrement + "', `idUtilisateur` = " + idUtilisateur + " WHERE `idEntreprise` = " + idEntreprise + " AND `dossier` = '" + dossier + "';";
    }

    public static String getInsert(Object obj, long lastModified) {
        String sqlString = "INSERT INTO `BACKUP_" + obj.getClass().getSimpleName().toUpperCase() + "` (";
        String valeurs = "VALUES (";
        try {
            for (Field champ : obj.getClass().getDeclaredFields()) {
                if (!champ.getName().toLowerCase().equals("beta")
                        && !champ.getName().toLowerCase().equals("liaisonsclasses")
                        && !champ.getName().toLowerCase().equals("liaisonlfaiseleve")
                        && !champ.getName().toLowerCase().equals("liaisonclassefrais")
                        && !champ.getName().toLowerCase().equals("liaisonsperiodes")) {

                    sqlString += "`" + champ.getName() + "`, ";
                    if (champ.getType() == Date.class) {
                        valeurs += "'" + UtilObjet.getDateAnglais((Date) champ.get(obj)) + "',";
                    } else if (champ.getType() == String.class) {
                        valeurs += "'" + champ.get(obj) + "',";
                    } else {
                        valeurs += "" + champ.get(obj) + ",";
                    }
                }
            }
            valeurs += lastModified + ");";
            sqlString += "`lastModified`) " + valeurs;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sqlString;
    }

    public static String getUpdate(Object obj, long lastModified) {
        //UPDATE `BACKUP_CHARGE` SET `idUtilisateur` = '13', `nom` = 'SALAIRE DU PERSONNEL', `limiteAnnuelle` = '1000000' WHERE `BACKUP_CHARGE`.`id` = 1;
        String table = (obj.getClass().getSimpleName().toUpperCase() + "").toUpperCase();
        String sqlString = "UPDATE `BACKUP_" + table + "` SET";
        try {
            for (Field champ : obj.getClass().getDeclaredFields()) {
                if (!champ.getName().toLowerCase().equals("beta")
                        && !champ.getName().toLowerCase().equals("signature")
                        && !champ.getName().toLowerCase().equals("liaisonsclasses")
                        && !champ.getName().toLowerCase().equals("liaisonlfaiseleve")
                        && !champ.getName().toLowerCase().equals("liaisonclassefrais")
                        && !champ.getName().toLowerCase().equals("liaisonperiodefrais")) {
                    sqlString += " `" + champ.getName() + "` ";
                    if (champ.getType() == Date.class) {
                        sqlString += "= '" + UtilObjet.getDateAnglais((Date) champ.get(obj)) + "',";
                    } else if (champ.getType() == String.class) {
                        sqlString += "= '" + champ.get(obj) + "',";
                    } else {
                        sqlString += "= " + champ.get(obj) + ",";
                    }
                }
            }
            //sqlString = sqlString.substring(0, sqlString.length()-1);
            Field champSignature = obj.getClass().getDeclaredField("signature");//signature
            sqlString += " `lastModified`='" + lastModified + "' WHERE `BACKUP_" + table + "`.`signature` = '" + champSignature.get(obj) + "';";
            //System.out.println("Sql = " + sqlString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sqlString;
    }

    public static void main(String[] a) {
        try {
            Date date = new Date();
            Charge charge = new Charge(1, 2, 2, 1, "SALAIRE", 10000, 1, 44545444, "$", UtilObjet.getSignature(), InterfaceCharge.BETA_EXISTANT);
            System.out.println(InterpreteurSql.getUpdate(charge, date.getTime()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
