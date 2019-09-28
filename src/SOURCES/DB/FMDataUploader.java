/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.DB;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.sql.DataSource;

/**
 *
 * @author user
 */
public class FMDataUploader {
    private String server;
    private String port;
    private String dbName;
    private String dbUser;
    private String userPwd;

    private DataSource ds = null;
    private Connection con = null;
    private Statement stmt = null;
    private ResultSet rs = null;

    public FMDataUploader(String server, String port, String dbName, String dbUser, String userPwd) {
        this.server = server;
        this.port = port;
        this.dbName = dbName;
        this.dbUser = dbUser;
        this.userPwd = userPwd;
        open();
    }

    private void open() {
        try {
            this.ds = MyDataSourceFactory.getMySQLDataSource(server, port, dbName, dbUser, userPwd);
            this.con = this.ds.getConnection();
            this.stmt = this.con.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            if (this.rs != null) {
                this.rs.close();
            }
            
            if (this.stmt != null) {
                this.stmt.close();
            }
            if (this.con != null) {
                this.con.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int executerUpdate(String sql) {
        try {
            int rep = this.stmt.executeUpdate(sql);
            //System.out.println("\t\tRésultat d'exécution = " + rep);
            return rep;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        } finally {
            //close();
        }
    }
    
    public ResultSet executerQuery(String sql) {
        try {
            rs = this.stmt.executeQuery(sql);
            //System.out.println("\t\tRésultat d'exécution = " + rep);
            return rs;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            //close();
        }
    }

}













































