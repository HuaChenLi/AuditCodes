package src.SQLFunctions;

import java.sql.*;

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

    public void createExcelColumnSelectionTable() {
        try {
            DatabaseConnection Connection = new DatabaseConnection();
            java.sql.Connection connection = Connection.getConnection();
            Statement stmt = null;
            stmt = connection.createStatement();

            String sql = "CREATE TABLE IF NOT EXISTS excel_column_selection " +
                    "(excel_column_id INTEGER NOT NULL," +
                    "excel_category_mapping_id INTEGER NOT NULL)";
            stmt.executeUpdate(sql);

            stmt.close();

        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Created Excel Column Table successfully");
    }

    public void insertExcelColumnSelection(int excelColumnID, int excelCategoryMappingID) {
        try {
            Connection connection = getConnection();
            PreparedStatement insertColumnSelectionDetails = connection.prepareStatement("""
                    INSERT INTO excel_column_selection (excel_column_id, excel_category_mapping_id)
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
            Connection connection = getConnection();
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
            Connection connection = getConnection();
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
