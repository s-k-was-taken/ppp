package tools;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import representation.Texts;

/**
 * SQL to receive saved Texts for a company
 *
 */
public class TextLoader {
    /**
     * get text of a company
     * @param company: Table of the company in the database
     * @param date: searched date of the saved policy
     * @param link: web link to the policy
     * @return list of texts
     * @throws SQLException
     */
    public List<Texts> getTexts(String company, String date) throws SQLException{
        //get database instance
        DatabaseInitializer db = DatabaseInitializer.getInstance();
        //set and initialize db connection
        db.connection = null;
        db.initDBConnection();
        //initialize result set, statement and list
        PreparedStatement ps = null;
        ResultSet rs;
        List<Texts> allDates = new ArrayList<Texts>();
        try {
            //switch: SQL for company
            //20 standard companies
            //default for new companies
            String querie = "SELECT CONTENT, SYSTEM_DATE, LINK FROM "+ company.toUpperCase() + " WHERE SYSTEM_DATE = ?";
            ps = db.connection.prepareStatement(querie);
            ps.setString(1, date);
            rs = ps.executeQuery();
            while (rs.next()){
                Texts t = new Texts(rs.getString(1),rs.getString(2), rs.getString(3));
                allDates.add(t);
                //System.out.println(t);
            }
        } catch (SQLException e) {
            System.out.println("Fehler: Datenbankabfrage");
            e.printStackTrace();
        } 
        db.connection.close();
        return allDates;
    }
    
    public List<Texts> loadTexts(String user) throws SQLException{
        //get database instance
        DatabaseInitializer db = DatabaseInitializer.getInstance();
        //set and initialize db connection
        db.connection = null;
        db.initDBConnection();
        //initialize result set, statement and list
        PreparedStatement ps = null;
        ResultSet rs;
        List<Texts> userTexts = new ArrayList<Texts>();
        try {
            String querie = "SELECT TEXT, LINK, DATE FROM "+ user.toLowerCase();
            ps = db.connection.prepareStatement(querie);
            rs = ps.executeQuery();
            while (rs.next()){
            	System.out.println(rs.getString(1) + rs.getString(3) + rs.getString(2));
                Texts t = new Texts(rs.getString(1),rs.getString(3), rs.getString(2));
                userTexts.add(t);
                //System.out.println(t);
            }
            db.connection.close();
        } catch (SQLException e) {
            System.out.println("Fehler: Datenbankabfrage");
            e.printStackTrace();
        } 
        return userTexts;
    }
    
    public void setText(String text, String link, String date, String user) throws SQLException{
        //get database instance
        DatabaseInitializer db = DatabaseInitializer.getInstance();
        //set and initialize db connection
        db.connection = null;
        db.initDBConnection();
        //initialize result set, statement and list
        PreparedStatement ps = null;
        System.out.println("INSERT INTO admin(text,link,date) VALUES("+text+","+link+","+date+")");
		try {
			String insert = "INSERT INTO "+ user.toLowerCase() +"(text,link,date) VALUES(?,?,?)";
			ps = db.connection.prepareStatement(insert);
			ps.setString(1, text);
			ps.setString(2, link);
			ps.setString(3, date);
			ps.executeUpdate();
			db.connection.close();
		} catch (SQLException e) {
			System.out.println("Fehler: Datenbankabfrage");
			e.printStackTrace();
		}
    }
    
    public void removeText(String date, String link, String user) {
    	System.out.println("remove aufgerufen");
        //get database instance
        DatabaseInitializer db = DatabaseInitializer.getInstance();
        //set and initialize db connection
        db.connection = null;
        db.initDBConnection();
        //initialize result set, statement and list
        PreparedStatement ps = null;
        try {
            String querie = "DELETE FROM " +user.toLowerCase() +" WHERE link = ? AND date = ?";
            ps = db.connection.prepareStatement(querie);
            ps.setString(1, link);
            ps.setString(2, date);
            ps.executeUpdate();
            db.connection.close();
            System.out.println("DELETE FROM " +user.toLowerCase() +" WHERE link = "+ link +" AND date = " + date);
        } catch (SQLException e) {
            System.out.println("Fehler: Datenbankabfrage");
            e.printStackTrace();
            
        } 
        System.out.println("remove durchgeführt");
    }
    public static void main(String[] args) {
    	TextLoader tl = new TextLoader();
    	tl.removeText("www.google.de", "1999-29-21","admin");
    }
}
