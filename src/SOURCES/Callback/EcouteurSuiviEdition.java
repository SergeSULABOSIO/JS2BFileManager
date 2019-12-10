/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.Callback;

import java.util.Date;


/**
 *
 * @author HP Pavilion
 */


public abstract class EcouteurSuiviEdition {
    public abstract void onSuiveurActive(Date dateDernireModification);
    public abstract void onSuiveurDesactive();
}








