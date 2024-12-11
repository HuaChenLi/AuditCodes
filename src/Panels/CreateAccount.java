package src.Panels;

import src.SQLFunctions.AuditIDSQLs;

import javax.swing.*;

public class CreateAccount extends JPanel {
    JLabel accountNameLabel;
    JTextField accountNameText;
    JButton createAccountButton;
    AccountSelectionComboBox accountSelectionComboBox = new AccountSelectionComboBox();
    AuditIDSQLs auditIDSQLs = new AuditIDSQLs();
    public CreateAccount() {
        accountNameLabel = new JLabel("Account Name");
        accountNameText = new JTextField();
        accountNameText.setColumns(20);
        createAccountButton = new JButton("Create Account");
        createAccountButton.addActionListener(e -> {
            auditIDSQLs.createAccount(accountNameText.getText());
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
