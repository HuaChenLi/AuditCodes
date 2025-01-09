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
    KnownDescriptionPanel knownDescriptionPanel;
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
        incomeMappingsTable.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) {
                int row = incomeMappingsTable.getSelectedRow();
                if (row != -1) {
                    int column = 2;
                    String id = incomeMappingsTable.getModel().getValueAt(row, column).toString();
                    knownDescriptionPanel.setNewCategory(id);
                }
            }
        });
        expenseMappingsTable = new JTable(expenseMappingsModel);
        expenseMappingsTable.setDefaultEditor(Object.class, null);
        expenseMappingsTable.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) {
                int row = expenseMappingsTable.getSelectedRow();
                if (row != -1) {
                    int column = 2;
                    String id = expenseMappingsTable.getModel().getValueAt(row, column).toString();
                    knownDescriptionPanel.setNewCategory(id);
                }
            }
        });

        TablePanel tablePanel = new TablePanel();

        createMapPanel.add(mappingFromLabel);
        createMapPanel.add(mappingToLabel);
        createMapPanel.add(blankLabel);
        createMapPanel.add(mappingFrom);
        createMapPanel.add(mappingTo);
        createMapPanel.add(createMapping);

        this.add(createMapPanel);
        this.add(tablePanel);

        refreshMappingTable();
    }
    public void createMappingFunction() {
        if (AuditAccountClass.isAuditIDEntered() && AuditAccountClass.isIncomeExpenseEntered()) {
            String mapFromText = mappingFrom.getText().trim();
            String mapToText = mappingTo.getText().trim();

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
            incomeMappingsTable.removeColumn(incomeMappingsTable.getColumn("id"));
            incomeMappingsTable.getColumnModel().getColumn(0).setPreferredWidth(250);
            incomeMappingsTable.getColumnModel().getColumn(1).setPreferredWidth(200);

            temp = mappingTableSQLs.getMappings(AuditAccountClass.getAuditID(), false);
            expenseMappingsModel = buildTableModel(temp);
            expenseMappingsTable.setModel(expenseMappingsModel);
            expenseMappingsTable.removeColumn(expenseMappingsTable.getColumn("id"));
            expenseMappingsTable.getColumnModel().getColumn(0).setPreferredWidth(250);
            expenseMappingsTable.getColumnModel().getColumn(1).setPreferredWidth(200);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class TablePanel extends JPanel {
        JLabel incomeMappingLabel = new JLabel("Income Mappings");
        JLabel expenseMappingLabel = new JLabel("Expense Mappings");
        JButton deleteIncomeMapping = new JButton("Delete Selected Income Mapping");
        JButton deleteExpenseMapping = new JButton("Delete Selected Expense Mapping");
        JPanel justLabelPanel = new JPanel();
        JPanel justTablePanel = new JPanel();
        JPanel justButtonPanel = new JPanel();
        JScrollPane scrollPane;
        GridBagConstraints gbc = new GridBagConstraints();
        Border smallBorder = BorderFactory.createEmptyBorder(5,5,5,5);
        public TablePanel() {
            incomeMappingLabel.setBorder(smallBorder);
            expenseMappingLabel.setBorder(smallBorder);
            justLabelPanel.add(incomeMappingLabel);
            justLabelPanel.add(expenseMappingLabel);

            scrollPane = new JScrollPane(justTablePanel);
            scrollPane.getVerticalScrollBar().setUnitIncrement(16);
            scrollPane.setPreferredSize(new Dimension(1000,500));

            justTablePanel.add(incomeMappingsTable);
            justTablePanel.add(expenseMappingsTable);

            deleteIncomeMapping.addActionListener(e -> {
                int row = incomeMappingsTable.getSelectedRow();
                if (row != -1) {
                    int column = 0;
                    String id = incomeMappingsTable.getModel().getValueAt(row, column).toString();
                    mappingTableSQLs.deleteMapping(Integer.parseInt(id));
                    refreshMappingTable();
                }
            });
            gbc.gridx = 0;
            gbc.gridy = 2;
            justButtonPanel.add(deleteIncomeMapping, gbc);

            deleteExpenseMapping.addActionListener(e -> {
                int row = expenseMappingsTable.getSelectedRow();
                if (row != -1) {
                    int column = 0;
                    String id = expenseMappingsTable.getModel().getValueAt(row, column).toString();
                    mappingTableSQLs.deleteMapping(Integer.parseInt(id));
                    refreshMappingTable();
                }
            });
            gbc.gridx = 1;
            justButtonPanel.add(deleteExpenseMapping, gbc);

            this.setLayout(new GridBagLayout());
            gbc.gridx = 0;
            gbc.gridy = 0;
            this.add(justLabelPanel);
            gbc.gridx = 0;
            gbc.gridy = 1;
            this.add(scrollPane, gbc);
            gbc.gridx = 0;
            gbc.gridy = 2;
            this.add(justButtonPanel, gbc);
        }
    }

    public void setKnownDescriptionPanel(KnownDescriptionPanel knownDescriptionPanel) {
        this.knownDescriptionPanel = knownDescriptionPanel;
    }
}
