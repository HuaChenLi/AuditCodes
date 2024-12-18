package src.Panels;

import src.SQLFunctions.AuditIDSQLs;

import javax.swing.*;
import java.util.Vector;

public class AccountSelectionComboBox extends JComboBox {
    AuditIDSQLs auditIDSQLs = new AuditIDSQLs();
    public AccountSelectionComboBox() {
        refreshAccountComboBox();

        this.addActionListener(e -> {
            AuditAccountID auditAccountID = (AuditAccountID) this.getSelectedItem();
            if (auditAccountID == null) {
                AuditAccountClass.setAuditID(auditIDSQLs.getStartingAuditNumber());
            } else {
                AuditAccountClass.setAuditID(auditAccountID.id);
            }
        });
    }

    public void refreshAccountComboBox() {
        this.removeAllItems();
        Vector<AuditAccountID> allAccounts = auditIDSQLs.getAllAccounts();
        for (AuditAccountID a : allAccounts) {
            this.addItem(a);
        }
        AuditAccountClass.setAuditID(auditIDSQLs.getStartingAuditNumber());
    }
}
