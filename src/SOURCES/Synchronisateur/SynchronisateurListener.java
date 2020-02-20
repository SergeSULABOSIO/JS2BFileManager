/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.Synchronisateur;

import SOURCES.DB.PhotoDisqueLocal;
import java.util.Vector;

/**
 *
 * @author HP Pavilion
 */


public abstract class SynchronisateurListener {
    public abstract boolean onDeleteSignature(PhotoSuppressionDistante photoSignature);
    public abstract boolean onSaveManifeste(PhotoManifesteServeur photoManifeste);
    public abstract Vector<PhotoSuppressionLocale> onCopieSuppressionsDuPC();
    public abstract Vector<PhotoManifestePC> onCopieManifestesDuPC();
    public abstract PhotoDisqueLocal onCopieLePC();
    public abstract Object onLoadData(Class classe, String dossier, int id);
    public abstract boolean onSaveData(EnregistrementServeur enregistrementServeur);
    public abstract void onAfficheProgression(int total, int actuel, String message);
    public abstract void onSuccess(String message);
    public abstract void onEchec(String message);
}








