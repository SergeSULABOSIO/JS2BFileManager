/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.Utilitaires;

import com.fasterxml.jackson.databind.ObjectMapper;
import static com.sun.org.apache.xalan.internal.lib.ExsltDatetime.date;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author user
 */
public class UtilFileManager {

    public static final int ACTION_CONNEXION = 100;

    public static Date convertDatePaiement(String Sdate) {
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(Sdate);
            System.out.println(Sdate + "\t" + date.toLocaleString());
            return date;
        } catch (Exception e) {
            //e.printStackTrace();
            return null;
        }

    }

    public static boolean ecrire(String fichierDestination, Object objet) {
        if (objet != null) {
            try {
                FileWriter writer = new FileWriter(fichierDestination, false);
                String js = getJSONStringFromObject(objet);
                writer.write(js);
                writer.close();
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public static Object lire(String fichierSource, Class NomClasse) {
        Object obj = null;
        String jsonString = "";
        if (new File(fichierSource).exists() == true) {
            try {
                FileReader reader = new FileReader(fichierSource);
                BufferedReader bufferedReader = new BufferedReader(reader);
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    jsonString = jsonString + line;
                }
                reader.close();

                //On transforme le texte lu en Objet JSON, puis en Objet Utilisateur
                if (jsonString.length() != 0) {
                    //System.out.println("JSON : " + data.toString());
                    obj = getObjetFromJSONString(NomClasse, jsonString);
                    //System.out.println("JSON : " + util.toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return obj;
    }

    private static String getJSONStringFromObject(Object object) {
        String jsonString = "";
        ObjectMapper mapper = new ObjectMapper();
        try {
            jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
            //System.out.println(jsonString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonString;
    }

    private static Object getObjetFromJSONString(Class NomClasse, String JSONString) {
        Object obj = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            obj = mapper.readValue(JSONString, NomClasse);
            //System.out.println(obj.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }
}











