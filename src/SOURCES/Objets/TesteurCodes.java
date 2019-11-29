/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.Objets;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author HP Pavilion
 */
public class TesteurCodes {
    public static String data = "[LiaisonPeriodeFrais{idPeriode=1, nomPeriode=1er Trimestre, signaturePeriode=5450854614453354157, pourcentage=33.0}, LiaisonPeriodeFrais{idPeriode=2, nomPeriode=2ème Trimestre, signaturePeriode=-9105976197124345236, pourcentage=33.0}, LiaisonPeriodeFrais{idPeriode=3, nomPeriode=3ème Trimestre, signaturePeriode=-5421709498265554820, pourcentage=33.0}]";
    
    public static void main(String[] a){
        data = data.substring(1);
        data = data.substring(0, data.length()-1);
        System.out.println(data);
        //String[] tabData = data.split("(L|l)iaison(P|p)eriode(F|f)rais{");
        
        
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






























