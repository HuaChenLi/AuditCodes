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
                    "(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "audit_id INTEGER NOT NULL, " +
                    "column_name STRING NOT NULL, " +
                    "is_default BOOLEAN, " +
                    "gst_included BOOLEAN, " +
                    "is_income BOOLEAN)";
            stmt.executeUpdate(sql);

            stmt.close();

        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Created Excel Column Table successfully");
    }

    public void dropExcelColumnTables() {
        try {
            DatabaseConnection Connection = new DatabaseConnection();
            java.sql.Connection connection = Connection.getConnection();
            Statement stmt = null;
            stmt = connection.createStatement();

            String sql = "DROP TABLE IF EXISTS excel_columns";
            stmt.executeUpdate(sql);

            stmt.close();


        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Dropped excel_columns Table successfully");
    }

    public void deleteCategory(int id) {
        try {
            Connection connection = getConnection();
            PreparedStatement deleteCategory = connection.prepareStatement(
                        "DELETE FROM excel_columns " +
                            "WHERE id = ?"
            );
            deleteCategory.setInt(1,id);
            deleteCategory.executeUpdate();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void createCategoryMappings() {
        try {
            DatabaseConnection Connection = new DatabaseConnection();
            java.sql.Connection connection = Connection.getConnection();
            Statement stmt = null;
            stmt = connection.createStatement();

            String sql = "CREATE TABLE IF NOT EXISTS excel_category_mapping " +
                    "(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "account_id INTEGER NOT NULL, " +
                    "category_values STRING NOT NULL, " +
                    "is_income BOOLEAN)";
            stmt.executeUpdate(sql);

            stmt.close();

        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Created Excel Column Table successfully");
    }

    public void createCategory(String s, int accountID, boolean isIncome) {
        try {
            DatabaseConnection Connection = new DatabaseConnection();
            java.sql.Connection connection = Connection.getConnection();
            PreparedStatement insertColumnSelectionDetails = connection.prepareStatement("""
                    INSERT INTO excel_category_mapping (category_values, account_id, is_income)
                    VALUES (?, ?, ?)
                    """);
            insertColumnSelectionDetails.setString(1, s);
            insertColumnSelectionDetails.setInt(2, accountID);
            insertColumnSelectionDetails.setBoolean(3, isIncome);
            insertColumnSelectionDetails.executeUpdate();
            insertColumnSelectionDetails.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
            DatabaseConnection Connection = new DatabaseConnection();
            java.sql.Connection connection = Connection.getConnection();
            PreparedStatement insertColumnSelectionDetails = connection.prepareStatement("""
                    INSERT INTO excel_column_selection (excel_column_id, excel_category_mapping_id)
                    VALUES (?, ?)
                    """);
            insertColumnSelectionDetails.setInt(1, excelColumnID);
            insertColumnSelectionDetails.setInt(2, excelCategoryMappingID);
            insertColumnSelectionDetails.executeUpdate();
            insertColumnSelectionDetails.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ResultSet getExcelColumns(int auditID, boolean isIncome) {
        try {
            DatabaseConnection Connection = new DatabaseConnection();
            java.sql.Connection connection = Connection.getConnection();
            PreparedStatement selectExcelColumns = connection.prepareStatement("""
                    SELECT column_name, id FROM excel_columns
                    WHERE audit_id = ?
                    AND is_income = ?
                    """);
            selectExcelColumns.setInt(1, auditID);
            selectExcelColumns.setBoolean(2, isIncome);

            ResultSet excelColumns;
            excelColumns = selectExcelColumns.executeQuery();

            return excelColumns;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public ResultSet getCategories(int accountID, boolean isIncome) {
        try {
            DatabaseConnection Connection = new DatabaseConnection();
            Connection connection = Connection.getConnection();
            PreparedStatement selectExcelColumns = connection.prepareStatement("""
                    SELECT category_values, id FROM excel_category_mapping
                    WHERE account_id = ?
                    AND is_income = ?
                    """);
            selectExcelColumns.setInt(1, accountID);
            selectExcelColumns.setBoolean(2, isIncome);

            ResultSet excelColumns;
            excelColumns = selectExcelColumns.executeQuery();

            return excelColumns;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
