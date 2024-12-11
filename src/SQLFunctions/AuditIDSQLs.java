package src.SQLFunctions;

import src.Panels.AuditAccountID;

import javax.swing.table.DefaultTableModel;
import javax.xml.crypto.Data;
import java.sql.*;
import java.util.Vector;

public class AuditIDSQLs extends DatabaseConnection {
    public void createAuditTable() {
        try {
            DatabaseConnection Connection = new DatabaseConnection();
            java.sql.Connection connection = Connection.getConnection();
            Statement stmt = null;
            stmt = connection.createStatement();

            String sql = "CREATE TABLE IF NOT EXISTS audit_id " +
                    "(id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "audit_name STRING NOT NULL)";
            stmt.executeUpdate(sql);

            stmt.close();

        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Created Audit ID Table successfully");
    }

    public void dropAuditID() {
        try {
            DatabaseConnection Connection = new DatabaseConnection();
            java.sql.Connection connection = Connection.getConnection();
            Statement stmt = null;
            stmt = connection.createStatement();

            String sql = "DROP TABLE audit_id";
            stmt.executeUpdate(sql);

            stmt.close();


        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Dropped audit_id Table successfully");
    }

    public void createAccount(String str) {
        try {
            DatabaseConnection Connection = new DatabaseConnection();
            Connection connection = Connection.getConnection();
            PreparedStatement ps = connection.prepareStatement("""
                    INSERT INTO audit_id(audit_name)
                    VALUES(?)""");
            ps.setString(1, str);
            ps.executeUpdate();
            connection.close();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public Vector<AuditAccountID> getAllAccounts() {
        try {
            DatabaseConnection Connection = new DatabaseConnection();
            Connection connection = Connection.getConnection();
            PreparedStatement getAuditID = connection.prepareStatement("""
                    SELECT id, audit_name FROM audit_id
                    ORDER BY id
                    """);

            ResultSet rs = getAuditID.executeQuery();

            // data of the table
            Vector<AuditAccountID> data = new Vector<AuditAccountID>();
            while (rs.next()) {
                AuditAccountID auditAccountID = new AuditAccountID(rs.getInt("id"), rs.getString("audit_name"));
                data.add(auditAccountID);
            }

            connection.close();
            return data;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    public String getAuditName(int auditID) {
        Statement statement;
        try {
            DatabaseConnection Connection = new DatabaseConnection();
            Connection connection = getConnection();
            PreparedStatement getAuditNames = connection.prepareStatement("""
                    SELECT audit_name FROM audit_id WHERE id = ?
                    """);
            getAuditNames.setInt(1, auditID);

            ResultSet auditName;
            auditName = getAuditNames.executeQuery();

            if(auditName.next()) {
                return auditName.getString("audit_name");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getStartingAuditNumber() {
        Statement statement;
        try {
            DatabaseConnection Connection = new DatabaseConnection();
            Connection connection = getConnection();
            PreparedStatement getAuditNames = connection.prepareStatement("""
                    SELECT id FROM audit_id LIMIT 1
                    """);

            ResultSet rs;
            rs = getAuditNames.executeQuery();
            int id = rs.getInt(1);
            connection.close();
            return id;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }
}
