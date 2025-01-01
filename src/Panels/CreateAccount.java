package src.Panels;

import src.SQLFunctions.AuditIDSQLs;

import javax.swing.*;

public class CreateAccount extends JPanel {
    JLabel accountNameLabel;
    JTextField accountNameText;
    JButton createAccountButton;
    AuditIDSQLs auditIDSQLs = new AuditIDSQLs();
    public CreateAccount(ExcelColumnViewPanel excelColumnViewPanel) {
        AccountSelectionComboBox accountSelectionComboBox = new AccountSelectionComboBox(excelColumnViewPanel);
        accountNameLabel = new JLabel("Account Name");
        accountNameText = new JTextField();
        accountNameText.setColumns(20);
        createAccountButton = new JButton("Create Account");
        createAccountButton.addActionListener(e -> {
            if (!accountNameText.getText().trim().equals("")) {
                auditIDSQLs.createAccount(accountNameText.getText());
            }
            accountNameText.setText("");
            accountSelectionComboBox.refreshAccountComboBox();
            this.revalidate();
        });

        this.add(accountNameLabel);
        this.add(accountNameText);
        this.add(createAccountButton);
        this.add(accountSelectionComboBox);
    }
}
