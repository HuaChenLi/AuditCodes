package src.SQLFunctions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class CreateNewColumns extends DatabaseConnection{
    public void createExcelColumnTables() {
        try {
            DatabaseConnection Connection = new DatabaseConnection();
            java.sql.Connection connection = Connection.getConnection();
            Statement stmt = null;
            stmt = connection.createStatement();

            String sql = "CREATE TABLE IF NOT EXISTS excel_columns " +
                    "(audit_id INTEGER NOT NULL," +
                    "column_name STRING NOT NULL," +
                    "is_default BOOLEAN," +
                    "gst_included BOOLEAN," +
                    "is_income BOOLEAN)";
            stmt.executeUpdate(sql);

            stmt.close();

        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Created Excel Column Table successfully");
    }

    public void insertColumn(int auditID, String columnName, boolean gSTIncluded, boolean isIncome, boolean isExpense) {
        Statement statement;
        if (isIncome) {
            try {
                Connection connection = getConnection();
                PreparedStatement insertColumnDetails = connection.prepareStatement("""
                    INSERT INTO excel_columns (audit_id, column_name, is_default, gst_included, is_income)
                    VALUES (?, ?, ?, ?, ?)
                    """);
                insertColumnDetails.setInt(1, auditID);
                insertColumnDetails.setString(2, columnName);
                insertColumnDetails.setBoolean(3, false);
                insertColumnDetails.setBoolean(4, gSTIncluded);
                insertColumnDetails.setBoolean(5, isIncome);
                insertColumnDetails.executeUpdate();
                insertColumnDetails.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (isExpense) {
            try {
                Connection connection = getConnection();
                PreparedStatement insertColumnDetails = connection.prepareStatement("""
                    INSERT INTO excel_columns (audit_id, column_name, is_default, gst_included, is_income)
                    VALUES (?, ?, ?, ?, ?)
                    """);
                insertColumnDetails.setInt(1, auditID);
                insertColumnDetails.setString(2, columnName);
                insertColumnDetails.setBoolean(3, false);
                insertColumnDetails.setBoolean(4, gSTIncluded);
                insertColumnDetails.setBoolean(5, !isExpense);
                insertColumnDetails.executeUpdate();
                insertColumnDetails.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void insertExcelColumnSelection(int excelColumnID, int excelCategoryMappingID) {
        try {
            Connection connection = getConnection();
            PreparedStatement insertColumnSelectionDetails = connection.prepareStatement("""
                    INSERT INTO ExcelColumnSelection (ExcelColumnID, ExcelCategoryMappingID)
                    VALUES (?, ?)
                    """);
            insertColumnSelectionDetails.setInt(1, excelColumnID);
            insertColumnSelectionDetails.setInt(2, excelCategoryMappingID);
            insertColumnSelectionDetails.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
