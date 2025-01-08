package src.Panels;

import src.SQLFunctions.CreateNewColumns;

import javax.swing.*;
import java.awt.*;

public class CategoriseValuesPanel extends JPanel {
    JButton categoriseButton;
    JLabel columnIDLabel, categoryValueLabel, blankLabel;
    JTextField valueToCategorise, columnIDText;
    int columnID, categoryValueID;

    public CategoriseValuesPanel() {
        this.setLayout(new GridLayout(0,3));
        this.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        columnIDLabel = new JLabel("Category ID");
        categoryValueLabel = new JLabel("Known Description ID");
        blankLabel = new JLabel();
        valueToCategorise = new JTextField();
        columnIDText = new JTextField();

        categoriseButton = new JButton("Assign Description to Category");
        categoriseButton.addActionListener(e1 -> {
            categoryValueID = Integer.parseInt(valueToCategorise.getText());
            valueToCategorise.setText("");
            columnID = Integer.parseInt(columnIDText.getText());
            columnIDText.setText("");

            CreateNewColumns createNewColumns = new CreateNewColumns();
            createNewColumns.insertExcelColumnSelection(columnID, categoryValueID);
        });

        this.add(categoryValueLabel);
        this.add(columnIDLabel);
        this.add(blankLabel);
        this.add(valueToCategorise);
        this.add(columnIDText);
        this.add(categoriseButton);
    }

    public void setColumnIDText(String s) {
        columnIDText.setText(s);
    }

    public void setValueToCategorise(String s) {
        valueToCategorise.setText(s);
    }
}
