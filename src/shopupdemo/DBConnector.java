/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shopupdemo;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;
import java.sql.DriverManager;

/**
 *
 * @author Jeba Sultana
 */
class DBConnector {
     private Connection conn;
    private Statement statement;
    
    public Connection openConnection(){
        if(conn == null){
            String url = "jdbc:mysql://localhost/";
            String dbName = "shopup";
            String driver = "com.mysql.jdbc.Driver";
            String userName = "root";
            String password = "";
            
            try{
                Class.forName(driver);
                this.conn = (Connection) DriverManager.getConnection(url+dbName, userName, password);
             
                System.out.println("Connection Succeed");
                
            }catch(Exception e){
                System.out.println(e.getMessage());
            }
        }
        return conn;
    }
    
}

    

