package src.SQLFunctions;

import java.sql.*;

public class CreateNewColumns extends DatabaseConnection {
    Connection connection = getConnection();
    public void insertColumn(int auditID, String columnName, boolean gSTIncluded, boolean isIncome, boolean isExpense) {
        Statement statement;
        if (isIncome) {
            try {
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

    public ResultSet getExcelColumns(int auditID, boolean isIncome) {
        try {
            PreparedStatement selectExcelColumns = connection.prepareStatement("""
                    SELECT ColumnName, ExcelColumnID FROM ExcelColumns
                    WHERE AuditID = ?
                    AND IsIncome = ?
                    """);
            selectExcelColumns.setInt(1, auditID);
            selectExcelColumns.setBoolean(2, isIncome);

            ResultSet excelColumns;
            excelColumns = selectExcelColumns.executeQuery();
            System.out.println(excelColumns);

            return excelColumns;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public ResultSet getCategories() {
        try {
            PreparedStatement selectExcelColumns = connection.prepareStatement("""
                    SELECT CategoryValues, ID FROM ExcelCategoryMapping
                    """);

            ResultSet excelColumns;
            excelColumns = selectExcelColumns.executeQuery();
            System.out.println(excelColumns);

            return excelColumns;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



}
