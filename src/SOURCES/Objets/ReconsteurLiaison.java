/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.Objets;

import SOURCES.Utilitaires.UtilFileManager;
import Source.Objet.LiaisonFraisPeriode;
import java.lang.reflect.Field;
import java.sql.Date;
import java.util.Vector;

/**
 *
 * @author HP Pavilion
 */
public class ReconsteurLiaison {

    public static String data = "[LiaisonFraisPeriode{idPeriode=1, nomPeriode=1er Trimestre, signaturePeriode=4610921427038036296, pourcentage=33.0}, LiaisonFraisPeriode{idPeriode=2, nomPeriode=2ème Trimestre, signaturePeriode=-6296768174954075040, pourcentage=33.0}, LiaisonFraisPeriode{idPeriode=3, nomPeriode=3ème Trimestre, signaturePeriode=1004137776615229220, pourcentage=33.0}]";

    public static Vector getLiaison(Class objetType, String data) {
        Vector listeOutput = new Vector();
        
        data = data.substring(1);
        data = data.substring(0, data.length() - 1);
        data = data.replaceAll("}$", "");
        System.out.println(data);
        //String[] tabData = data.split("(L|l)iaison(P|p)eriode(F|f)rais\\{");
        //System.out.println(" **** " + objetType.getSimpleName());
        String[] tabData = data.split("" + objetType.getSimpleName() + "\\{");
        for (String liaison : tabData) {
            liaison = liaison.replaceAll("},\\s?$", "");
            if (liaison.trim().length() != 0) {
                //System.out.println(" * " + liaison);
                Field[] TabAttributs = objetType.getDeclaredFields();
                String patternVal = "(";
                for (Field champ : TabAttributs) {
                    //System.out.println("\t - " + champ.getName());
                    patternVal += champ.getName() + "=|";
                }

                patternVal = patternVal.substring(0, patternVal.length() - 1);
                patternVal = patternVal + ")";
                //System.out.println(" ** " + patternVal);

                liaison = liaison.replaceAll(patternVal, "sososo");
                //System.out.println(" * " + liaison);
                String[] tabVal = liaison.split("sososo");
                Vector listeValeurs = new Vector();
                for (String valeurAttr : tabVal) {
                    if (valeurAttr.trim().length() != 0) {
                        valeurAttr = valeurAttr.trim().replaceAll(",", "");
                        //System.out.println("\t\t" + valeurAttr);
                        listeValeurs.add(valeurAttr);
                    }
                }

                //On charge les valeurs dans les attributs de l'objet
                try {
                    Object tempObject = objetType.newInstance();
                    int index = 0;
                    for (Field attrib : TabAttributs) {
                        try {
                            if (attrib.getType() == Integer.TYPE) {
                                attrib.setInt(tempObject, Integer.parseInt("" + (listeValeurs.elementAt(index))));
                            } else if (attrib.getType() == Double.TYPE) {
                                attrib.setDouble(tempObject, Double.parseDouble("" + (listeValeurs.elementAt(index))));
                            } else if (attrib.getType() == String.class) {
                                attrib.set(tempObject, "" + (listeValeurs.elementAt(index)));
                            } else if (attrib.getType() == Long.TYPE) {
                                attrib.setLong(tempObject, Long.parseLong("" + (listeValeurs.elementAt(index))));
                            } else if (attrib.getType() == Date.class) {
                                attrib.set(tempObject, UtilFileManager.convertDatePaiement("" + (listeValeurs.elementAt(index))));
                            }
                            //System.out.println(attirib.getType());
                            index++;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    //System.out.println("Output: " + tempObject.toString());
                    listeOutput.add(tempObject);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return listeOutput;
    }

    public static void main(String[] a) {
        
        Vector tabLiaison = getLiaison(LiaisonFraisPeriode.class, data);
        for(Object oLiaison : tabLiaison){
            LiaisonFraisPeriode lPeriode = (LiaisonFraisPeriode) oLiaison;
            System.out.println(lPeriode.toString());
        }
        System.out.println("!" + tabLiaison+"!");
    }

}
