package src.Panels;

import src.SQLFunctions.CreateNewColumns;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class CreateCategoryPanel extends JPanel {
    JTextField newCategory;
    JButton createCategoryButton;
    ExcelColumnViewPanel excelColumnViewPanel;
    public CreateCategoryPanel(ExcelColumnViewPanel excelColumnViewPanel) {
        newCategory = new JTextField();
        newCategory.addKeyListener(new CreateCategoryKeyListener());
        createCategoryButton = new JButton("Create Category");
        createCategoryButton.addActionListener(e -> createCategory());

        this.excelColumnViewPanel = excelColumnViewPanel;

        this.add(newCategory);
        this.add(createCategoryButton);

        this.setLayout(new GridLayout());
    }

    private void createCategory() {
        String tempCategory = newCategory.getText();
        if (tempCategory.trim().length() != 0) {
            CreateNewColumns createNewColumns = new CreateNewColumns();
            createNewColumns.createCategory(tempCategory, AuditAccountClass.getAuditID(), AuditAccountClass.isIncome());
            excelColumnViewPanel.refreshAll();
            newCategory.setText("");
        }

    }
    private class CreateCategoryKeyListener implements KeyListener {

        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                createCategory();
            }

        }

        @Override
        public void keyReleased(KeyEvent e) {

        }
    }
}


