/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.DB;


import com.mysql.cj.jdbc.MysqlDataSource;
import javax.sql.DataSource;

/**
 *
 * @author user
 */
public class MyDataSourceFactory {

    public static DataSource getMySQLDataSource(String server, String port, String dbname, String user, String pswd) {
        //Properties props = new Properties();
        MysqlDataSource mysqlDS = null;
        try {
            mysqlDS = new MysqlDataSource();
            mysqlDS.setURL("jdbc:mysql://" + server + ":" + port + "/" + dbname+"?zeroDateTimeBehavior=CONVERT_TO_NULL&serverTimezone=UTC");
            mysqlDS.setUser(user);
            mysqlDS.setPassword(pswd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mysqlDS;
    }
}




























