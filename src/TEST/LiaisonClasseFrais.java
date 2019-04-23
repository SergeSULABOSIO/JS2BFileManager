/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TEST;

/**
 *
 * @author HP Pavilion
 */
public class LiaisonClasseFrais {
    private int idClasse;
    private String nomClasse;
    private double montant;

    public LiaisonClasseFrais() {
        
    }
    
    public LiaisonClasseFrais(int idClasse, String nomClasse, double montant) {
        this.idClasse = idClasse;
        this.nomClasse = nomClasse;
        this.montant = montant;
    }

    public int getIdClasse() {
        return idClasse;
    }

    public void setIdClasse(int idClasse) {
        this.idClasse = idClasse;
    }

    public String getNomClasse() {
        return nomClasse;
    }

    public void setNomClasse(String nomClasse) {
        this.nomClasse = nomClasse;
    }

    
    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    @Override
    public String toString() {
        return "LiaisonClasseFrais{" + "idClasse=" + idClasse + ", nomClasse=" + nomClasse + ", montant=" + montant + '}';
    }
}




