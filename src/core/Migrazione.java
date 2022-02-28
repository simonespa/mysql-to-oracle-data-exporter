package core;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Migrazione {
	
	// connessione a MySQL
	public static final String MySQL_DRIVER = "com.mysql.jdbc.Driver";
	public static final String MySQL_URL = "jdbc:mysql://localhost";
	public static final String MySQL_SUPER_USER = "root";
	public static final String MySQL_PASSWORD = "admin";
	
	// connessione ad Oracle
	public static final String ORACLE_DRIVER = "oracle.jdbc.driver.OracleDriver";
	public static final String ORACLE_URL = "jdbc:oracle:thin:@localhost:1521:xe";
	public static final String ORACLE_SUPER_USER = "system";
	public static final String ORACLE_PASSWORD = "administrator";
	
	
	private void loadDriver(String driver) {
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			System.err.println("Errore nel caricamento del driver: " + driver);
			System.err.println(e.getMessage());
		}
	}
	
	public void extractDatabaseFrom(String url, String user, String password) {
		
		// dichiarazione delle variabili locali
		Connection connection = null;
		DatabaseMetaData metaData = null;
		ResultSet result = null;
		
		// stabilisce la connessione con il DBMS
		try {
			connection = DriverManager.getConnection(url, user, password);
		} catch (SQLException e) {
			System.err.println("Problemi nella connessione al DBMS: " + url);
			System.err.println(e.getMessage());
		}
		
		// acquisice i meta dati dalla connessione
		try {
			metaData = connection.getMetaData();
		} catch (SQLException e) {
			System.err.println("Non è possibile acquisire i MetaData");
			System.err.println(e.getMessage());
		}
		
		// acquisisce lo schema delle tabelle
		String[] tabelle = null;
		try {
			result = metaData.getTables("esame", null, null, null);
			result.last();
			int row = result.getRow();
			result.first();
			tabelle = new String[row];
			for( int i = 1; i <= row; i++ ) {
				System.out.println(result.getString(1));
			}
			System.out.println("Row: " + row);
			result.first();
			System.out.println(result.getString(3));
		} catch (SQLException e) {
			System.err.println("Impossibile acquisire i nomi delle tabelle dello schema");
			System.err.println(e.getMessage());
		}
		
		// chiude il ResultSet
		try {
			result.close();
		} catch (SQLException e) {
			System.err.println("Non è possibile chiudere i cursori nel DBMS");
			System.err.println(e.getMessage());
		}
		
		// chiude la Connection
		try {
			connection.close();
		} catch (SQLException e) {
			System.err.println("Non è possibile chiudere correttamente la connessione");
			System.err.println(e.getMessage());
		}
		
	}
	
	public Migrazione(){
		loadDriver(MySQL_DRIVER);
		loadDriver(ORACLE_DRIVER);
	}
	
	public static void main(String[] args){
		Migrazione m = new Migrazione();
		m.extractDatabaseFrom(MySQL_URL, MySQL_SUPER_USER, MySQL_PASSWORD);
	}
	
}
