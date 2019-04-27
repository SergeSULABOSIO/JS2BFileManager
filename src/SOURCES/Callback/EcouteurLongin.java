/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.Callback;

import SOURCES.Objets.Session;

/**
 *
 * @author HP Pavilion
 */


public abstract class EcouteurLongin {
    public abstract void onConnected(String message, Session session);
    public abstract void onEchec(String message);
    public abstract void onProcessing(String message);
}








