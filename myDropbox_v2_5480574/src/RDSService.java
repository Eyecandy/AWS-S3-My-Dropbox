
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author joakimnilfjord
 */
public class RDSService {
    
    //connects to db via mysql.properties file which is located in same folder as classes
    private static Connection getConnection()      {
        try {
            Properties props = new Properties();
            InputStream in;
            in = RDSService.class.getResourceAsStream("mysql.properties");
            props.load(in);
            in.close();
            String driver = props.getProperty("jdbc.driver");     
            Class.forName(driver);
            String url = props.getProperty("jdbc.url");
            String username = props.getProperty("jdbc.username");
            String password = props.getProperty("jdbc.password");   
            Connection con = DriverManager.getConnection(url, username, password);
            return con;
        }   
        catch ( IOException  | ClassNotFoundException|  SQLException e ) {
         Logger.getLogger(RDSService.class.getName()).log(Level.SEVERE, null, e);
        }
        return null; 
    }
    public static String shareUserExist(String inputList[]) {
        Connection con = getConnection();
        String sharedName = inputList[2];
        PreparedStatement ps;
        ResultSet rs;
        String selectSQL = "SELECT id FROM users WHERE username = ?";
         try {
            ps = con.prepareStatement(selectSQL);
            ps.setString(1, sharedName);
            rs = ps.executeQuery();
            String sharedUserId;
            while (rs.next()) {
                sharedUserId = rs.getString("id");
                con.close();
                return sharedUserId;  
            }
        } catch (SQLException ex) {
            Logger.getLogger(RDSService.class.getName()).log(Level.SEVERE, null, ex);
        } 
        
        
        
        return null;
    }
    
    
    
    
    
    
    //registers username & password, given username does not exist in RDS db.
    public static void register(String[] inputList) {
        
        Connection con = getConnection();
        String newUsername = inputList[1];
        String newPassword = inputList[3];
        
        String query = "insert into users (username, password)"
        + " values (?,?)";
        try {
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, newUsername);
            ps.setString(2, newPassword);
            ps.execute();
            con.close();
            MessageHandler.successMessage();
            
        } catch (SQLException ex) {
            Logger.getLogger(RDSService.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    //logs the user in, given that username and password exist in RDS
    public static String login(String[] inputList) {
        
        try {
            String enteredUsername = inputList[1];
            String enteredPassword = inputList[2];
            PreparedStatement ps;
            ResultSet rs;
            String selectSQL = "SELECT id FROM users WHERE username = ? AND password = ?" ;
            Connection con = getConnection();
            ps = con.prepareStatement(selectSQL);
            ps.setString(1, enteredUsername);
            ps.setString(2, enteredPassword);
            rs = ps.executeQuery();
            String userId;
            
            while (rs.next()) {
                userId = rs.getString("id");
                con.close();
                return userId;  
            }
            
            

        } catch (SQLException ex) {
            Logger.getLogger(RDSService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    
    }
}

   
  
    
    
    
   
    
    

