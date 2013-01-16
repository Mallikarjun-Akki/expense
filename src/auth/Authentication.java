/**
 * Package that contains all classes needed for authentication
 */
package auth;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;

import db.Db;

/**
 * @author	Srivathsa PV
 * @email	pv.srivathsa@gmail.com
 * @date	16/01/2013
 * 
 * Authentication class provides all functions that maintain the user's authenticity
 */
public class Authentication {
	
	/**
	 * Name of the table to which the class is mapped to
	 * 
	 * @var String
	 */
	private static String t_name = "LOGIN";
	
	/**
	 * Id using which the user logs in
	 * 
	 * @var String
	 */
	private String userid;
	
	/**
	 * Authentication password
	 * 
	 * @var String
	 */
	private String password;
	
	/**
	 * Role of the user. Ex: employee,manager,supervisor,ceo,md,accontant etc.
	 * 
	 * @var String
	 */
	private String role;
	
	/**
	 * Timestamp of last login
	 * 
	 * @var String
	 */
	private String lastlogin;
	
	/**
	 * Constructs an empty object
	 */
	public Authentication() {}
	
	/**
	 * Fetches necessary data and initializes the variables
	 * 
	 * @param String
	 * Id of the user to be fetched (Optional)
	 * 
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	public Authentication(String userid) throws ClassNotFoundException, SQLException {
		Db db = new Db();
		db.connect();
		
		ResultSet rs = db.executeQuery("SELECT * FROM " + t_name + " WHERE ID = '" + userid + "'");
		rs.next();
		
		this.userid = userid;
		this.password = rs.getString("PASSWORD");
		this.role = rs.getString("ROLE");
		this.lastlogin = rs.getString("LASTLOGIN");
		
		db.disconnect();
	}
	
	/**
	 * Gets the id of the user
	 * 
	 * @return String
	 */
	public String getUserid() {
		return this.userid;
	}
	
	/**
	 * Gets the password of the user
	 * 
	 * @return String
	 */
	public String getPassword() {
		return this.password;
	}
	
	/**
	 * Sets the password of the user
	 * 
	 * @param String
	 * 
	 * @throws NoSuchAlgorithmException 
	 */
	public void setPassword(String password) throws NoSuchAlgorithmException {
		this.password = this.encrypt(password);
	}
	
	/**
	 * Checks if the given password is valid
	 * 
	 * @return Boolean
	 * 
	 * @throws NoSuchAlgorithmException 
	 */
	public boolean isPassword(String password) throws NoSuchAlgorithmException {
		return this.password.equals(this.encrypt(password));
	}
	
	/**
	 * Generates a salt and encrypts the given password using MD5 hashing algorithm
	 * 
	 * @param String
	 * 
	 * @return String
	 * 
	 * @throws NoSuchAlgorithmException
	 */
	private String encrypt(String password) throws NoSuchAlgorithmException {
		
		MessageDigest MD5 = MessageDigest.getInstance("MD5");
        MD5.update(this.userid.getBytes());
        BigInteger output = new BigInteger(1, MD5.digest());
        
        String hasheduserid = output.toString(16);
        
        String salt = password + "{" + hasheduserid + "}";
        MD5.update(salt.getBytes());
        output = new BigInteger(1,MD5.digest());
        
        return output.toString(16);
	}
	
	/**
	 * Gets the role of the user
	 * 
	 * @return String
	 */
	public String getRole() {
		return this.role;
	}
	
	/**
	 * Sets the role of the user
	 * 
	 * @param String
	 */
	public void setRole(String role) {
		this.role = role;
	}
	
	/**
	 * Gets the last login timestamp
	 * 
	 * @return String
	 */
	public String getLastlogin() {
		return this.lastlogin;
	}
	
	/**
	 * Sets the last login timestamp
	 * 
	 * @param String
	 */
	public void setLastlogin(String lastlogin) {
		this.lastlogin = lastlogin;
	}
	
	/**
	 * Saves the local values to the database
	 * 
	 * @return Boolean - Returns true on success
	 * 
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	public boolean save() throws ClassNotFoundException, SQLException {
		Db db = new Db();
		db.connect();
		
		String query = "";
		
		if(this.userid.equals("")) {
			query = "INSERT INTO " + t_name +
					" VALUES(DEFAULT,'" + this.password  + "','" + this.role + "','" + this.lastlogin + "')";
		}
		else {
			query = "UPDATE " + t_name + " SET PASSWORD = '" + this.password + "', ROLE = '" + 
					this.role + "', LASTLOGIN = '" + this.lastlogin + "' WHERE USERID = '" + this.userid + "'";
			
		}
		int n = db.executeUpdate(query);
		db.disconnect();
		
		if(n > 0) return true;
		else return false;
	}
	
	/**
	 * Returns a list of authentication objects
	 * 
	 * @param String - Column name as the filter parameter
	 * @param String - Value
	 * 
	 * @return Array[user.Authentication]
	 * 
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	public static Authentication[] list(String column,String value) throws ClassNotFoundException, SQLException {
		Db db = new Db();
		db.connect();
		
		ResultSet rs = db.executeQuery("SELECT COUNT(*) FROM " + t_name + " WHERE " + column + " = '" + value);
		rs.next();
		
		Authentication[] list = new Authentication[rs.getInt(1)];
		
		rs = db.executeQuery("SELECT * FROM " + t_name + " WHERE " + column + " = '" + value);
		
		int i=0;
		while(rs.next()) {
			list[i++] = new Authentication(rs.getString("USERID"));
		}
		
		return list;
	}
	
	/**
	 * Returns a count based on the values
	 * 
	 * @param String - Column name as filter parameter
	 * @param String - Value
	 * 
	 * @return Integer - The count
	 * 
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static int count(String column,String value) throws ClassNotFoundException, SQLException {
		Db db = new Db();
		db.connect();
		
		ResultSet rs = db.executeQuery("SELECT COUNT(*) FROM " + t_name + " WHERE " + column + " = '" + value);
		rs.next();
		
		return rs.getInt(1);
	}
}