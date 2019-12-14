/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.Utilitaires;

import SOURCES.Callback.EcouteurInternet;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

/**
 *
 * @author user
 */
public class UtilFileManager {

    public static final int ACTION_CONNEXION = 100;

    public static Date convertDatePaiement(String Sdate) {
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(Sdate);
            //System.out.println(Sdate + "\t" + date.toLocaleString());
            return date;
        } catch (Exception e) {
            //e.printStackTrace();
            return null;
        }

    }

    public static void isNewWorkAvailable(String pageWeb, EcouteurInternet ei) {
        if (ei != null) {
            new Thread() {
                public void run() {
                    try {
                        ei.onVerification("Vérification de la connexion Internet...");
                        URL url = new URL(pageWeb);//new URL("https://www.geeksforgeeks.org/"); 
                        URLConnection connection = url.openConnection();
                        connection.connect();
                        ei.onInternet(pageWeb);
                    } catch (Exception e) {
                        //e.printStackTrace();
                        ei.onError();
                    }
                }
            }.start();
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

    public static boolean ecrire_txt(String fichierDestination, String text, boolean append) {
        if (text.trim().length() != 0) {
            try {
                FileWriter writer = new FileWriter(fichierDestination, append);
                writer.write(text);
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

    public static boolean containsSignature(String fichierSource, long signature) {
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
                    System.out.println(jsonString);
                    return jsonString.contains(signature + "");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static Object[] lire_signaturesDeleted(String fichierSource) {
        Vector vectSign = new Vector();
        String stringData = "";
        if (new File(fichierSource).exists() == true) {
            try {
                FileReader reader = new FileReader(fichierSource);
                BufferedReader bufferedReader = new BufferedReader(reader);
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    stringData = stringData + line;
                }
                reader.close();
                if (stringData.length() != 0) {
                    String[] tabSignatures = stringData.split(",");
                    for (String sign : tabSignatures) {
                        if (sign.trim().length() != 0 && !vectSign.contains(sign.trim())) {
                            vectSign.add(sign.trim());
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return vectSign.toArray();
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
