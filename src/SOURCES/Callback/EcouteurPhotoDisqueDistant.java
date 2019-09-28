/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.Callback;

import SOURCES.DB.PhotoDisqueDistant;

/**
 *
 * @author HP Pavilion
 */


public abstract class EcouteurPhotoDisqueDistant {
    public abstract void onDone(PhotoDisqueDistant photoDisqueDistant);
    public abstract void onEchec(String message);
    public abstract void onProcessing(String message);
}








