package src.Panels;

import src.Lib.AlertMessage;
import src.Lib.Transaction;
import src.SQLFunctions.AuditIDSQLs;
import src.SQLFunctions.MappingTableSQLs;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ClassifyDescriptionsPanel extends JPanel {
    JScrollPane scrollPane;
    JPanel innerPanel = new JPanel();
    MappingTableSQLs mappingTableSQLs = new MappingTableSQLs();
    public void addPanels(ArrayList<Transaction> transactions) {
        scrollPane = new JScrollPane(innerPanel);

        scrollPane.setPreferredSize(new Dimension(2000,1000));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.Y_AXIS));
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        this.add(BorderLayout.CENTER, scrollPane);

        for (Transaction t: transactions) {
            addPanel(t);
        }
    }

    public void addPanel(Transaction t) {
        JLabel dateLabel = new JLabel(t.getDate().toString());
        JLabel descLabel = new JLabel(t.getDescription());
        JLabel amtLabel = new JLabel(String.valueOf(t.getAmount()));
        JTextField mapFrom = new JTextField();
        JTextField mapTo = new JTextField();
        JButton createMapping = new JButton("Create Mapping");
        JButton findExistingMapping = new JButton("Find Existing Mapping");

        createMapping.addActionListener(e -> {
            if (mapTo.getText().trim().length() == 0 || mapFrom.getText().trim().length() == 0) {
                AlertMessage.errorBox("No Value in Text Field", "Alert");
            } else {
                String mapFromValue = mapFrom.getText();
                String mapToValue = mapTo.getText();
                char incomeExpenseChar = t.getAmount() >= 0 ? 'I' : 'E';
                mappingTableSQLs.insertMapping(mapFromValue, mapToValue, AuditAccountClass.getAuditID(), incomeExpenseChar);
                mapFrom.setText("");
                mapTo.setText("");
            }
        });

        JPanel p1 = new JPanel();
        JPanel p2 = new JPanel();
        JPanel p3 = new JPanel();

        p1.setMinimumSize(new Dimension(65,10));
        p1.setPreferredSize(new Dimension(65,10));
        p1.add(dateLabel);
        p1.setLayout(new BoxLayout(p1, BoxLayout.X_AXIS));
        p1.setAlignmentX(Component.LEFT_ALIGNMENT);

        p2.setMinimumSize(new Dimension(600,10));
        p2.setPreferredSize(new Dimension(600,10));
        p2.add(descLabel);
        p2.setLayout(new BoxLayout(p2, BoxLayout.X_AXIS));
        p2.setAlignmentX(Component.LEFT_ALIGNMENT);

        p3.setMinimumSize(new Dimension(65,10));
        p3.setPreferredSize(new Dimension(65,10));
        p3.add(amtLabel);
        p3.setLayout(new BoxLayout(p3, BoxLayout.X_AXIS));
        p3.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(p1);
        panel.add(p2);
        panel.add(p3);
        panel.add(mapFrom);
        panel.add(mapTo);
        panel.add(createMapping);
        panel.add(findExistingMapping);

        innerPanel.add(panel);
        innerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
    }
}
