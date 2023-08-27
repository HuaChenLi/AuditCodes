package src.SQLFunctions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class AuditIDSQLs extends DatabaseConnection {
    Connection connection = getConnection();
    public String getAuditName(int auditID) {
        Statement statement;
        try {
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
