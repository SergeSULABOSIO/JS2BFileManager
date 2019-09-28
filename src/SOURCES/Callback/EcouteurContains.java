/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.Callback;

import java.util.Vector;


/**
 *
 * @author HP Pavilion
 */


public abstract class EcouteurContains {
    public abstract boolean isOk(Object objectToCheck);
    public abstract void onSuccess(String message, boolean reponse, Vector TabObjsFound);
    public abstract void onError(String message);
    public abstract void onProcessing(String message);
    
}










































