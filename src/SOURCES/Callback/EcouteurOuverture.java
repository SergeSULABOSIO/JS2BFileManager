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


public abstract class EcouteurOuverture {
    public abstract void onDone(String message, Vector data);
    public abstract void onError(String message);
    public abstract void onProcessing(String message);
}








