package src.Panels;

import src.Lib.CategoryModel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseEvent;

public class FindExistingCategoryPanel extends JPanel {
    private int categoryID;
    private String selectedCategory;
    public FindExistingCategoryPanel(boolean isIncome) {
        JTable categoryTable;
        JScrollPane categoryScroll;
        DefaultTableModel categoryModel;

        categoryTable = new JTable();
        categoryTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        try {
            categoryModel = CategoryModel.getCategoryDataModel(AuditAccountClass.getAuditID(), isIncome);
            categoryTable.setModel(categoryModel);
            categoryTable.removeColumn(categoryTable.getColumn("id"));
            categoryTable.removeColumn(categoryTable.getColumn("sheet_order"));
            String header = isIncome ? "Income Categories" : "Expense Categories";
            categoryTable.getColumn("column_name").setHeaderValue(header);
            categoryTable.setAutoCreateRowSorter(true);
            categoryTable.addMouseListener(new MouseListener());

        } catch (Exception e1) {
            e1.printStackTrace();
        }

        categoryScroll = new JScrollPane(categoryTable);

        this.add(categoryScroll, BorderLayout.CENTER);
        this.setVisible(true);
    }

    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }
    public String getSelectedCategory() {
        return selectedCategory;
    }

    public void setSelectedCategory(String selectedCategory) {
        this.selectedCategory = selectedCategory;
    }

    public class MouseListener implements java.awt.event.MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {

        }

        @Override
        public void mousePressed(MouseEvent mouseEvent) {
            JTable table =(JTable) mouseEvent.getSource();
            Point point = mouseEvent.getPoint();
            int row = table.rowAtPoint(point);
            if (mouseEvent.getClickCount() == 2 && table.getSelectedRow() != -1 && row != -1) {
                int modelRow = table.convertRowIndexToModel(row);
                String id = table.getModel().getValueAt(modelRow, 1).toString();
                categoryID = Integer.parseInt(id);
                selectedCategory = table.getModel().getValueAt(modelRow, 0).toString();

                Window activeWindow = javax.swing.FocusManager.getCurrentManager().getActiveWindow();
                activeWindow.dispose();
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
}
