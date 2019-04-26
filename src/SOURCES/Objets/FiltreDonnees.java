/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.Objets;

import java.io.File;
import java.io.FilenameFilter;

/**
 *
 * @author user
 */
public class FiltreDonnees implements FilenameFilter{

    private String elementAIgnorer;

    public FiltreDonnees(String elementAIgnorer) {
        this.elementAIgnorer = elementAIgnorer;
    }
    
    
    @Override
    public boolean accept(File dir, String fileName) {
        if (!fileName.endsWith(elementAIgnorer)) {
            return true;
        }
        return false;
    }
}
















