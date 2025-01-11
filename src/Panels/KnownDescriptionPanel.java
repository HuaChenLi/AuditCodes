package src.Panels;

import src.SQLFunctions.CreateNewColumns;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KnownDescriptionPanel extends JPanel {
    JTextField newDescription;
    JButton createDescriptionButton;
    ExcelColumnViewPanel excelColumnViewPanel;
    CategoriseValuesPanel categoriseValuesPanel;
    CreateNewColumns createNewColumns = new CreateNewColumns();
    public KnownDescriptionPanel(ExcelColumnViewPanel excelColumnViewPanel) {
        this.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        newDescription = new JTextField();
        newDescription.addKeyListener(new CreateCategoryKeyListener());
        createDescriptionButton = new JButton("Create Known Description");
        createDescriptionButton.addActionListener(e -> {
            descriptionButtonActions();
        });

        this.excelColumnViewPanel = excelColumnViewPanel;

        this.add(newDescription);
        this.add(createDescriptionButton);

        this.setLayout(new GridLayout());
    }

    private void createDescription(String category) {
        if (category.trim().length() != 0) {
            CreateNewColumns createNewColumns = new CreateNewColumns();
            createNewColumns.createCategory(category, AuditAccountClass.getAuditID(), AuditAccountClass.isIncome());
            excelColumnViewPanel.refreshAll();
            newDescription.setText("");
        }
    }
    private class CreateCategoryKeyListener implements KeyListener {

        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                descriptionButtonActions();
            }

        }

        @Override
        public void keyReleased(KeyEvent e) {

        }
    }

    public void setNewDescription(String s) {
        newDescription.setText(s);
    }

    private void descriptionButtonActions() {
        String tempDescription = newDescription.getText();
        createDescription(tempDescription);
        categoriseValuesPanel.setValueToCategorise(tempDescription);

        int id = createNewColumns.getLastDescriptionID();
        categoriseValuesPanel.setDescriptionID(id);
    }

    public void setCategoriseValuesPanel(CategoriseValuesPanel categoriseValuesPanel) {
        this.categoriseValuesPanel = categoriseValuesPanel;
    }
}


