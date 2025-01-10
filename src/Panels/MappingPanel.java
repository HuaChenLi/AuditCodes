package src.Panels;

import src.Interfaces.Model;
import src.SQLFunctions.MappingTableSQLs;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.util.List;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.sql.ResultSet;
import java.util.ArrayList;

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
    private final int MAPPING_TABLE_WIDTH = 300;
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
                if (row >= 0) {
                    int modelRow = incomeMappingsTable.convertRowIndexToModel(row);
                    int column = 2;
                    String s = incomeMappingsTable.getModel().getValueAt(modelRow, column).toString();
                    knownDescriptionPanel.setNewCategory(s);
                }
            }
        });
        expenseMappingsTable = new JTable(expenseMappingsModel);
        expenseMappingsTable.setDefaultEditor(Object.class, null);
        expenseMappingsTable.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) {
                int row = expenseMappingsTable.getSelectedRow();
                if (row >= 0) {
                    int modelRow = expenseMappingsTable.convertRowIndexToModel(row);
                        int column = 2;
                        String s = expenseMappingsTable.getModel().getValueAt(modelRow, column).toString();
                        knownDescriptionPanel.setNewCategory(s);
                        mappingTo.setText(s);
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
            TableRowSorter sorter;
            List<? extends RowSorter.SortKey> sortKeys;
            ResultSet temp;

            sorter = (TableRowSorter) incomeMappingsTable.getRowSorter();
            sortKeys = sorter.getSortKeys();
            temp = mappingTableSQLs.getMappings(AuditAccountClass.getAuditID(), true);
            incomeMappingsModel = buildTableModel(temp);
            incomeMappingsTable.setModel(incomeMappingsModel);
            sorter = (TableRowSorter) incomeMappingsTable.getRowSorter();
            sorter.setSortKeys(sortKeys);

            sorter = (TableRowSorter) expenseMappingsTable.getRowSorter();
            sortKeys = sorter.getSortKeys();
            temp = mappingTableSQLs.getMappings(AuditAccountClass.getAuditID(), false);
            expenseMappingsModel = buildTableModel(temp);
            expenseMappingsTable.setModel(expenseMappingsModel);
            sorter = (TableRowSorter) expenseMappingsTable.getRowSorter();
            sorter.setSortKeys(sortKeys);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class TablePanel extends JPanel {
        JLabel incomeMappingLabel = new JLabel("Income Mappings");
        JLabel expenseMappingLabel = new JLabel("Expense Mappings");
        JButton deleteIncomeMapping = new JButton("Delete Selected Income Mapping");
        JButton deleteExpenseMapping = new JButton("Delete Selected Expense Mapping");
        JPanel incomePanel = new JPanel();
        JPanel expensePanel = new JPanel();
        JScrollPane incomeScroll;
        JScrollPane expenseScroll;
        GridBagConstraints gbc = new GridBagConstraints();
        Border smallBorder = BorderFactory.createEmptyBorder(5,5,5,5);
        public TablePanel() {
            incomeMappingLabel.setBorder(smallBorder);
            expenseMappingLabel.setBorder(smallBorder);

            initMappingPanelTables();

            incomeScroll = new JScrollPane(incomeMappingsTable);
            expenseScroll = new JScrollPane(expenseMappingsTable);

            deleteIncomeMapping.addActionListener(e -> {
                int row = incomeMappingsTable.getSelectedRow();
                if (row >= 0) {
                    int modelRow = incomeMappingsTable.convertRowIndexToModel(row);
                    int column = 0;
                    String id = incomeMappingsTable.getModel().getValueAt(modelRow, column).toString();
                    mappingTableSQLs.deleteMapping(Integer.parseInt(id));
                    refreshMappingTable();
                }

            });
            gbc.gridx = 0;
            gbc.gridy = 2;

            deleteExpenseMapping.addActionListener(e -> {
                int row = expenseMappingsTable.getSelectedRow();
                if (row >= 0) {
                    int modelRow = expenseMappingsTable.convertRowIndexToModel(row);
                    int column = 0;
                    String id = expenseMappingsTable.getModel().getValueAt(modelRow, column).toString();
                    mappingTableSQLs.deleteMapping(Integer.parseInt(id));
                    refreshMappingTable();
                }
            });
            gbc.gridx = 1;

            incomePanel.setLayout(new GridBagLayout());
            gbc.gridx = 0;
            gbc.gridy = 0;
            incomePanel.add(incomeMappingLabel, gbc);
            gbc.gridx = 0;
            gbc.gridy = 1;
            incomePanel.add(incomeScroll, gbc);
            gbc.gridx = 0;
            gbc.gridy = 2;
            incomePanel.add(deleteIncomeMapping, gbc);

            expensePanel.setLayout(new GridBagLayout());
            gbc.gridx = 0;
            gbc.gridy = 0;
            expensePanel.add(expenseMappingLabel, gbc);
            gbc.gridx = 0;
            gbc.gridy = 1;
            expensePanel.add(expenseScroll, gbc);
            gbc.gridx = 0;
            gbc.gridy = 2;
            expensePanel.add(deleteExpenseMapping, gbc);

            this.add(incomePanel);
            this.add(expensePanel);
        }
    }

    public void setKnownDescriptionPanel(KnownDescriptionPanel knownDescriptionPanel) {
        this.knownDescriptionPanel = knownDescriptionPanel;
    }

    private void initMappingPanelTables() {
        try {
            ResultSet temp;
            temp = mappingTableSQLs.getMappings(AuditAccountClass.getAuditID(), true);
            incomeMappingsModel = buildTableModel(temp);
            incomeMappingsTable.setModel(incomeMappingsModel);
            incomeMappingsTable.removeColumn(incomeMappingsTable.getColumn("id"));
            incomeMappingsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            incomeMappingsTable.setAutoCreateColumnsFromModel(false);
            incomeMappingsTable.getColumn("map_from").setPreferredWidth(MAPPING_TABLE_WIDTH);
            incomeMappingsTable.getColumn("map_to").setPreferredWidth(MAPPING_TABLE_WIDTH);
            incomeMappingsTable.getColumn("map_from").setHeaderValue("Income Raw Data");
            incomeMappingsTable.getColumn("map_to").setHeaderValue("Income Mapped Description");
            incomeMappingsTable.setAutoCreateRowSorter(true);

            temp = mappingTableSQLs.getMappings(AuditAccountClass.getAuditID(), false);
            expenseMappingsModel = buildTableModel(temp);
            expenseMappingsTable.setModel(expenseMappingsModel);
            expenseMappingsTable.removeColumn(expenseMappingsTable.getColumn("id"));
            expenseMappingsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            expenseMappingsTable.setAutoCreateColumnsFromModel(false);
            expenseMappingsTable.getColumn("map_from").setPreferredWidth(MAPPING_TABLE_WIDTH);
            expenseMappingsTable.getColumn("map_to").setPreferredWidth(MAPPING_TABLE_WIDTH);
            expenseMappingsTable.getColumn("map_from").setHeaderValue("Expense Raw Data");
            expenseMappingsTable.getColumn("map_to").setHeaderValue("Expense Mapped Description");
            expenseMappingsTable.setAutoCreateRowSorter(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
