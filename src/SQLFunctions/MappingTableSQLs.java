package src.SQLFunctions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class MappingTableSQLs extends DatabaseConnection {
    Connection connection = getConnection();
    public void insertMapping(String mapFrom, String mapTo, int auditID, char incomeExpenseChar) {
        Statement statement;
        try {
            statement = connection.createStatement();

            PreparedStatement queryStatement = connection.prepareStatement("""
                        SELECT id, map_from, map_to FROM mapping_table WHERE map_from = ? AND map_to = ?""");
            queryStatement.setString(1, mapFrom);
            queryStatement.setString(2, mapTo);

            ResultSet existingMappingTableRecord;
            existingMappingTableRecord = queryStatement.executeQuery();

//            This chunk is so hard to read
//            I dread coming back here to update stuff, no idea how to make it better
            if (existingMappingTableRecord.next() == false) {
                PreparedStatement createMappingRow = connection.prepareStatement("""
                        INSERT INTO mapping_table (map_from, map_to)
                        VALUES (?, ?);
                        """);
                createMappingRow.setString(1, mapFrom);
                createMappingRow.setString(2, mapTo);

                createMappingRow.executeUpdate();

                PreparedStatement grabLastRowID = connection.prepareStatement("""
                        SELECT MAX(id) FROM mapping_table
                        """);
                ResultSet lastRowID;
                lastRowID = grabLastRowID.executeQuery();
                int lastRowIDValue = lastRowID.getInt("MAX(id)");
                createMappingSelection(auditID, lastRowIDValue, incomeExpenseChar);

                System.out.println("New Mapping created");
            } else {
//                There is an existing mapping table record
                PreparedStatement grabExistingMappingSelectionRow = connection.prepareStatement("""
                        SELECT MappingTableID, AuditID, IncomeExpense FROM mapping_selection WHERE MappingTableID = ? AND AuditID = ?
                        """);

                int existingMappingTableID = existingMappingTableRecord.getInt("id");
                grabExistingMappingSelectionRow.setInt(1, existingMappingTableID);
                grabExistingMappingSelectionRow.setInt(2, auditID);

                ResultSet mappingSelectionRow;
                mappingSelectionRow = grabExistingMappingSelectionRow.executeQuery();

//                This section checks if the mapping_selection has a row existing already
                if (!mappingSelectionRow.next()) {
                    System.out.println("Creating new Mapping Selection record");
                    createMappingSelection(auditID, existingMappingTableID, incomeExpenseChar);

                } else {
                    PreparedStatement updateMappingSelection = connection.prepareStatement("""
                            UPDATE mapping_selection
                            SET IncomeExpense = ?
                            WHERE AuditID = ? AND MappingTableID = ?
                            """);
                    updateMappingSelection.setString(1, String.valueOf(incomeExpenseChar));
                    updateMappingSelection.setInt(2, auditID);
                    updateMappingSelection.setInt(3, existingMappingTableID);
                    System.out.println("Update Existing Mapping Selection Record");
                }
            }
            existingMappingTableRecord.close();
            statement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createMappingSelection(int auditID1, int mappingTableID1, char incomeExpense1) {
        try{
            PreparedStatement createMappingSelection = connection.prepareStatement("""
                    INSERT INTO mapping_selection (AuditID, MappingTableID, IncomeExpense)
                    VALUES (?, ?, ?)
                    """);
            createMappingSelection.setInt(1, auditID1);
            createMappingSelection.setInt(2, mappingTableID1);
            createMappingSelection.setString(3, String.valueOf(incomeExpense1));
            createMappingSelection.executeUpdate();

        } catch (Exception e ) {
            e.printStackTrace();
        }

    }
}
