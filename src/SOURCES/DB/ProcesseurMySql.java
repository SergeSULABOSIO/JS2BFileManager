/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.DB;

import SOURCES.Utilitaires.UtilFileManager;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.sql.DataSource;

/**
 *
 * @author user
 */
public class ProcesseurMySql {

    public static void main(String[] args) {
        try {
            new Thread() {
                public void run() {
                    testDataSource();
                }
            }.start();
            System.out.println("Patientez...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void testDataSource() {
        DataSource ds = null;
        ds = MyDataSourceFactory.getMySQLDataSource("www.visiterlardc.com", "3306", "visiterl_s2b", "visiterl_s2bUser", "ssula@s2b-simple.com");
        
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            con = ds.getConnection();
            stmt = con.createStatement();
            rs = stmt.executeQuery("select * from Entreprise");
            while (rs.next()) {
                //System.out.println("Utilisateur ID=" + rs.getInt("id") + ", Nom=" + rs.getString("nom"));
                System.out.println("Utilisateur ID=" + rs.getString(1) + ", Nom=" + rs.getString(2));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
