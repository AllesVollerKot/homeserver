import java.io.IOException;
import java.sql.*;
import java.util.Date;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;

public class MySQLAccess {
	private Connection con;
	private Statement st;
	private ResultSet rs;

	public MySQLAccess() {

		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://vweb11.nitrado.net:3306/ni117501_1sql3", "ni117501_1sql3", "DesireRogue1337");
			st = con.createStatement();
		} catch (Exception ex) {
			System.out.println("Error: " + ex);
		}

	}
	
	public void sendMessage(String text) {
		String Nachricht = text.replaceAll(" ", "+");
		HttpClient client = new HttpClient();
		GetMethod get = new GetMethod("http://sexyateam.de/home/send_message.php?message=" + Nachricht);
		try {
			client.executeMethod(get);			
			byte[] responseBody = get.getResponseBody();
			get.releaseConnection();
			System.out.println(new String(responseBody));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void getSingleData(String query, String Spaltenname) {
		try {

			rs = st.executeQuery(query);
			System.out.println("Records");
			while (rs.next()) {
				String titel = rs.getString(Spaltenname);
				System.out.println(titel);
			}
		} catch (Exception ex) {
			System.out.println("Error: " + ex);
		}
	}
	
	public void setNewsData(String titel, String text, String link, String html, Date time) {
		String query = " insert news_data (titel, newstext, link, html, time)" + " values (?, ?, ?, ?, ?)";

		java.text.SimpleDateFormat sdf = 
			     new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String timeString = sdf.format(time);		
		
		PreparedStatement preparedStmt;
		try {
			preparedStmt = con.prepareStatement(query);
			preparedStmt.setString(1, titel);
			preparedStmt.setString(2, text);
			preparedStmt.setString(3, link);
			preparedStmt.setString(4, html);
			preparedStmt.setString(5, timeString);
			System.out.println(preparedStmt.toString());
			preparedStmt.execute();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}		
	}
	
	
	
	public void setEventData(String titel, String link, String ort, String beginnZeit, String endZeit, String beginnDatum, String endDatum, String html) {
		String query = " insert event_data (titel, ort, link, beginnzeit, endzeit, begindatum, enddatum, html)" + " values (?, ?, ?, ?, ?, ?, ?, ?)";
		PreparedStatement preparedStmt;
		try {
			preparedStmt = con.prepareStatement(query);
			preparedStmt.setString(1, titel);
			preparedStmt.setString(2, ort);
			preparedStmt.setString(3, link);
			preparedStmt.setString(4, beginnZeit);
			preparedStmt.setString(5, endZeit);
			preparedStmt.setString(6, beginnDatum);
			preparedStmt.setString(7, endDatum);
			preparedStmt.setString(8, html);
			System.out.println(preparedStmt.toString());
			preparedStmt.execute();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}		
	}

	public void setWiwiData(String titel, String text, String link, String html) {
		String query = " insert wiwi_data (titel, wiwitext, link, html)" + " values (?, ?, ?, ?)";
		
		
		
		PreparedStatement preparedStmt;
		try {
			preparedStmt = con.prepareStatement(query);
			preparedStmt.setString(1, titel);
			preparedStmt.setString(2, text);
			preparedStmt.setString(3, link);
			preparedStmt.setString(4, html);		
			System.out.println(preparedStmt.toString());
			preparedStmt.execute();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}		
	}
	
	public void setSmkData(String titel, String text, String text2, String link, String html) {		
		String query = " insert smk_data (titel, smktext, smktext2, link, html)" + " values (?, ?, ?, ?, ?)";
		PreparedStatement preparedStmt;
		try {
			preparedStmt = con.prepareStatement(query);
			preparedStmt.setString(1, titel);
			preparedStmt.setString(2, text);
			preparedStmt.setString(3, text2);
			preparedStmt.setString(4, link);
			preparedStmt.setString(5, html);
			System.out.println(preparedStmt.toString());
			preparedStmt.execute();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}		
	}
	
	public void setIksData(String titel, String text) {
		String query = "INSERT INTO iks_data SET titel = '"
						+titel+
						"', ikstext = '"
						+text+
						"';";
		
		System.out.println(query);
		try {
			st.execute(query);
		} catch (SQLException e) {
			System.out.println("Error: " + e);
			e.printStackTrace();
		}
	}
	
	
	
	public int testData(String query) {
		try {
			rs = st.executeQuery(query);
			rs.last();			
		} catch (Exception ex) {
			System.out.println("Error: " + ex);
		}
		int anzahlRs = 0;
		try {
			anzahlRs = rs.getRow();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return anzahlRs;
	}
}
