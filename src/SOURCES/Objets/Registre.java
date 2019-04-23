/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.Objets;

import java.util.Date;

/**
 *
 * @author user
 */


public class Registre {
    private int dernierID;
    private Date dateEnregistrement;
    private String fichierRegistre = "MANIFEST.man";    //Fichier qui contient le dernier ID

    public Registre() {
    }

    public Registre(int dernierID, Date dateEnregistrement) {
        this.dernierID = dernierID;
        this.dateEnregistrement = dateEnregistrement;
    }
    
    public void incrementer(boolean confirmed){
        if(confirmed == true){
            this.dernierID++;
        }
        this.dateEnregistrement = new Date();
    }

    public String getFichierRegistre() {
        return fichierRegistre;
    }
    
    public int getDernierID() {
        return dernierID;
    }

    public void setDernierID(int dernierID) {
        this.dernierID = dernierID;
    }

    public Date getDateEnregistrement() {
        return dateEnregistrement;
    }

    public void setDateEnregistrement(Date dateEnregistrement) {
        this.dateEnregistrement = dateEnregistrement;
    }

    @Override
    public String toString() {
        return "Parametre{" + "dernierID=" + dernierID + ", dateEnregistrement=" + dateEnregistrement + '}';
    }
}
























