/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.Objets;

import Source.Objet.LiaisonFraisPeriode;
import java.lang.reflect.Field;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author HP Pavilion
 */
public class TesteurCodes {

    public static String data = "[LiaisonPeriodeFrais{idPeriode=1, nomPeriode=1er Trimestre, signaturePeriode=5450854614453354157, pourcentage=33.0}, LiaisonPeriodeFrais{idPeriode=2, nomPeriode=2ème Trimestre, signaturePeriode=-9105976197124345236, pourcentage=33.0}, LiaisonPeriodeFrais{idPeriode=3, nomPeriode=3ème Trimestre, signaturePeriode=-5421709498265554820, pourcentage=33.0}]";

    public static void main(String[] a) {
        LiaisonFraisPeriode objetLiaison = new LiaisonFraisPeriode();

        data = data.substring(1);
        data = data.substring(0, data.length() - 1);
        data = data.replaceAll("}$", "");
        System.out.println(data);
        String[] tabData = data.split("(L|l)iaison(P|p)eriode(F|f)rais\\{");
        for (String liaison : tabData) {
            liaison = liaison.replaceAll("},\\s?$", "");
            if (liaison.trim().length() != 0) {
                System.out.println(" * " + liaison);
                Field[] TabAttributs = objetLiaison.getClass().getDeclaredFields();
                String patternVal = "(";
                for (Field champ : TabAttributs) {
                    System.out.println("\t - " + champ.getName());
                    patternVal += champ.getName() + "=|";
                }

                patternVal = patternVal.substring(0, patternVal.length() - 1);
                patternVal = patternVal + ")";
                System.out.println(" ** " + patternVal);

                liaison = liaison.replaceAll(patternVal, "sososo");
                System.out.println(" * " + liaison);
                String[] tabVal = liaison.split("sososo");
                Vector listeValeurs = new Vector();
                for (String valeurAttr : tabVal) {
                    if (valeurAttr.trim().length() != 0) {
                        valeurAttr = valeurAttr.trim().replaceAll(",", "");
                        System.out.println("\t\t" + valeurAttr);
                        listeValeurs.add(valeurAttr);
                    }
                }

                //On charge les valeurs dans les attributs de l'objet
                int index = 0;
                for (Field attirib : TabAttributs) {
                    try {
                        if (attirib.getType().equals(Integer.class)) {
                            attirib.setInt(objetLiaison, Integer.parseInt("" + (listeValeurs.elementAt(index))));
                        }else if(attirib.getType().equals(Double.class)){
                            attirib.setDouble(objetLiaison, Double.parseDouble("" + (listeValeurs.elementAt(index))));
                        }else if(attirib.getType().equals(String.class)){
                            attirib.set(objetLiaison, "" + (listeValeurs.elementAt(index)));
                        }else if(attirib.getType().equals(Long.class)){
                            attirib.setLong(objetLiaison, Long.parseLong("" + (listeValeurs.elementAt(index))));
                        }
                        index++;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        /*
        Pattern pattern = Pattern.compile("(L|l)iaison(P|p)eriode(F|f)rais");
        // in case you would like to ignore case sensitivity,
        // you could use this statement:
        // Pattern pattern = Pattern.compile("\\s+", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(data);
        // check all occurance
        while (matcher.find()) {
            //System.out.print("Start index: " + matcher.start());
            //System.out.print(" End index: " + matcher.end() + " ");
            System.out.println(matcher.group());
        }
         */
    }

}





























