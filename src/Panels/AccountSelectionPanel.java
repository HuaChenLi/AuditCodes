package src.Panels;

import javax.swing.*;
import java.awt.*;

public class AccountSelectionPanel extends JPanel {
    JLabel accountSelectionLabel;
    JTextField accountSelectionText;
    JButton accountSelectionButton;
    int accountID;
    public AccountSelectionPanel() {
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
                accountSelectionLabel.setText(String.valueOf(accountID));

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
