/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.db;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Gian Poltéra
 */
public class Derby {

    /**
     * Verbindung zur Datenbank
     */
    Connection connection;

    /**
     * JDBC-Treiber-Name. Muss im Klassenpfad sein.
     */
    static final String DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";

    /**
     * Verbindungs-URL. Erstellt beim ersten Aufruf eine neue Datenbank.
     */
    static final String URL = "jdbc:derby:db;create=true";

    /**
     * Verbindung zur Datenbank herstellen.
     */
    public void connect() {
        // Treiber laden
        try {
            Class.forName(DRIVER).newInstance();
        } catch (Exception ex) {
            System.out.println("Der JDBC-Treiber konnte nicht " + "geladen werden.");
            System.exit(1);
        }

        // Verbindung herstellen
        try {
            connection = DriverManager.getConnection(URL);
        } catch (SQLException ex) {
            System.out.println("Die Verbindung zur Datenbank konnte "
                    + "nicht hergestellt werden. "
                    + "Die Fehlermeldung lautet: " + ex.getMessage());
            System.exit(1);
        }
    }

    /**
     * Verbindung trennen
     */
    public void disconnect() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                connection = null;
            }
        } catch (SQLException ex) {
            System.out.println("Die Verbindung zur Datenbank "
                    + "konnte nicht geschlossen werden. "
                    + "Die Fehlermeldung lautet: " + ex.getMessage());
            System.exit(1);
        }
    }

    /**
     * Alle Tabellennamen anzeigen
     */
    public void showTables() {
        String query = "select tablename from sys.systables";
        String message = "Datenbankfehler. Die Fehlermeldung lautet: ";
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            // Benötige Ressourcen für eine SQL-Anweisung bereitstellen 
            statement = connection.createStatement();
            // Select-Anweisung ausführen
            resultSet = statement.executeQuery(query);
            // Alle Tabllennamen anzeigen
            while (resultSet.next()) {
                System.out.println(resultSet.getString(1));
            }
        } catch (SQLException ex) {
            System.out.println(message + ex.getMessage());
        } finally {

            // Alle Ressourcen wieder freigeben
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException ex) {
                    System.out.println(message + ex.getMessage());
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException ex) {
                    System.out.println(message + ex.getMessage());
                }
            }
        }
    }

    public void createTables() throws SQLException {
        
        Statement statement = connection.createStatement();
        DatabaseMetaData dmd = connection.getMetaData();
        ResultSet rs = dmd.getTables(null, "APP", "ELECTIONSYSTEMINFO", null);
        if (!rs.next()) {
            statement.execute("CREATE TABLE ELECTIONSYSTEMINFO (electionId VARCHAR(100))");
        }
        statement.close();
        
        
        

        //statement = connection.createStatement();
        //statement.execute("CREATE TABLE ElectionSystemInfo (electionId VARCHAR(100))");
        

    }

    public static void main(String[] args) throws SQLException {
        Derby derby = new Derby();
        derby.connect();
        derby.createTables();
        derby.showTables();
        derby.disconnect();

    }
}
