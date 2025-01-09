package src.Panels;

import src.Interfaces.Model;
import src.SQLFunctions.CreateNewColumns;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
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
    DefaultTableModel excelIncomeColumnsDataModel;
    DefaultTableModel excelExpenseColumnsDataModel;
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
    CategoriseValuesPanel categoriseValuesPanel;

    public ExcelColumnViewPanel() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        excelIncomeColumnTable = new JTable(excelIncomeColumnsDataModel);
        excelExpenseColumnTable = new JTable(excelExpenseColumnsDataModel);
        incomeCategoriesTable = new JTable(incomeCategoriesDataModel);
        expenseCategoriesTable = new JTable(expenseCategoriesDataModel);

        setCategoryTables();

        incomeCategoriesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        TableColumnModel tcmIncomeCategories = incomeCategoriesTable.getColumnModel();
        tcmIncomeCategories.getColumn(0).setPreferredWidth(150);
        tcmIncomeCategories.removeColumn(tcmIncomeCategories.getColumn(1));
        incomeCategoriesTable.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) {
                int row = incomeCategoriesTable.getSelectedRow();
                if (row != -1) {
                    String description = incomeCategoriesTable.getModel().getValueAt(row, 0).toString();
                    int id = Integer.parseInt(incomeCategoriesTable.getModel().getValueAt(row, 1).toString());
                    categoriseValuesPanel.setValueToCategorise(description);
                    categoriseValuesPanel.setDescriptionID(id);
                }
            }
        });

        expenseCategoriesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        TableColumnModel tcmExpenseCategories = expenseCategoriesTable.getColumnModel();
        tcmExpenseCategories.getColumn(0).setPreferredWidth(150);
        tcmExpenseCategories.removeColumn(tcmExpenseCategories.getColumn(1));
        expenseCategoriesTable.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) {
                int row = expenseCategoriesTable.getSelectedRow();
                if (row != -1) {
                    String description = expenseCategoriesTable.getModel().getValueAt(row, 0).toString();
                    int id = Integer.parseInt(expenseCategoriesTable.getModel().getValueAt(row, 1).toString());
                    categoriseValuesPanel.setValueToCategorise(description);
                    categoriseValuesPanel.setDescriptionID(id);
                }
            }
        });

        this.revalidate();
        this.validate();

        tablePanel = new JPanel();
        Border smallBorder = BorderFactory.createEmptyBorder(5,5,5,5);
        tablePanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        tablePanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        JLabel incomeCategoryLabel = new JLabel("Income Categories");
        incomeCategoryLabel.setBorder(smallBorder);
        JLabel expenseCategoryLabel = new JLabel("Expense Categories");
        expenseCategoryLabel.setBorder(smallBorder);
        JLabel incomeDescriptionLabel = new JLabel("Income Descriptions");
        incomeDescriptionLabel.setBorder(smallBorder);
        JLabel expenseDescriptionLabel = new JLabel("Expense Descriptions");
        expenseDescriptionLabel.setBorder(smallBorder);

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
        JButton deleteIncomeDescriptionButton = new JButton("Delete Selected Known Description");
        deleteIncomeDescriptionButton.addActionListener(e -> {
            int row = incomeCategoriesTable.getSelectedRow();
            if (row != -1) {
                int column = 1;
                String id = incomeCategoriesTable.getModel().getValueAt(row, column).toString();
                createNewColumns.deleteDescription(Integer.parseInt(id));
                refreshAll();
            }
        });
        JButton deleteExpenseDescriptionButton = new JButton("Delete Selected Known Description");
        deleteExpenseDescriptionButton.addActionListener(e -> {
            int row = expenseCategoriesTable.getSelectedRow();
            if (row != -1) {
                int column = 1;
                String id = expenseCategoriesTable.getModel().getValueAt(row, column).toString();
                createNewColumns.deleteDescription(Integer.parseInt(id));
                refreshAll();
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 0;
        tablePanel.add(incomeCategoryLabel, gbc);
        gbc.gridx = 1;
        tablePanel.add(expenseCategoryLabel, gbc);
        gbc.gridx = 2;
        tablePanel.add(incomeDescriptionLabel, gbc);
        gbc.gridx = 3;
        tablePanel.add(expenseDescriptionLabel, gbc);

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

        gbc.gridx = 0;
        gbc.gridy = 2;
        tablePanel.add(deleteCategoryIncomeButton, gbc);
        gbc.gridx = 1;
        tablePanel.add(deleteCategoryExpenseButton, gbc);
        gbc.gridx = 2;
        tablePanel.add(deleteIncomeDescriptionButton, gbc);
        gbc.gridx = 3;
        tablePanel.add(deleteExpenseDescriptionButton, gbc);

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
            excelIncomeColumnTable.setModel(excelIncomeColumnsDataModel);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            excelExpenseColumnsDataModel = getExcelColumnsDataModel(excelExpenseColumnsDataModel, false);
            excelExpenseColumnTable.setDefaultEditor(Object.class, null);
            excelExpenseColumnTable.setModel(excelExpenseColumnsDataModel);
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.revalidate();
        this.validate();
    }

    public void refreshCategoriesTable() {
        try {
            incomeCategoriesDataModel = getCategoriesDataModel(true);
            incomeCategoriesTable.setModel(incomeCategoriesDataModel);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            expenseCategoriesDataModel = getCategoriesDataModel(false);
            expenseCategoriesTable.setModel(expenseCategoriesDataModel);
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

    public void setCategoriseValuesPanel(CategoriseValuesPanel categoriseValuesPanel) {
        this.categoriseValuesPanel = categoriseValuesPanel;
    }

    public void setCategoryTables() {
        incomeCategoriesTable.setDefaultEditor(Object.class, null);
        expenseCategoriesTable.setDefaultEditor(Object.class, null);

        try {
            excelIncomeColumnsDataModel = getExcelColumnsDataModel(excelIncomeColumnsDataModel, true);
            excelIncomeColumnTable.setModel(excelIncomeColumnsDataModel);
            TableColumnModel tcmIncomeColumn = excelIncomeColumnTable.getColumnModel();
            tcmIncomeColumn.getColumn(0).setPreferredWidth(150);
            tcmIncomeColumn.removeColumn(tcmIncomeColumn.getColumn(1));
        } catch (Exception e) {
            e.printStackTrace();
        }

        excelIncomeColumnTable.setModel(excelIncomeColumnsDataModel);
        excelIncomeColumnTable.setAutoCreateColumnsFromModel(false);
        excelIncomeColumnTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        excelIncomeColumnTable.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) {
                int row = excelIncomeColumnTable.getSelectedRow();
                if (row != -1) {
                    String description = excelIncomeColumnTable.getModel().getValueAt(row, 0).toString();
                    int id = Integer.parseInt(excelIncomeColumnTable.getModel().getValueAt(row, 1).toString());
                    categoriseValuesPanel.setColumnIDText(description);
                    categoriseValuesPanel.setCategoryID(id);
                }
            }
        });

        try {
            excelExpenseColumnsDataModel = getExcelColumnsDataModel(excelExpenseColumnsDataModel, false);
            excelExpenseColumnTable.setModel(excelExpenseColumnsDataModel);
            TableColumnModel tcmExpenseColumn = excelExpenseColumnTable.getColumnModel();
            tcmExpenseColumn.getColumn(0).setPreferredWidth(150);
            tcmExpenseColumn.removeColumn(tcmExpenseColumn.getColumn(1));
        } catch (Exception e) {
            e.printStackTrace();
        }

        excelExpenseColumnTable.setModel(excelExpenseColumnsDataModel);
        excelExpenseColumnTable.setAutoCreateColumnsFromModel(false);
        excelExpenseColumnTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        excelExpenseColumnTable.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) {
                int row = excelExpenseColumnTable.getSelectedRow();
                if (row != -1) {
                    String description = excelExpenseColumnTable.getModel().getValueAt(row, 0).toString();
                    int id = Integer.parseInt(excelExpenseColumnTable.getModel().getValueAt(row, 1).toString());
                    categoriseValuesPanel.setColumnIDText(description);
                    categoriseValuesPanel.setCategoryID(id);
                }
            }
        });
    }
}
