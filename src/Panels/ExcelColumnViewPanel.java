package src.Panels;

import src.SQLFunctions.CreateNewColumns;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;

public class ExcelColumnViewPanel extends JPanel{
    JTable excelColumnTable, categoriesTable;
    JButton showExcelColumnsButton, showCategoriesButton;
    JPanel buttonPanel;
    JPanel tablePanel;


    DefaultTableModel excelColumnsDataModel = new DefaultTableModel() {
        public int getColumnCount() { return 2; }
        public int getRowCount() { return 40;}
        public Object getValueAt(int row, int col) { return null; }
        @Override
        public boolean isCellEditable(int row, int column) {
            //all cells false
            return false;
        }
    };

    DefaultTableModel categoriesDataModel = new DefaultTableModel() {
        public int getColumnCount() { return 2; }
        public int getRowCount() { return 40;}
        public Object getValueAt(int row, int col) { return null; }
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    public ExcelColumnViewPanel() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        excelColumnTable = new JTable(excelColumnsDataModel);
        excelColumnTable.getColumnModel().getColumn(0).setPreferredWidth(150);
        categoriesTable = new JTable(categoriesDataModel);
        categoriesTable.getColumnModel().getColumn(0).setPreferredWidth(150);

        showExcelColumnsButton = new JButton("Show Excel Columns");
        showExcelColumnsButton.addActionListener(e1 -> {
            try {
                excelColumnsDataModel = getExcelColumnsDataModel(excelColumnsDataModel);
                excelColumnTable.setDefaultEditor(Object.class, null);
                excelColumnTable.setModel(excelColumnsDataModel);
                excelColumnTable.getColumnModel().getColumn(0).setPreferredWidth(150);
            } catch (Exception e) {
                e.printStackTrace();
            }
            this.revalidate();
            this.validate();
        });


        showCategoriesButton = new JButton("Show Categories");
        showCategoriesButton.addActionListener(e1 -> {
            try {
                categoriesDataModel = getCategoriesDataModel(categoriesDataModel);
                categoriesTable.setDefaultEditor(Object.class, null);
                categoriesTable.setModel(categoriesDataModel);
                categoriesTable.getColumnModel().getColumn(0).setPreferredWidth(150);
            } catch (Exception e) {
                e.printStackTrace();
            }
            this.revalidate();
            this.validate();
        });

        this.revalidate();
        this.validate();

        buttonPanel = new JPanel();
        buttonPanel.add(showExcelColumnsButton);
        buttonPanel.add(showCategoriesButton);

        tablePanel = new JPanel();
        tablePanel.add(excelColumnTable);
        tablePanel.add(categoriesTable);

        this.add(buttonPanel);
        JScrollPane scroller = new JScrollPane(tablePanel);
        scroller.getVerticalScrollBar().setUnitIncrement(16);
        this.add(BorderLayout.CENTER, scroller);

    }

    public DefaultTableModel getExcelColumnsDataModel(DefaultTableModel dataModel) throws SQLException {
        // guard clause
        if (!AuditAccountClass.isAuditIDEntered() || !AuditAccountClass.isIncomeExpenseEntered()) {
            System.out.println("Please Enter Audit ID and Income/Expense");
            return dataModel;
        }
        if (AuditAccountClass.getIncomeExpenseChar() == 'B') {
            System.out.println("Please specify Income or Expense Individually");
            return dataModel;
        }

        int auditID = AuditAccountClass.getAuditID();
        char incomeExpenseChar = AuditAccountClass.getIncomeExpenseChar();
        boolean isIncome = incomeExpenseChar == 'I';

        CreateNewColumns createNewColumns = new CreateNewColumns();
        ResultSet excelColumns;
        excelColumns = createNewColumns.getExcelColumns(auditID, isIncome);

        this.validate();
        this.revalidate();

        dataModel = buildTableModel(excelColumns);
        return dataModel;
    }

    public DefaultTableModel getCategoriesDataModel(DefaultTableModel dataModel) throws SQLException {
        CreateNewColumns createNewColumns = new CreateNewColumns();
        ResultSet excelColumns;
        excelColumns = createNewColumns.getCategories();

        this.validate();
        this.revalidate();

        dataModel = buildTableModel(excelColumns);
        return dataModel;
    }



    public static DefaultTableModel buildTableModel(ResultSet rs)
            throws SQLException {

        ResultSetMetaData metaData = rs.getMetaData();

        // names of columns
        Vector<String> columnNames = new Vector<String>();
        int columnCount = metaData.getColumnCount();
        for (int column = 1; column <= columnCount; column++) {
            columnNames.add(metaData.getColumnName(column));
        }

        // data of the table
        Vector<Vector<Object>> data = new Vector<Vector<Object>>();
        while (rs.next()) {
            Vector<Object> vector = new Vector<Object>();
            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                vector.add(rs.getObject(columnIndex));
            }
            data.add(vector);
        }

        return new DefaultTableModel(data, columnNames);
    }

}
