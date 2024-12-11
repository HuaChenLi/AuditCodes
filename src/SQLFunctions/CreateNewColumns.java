package src.SQLFunctions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class CreateNewColumns extends DatabaseConnection{
    public void insertColumn(int auditID, String columnName, boolean gSTIncluded, boolean isIncome, boolean isExpense) {
        Statement statement;
        if (isIncome) {
            try {
                Connection connection = getConnection();
                PreparedStatement insertColumnDetails = connection.prepareStatement("""
                    INSERT INTO ExcelColumns (AuditID, ColumnName, IsDefault, GSTIncluded, IsIncome)
                    VALUES (?, ?, ?, ?, ?)
                    """);
                insertColumnDetails.setInt(1, auditID);
                insertColumnDetails.setString(2, columnName);
                insertColumnDetails.setBoolean(3, false);
                insertColumnDetails.setBoolean(4, gSTIncluded);
                insertColumnDetails.setBoolean(5, isIncome);
                insertColumnDetails.executeUpdate();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (isExpense) {
            try {
                Connection connection = getConnection();
                PreparedStatement insertColumnDetails = connection.prepareStatement("""
                    INSERT INTO ExcelColumns (AuditID, ColumnName, IsDefault, GSTIncluded, IsIncome)
                    VALUES (?, ?, ?, ?, ?)
                    """);
                insertColumnDetails.setInt(1, auditID);
                insertColumnDetails.setString(2, columnName);
                insertColumnDetails.setBoolean(3, false);
                insertColumnDetails.setBoolean(4, gSTIncluded);
                insertColumnDetails.setBoolean(5, !isExpense);
                insertColumnDetails.executeUpdate();
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
