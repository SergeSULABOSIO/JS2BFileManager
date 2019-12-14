/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.Callback;

/**
 *
 * @author HP Pavilion
 */


public abstract class EcouteurInternet {
    public abstract void onInternet(String adresseWebDisponible);
    public abstract void onError();
    public abstract void onVerification(String message);
}



