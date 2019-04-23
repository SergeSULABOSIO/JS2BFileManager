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
public class XX_Utilisateur implements InterfaceUtilisateur{
    
    public int id;
    public int idEntreprise;
    public String email;
    public String motDePasse;
    public int type;
    public long signature;
    public String nom;
    public String postnom;
    public String prenom;
    //les droit d'access aux rubriques existantes
    public int droitLitige;
    public int droitExercice;
    public int droitFacture;
    public int droitInscription;
    public int droitPaie;
    public int droitTresorerie;
    public int droitUtilisateur;
    public int beta;

    public XX_Utilisateur() {
    }

    public XX_Utilisateur(int id, int idEntreprise, String nom, String postnom, String prenom, String email, String motDePasse, int type, long signature, int droitLitige, int droitExercice, int droitFacture, int droitInscription, int droitPaie, int droitTresorerie, int droitUtilisateur, int beta) {
        this.id = id;
        this.idEntreprise = idEntreprise;
        this.email = email;
        this.motDePasse = motDePasse;
        this.type = type;
        this.signature = signature;
        this.nom = nom;
        this.postnom = postnom;
        this.prenom = prenom;
        this.droitLitige = droitLitige;
        this.droitExercice = droitExercice;
        this.droitFacture = droitFacture;
        this.droitInscription = droitInscription;
        this.droitPaie = droitPaie;
        this.droitTresorerie = droitTresorerie;
        this.droitUtilisateur = droitUtilisateur;
        this.beta = beta;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdEntreprise() {
        return idEntreprise;
    }

    public void setIdEntreprise(int idEntreprise) {
        this.idEntreprise = idEntreprise;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getSignature() {
        return signature;
    }

    public void setSignature(long signature) {
        this.signature = signature;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPostnom() {
        return postnom;
    }

    public void setPostnom(String postnom) {
        this.postnom = postnom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public int getDroitLitige() {
        return droitLitige;
    }

    public void setDroitLitige(int droitLitige) {
        this.droitLitige = droitLitige;
    }

    public int getDroitExercice() {
        return droitExercice;
    }

    public void setDroitExercice(int droitExercice) {
        this.droitExercice = droitExercice;
    }

    public int getDroitFacture() {
        return droitFacture;
    }

    public void setDroitFacture(int droitFacture) {
        this.droitFacture = droitFacture;
    }

    public int getDroitInscription() {
        return droitInscription;
    }

    public void setDroitInscription(int droitInscription) {
        this.droitInscription = droitInscription;
    }

    public int getDroitPaie() {
        return droitPaie;
    }

    public void setDroitPaie(int droitPaie) {
        this.droitPaie = droitPaie;
    }

    public int getDroitTresorerie() {
        return droitTresorerie;
    }

    public void setDroitTresorerie(int droitTresorerie) {
        this.droitTresorerie = droitTresorerie;
    }

    public int getDroitUtilisateur() {
        return droitUtilisateur;
    }

    public void setDroitUtilisateur(int droitUtilisateur) {
        this.droitUtilisateur = droitUtilisateur;
    }

    public int getBeta() {
        return beta;
    }

    public void setBeta(int beta) {
        this.beta = beta;
    }

    @Override
    public String toString() {
        return "XX_Utilisateur{" + "id=" + id + ", idEntreprise=" + idEntreprise + ", email=" + email + ", motDePasse=" + motDePasse + ", type=" + type + ", signature=" + signature + ", nom=" + nom + ", postnom=" + postnom + ", prenom=" + prenom + ", droitLitige=" + droitLitige + ", droitExercice=" + droitExercice + ", droitFacture=" + droitFacture + ", droitInscription=" + droitInscription + ", droitPaie=" + droitPaie + ", droitTresorerie=" + droitTresorerie + ", droitUtilisateur=" + droitUtilisateur + ", beta=" + beta + '}';
    }
}



