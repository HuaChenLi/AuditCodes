package src.Panels;

import src.SQLFunctions.CreateNewColumns;

import javax.swing.*;
import java.awt.*;

public class CreateCategoryPanel extends JPanel {
    JTextField newCategory;
    JButton createCategoryButton;
    public CreateCategoryPanel(ExcelColumnViewPanel excelColumnViewPanel) {
        newCategory = new JTextField();
        createCategoryButton = new JButton("Create Category");
        createCategoryButton.addActionListener(e -> {
            String tempCategory = newCategory.getText();
            if (tempCategory.trim().length() != 0) {
                CreateNewColumns createNewColumns = new CreateNewColumns();
                createNewColumns.createCategory(tempCategory);
                excelColumnViewPanel.refreshAll();
                newCategory.setText("");
            }
        });

        this.add(newCategory);
        this.add(createCategoryButton);

        this.setLayout(new GridLayout());
    }
}


