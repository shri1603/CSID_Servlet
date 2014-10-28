

import java.sql.Connection;
import java.sql.DriverManager;

	public class DBConnection {
	      
	    public static Connection con = null;
	    String strDBString;
	    String strDBUname;
	    String strDBPwd;
	  
	    public DBConnection() {
	       
	        strDBString = "jdbc:mysql://192.168.1.131:3306/ITM_CSI_26SEPT";
	        strDBUname = "root";
	        strDBPwd = "netweb12";
	    }
	  
	    public Connection getConnection() {
	        try {
	            Class.forName("com.mysql.jdbc.Driver");

	            if (con == null) {
	                con = DriverManager.getConnection(strDBString, strDBUname, strDBPwd);
                       
	            }
	            if (null != con) {
	                //Statement SubcriberStatetab = con.createStatement();
	                System.out.println(" Connection Successful");
	            } else {
	                System.out.println(" Connection Fail");
	            }
	        } catch (Exception e) {
	            try {
	                con = DriverManager.getConnection(strDBString, strDBUname, strDBPwd);
	            } catch (Exception e1) {
	                System.out.println("Failed to reconnect");
	            }
	            e.printStackTrace();
	        }
	        return con;
	    }
	}

