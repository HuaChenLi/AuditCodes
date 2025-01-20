package src.Lib;

import src.Panels.AuditAccountClass;
import src.SQLFunctions.CategoryColumnSQLs;

import javax.swing.table.DefaultTableModel;
import java.sql.ResultSet;
import java.sql.SQLException;

import static src.SQLFunctions.DatabaseConnection.buildTableModel;

public class DescData {
    private static final CategoryColumnSQLs categoryColumnSQLs = new CategoryColumnSQLs();
    public static DefaultTableModel getDescriptionDataModel(boolean isIncome) throws SQLException {
        ResultSet excelColumns;
        excelColumns = categoryColumnSQLs.getCategories(AuditAccountClass.getAuditID(), isIncome);

        return buildTableModel(excelColumns);
    }
}
