package src.Lib;

import src.SQLFunctions.CategoryColumnSQLs;

import javax.swing.table.DefaultTableModel;
import java.sql.ResultSet;
import java.sql.SQLException;

import static src.SQLFunctions.DatabaseConnection.buildTableModel;

public class CategoryModel {
    public static DefaultTableModel getCategoryDataModel(int accountID, boolean isIncome) throws SQLException {
        CategoryColumnSQLs categoryColumnSQLs = new CategoryColumnSQLs();
        ResultSet excelColumns;
        excelColumns = categoryColumnSQLs.getExcelColumns(accountID, isIncome);

        return buildTableModel(excelColumns);
    }

}
