package src.Panels;

import src.SQLFunctions.CategoryColumnSQLs;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;

import static src.SQLFunctions.DatabaseConnection.buildTableModel;

public class FindExistingMappingFrame extends JFrame {
    JTable knownDescTable;
    JScrollPane knownDescScroll;
    DefaultTableModel knownDescDataModel;
    CategoryColumnSQLs categoryColumnSQLs = new CategoryColumnSQLs();

    public FindExistingMappingFrame(boolean isIncome) {
        knownDescTable = new JTable();

        try {
            knownDescDataModel = getDescriptionDataModel(isIncome);
            knownDescTable.setModel(knownDescDataModel);
            knownDescTable.removeColumn(knownDescTable.getColumn("id"));
            String header = isIncome ? "Income Categories" : "Expense Categories";
            knownDescTable.getColumn("category_values").setHeaderValue(header);
            knownDescTable.setAutoCreateRowSorter(true);
            knownDescTable.getRowSorter().toggleSortOrder(0);
        } catch (Exception e) {
            e.printStackTrace();
        }

        knownDescScroll = new JScrollPane(knownDescTable);

        this.add(knownDescScroll, BorderLayout.CENTER);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setTitle("Existing Descriptions");
        this.pack();
        this.setVisible(true);
    }

    public DefaultTableModel getDescriptionDataModel(boolean isIncome) throws SQLException {
        ResultSet excelColumns;
        excelColumns = categoryColumnSQLs.getCategories(AuditAccountClass.getAuditID(), isIncome);

        this.validate();
        this.revalidate();

        return buildTableModel(excelColumns);
    }

}
