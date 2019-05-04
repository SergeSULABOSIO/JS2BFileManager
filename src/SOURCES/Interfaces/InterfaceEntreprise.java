/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.Interfaces;

/**
 *
 * @author HP Pavilion
 */
public interface InterfaceEntreprise {
    
    public abstract int getId();
    public abstract String getNom();
    public abstract String getAdresse();
    public abstract String getTelephone();
    public abstract String getEmail();
    public abstract String getSiteWeb();
    public abstract String getLogo();
    public abstract String getRccm();
    public abstract String getIdnat();
    public abstract String getNumeroImpot();
    //Details bancaires
    public abstract String getBanque();
    public abstract String getIntituleCompte();
    public abstract String getNumeroCompte();
    public abstract String getIban();
    public abstract String getCodeSwift();
    
    public abstract void setId(int id);
    public abstract void setNom(String nom);
    public abstract void setAdresse(String adresse);
    public abstract void setTelephone(String telephone);
    public abstract void setEmail(String email);
    public abstract void setSiteWeb(String siteweb);
    public abstract void setLogo(String logo);
    public abstract void setRccm(String rccm);
    public abstract void setIdnat(String idnat);
    public abstract void setNumeroImpot(String numeroimpot);
    //Details
    public abstract void setBanque(String banque);
    public abstract void setIntituleCompte(String inititule);
    public abstract void setNumeroCompte(String numero);
    public abstract void setIban(String IBAN);
    public abstract void setCodeSwift(String codeswift);
}
