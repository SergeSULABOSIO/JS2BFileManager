/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TEST;

import java.util.Date;

/**
 *
 * @author user
 */
public class TesteurTempo {

    public static void main(String[] a) {
        
        //String userHome = System.getProperty("user.home");
        //System.out.println(userHome);
        //System.getProperties();
        Date test = new Date();
        long temps = Long.parseLong("1582226200650");
        Date testb = new Date(temps);
        System.out.println("" + testb);
        //1582226200650
        //1582222481360
        //1582226330576
        
    }
}






