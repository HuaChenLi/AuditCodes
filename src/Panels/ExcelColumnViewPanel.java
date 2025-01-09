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
    JScrollPane incomeColumnScroll;
    JScrollPane expenseColumnScroll;
    JScrollPane incomeCategoryScroll;
    JScrollPane expenseCategoryScroll;
    JPanel tablePanel;
    CreateNewColumns createNewColumns = new CreateNewColumns();
    DefaultTableModel excelIncomeColumnsDataModel;
    DefaultTableModel excelExpenseColumnsDataModel;
    DefaultTableModel incomeCategoriesDataModel;
    DefaultTableModel expenseCategoriesDataModel;
    CategoriseValuesPanel categoriseValuesPanel;
    private final int TABLE_WIDTH = 450;

    public ExcelColumnViewPanel() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        excelIncomeColumnTable = new JTable(excelIncomeColumnsDataModel);
        excelExpenseColumnTable = new JTable(excelExpenseColumnsDataModel);
        incomeCategoriesTable = new JTable(incomeCategoriesDataModel);
        expenseCategoriesTable = new JTable(expenseCategoriesDataModel);

        setCategoryTables();
        initDescriptionTables();

        tablePanel = new JPanel();
        Border smallBorder = BorderFactory.createEmptyBorder(5,5,5,5);
        tablePanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        tablePanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

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
        gbc.gridy = 1;
        incomeColumnScroll = new JScrollPane(excelIncomeColumnTable);
        tablePanel.add(incomeColumnScroll, gbc);
        excelIncomeColumnTable.setBorder(smallBorder);
        gbc.gridx = 1;
        expenseColumnScroll = new JScrollPane(excelExpenseColumnTable);
        tablePanel.add(expenseColumnScroll, gbc);
        excelExpenseColumnTable.setBorder(smallBorder);
        gbc.gridx = 2;
        incomeCategoryScroll = new JScrollPane(incomeCategoriesTable);
        tablePanel.add(incomeCategoryScroll, gbc);
        incomeCategoriesTable.setBorder(smallBorder);
        gbc.gridx = 3;
        expenseCategoryScroll = new JScrollPane(expenseCategoriesTable);
        tablePanel.add(expenseCategoryScroll, gbc);
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
        this.revalidate();
        this.validate();

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

        return buildTableModel(excelColumns);
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

    private void setCategoryTables() {
        excelIncomeColumnTable.setDefaultEditor(Object.class, null);
        excelExpenseColumnTable.setDefaultEditor(Object.class, null);

        try {
            excelIncomeColumnsDataModel = getExcelColumnsDataModel(excelIncomeColumnsDataModel, true);
            excelIncomeColumnTable.setModel(excelIncomeColumnsDataModel);
            TableColumnModel tcmIncomeColumn = excelIncomeColumnTable.getColumnModel();
            tcmIncomeColumn.getColumn(0).setPreferredWidth(TABLE_WIDTH);
            excelIncomeColumnTable.removeColumn(excelIncomeColumnTable.getColumn("id"));
            excelIncomeColumnTable.getColumn("column_name").setHeaderValue("Income Categories");
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
            tcmExpenseColumn.getColumn(0).setPreferredWidth(TABLE_WIDTH);
            excelExpenseColumnTable.removeColumn(excelExpenseColumnTable.getColumn("id"));
            excelExpenseColumnTable.getColumn("column_name").setHeaderValue("Expense Categories");
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

    private void initDescriptionTables() {
        incomeCategoriesTable.setDefaultEditor(Object.class, null);
        expenseCategoriesTable.setDefaultEditor(Object.class, null);

        try {
            incomeCategoriesDataModel = getCategoriesDataModel(true);
            incomeCategoriesTable.setModel(incomeCategoriesDataModel);
            TableColumnModel tcmIncomeCategory = incomeCategoriesTable.getColumnModel();
            tcmIncomeCategory.getColumn(0).setPreferredWidth(TABLE_WIDTH);
            incomeCategoriesTable.removeColumn(incomeCategoriesTable.getColumn("id"));
            incomeCategoriesTable.getColumn("category_values").setHeaderValue("Income Descriptions");
        } catch (Exception e) {
            e.printStackTrace();
        }

        incomeCategoriesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
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

        try {
            expenseCategoriesDataModel = getCategoriesDataModel(false);
            expenseCategoriesTable.setModel(expenseCategoriesDataModel);
            TableColumnModel tcmExpenseCategories = expenseCategoriesTable.getColumnModel();
            tcmExpenseCategories.getColumn(0).setPreferredWidth(TABLE_WIDTH);
            expenseCategoriesTable.removeColumn(expenseCategoriesTable.getColumn("id"));
            expenseCategoriesTable.getColumn("category_values").setHeaderValue("Expense Descriptions");
        } catch (Exception e) {
            e.printStackTrace();
        }

        expenseCategoriesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
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
    }
}
