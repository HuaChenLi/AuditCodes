package src.Panels;

import src.Interfaces.Model;
import src.Lib.DescData;
import src.Lib.TableCellListener;
import src.Lib.TableReorderer;
import src.SQLFunctions.CategoryColumnSQLs;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ExcelColumnViewPanel extends JPanel implements Model {
    JTable incomeCategoryTable;
    JTable expenseCategoryTable;
    JTable incomeDescriptionTable;
    JTable expenseDescriptionTable;
    JScrollPane incomeCategoryScroll;
    JScrollPane expenseCategoryScroll;
    JScrollPane incomeDescriptionScroll;
    JScrollPane expenseDescriptionScroll;
    JPanel tablePanel;
    CategoryColumnSQLs categoryColumnSQLs = new CategoryColumnSQLs();
    DefaultTableModel incomeCategoryDataModel;
    DefaultTableModel expenseCategoryDataModel;
    DefaultTableModel incomeDescriptionDataModel;
    DefaultTableModel expenseDescriptionDataModel;
    CategoriseValuesPanel categoriseValuesPanel;
    private final int TABLE_WIDTH = 450;

    public ExcelColumnViewPanel() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        incomeCategoryTable = new JTable(incomeCategoryDataModel);
        expenseCategoryTable = new JTable(expenseCategoryDataModel);
        incomeDescriptionTable = new JTable(incomeDescriptionDataModel);
        expenseDescriptionTable = new JTable(expenseDescriptionDataModel);

        setCategoryTables();
        initDescriptionTables();

        tablePanel = new JPanel();
        Border smallBorder = BorderFactory.createEmptyBorder(5,5,5,5);
        tablePanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        tablePanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JButton deleteCategoryIncomeButton = new JButton("Delete Selected Category");
        deleteCategoryIncomeButton.addActionListener(e -> {
            int row = incomeCategoryTable.getSelectedRow();
            if (row != -1) {
                int column = 1;
                String id = incomeCategoryTable.getModel().getValueAt(row, column).toString();
                categoryColumnSQLs.deleteCategory(Integer.parseInt(id));
                refreshAll();
            }
        });
        JButton deleteCategoryExpenseButton = new JButton("Delete Selected Category");
        deleteCategoryExpenseButton.addActionListener(e -> {
            int row = expenseCategoryTable.getSelectedRow();
            if (row != -1) {
                int column = 1;
                String id = expenseCategoryTable.getModel().getValueAt(row, column).toString();
                categoryColumnSQLs.deleteCategory(Integer.parseInt(id));
                refreshAll();
            }
        });
        JButton deleteIncomeDescriptionButton = new JButton("Delete Selected Known Description");
        deleteIncomeDescriptionButton.addActionListener(e -> {
            int row = incomeDescriptionTable.getSelectedRow();
            if (row >= 0) {
                int column = 1;
                int modelRow = incomeDescriptionTable.convertRowIndexToModel(row);
                String id = incomeDescriptionTable.getModel().getValueAt(modelRow, column).toString();
                categoryColumnSQLs.deleteDescription(Integer.parseInt(id));
                refreshAll();
            }
        });
        JButton deleteExpenseDescriptionButton = new JButton("Delete Selected Known Description");
        deleteExpenseDescriptionButton.addActionListener(e -> {
            int row = expenseDescriptionTable.getSelectedRow();
            if (row >= 0) {
                int column = 1;
                int modelRow = expenseDescriptionTable.convertRowIndexToModel(row);
                String id = expenseDescriptionTable.getModel().getValueAt(modelRow, column).toString();
                categoryColumnSQLs.deleteDescription(Integer.parseInt(id));
                refreshAll();
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 1;
        incomeCategoryScroll = new JScrollPane(incomeCategoryTable);
        tablePanel.add(incomeCategoryScroll, gbc);
        incomeCategoryTable.setBorder(smallBorder);
        gbc.gridx = 1;
        expenseCategoryScroll = new JScrollPane(expenseCategoryTable);
        tablePanel.add(expenseCategoryScroll, gbc);
        expenseCategoryTable.setBorder(smallBorder);
        gbc.gridx = 2;
        incomeDescriptionScroll = new JScrollPane(incomeDescriptionTable);
        tablePanel.add(incomeDescriptionScroll, gbc);
        incomeDescriptionTable.setBorder(smallBorder);
        gbc.gridx = 3;
        expenseDescriptionScroll = new JScrollPane(expenseDescriptionTable);
        tablePanel.add(expenseDescriptionScroll, gbc);
        expenseDescriptionTable.setBorder(smallBorder);

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

    public DefaultTableModel getCategoryDataModel(int accountID, boolean isIncome) throws SQLException {CategoryColumnSQLs categoryColumnSQLs = new CategoryColumnSQLs();
        ResultSet excelColumns;
        excelColumns = categoryColumnSQLs.getExcelColumns(accountID, isIncome);

        this.validate();
        this.revalidate();

        return buildTableModel(excelColumns);
    }

    public void refreshExcelColumnsTable() {
        try {
            incomeCategoryDataModel = getCategoryDataModel(AuditAccountClass.getAuditID(), true);
            incomeCategoryTable.setModel(incomeCategoryDataModel);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            expenseCategoryDataModel = getCategoryDataModel(AuditAccountClass.getAuditID(), false);
            expenseCategoryTable.setModel(expenseCategoryDataModel);
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.revalidate();
        this.validate();
    }

    public void refreshCategoriesTable() {
        try {
            TableRowSorter sorter;
            List<? extends RowSorter.SortKey> sortKeys;
            sorter = (TableRowSorter) incomeDescriptionTable.getRowSorter();
            sortKeys = sorter.getSortKeys();
            incomeDescriptionDataModel = DescData.getDescriptionDataModel(AuditAccountClass.getAuditID(), true);
            incomeDescriptionTable.setModel(incomeDescriptionDataModel);
            sorter = (TableRowSorter) incomeDescriptionTable.getRowSorter();
            sorter.setSortKeys(sortKeys);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            TableRowSorter sorter;
            List<? extends RowSorter.SortKey> sortKeys;
            sorter = (TableRowSorter) expenseDescriptionTable.getRowSorter();
            sortKeys = sorter.getSortKeys();
            expenseDescriptionDataModel = DescData.getDescriptionDataModel(AuditAccountClass.getAuditID(), false);
            expenseDescriptionTable.setModel(expenseDescriptionDataModel);
            sorter = (TableRowSorter) expenseDescriptionTable.getRowSorter();
            sorter.setSortKeys(sortKeys);
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

        try {
            incomeCategoryDataModel = getCategoryDataModel(AuditAccountClass.getAuditID(), true);
            incomeCategoryTable.setModel(incomeCategoryDataModel);
            TableCellListener tcl = new TableCellListener(incomeCategoryTable, new CategoryCellListener());

            TableColumnModel tcmIncomeColumn = incomeCategoryTable.getColumnModel();
            tcmIncomeColumn.getColumn(0).setPreferredWidth(TABLE_WIDTH);
            incomeCategoryTable.removeColumn(incomeCategoryTable.getColumn("id"));
            incomeCategoryTable.removeColumn(incomeCategoryTable.getColumn("sheet_order"));
            incomeCategoryTable.getColumn("column_name").setHeaderValue("Income Categories");

            incomeCategoryTable.setDragEnabled(true);
            incomeCategoryTable.setDropMode(DropMode.INSERT_ROWS);
            incomeCategoryTable.setTransferHandler(new IncomeColumnReorderer());
        } catch (Exception e) {
            e.printStackTrace();
        }

        incomeCategoryTable.setAutoCreateColumnsFromModel(false);
        incomeCategoryTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        incomeCategoryTable.addMouseListener(new IncomeColumnMouseListener());

        try {
            expenseCategoryDataModel = getCategoryDataModel(AuditAccountClass.getAuditID(), false);
            expenseCategoryTable.setModel(expenseCategoryDataModel);
            TableCellListener tcl = new TableCellListener(expenseCategoryTable, new CategoryCellListener());

            TableColumnModel tcmExpenseColumn = expenseCategoryTable.getColumnModel();
            tcmExpenseColumn.getColumn(0).setPreferredWidth(TABLE_WIDTH);
            expenseCategoryTable.removeColumn(expenseCategoryTable.getColumn("id"));
            expenseCategoryTable.removeColumn(expenseCategoryTable.getColumn("sheet_order"));
            expenseCategoryTable.getColumn("column_name").setHeaderValue("Expense Categories");
            expenseCategoryTable.setDragEnabled(true);
            expenseCategoryTable.setDropMode(DropMode.INSERT_ROWS);
            expenseCategoryTable.setTransferHandler(new ExpenseColumnReorderer());
        } catch (Exception e) {
            e.printStackTrace();
        }

        expenseCategoryTable.setAutoCreateColumnsFromModel(false);
        expenseCategoryTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        expenseCategoryTable.addMouseListener(new ExpenseColumnMouseListener());
    }

    private void initDescriptionTables() {
        incomeDescriptionTable.setDefaultEditor(Object.class, null);
        expenseDescriptionTable.setDefaultEditor(Object.class, null);

        try {
            incomeDescriptionDataModel = DescData.getDescriptionDataModel(AuditAccountClass.getAuditID(), true);
            incomeDescriptionTable.setModel(incomeDescriptionDataModel);
            TableColumnModel tcmIncomeCategory = incomeDescriptionTable.getColumnModel();
            tcmIncomeCategory.getColumn(0).setPreferredWidth(TABLE_WIDTH);
            incomeDescriptionTable.removeColumn(incomeDescriptionTable.getColumn("id"));
            incomeDescriptionTable.getColumn("category_values").setHeaderValue("Income Descriptions");
            incomeDescriptionTable.setAutoCreateRowSorter(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        incomeDescriptionTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        incomeDescriptionTable.setAutoCreateColumnsFromModel(false);
        incomeDescriptionTable.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) {
                int row = incomeDescriptionTable.getSelectedRow();
                if (row >= 0) {
                    int modelRow = incomeDescriptionTable.convertRowIndexToModel(row);
                    String description = incomeDescriptionTable.getModel().getValueAt(modelRow, 0).toString();
                    int id = Integer.parseInt(incomeDescriptionTable.getModel().getValueAt(modelRow, 1).toString());
                    categoriseValuesPanel.setValueToCategorise(description);
                    categoriseValuesPanel.setDescriptionID(id);
                }
            }
        });

        try {
            expenseDescriptionDataModel = DescData.getDescriptionDataModel(AuditAccountClass.getAuditID(), false);
            expenseDescriptionTable.setModel(expenseDescriptionDataModel);
            TableColumnModel tcmExpenseCategories = expenseDescriptionTable.getColumnModel();
            tcmExpenseCategories.getColumn(0).setPreferredWidth(TABLE_WIDTH);
            expenseDescriptionTable.removeColumn(expenseDescriptionTable.getColumn("id"));
            expenseDescriptionTable.getColumn("category_values").setHeaderValue("Expense Descriptions");
            expenseDescriptionTable.setAutoCreateRowSorter(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        expenseDescriptionTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        expenseDescriptionTable.setAutoCreateColumnsFromModel(false);
        expenseDescriptionTable.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) {
                int row = expenseDescriptionTable.getSelectedRow();
                if (row >= 0) {
                    int modelRow = expenseDescriptionTable.convertRowIndexToModel(row);
                    String description = expenseDescriptionTable.getModel().getValueAt(modelRow, 0).toString();
                    int id = Integer.parseInt(expenseDescriptionTable.getModel().getValueAt(modelRow, 1).toString());
                    categoriseValuesPanel.setValueToCategorise(description);
                    categoriseValuesPanel.setDescriptionID(id);
                }
            }
        });
    }

    private class IncomeColumnMouseListener implements MouseListener {
        @Override
        public void mouseClicked(MouseEvent e) {

        }

        @Override
        public void mousePressed(MouseEvent e) {
            int row = incomeCategoryTable.getSelectedRow();
            if (row != -1) {
                String description = incomeCategoryTable.getModel().getValueAt(row, 0).toString();
                int id = Integer.parseInt(incomeCategoryTable.getModel().getValueAt(row, 1).toString());
                categoriseValuesPanel.setColumnIDText(description);
                categoriseValuesPanel.setCategoryID(id);
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    }

    private class ExpenseColumnMouseListener implements MouseListener {
        @Override
        public void mouseClicked(MouseEvent e) {

        }

        @Override
        public void mousePressed(MouseEvent e) {
            int row = expenseCategoryTable.getSelectedRow();
            if (row != -1) {
                String description = expenseCategoryTable.getModel().getValueAt(row, 0).toString();
                int id = Integer.parseInt(expenseCategoryTable.getModel().getValueAt(row, 1).toString());
                categoriseValuesPanel.setColumnIDText(description);
                categoriseValuesPanel.setCategoryID(id);
            }

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    }

    private class IncomeColumnReorderer extends TableReorderer {
        @Override
        public boolean importData(TransferSupport support) {
            boolean returnValue;
            returnValue = super.importData(support);

            DefaultTableModel model = (DefaultTableModel) incomeCategoryTable.getModel();

            if (returnValue) {
                for (int i = 0; i < incomeCategoryTable.getRowCount(); i++) {
                    int id = (int) model.getValueAt(i, 1);
                    int order = (int) model.getValueAt(i, 2);
                    categoryColumnSQLs.updateColumnOrder(id, order);
                }
            }
            return returnValue;
        }
    }

    private class ExpenseColumnReorderer extends TableReorderer {
        @Override
        public boolean importData(TransferSupport support) {
            boolean returnValue;
            returnValue = super.importData(support);

            DefaultTableModel model = (DefaultTableModel) expenseCategoryTable.getModel();

            if (returnValue) {
                for (int i = 0; i < expenseCategoryTable.getRowCount(); i++) {
                    int id = (int) model.getValueAt(i, 1);
                    int order = (int) model.getValueAt(i, 2);
                    categoryColumnSQLs.updateColumnOrder(id, order);
                }
            }
            return returnValue;
        }
    }

    private class CategoryCellListener implements Action {

        @Override
        public Object getValue(String key) {
            return null;
        }

        @Override
        public void putValue(String key, Object value) {

        }

        @Override
        public void setEnabled(boolean b) {

        }

        @Override
        public boolean isEnabled() {
            return false;
        }

        @Override
        public void addPropertyChangeListener(PropertyChangeListener listener) {

        }

        @Override
        public void removePropertyChangeListener(PropertyChangeListener listener) {

        }

        @Override
        public void actionPerformed(ActionEvent e) {
            TableCellListener tcl = (TableCellListener) e.getSource();
            TableModel model = tcl.getTable().getModel();
            String name = (String) model.getValueAt(tcl.getRow(), 0);
            name = name.trim();
            int id = (int) model.getValueAt(tcl.getRow(), 1);
            categoryColumnSQLs.updateColumnName(id, name);
        }
    }

}
