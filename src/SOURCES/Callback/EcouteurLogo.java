/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.Callback;

import java.io.File;

/**
 *
 * @author HP Pavilion
 */


public abstract class EcouteurLogo {
    public abstract void onLogoReady(File fichierLocaleLogo);
    public abstract void onLogoDeleted();
    public abstract void onLogoError(String message);
    public abstract void onLogoProcessing(String message);
}



