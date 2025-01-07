package src.Panels;

import src.Interfaces.Model;
import src.SQLFunctions.MappingTableSQLs;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.ResultSet;

public class MappingPanel extends JPanel implements Model {
    JLabel mappingFromLabel, mappingToLabel, blankLabel;
    JButton createMapping;
    JTextField mappingFrom, mappingTo;
    JPanel createMapPanel;
    JTable incomeMappingsTable;
    JTable expenseMappingsTable;
    DefaultTableModel incomeMappingsModel = new DefaultTableModel();
    DefaultTableModel expenseMappingsModel = new DefaultTableModel();
    MappingTableSQLs mappingTableSQLs = new MappingTableSQLs();
    public MappingPanel() {
        createMapPanel = new JPanel();
        createMapPanel.setLayout(new GridLayout(0,3));
        createMapPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        mappingFromLabel = new JLabel("Map From");
        mappingToLabel = new JLabel("Map To");
        blankLabel = new JLabel();

        createMapping = new JButton("Create Mapping");
        createMapping.addActionListener(e1 -> {
            try{
                createMappingFunction();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        mappingFrom = new JTextField();
        mappingFrom.setColumns(30);
        mappingTo = new JTextField();
        mappingTo.setColumns(25);

        incomeMappingsTable = new JTable(incomeMappingsModel);
        incomeMappingsTable.setDefaultEditor(Object.class, null);
        expenseMappingsTable = new JTable(expenseMappingsModel);
        expenseMappingsTable.setDefaultEditor(Object.class, null);

        TablePanel tablePanel = new TablePanel();

        createMapPanel.add(mappingFromLabel);
        createMapPanel.add(mappingToLabel);
        createMapPanel.add(blankLabel);
        createMapPanel.add(mappingFrom);
        createMapPanel.add(mappingTo);
        createMapPanel.add(createMapping);
        createMapPanel.add(tablePanel);

        this.add(createMapPanel);
        this.add(tablePanel);

        refreshMappingTable();
    }
    public void createMappingFunction() {
        if (AuditAccountClass.isAuditIDEntered() && AuditAccountClass.isIncomeExpenseEntered()) {
            String mapFromText = mappingFrom.getText();
            String mapToText = mappingTo.getText();

            if (mapFromText.trim().length() == 0) {
                System.out.println("Map from must have a value");
                return;
            }

            if (mapToText.trim().length() == 0) {
                System.out.println("Map to must have a value");
                return;
            }

            mappingFrom.setText("");
            mappingTo.setText("");

            int auditID = AuditAccountClass.getAuditID();
            char incomeExpenseChar = AuditAccountClass.getIncomeExpenseChar();

            mappingTableSQLs.insertMapping(mapFromText, mapToText, auditID, incomeExpenseChar);
            refreshMappingTable();
        } else {
            System.out.println("Please Enter Audit ID and Income/Expense");
        }
        System.out.println(AuditAccountClass.getIncomeExpenseChar());
    }

    public void refreshMappingTable() {
        try {
            ResultSet temp;
            temp = mappingTableSQLs.getMappings(AuditAccountClass.getAuditID(), true);
            incomeMappingsModel = buildTableModel(temp);
            incomeMappingsTable.setModel(incomeMappingsModel);
            incomeMappingsTable.getColumnModel().getColumn(0).setPreferredWidth(200);
            incomeMappingsTable.getColumnModel().getColumn(1).setPreferredWidth(200);

            temp = mappingTableSQLs.getMappings(AuditAccountClass.getAuditID(), false);
            expenseMappingsModel = buildTableModel(temp);
            expenseMappingsTable.setModel(expenseMappingsModel);
            expenseMappingsTable.getColumnModel().getColumn(0).setPreferredWidth(200);
            expenseMappingsTable.getColumnModel().getColumn(1).setPreferredWidth(200);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class TablePanel extends JPanel {
        public TablePanel() {
            Border smallBorder = BorderFactory.createEmptyBorder(5,5,5,5);
            this.setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            JLabel incomeMappingLabel = new JLabel("Income Mappings");
            incomeMappingLabel.setBorder(smallBorder);
            this.add(incomeMappingLabel, gbc);
            gbc.gridx = 1;
            JLabel expenseMappingLabel = new JLabel("Expense Mappings");
            expenseMappingLabel.setBorder(smallBorder);
            this.add(expenseMappingLabel, gbc);
            gbc.gridx = 0;
            gbc.gridy = 1;
            this.add(incomeMappingsTable, gbc);
            gbc.gridx = 1;
            this.add(expenseMappingsTable, gbc);
        }
    }
}
