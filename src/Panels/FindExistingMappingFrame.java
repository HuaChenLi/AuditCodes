package src.Panels;

import src.Lib.DescData;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class FindExistingMappingFrame extends JFrame {
    JTable knownDescTable;
    JScrollPane knownDescScroll;
    DefaultTableModel knownDescDataModel;

    public FindExistingMappingFrame(boolean isIncome) {
        knownDescTable = new JTable();

        try {
            knownDescDataModel = DescData.getDescriptionDataModel(isIncome);
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
}
