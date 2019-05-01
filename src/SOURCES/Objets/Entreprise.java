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
    private int id;
    private String nom;
    private String adresse;
    private String telephone;
    private String email;
    private String siteWeb;
    private String logo;
    private String RCCM;
    private String IDNAT;
    private String numeroImpot;
    //Details bancaires
    private String banque;
    private String intituleCompte;
    private String numeroCompte;
    private String IBAN;
    private String codeSwift;

    public Entreprise() {
    }
    
    

    public Entreprise(int id, String nom, String adresse, String telephone, String email, String siteWeb, String logo, String RCCM, String IDNAT, String numeroImpot, String banque, String intituleCompte, String numeroCompte, String IBAN, String codeSwift) {
        this.id = id;
        this.nom = nom;
        this.adresse = adresse;
        this.telephone = telephone;
        this.email = email;
        this.siteWeb = siteWeb;
        this.logo = logo;
        this.RCCM = RCCM;
        this.IDNAT = IDNAT;
        this.numeroImpot = numeroImpot;
        this.banque = banque;
        this.intituleCompte = intituleCompte;
        this.numeroCompte = numeroCompte;
        this.IBAN = IBAN;
        this.codeSwift = codeSwift;
    }

    public String getRCCM() {
        return RCCM;
    }

    public void setRCCM(String RCCM) {
        this.RCCM = RCCM;
    }

    public String getIDNAT() {
        return IDNAT;
    }

    public void setIDNAT(String IDNAT) {
        this.IDNAT = IDNAT;
    }

    @Override
    public String toString() {
        return "Entreprise{" + "id=" + id + ", nom=" + nom + ", adresse=" + adresse + ", telephone=" + telephone + ", email=" + email + ", siteWeb=" + siteWeb + ", logo=" + logo + ", RCCM=" + RCCM + ", IDNAT=" + IDNAT + ", numeroImpot=" + numeroImpot + ", banque=" + banque + ", intituleCompte=" + intituleCompte + ", numeroCompte=" + numeroCompte + ", IBAN=" + IBAN + ", codeSwift=" + codeSwift + '}';
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

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getNumeroImpot() {
        return numeroImpot;
    }

    public void setNumeroImpot(String numeroImpot) {
        this.numeroImpot = numeroImpot;
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

    
}
