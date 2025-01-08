package src.Panels;

import src.Interfaces.Model;
import src.SQLFunctions.CreateNewColumns;

import javax.swing.*;
import javax.swing.border.Border;
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
    CreateNewColumns createNewColumns = new CreateNewColumns();
    DefaultTableModel excelIncomeColumnsDataModel = new DefaultTableModel() {
        public int getColumnCount() { return 2; }
        public int getRowCount() { return 40; }
        public Object getValueAt(int row, int col) { return null; }
    };
    DefaultTableModel excelExpenseColumnsDataModel = new DefaultTableModel() {
        public int getColumnCount() { return 2; }
        public int getRowCount() { return 40; }
        public Object getValueAt(int row, int col) { return null; }
    };
    DefaultTableModel incomeCategoriesDataModel = new DefaultTableModel() {
        public int getColumnCount() { return 2; }
        public int getRowCount() { return 40; }
        public Object getValueAt(int row, int col) { return null; }
    };

    DefaultTableModel expenseCategoriesDataModel = new DefaultTableModel() {
        public int getColumnCount() { return 2; }
        public int getRowCount() { return 40; }
        public Object getValueAt(int row, int col) { return null; }
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
        Border smallBorder = BorderFactory.createEmptyBorder(5,5,5,5);
        tablePanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        tablePanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel incomeCategoryLabel = new JLabel("Income Categories");
        incomeCategoryLabel.setBorder(smallBorder);
        tablePanel.add(incomeCategoryLabel, gbc);
        gbc.gridx = 1;
        JLabel expenseCategoryLabel = new JLabel("Expense Categories");
        expenseCategoryLabel.setBorder(smallBorder);
        tablePanel.add(expenseCategoryLabel, gbc);
        gbc.gridx = 2;
        JLabel incomeDescriptionLabel = new JLabel("Income Descriptions");
        incomeDescriptionLabel.setBorder(smallBorder);
        tablePanel.add(incomeDescriptionLabel, gbc);
        gbc.gridx = 3;
        JLabel expenseDescriptionLabel = new JLabel("Expense Descriptions");
        expenseDescriptionLabel.setBorder(smallBorder);
        tablePanel.add(expenseDescriptionLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        JButton deleteCategoryIncomeButton = new JButton("Delete Selected Category");
        deleteCategoryIncomeButton.addActionListener(e -> {
            int row = excelIncomeColumnTable.getSelectedRow();
            if (row != -1) {
                int column = 1;
                String id = excelIncomeColumnTable.getModel().getValueAt(row, column).toString();
                createNewColumns.deleteCategory(Integer.parseInt(id));
                refreshAll();
            }
        });
        tablePanel.add(deleteCategoryIncomeButton, gbc);
        gbc.gridx = 1;
        JButton deleteCategoryExpenseButton = new JButton("Delete Selected Category");
        deleteCategoryExpenseButton.addActionListener(e -> {
            int row = excelExpenseColumnTable.getSelectedRow();
            if (row != -1) {
                int column = 1;
                String id = excelExpenseColumnTable.getModel().getValueAt(row, column).toString();
                createNewColumns.deleteCategory(Integer.parseInt(id));
                refreshAll();
            }
        });
        tablePanel.add(deleteCategoryExpenseButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        tablePanel.add(excelIncomeColumnTable, gbc);
        excelIncomeColumnTable.setBorder(smallBorder);
        gbc.gridx = 1;
        tablePanel.add(excelExpenseColumnTable, gbc);
        excelExpenseColumnTable.setBorder(smallBorder);
        gbc.gridx = 2;
        tablePanel.add(incomeCategoriesTable, gbc);
        incomeCategoriesTable.setBorder(smallBorder);
        gbc.gridx = 3;
        tablePanel.add(expenseCategoriesTable, gbc);
        expenseCategoriesTable.setBorder(smallBorder);

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

    public DefaultTableModel getCategoriesDataModel(boolean isIncome) throws SQLException {
        ResultSet excelColumns;
        excelColumns = createNewColumns.getCategories(AuditAccountClass.getAuditID(), isIncome);

        this.validate();
        this.revalidate();

        DefaultTableModel dataModel = buildTableModel(excelColumns);
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
            incomeCategoriesDataModel = getCategoriesDataModel(true);
            incomeCategoriesTable.setDefaultEditor(Object.class, null);
            incomeCategoriesTable.setModel(incomeCategoriesDataModel);
            incomeCategoriesTable.getColumnModel().getColumn(0).setPreferredWidth(150);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            expenseCategoriesDataModel = getCategoriesDataModel(false);
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
