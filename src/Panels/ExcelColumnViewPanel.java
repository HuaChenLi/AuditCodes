package src.Panels;

import src.Interfaces.Model;
import src.SQLFunctions.CreateNewColumns;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ExcelColumnViewPanel extends JPanel implements Model {
    JTable excelIncomeColumnTable;
    JTable excelExpenseColumnTable;
    JTable incomeCategoriesTable;
    JTable expenseCategoriesTable;
    JPanel tablePanel;
    DefaultTableModel excelIncomeColumnsDataModel = new DefaultTableModel() {
        public int getColumnCount() { return 2; }
        public int getRowCount() { return 40; }
        public Object getValueAt(int row, int col) { return null; }
        @Override
        public boolean isCellEditable(int row, int column) {
            //all cells false
            return false;
        }
    };
    DefaultTableModel excelExpenseColumnsDataModel = new DefaultTableModel() {
        public int getColumnCount() { return 2; }
        public int getRowCount() { return 40; }
        public Object getValueAt(int row, int col) { return null; }
        @Override
        public boolean isCellEditable(int row, int column) {
            //all cells false
            return false;
        }
    };
    DefaultTableModel incomeCategoriesDataModel = new DefaultTableModel() {
        public int getColumnCount() { return 2; }
        public int getRowCount() { return 40;}
        public Object getValueAt(int row, int col) { return null; }
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    DefaultTableModel expenseCategoriesDataModel = new DefaultTableModel() {
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

        excelIncomeColumnTable = new JTable(excelIncomeColumnsDataModel);
        excelIncomeColumnTable.getColumnModel().getColumn(0).setPreferredWidth(150);

        excelExpenseColumnTable = new JTable(excelExpenseColumnsDataModel);
        excelExpenseColumnTable.getColumnModel().getColumn(0).setPreferredWidth(150);

        incomeCategoriesTable = new JTable(incomeCategoriesDataModel);
        incomeCategoriesTable.getColumnModel().getColumn(0).setPreferredWidth(150);

        expenseCategoriesTable = new JTable(expenseCategoriesDataModel);
        expenseCategoriesTable.getColumnModel().getColumn(0).setPreferredWidth(150);

        this.revalidate();
        this.validate();

        tablePanel = new JPanel();
        tablePanel.add(excelIncomeColumnTable);
        tablePanel.add(excelExpenseColumnTable);
        tablePanel.add(incomeCategoriesTable);
        tablePanel.add(expenseCategoriesTable);

        JScrollPane scroller = new JScrollPane(tablePanel);
        scroller.getVerticalScrollBar().setUnitIncrement(16);
        this.add(BorderLayout.CENTER, scroller);

    }

    public DefaultTableModel getExcelColumnsDataModel(DefaultTableModel dataModel, boolean isIncome) throws SQLException {
        // guard clause
        if (!AuditAccountClass.isAuditIDEntered()) {
            System.out.println("Please Enter Audit ID");
            return dataModel;
        }
        if (!AuditAccountClass.isIncomeExpenseEntered()) {
            System.out.println("Please Enter Income or Expense");
            return dataModel;
        }
        if (AuditAccountClass.getIncomeExpenseChar() == 'B') {
            System.out.println("Please specify Income or Expense Individually");
            return dataModel;
        }

        int auditID = AuditAccountClass.getAuditID();

        CreateNewColumns createNewColumns = new CreateNewColumns();
        ResultSet excelColumns;
        excelColumns = createNewColumns.getExcelColumns(auditID, isIncome);

        this.validate();
        this.revalidate();

        dataModel = buildTableModel(excelColumns);
        return dataModel;
    }

    public DefaultTableModel getCategoriesDataModel(DefaultTableModel dataModel, boolean isIncome) throws SQLException {
        CreateNewColumns createNewColumns = new CreateNewColumns();
        ResultSet excelColumns;
        excelColumns = createNewColumns.getCategories(AuditAccountClass.getAuditID(), isIncome);

        this.validate();
        this.revalidate();

        dataModel = buildTableModel(excelColumns);
        return dataModel;
    }

    public void refreshExcelColumnsTable() {
        try {
            excelIncomeColumnsDataModel = getExcelColumnsDataModel(excelIncomeColumnsDataModel, true);
            excelIncomeColumnTable.setDefaultEditor(Object.class, null);
            excelIncomeColumnTable.setModel(excelIncomeColumnsDataModel);
            excelIncomeColumnTable.getColumnModel().getColumn(0).setPreferredWidth(150);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            excelExpenseColumnsDataModel = getExcelColumnsDataModel(excelExpenseColumnsDataModel, false);
            excelExpenseColumnTable.setDefaultEditor(Object.class, null);
            excelExpenseColumnTable.setModel(excelExpenseColumnsDataModel);
            excelExpenseColumnTable.getColumnModel().getColumn(0).setPreferredWidth(150);
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.revalidate();
        this.validate();
    }

    public void refreshCategoriesTable() {
        try {
            incomeCategoriesDataModel = getCategoriesDataModel(incomeCategoriesDataModel, true);
            incomeCategoriesTable.setDefaultEditor(Object.class, null);
            incomeCategoriesTable.setModel(incomeCategoriesDataModel);
            incomeCategoriesTable.getColumnModel().getColumn(0).setPreferredWidth(150);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            expenseCategoriesDataModel = getCategoriesDataModel(expenseCategoriesDataModel, false);
            expenseCategoriesTable.setDefaultEditor(Object.class, null);
            expenseCategoriesTable.setModel(expenseCategoriesDataModel);
            expenseCategoriesTable.getColumnModel().getColumn(0).setPreferredWidth(150);
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.revalidate();
        this.validate();
    }

    public void refreshAll() {
        refreshExcelColumnsTable();
        refreshCategoriesTable();
    }
}
