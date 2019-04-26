/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.Callback;

/**
 *
 * @author HP Pavilion
 */


public abstract class EcouteurSuppression {
    public abstract void onDone(String message, Object[] idsNonSupprimes);
    public abstract void onError(String message);
    public abstract void onProcessing(String message);
}








