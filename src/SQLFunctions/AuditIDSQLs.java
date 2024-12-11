package src.SQLFunctions;

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

    public Vector getAllAccounts() {
        try {
            DatabaseConnection Connection = new DatabaseConnection();
            Connection connection = Connection.getConnection();
            PreparedStatement getAuditID = connection.prepareStatement("""
                    SELECT id, audit_name FROM audit_id
                    ORDER BY id
                    """);

            ResultSet rs = getAuditID.executeQuery();

            ResultSetMetaData metaData = rs.getMetaData();

            // names of columns
            Vector<String> columnNames = new Vector<String>();
            int columnCount = metaData.getColumnCount();
            for (int column = 1; column <= columnCount; column++) {
                columnNames.add(metaData.getColumnName(column));
            }


            // data of the table
            Vector<Vector<Object>> data = new Vector<Vector<Object>>();
            while (rs.next()) {
                Vector<Object> vector = new Vector<Object>();
                for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                    vector.add(rs.getObject(columnIndex));
                }
                data.add(vector);
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
}
