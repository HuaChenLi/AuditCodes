package src.SQLFunctions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class MappingTableSQLs extends DatabaseConnection {

    public void createMappingTable() {
        try {
            DatabaseConnection Connection = new DatabaseConnection();
            java.sql.Connection connection = Connection.getConnection();
            Statement stmt = null;
            stmt = connection.createStatement();

            String sql = "CREATE TABLE IF NOT EXISTS mapping_table " +
                    "(id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "map_to STRING," +
                    "map_from STRING)";
            stmt.executeUpdate(sql);

            stmt.close();

        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Created Mapping Table successfully");
    }

    public void createMappingSelectionTable() {
        try {
            DatabaseConnection Connection = new DatabaseConnection();
            java.sql.Connection connection = Connection.getConnection();
            Statement stmt = null;
            stmt = connection.createStatement();

            String sql = "CREATE TABLE IF NOT EXISTS mapping_selection " +
                    "(mapping_table_id INTEGER," +
                    "audit_id INTEGER," +
                    "income_expense STRING)";
            stmt.executeUpdate(sql);

            stmt.close();

        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Created Mapping Selection Table successfully");
    }

    public void insertMapping(String mapFrom, String mapTo, int auditID, char incomeExpenseChar) {
        Statement statement;
        try {
            Connection connection = getConnection();
            statement = connection.createStatement();

            PreparedStatement queryStatement = connection.prepareStatement("""
                        SELECT id, map_from, map_to FROM mapping_table WHERE map_from = ? AND map_to = ? """);
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
                lastRowID.next();
                int lastRowIDValue = lastRowID.getInt("MAX(id)");
                connection.close();
                createMappingSelection(auditID, lastRowIDValue, incomeExpenseChar);

                System.out.println("New Mapping created");
            } else {
//                There is an existing mapping table record
                PreparedStatement grabExistingMappingSelectionRow = connection.prepareStatement("""
                        SELECT mapping_table_id, audit_id, income_expense FROM mapping_selection WHERE mapping_table_id = ? AND audit_id = ?
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
                            SET income_expense = ?
                            WHERE audit_id = ? AND mapping_table_id = ?
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

    public void deleteMapping(int id) {
        try {
            Connection connection = getConnection();
            PreparedStatement deleteMapping = connection.prepareStatement(
                        "DELETE FROM mapping_table " +
                            "WHERE id = ?"
            );
            deleteMapping.setInt(1, id);
            deleteMapping.executeUpdate();
            connection.close();
        } catch (Exception e ) {
            e.printStackTrace();
        }
    }

    public void createMappingSelection(int auditID1, int mappingTableID1, char incomeExpense1) {
        try{
            Connection connection = getConnection();
            PreparedStatement createMappingSelection = connection.prepareStatement("""
                    INSERT INTO mapping_selection (audit_id, mapping_table_id, income_expense)
                    VALUES (?, ?, ?)
                    """);
            createMappingSelection.setInt(1, auditID1);
            createMappingSelection.setInt(2, mappingTableID1);
            createMappingSelection.setString(3, String.valueOf(incomeExpense1));
            createMappingSelection.executeUpdate();
            connection.close();

        } catch (Exception e ) {
            e.printStackTrace();
        }
    }

    public ResultSet getMappings(int accountID, boolean isIncome) {
        char incomeExpenseChar;
        if (isIncome) {
            incomeExpenseChar ='I';
        } else {
            incomeExpenseChar = 'E';
        }

        try {
            Connection connection = getConnection();
            PreparedStatement selectMappings = connection.prepareStatement("""
                    SELECT id, map_from, map_to FROM mapping_table
                    INNER JOIN mapping_selection ON mapping_table.id = mapping_selection.mapping_table_id
                    WHERE audit_id = ?
                    AND (income_expense = ? OR income_expense  = 'B')
                    """);
            selectMappings.setInt(1, accountID);
            selectMappings.setString(2, String.valueOf(incomeExpenseChar));

            ResultSet mappings;
            mappings = selectMappings.executeQuery();
            return mappings;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<String> getMapFrom(int accountID, boolean isIncome) {
        ArrayList<String> mapFromValues = new ArrayList<>();
        char incomeExpenseChar;
        if (isIncome) {
            incomeExpenseChar ='I';
        } else {
            incomeExpenseChar = 'E';
        }
        try {
            Connection connection = getConnection();
            PreparedStatement selectMappings = connection.prepareStatement("""
                    SELECT id, map_from FROM mapping_table
                    INNER JOIN mapping_selection ON mapping_table.id = mapping_selection.mapping_table_id
                    WHERE audit_id = ?
                    AND (income_expense = ? OR income_expense  = 'B')
                    """);
            selectMappings.setInt(1, accountID);
            selectMappings.setString(2, String.valueOf(incomeExpenseChar));

            ResultSet mappings;
            mappings = selectMappings.executeQuery();

            while (mappings.next()) {
                mapFromValues.add(mappings.getString("map_from"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return mapFromValues;
    }
}
