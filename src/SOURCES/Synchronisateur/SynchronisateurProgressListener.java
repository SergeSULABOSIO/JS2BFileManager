/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.Synchronisateur;



/**
 *
 * @author HP Pavilion
 */


public abstract class SynchronisateurProgressListener {
    public abstract void onAffiche(int total, int actuel, String message);
    public abstract void onSuccess(String message);
    public abstract void onEchec(String message);
}








