package src.Panels;

import src.SQLFunctions.CreateNewColumns;

import javax.swing.*;
import java.awt.*;

public class ColumnCreationPanel extends JPanel {
    JLabel columnNameLabel, defaultLabel, gSTLabel, blankLabel;
    JTextField columnNameText;
    JButton defaultButton, gSTButton, addColumnButton;
    boolean isDefault = false, gSTIncluded = false;
    public ColumnCreationPanel(ExcelColumnViewPanel excelColumnViewPanel) {
        this.setLayout(new GridLayout(0,4));
        this.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        columnNameLabel = new JLabel("Column Name");
        defaultLabel = new JLabel("Default?");
        gSTLabel = new JLabel("GST Included?");
        gSTButton = new JButton("GST Not Included");
        addColumnButton = new JButton("Create Column");
        columnNameText = new JTextField();
        blankLabel = new JLabel();
        defaultButton = new JButton("Not Default");
        defaultButton.addActionListener(e1 -> {
            isDefault = !isDefault;
            if (isDefault) {
                defaultButton.setText("Is Default");
            } else {
                defaultButton.setText("Is Default");
            }
        });
        gSTButton.addActionListener(e1 -> {
            gSTIncluded = !gSTIncluded;
            if (gSTIncluded) {
                gSTButton.setText("GST Included");
            } else {
                gSTButton.setText("GST Not Included");
            }
        });

        addColumnButton.addActionListener(e1 -> {
            CreateNewColumns createNewColumns = new CreateNewColumns();
            if (columnNameText.getText().trim().length() >= 1) {
                createNewColumns.insertColumn(AuditAccountClass.getAuditID(), columnNameText.getText(), gSTIncluded, AuditAccountClass.isIncome(), AuditAccountClass.isExpense());
            }
            columnNameText.setText("");

            excelColumnViewPanel.refreshAll();
        });


        this.add(columnNameLabel);
        this.add(gSTLabel);
        this.add(defaultLabel);
        this.add(blankLabel);
        this.add(columnNameText);
        this.add(addColumnButton);
        this.add(gSTButton);
        this.add(defaultButton);
        this.add(addColumnButton);
    }
}
