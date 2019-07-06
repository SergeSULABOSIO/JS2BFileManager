/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package SOURCES.Callback;

import SOURCES.Objets.Entreprise;
import SOURCES.Objets.Paiement;
import SOURCES.Objets.Utilisateur;

/**
 *
 * @author HP Pavilion
 */


public abstract class EcouteurLoginServeur {
    public abstract void onDone(String message, Entreprise entreprise, Utilisateur utilisateur, Paiement paiement);
    public abstract void onError(String message);
    public abstract void onProcessing(String message);
}












