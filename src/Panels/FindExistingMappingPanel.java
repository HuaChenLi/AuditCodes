package src.Panels;

import src.Lib.DescData;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseEvent;

public class FindExistingMappingPanel extends JPanel {
    private int selectedID;
    private String selectedCategory;

    public FindExistingMappingPanel(boolean isIncome) {
        JTable knownDescTable;
        JScrollPane knownDescScroll;
        DefaultTableModel knownDescDataModel;

        knownDescTable = new JTable();

        try {
            knownDescDataModel = DescData.getDescriptionDataModel(isIncome);
            knownDescTable.setModel(knownDescDataModel);
            knownDescTable.removeColumn(knownDescTable.getColumn("id"));
            String header = isIncome ? "Income Categories" : "Expense Categories";
            knownDescTable.getColumn("category_values").setHeaderValue(header);
            knownDescTable.setAutoCreateRowSorter(true);
            knownDescTable.getRowSorter().toggleSortOrder(0);

            knownDescTable.addMouseListener(new MouseListener());

        } catch (Exception e1) {
            e1.printStackTrace();
        }

        knownDescScroll = new JScrollPane(knownDescTable);

        this.add(knownDescScroll, BorderLayout.CENTER);
        this.setVisible(true);
    }

    public int getSelectedID() {
        return selectedID;
    }
    public String getSelectedCategory() {
        return selectedCategory;
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
                selectedID = Integer.parseInt(id);
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
