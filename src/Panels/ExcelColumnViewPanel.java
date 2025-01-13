package src.Panels;

import src.Interfaces.Model;
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
    JTable excelIncomeColumnTable;
    JTable excelExpenseColumnTable;
    JTable incomeCategoriesTable;
    JTable expenseCategoriesTable;
    JScrollPane incomeColumnScroll;
    JScrollPane expenseColumnScroll;
    JScrollPane incomeCategoryScroll;
    JScrollPane expenseCategoryScroll;
    JPanel tablePanel;
    CategoryColumnSQLs categoryColumnSQLs = new CategoryColumnSQLs();
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
                categoryColumnSQLs.deleteCategory(Integer.parseInt(id));
                refreshAll();
            }
        });
        JButton deleteCategoryExpenseButton = new JButton("Delete Selected Category");
        deleteCategoryExpenseButton.addActionListener(e -> {
            int row = excelExpenseColumnTable.getSelectedRow();
            if (row != -1) {
                int column = 1;
                String id = excelExpenseColumnTable.getModel().getValueAt(row, column).toString();
                categoryColumnSQLs.deleteCategory(Integer.parseInt(id));
                refreshAll();
            }
        });
        JButton deleteIncomeDescriptionButton = new JButton("Delete Selected Known Description");
        deleteIncomeDescriptionButton.addActionListener(e -> {
            int row = incomeCategoriesTable.getSelectedRow();
            if (row >= 0) {
                int column = 1;
                int modelRow = incomeCategoriesTable.convertRowIndexToModel(row);
                String id = incomeCategoriesTable.getModel().getValueAt(modelRow, column).toString();
                categoryColumnSQLs.deleteDescription(Integer.parseInt(id));
                refreshAll();
            }
        });
        JButton deleteExpenseDescriptionButton = new JButton("Delete Selected Known Description");
        deleteExpenseDescriptionButton.addActionListener(e -> {
            int row = expenseCategoriesTable.getSelectedRow();
            if (row >= 0) {
                int column = 1;
                int modelRow = expenseCategoriesTable.convertRowIndexToModel(row);
                String id = expenseCategoriesTable.getModel().getValueAt(modelRow, column).toString();
                categoryColumnSQLs.deleteDescription(Integer.parseInt(id));
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

        CategoryColumnSQLs categoryColumnSQLs = new CategoryColumnSQLs();
        ResultSet excelColumns;
        excelColumns = categoryColumnSQLs.getExcelColumns(auditID, isIncome);

        this.validate();
        this.revalidate();

        dataModel = buildTableModel(excelColumns);
        return dataModel;
    }

    public DefaultTableModel getCategoriesDataModel(boolean isIncome) throws SQLException {
        ResultSet excelColumns;
        excelColumns = categoryColumnSQLs.getCategories(AuditAccountClass.getAuditID(), isIncome);

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
            TableRowSorter sorter;
            List<? extends RowSorter.SortKey> sortKeys;
            sorter = (TableRowSorter) incomeCategoriesTable.getRowSorter();
            sortKeys = sorter.getSortKeys();
            incomeCategoriesDataModel = getCategoriesDataModel(true);
            incomeCategoriesTable.setModel(incomeCategoriesDataModel);
            sorter = (TableRowSorter) incomeCategoriesTable.getRowSorter();
            sorter.setSortKeys(sortKeys);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            TableRowSorter sorter;
            List<? extends RowSorter.SortKey> sortKeys;
            sorter = (TableRowSorter) expenseCategoriesTable.getRowSorter();
            sortKeys = sorter.getSortKeys();
            expenseCategoriesDataModel = getCategoriesDataModel(false);
            expenseCategoriesTable.setModel(expenseCategoriesDataModel);
            sorter = (TableRowSorter) expenseCategoriesTable.getRowSorter();
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
            excelIncomeColumnsDataModel = getExcelColumnsDataModel(excelIncomeColumnsDataModel, true);
            excelIncomeColumnTable.setModel(excelIncomeColumnsDataModel);
            TableCellListener tcl = new TableCellListener(excelIncomeColumnTable, new CategoryCellListener());

            TableColumnModel tcmIncomeColumn = excelIncomeColumnTable.getColumnModel();
            tcmIncomeColumn.getColumn(0).setPreferredWidth(TABLE_WIDTH);
            excelIncomeColumnTable.getColumn("column_name").setHeaderValue("Income Categories");

            excelIncomeColumnTable.setDragEnabled(true);
            excelIncomeColumnTable.setDropMode(DropMode.INSERT_ROWS);
            excelIncomeColumnTable.setTransferHandler(new IncomeColumnReorderer());
        } catch (Exception e) {
            e.printStackTrace();
        }

        excelIncomeColumnTable.setAutoCreateColumnsFromModel(false);
        excelIncomeColumnTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        excelIncomeColumnTable.addMouseListener(new IncomeColumnMouseListener());

        try {
            excelExpenseColumnsDataModel = getExcelColumnsDataModel(excelExpenseColumnsDataModel, false);
            excelExpenseColumnTable.setModel(excelExpenseColumnsDataModel);
            TableCellListener tcl = new TableCellListener(excelExpenseColumnTable, new CategoryCellListener());

            TableColumnModel tcmExpenseColumn = excelExpenseColumnTable.getColumnModel();
            tcmExpenseColumn.getColumn(0).setPreferredWidth(TABLE_WIDTH);
            excelExpenseColumnTable.removeColumn(excelExpenseColumnTable.getColumn("id"));
            excelExpenseColumnTable.removeColumn(excelExpenseColumnTable.getColumn("sheet_order"));
            excelExpenseColumnTable.getColumn("column_name").setHeaderValue("Expense Categories");
            excelExpenseColumnTable.setDragEnabled(true);
            excelExpenseColumnTable.setDropMode(DropMode.INSERT_ROWS);
            excelExpenseColumnTable.setTransferHandler(new ExpenseColumnReorderer());
        } catch (Exception e) {
            e.printStackTrace();
        }

        excelExpenseColumnTable.setAutoCreateColumnsFromModel(false);
        excelExpenseColumnTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        excelExpenseColumnTable.addMouseListener(new ExpenseColumnMouseListener());
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
            incomeCategoriesTable.setAutoCreateRowSorter(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        incomeCategoriesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        incomeCategoriesTable.setAutoCreateColumnsFromModel(false);
        incomeCategoriesTable.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) {
                int row = incomeCategoriesTable.getSelectedRow();
                if (row >= 0) {
                    int modelRow = incomeCategoriesTable.convertRowIndexToModel(row);
                    String description = incomeCategoriesTable.getModel().getValueAt(modelRow, 0).toString();
                    int id = Integer.parseInt(incomeCategoriesTable.getModel().getValueAt(modelRow, 1).toString());
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
            expenseCategoriesTable.setAutoCreateRowSorter(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        expenseCategoriesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        expenseCategoriesTable.setAutoCreateColumnsFromModel(false);
        expenseCategoriesTable.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) {
                int row = expenseCategoriesTable.getSelectedRow();
                if (row >= 0) {
                    int modelRow = expenseCategoriesTable.convertRowIndexToModel(row);
                    String description = expenseCategoriesTable.getModel().getValueAt(modelRow, 0).toString();
                    int id = Integer.parseInt(expenseCategoriesTable.getModel().getValueAt(modelRow, 1).toString());
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
            int row = excelIncomeColumnTable.getSelectedRow();
            if (row != -1) {
                String description = excelIncomeColumnTable.getModel().getValueAt(row, 0).toString();
                int id = Integer.parseInt(excelIncomeColumnTable.getModel().getValueAt(row, 1).toString());
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
            int row = excelExpenseColumnTable.getSelectedRow();
            if (row != -1) {
                String description = excelExpenseColumnTable.getModel().getValueAt(row, 0).toString();
                int id = Integer.parseInt(excelExpenseColumnTable.getModel().getValueAt(row, 1).toString());
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

            DefaultTableModel model = (DefaultTableModel) excelIncomeColumnTable.getModel();

            if (returnValue) {
                for (int i = 0; i < excelIncomeColumnTable.getRowCount(); i++) {
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

            DefaultTableModel model = (DefaultTableModel) excelExpenseColumnTable.getModel();

            if (returnValue) {
                for (int i = 0; i < excelExpenseColumnTable.getRowCount(); i++) {
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
