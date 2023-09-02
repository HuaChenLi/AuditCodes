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
        columnIDLabel = new JLabel("Excel Column ID");
        categoryValueLabel = new JLabel("Category Value ID");
        blankLabel = new JLabel();
        valueToCategorise = new JTextField();
        columnIDText = new JTextField();

        categoriseButton = new JButton("Categorise Column");
        categoriseButton.addActionListener(e1 -> {
            categoryValueID = Integer.parseInt(valueToCategorise.getText());
            valueToCategorise.setText("");
            columnID = Integer.parseInt(columnIDText.getText());
            columnIDText.setText("");

            CreateNewColumns createNewColumns = new CreateNewColumns();
            createNewColumns.insertExcelColumnSelection(columnID, categoryValueID);
        });

        this.add(columnIDLabel);
        this.add(categoryValueLabel);
        this.add(blankLabel);
        this.add(valueToCategorise);
        this.add(columnIDText);
        this.add(categoriseButton);
    }

}
