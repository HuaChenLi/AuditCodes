package src.SQLFunctions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class AuditIDSQLs extends DatabaseConnection {
    public void createAuditTable() {
        try {
            DatabaseConnection Connection = new DatabaseConnection();
            java.sql.Connection connection = Connection.getConnection();
            Statement stmt = null;
            stmt = connection.createStatement();

            String sql = "CREATE TABLE IF NOT EXISTS audit_id " +
                    "(id PRIMARY KEY NOT NULL UNIQUE," +
                    "audit_name STRING NOT NULL)";
            stmt.executeUpdate(sql);

            stmt.close();

        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Created Audit ID Table successfully");
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
