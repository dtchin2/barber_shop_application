import java.io.*;
import java.awt.event.*;
import java.awt.*;
import java.sql.*;

import java.io.*;
import java.awt.event.*;
import java.awt.*;
import java.sql.*;
import java.util.*;

public class BarberShopApp implements BarberFunctions {
	
	Connection dbConnection = null;
	Statement dbStatement = null;
	ResultSet dbResults = null;
	PreparedStatement insertIntoDatabase;
	PreparedStatement removeFromDatabase;
	
	private String database = "jdbc:mysql://localhost:3306/barber_shop";
	private String user = "root";
	private String password = "";
	
	PrintWriter schedule = null; // file for schedule
	PrintWriter revenue = null; // file for revenue report
	PrintWriter databaseLog = null; // file for status of the database (Ex. added, deleted, nothing changed etc)
	
	private int total = 0; // total for revenue
	
	Calendar currDate;
	
	long phoneNum;
	String name;
	String day;
	String time;
	String service;
	int price;

	public void connectToDatabase() throws SQLException, ClassNotFoundException {
		try {
			
			Class.forName("com.mysql.jdbc.Driver");
			// 1. Connect To database
			dbConnection = DriverManager.getConnection(database,user,password);
			//System.out.println("Test");
			// 2. Create the statement
			dbStatement = dbConnection.createStatement();
			
			// 3. Generate results
			dbResults = dbStatement.executeQuery("SELECT * FROM Barber_Shop"); // client phone
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		finally {
		}
	}

	public void generateSchedule() throws SQLException, IOException, ClassNotFoundException{
		try {
			currDate = Calendar.getInstance();
			schedule = new PrintWriter(new FileWriter("Schedule.txt"));
			schedule.println("\t\t\t\tCODE BUSTERS FOUR BARBERSHOP SCHEDULE");
			schedule.println("\t\t\t\t\t" + currDate.getTime());
			schedule.println(" ");
			
			schedule.println("PHONE NUMBER\t| \tNAME | \t\t\tDAY | \tTIME | \tSERVICE | \tPRICE");
			schedule.println("----------------------------------------------------------------------------");
			connectToDatabase();
			
			while(dbResults.next()) {
				phoneNum = dbResults.getLong(1);
				name = dbResults.getString(2);
				day = dbResults.getString(3);
				time = dbResults.getString(4);
				service = dbResults.getString(5);
				price = dbResults.getInt(6);
				
				schedule.println(phoneNum + "\t|\t" + name + "\t|\t" + day + "|\t" + time + "|\t" + service + "|\t $ " + price);
				
			}
		}
		catch(FileNotFoundException e) {
			System.out.println("File Not Found");
		}
		finally {
			schedule.close();
		}
		
	}

	public void generateRevenueReport() throws SQLException, IOException, ClassNotFoundException {
		try {
			total = 0;
			currDate = Calendar.getInstance();
			revenue = new PrintWriter(new FileWriter("Revenue.txt"));
			revenue.println("\t\t\t\tCODE BUSTERS FOUR BARBERSHOP WEEKLY REVENUE REPORT");
			revenue.println("\t\t\t\t\t" + currDate.getTime());
			revenue.println(" ");
			
			revenue.println("\t\t\t\tDAY\tTIME\tSERVICE\tPRICE");
			revenue.println("\t\t\t----------------------------------------------------------");
			connectToDatabase();
			
			while(dbResults.next()) {
				revenue.println("\t\t\t\t" + dbResults.getString(3) +  "\t " + dbResults.getString(4) + "\t " + dbResults.getString(5) + "  $ " 
						+ dbResults.getInt(6));
				
				int cost = dbResults.getInt(6);
				total = total + cost;
			}
			revenue.println(" ");
			revenue.println("\t\t\t\t\t   ---------------------------------");
			revenue.println("\t\t\t\t\t  Total Weekly Revenue: $ " + total);
		}
		catch(FileNotFoundException fe) {
			fe.printStackTrace();
		}
		finally {
			revenue.close();
		}
		
	}
	
	public void addAppointment(long phone,String n,String d,String t, String s, int p) throws ClassNotFoundException, SQLException, IOException {
		// method to add to database
		try {
			connectToDatabase(); // get connection to database
			 String intoData = " insert into barber_shop (client_phone, client_name, app_day, app_time, service,price)"
				        + " values (?, ?, ?, ?, ?,?)";
			 insertIntoDatabase = dbConnection.prepareStatement(intoData);
			 
			 try {
				 databaseLog = new PrintWriter(new FileWriter("Database Logs.txt"),true);
			 }
			 catch(FileNotFoundException e) {
				 System.out.println("File Not Found");
			 }
			 
			 while(dbResults.next()) {
				 if(phone == dbResults.getLong(1)) {
					 databaseLog.println(phone + " Is Already Scheduled For The Week");
				 }
			 }
			 if(phone < 1000000000) {
				 databaseLog.println(phone + " is an Invalid Phone Number" +
						 " Database Unaffected");
			 }
			 if(n.equals("") || n.equals(" ")) {
				 databaseLog.println("Invalid Name Entry Database Unaffected");
			 }
			 if(d.equals(" ")) {
				 databaseLog.print("No Day Selected Database Unaffected");
			 }
			 if(t.equals(" ")) {
				 databaseLog.print("No Time Selected Database Unaffected");
			 }
			 if(s.equals(" ")) {
				 databaseLog.print("No Service Selected Database Unaffected");
			 }
			 else {
				 insertIntoDatabase.setLong(1, phone);
				 insertIntoDatabase.setString(2, n);
				 insertIntoDatabase.setString(3, d);
				 insertIntoDatabase.setString(4, t);
				 insertIntoDatabase.setString(5, s);
				 insertIntoDatabase.setInt(6, p);
				 
				 insertIntoDatabase.execute();
			 }
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
	}

	public void deleteAppointment(long phone) throws ClassNotFoundException, SQLException {
		try {
			connectToDatabase();
			
			String removeFrom = "delete from barber_shop where client_phone = ?";
			removeFromDatabase = dbConnection.prepareStatement(removeFrom);
			
			while(dbResults.next()) {
				if(dbResults.getLong(1) != phone) {
					System.out.println("Not In Database");
				}
				else {
					removeFromDatabase.setLong(1, phone);
					removeFromDatabase.executeUpdate();
				}
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
	}
}
