/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.Objets;

import SOURCES.Interfaces.InterfaceEntreprise;

/**
 *
 * @author HP Pavilion
 */
public class Entreprise implements InterfaceEntreprise{
    public int id;
    public String nom;
    public String adresse;
    public String telephone;
    public String email;
    public String siteWeb;
    private String logo;
    private String rccm;
    private String idNat;
    private String numeroImpot;
    //Details bancaires
    public String banque;
    public String intituleCompte;
    public String numeroCompte;
    public String IBAN;
    public String codeSwift;

    public Entreprise(int id, String nom, String adresse, String telephone, String email, String siteWeb, String banque, String intituleCompte, String numeroCompte, String IBAN, String codeSwift, String logo, String rccm, String idNat, String numeroImpot) {
        this.id = id;
        this.nom = nom;
        this.adresse = adresse;
        this.telephone = telephone;
        this.email = email;
        this.siteWeb = siteWeb;
        this.banque = banque;
        this.intituleCompte = intituleCompte;
        this.numeroCompte = numeroCompte;
        this.IBAN = IBAN;
        this.codeSwift = codeSwift;
        this.logo = logo;
        this.rccm = rccm;
        this.idNat = idNat;
        this.numeroImpot = numeroImpot;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSiteWeb() {
        return siteWeb;
    }

    public void setSiteWeb(String siteWeb) {
        this.siteWeb = siteWeb;
    }

    public String getBanque() {
        return banque;
    }

    public void setBanque(String banque) {
        this.banque = banque;
    }

    public String getIntituleCompte() {
        return intituleCompte;
    }

    public void setIntituleCompte(String intituleCompte) {
        this.intituleCompte = intituleCompte;
    }

    public String getNumeroCompte() {
        return numeroCompte;
    }

    public void setNumeroCompte(String numeroCompte) {
        this.numeroCompte = numeroCompte;
    }

    public String getIBAN() {
        return IBAN;
    }

    public void setIBAN(String IBAN) {
        this.IBAN = IBAN;
    }

    public String getCodeSwift() {
        return codeSwift;
    }

    public void setCodeSwift(String codeSwift) {
        this.codeSwift = codeSwift;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getRccm() {
        return rccm;
    }

    public void setRccm(String rccm) {
        this.rccm = rccm;
    }

    public String getIdNat() {
        return idNat;
    }

    public void setIdNat(String idNat) {
        this.idNat = idNat;
    }

    public String getNumeroImpot() {
        return numeroImpot;
    }

    public void setNumeroImpot(String numeroImpot) {
        this.numeroImpot = numeroImpot;
    }
    
    

    @Override
    public String toString() {
        return "TESTEntreprise{" + "id=" + id + ", nom=" + nom + ", adresse=" + adresse + ", telephone=" + telephone + ", email=" + email + ", siteWeb=" + siteWeb + ", banque=" + banque + ", intituleCompte=" + intituleCompte + ", numeroCompte=" + numeroCompte + ", IBAN=" + IBAN + ", codeSwift=" + codeSwift + '}';
    }

    @Override
    public String getRCCM() {
        return this.rccm;
    }

    @Override
    public String getIDNAT() {
        return this.idNat;
    }

    @Override
    public void setRCCM(String rccm) {
        this.rccm = rccm;
    }

    @Override
    public void setIDNAT(String idnat) {
        this.idNat = idnat;
    }
    
}
