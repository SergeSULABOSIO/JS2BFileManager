/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.Callback;

import Source.Objet.Frais;
import Source.Objet.Monnaie;
import Source.Objet.Revenu;

/**
 *
 * @author HP Pavilion
 */


public abstract class EcouteurParametreEncaissement {
    public abstract int getIdUtilisateur();
    public abstract Frais getFrais(int idFrais);
    public abstract Monnaie getMonnaie(int idMonnaie);
    public abstract Revenu getRevenu();
    
}

























