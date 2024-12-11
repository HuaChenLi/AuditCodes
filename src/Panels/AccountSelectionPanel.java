package src.Panels;

import src.SQLFunctions.AuditIDSQLs;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import java.util.Vector;

public class AccountSelectionPanel extends JPanel {
    JLabel accountSelectionLabel;
    JTextField accountSelectionText;
    JButton accountSelectionButton;
    JComboBox accountComboBox;
    int accountID;
    AuditIDSQLs auditIDSQLs = new AuditIDSQLs();
    public AccountSelectionPanel() {

        Vector model = new Vector();

        Vector allAccounts = auditIDSQLs.getAllAccounts();

        accountComboBox = new JComboBox(model);

        accountComboBox.addItem(allAccounts);

        this.add(accountComboBox);

        this.setLayout(new GridLayout(0,3));
        this.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        accountSelectionLabel = new JLabel();
        accountSelectionText = new JTextField();
        accountSelectionButton = new JButton("Set Account");

        accountSelectionButton.addActionListener(e1 -> {
            String tempString;
            try {
                tempString = accountSelectionText.getText();

                accountID = Integer.parseInt(tempString);

                AuditIDSQLs auditIDSQLs = new AuditIDSQLs();
                accountSelectionLabel.setText(String.valueOf(auditIDSQLs.getAuditName(accountID)));

                AuditAccountClass.setAuditIDEntered(true);
                AuditAccountClass.setAuditID(accountID);
            } catch (Exception e) {
                e.printStackTrace();
            }

            accountSelectionText.setText("");
            this.revalidate();
            this.validate();
        });

        this.add(accountSelectionLabel);
        this.add(accountSelectionText);
        this.add(accountSelectionButton);
    }

}
