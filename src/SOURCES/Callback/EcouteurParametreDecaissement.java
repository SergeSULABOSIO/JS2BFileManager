/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.Callback;

import Source.Objet.Agent;
import Source.Objet.Charge;
import Source.Objet.Monnaie;

/**
 *
 * @author HP Pavilion
 */


public abstract class EcouteurParametreDecaissement {
    public abstract int getIdUtilisateur();
    public abstract Agent getAgent(int idAgent);
    public abstract Monnaie getMonnaie(int idMonnaie);
    public abstract Charge getCharge();
    
}































