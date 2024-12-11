package src.Panels;

import src.SQLFunctions.AuditIDSQLs;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import java.util.Vector;

public class AccountSelectionPanel extends JPanel {
    JComboBox accountComboBox;
    AuditIDSQLs auditIDSQLs = new AuditIDSQLs();
    public AccountSelectionPanel() {
        Vector<AuditAccountID> allAccounts = auditIDSQLs.getAllAccounts();
        accountComboBox = new JComboBox();
        for (AuditAccountID a : allAccounts) {
            accountComboBox.addItem(a);
        }

        this.add(accountComboBox);

        this.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        AuditAccountClass.setAuditID(auditIDSQLs.getStartingAuditNumber());

        accountComboBox.addActionListener(e -> {
            AuditAccountID auditAccountID = (AuditAccountID) accountComboBox.getSelectedItem();
            AuditAccountClass.setAuditID(auditAccountID.id);
            System.out.println(auditAccountID.id);
        });

    }

}
