/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.Objets;

import SOURCES.Utilitaires.Util;

/**
 *
 * @author user
 */
public class FileManager {
    
    private String dossier;

    public FileManager(String dossier) {
        this.dossier = dossier;
    }
    
    public boolean ecrire(Object obj) {
        return Util.ecrire(dossier, obj);
    }
    
    public Object lire(Class NomClasse) {
        return Util.lire(dossier, NomClasse);
    }
}















































