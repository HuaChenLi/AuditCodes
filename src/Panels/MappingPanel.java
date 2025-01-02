package src.Panels;

import src.Interfaces.Model;
import src.SQLFunctions.MappingTableSQLs;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MappingPanel extends JPanel implements Model {
    JLabel mappingFromLabel, mappingToLabel, blankLabel;
    JButton createMapping;
    JTextField mappingFrom, mappingTo;
    JPanel tablePanel;
    JTable incomeMappingsTable;
    JTable expenseMappingsTable;
    DefaultTableModel incomeMappingsModel = new DefaultTableModel();
    DefaultTableModel expenseMappingsModel = new DefaultTableModel();
    MappingTableSQLs mappingTableSQLs = new MappingTableSQLs();
    public MappingPanel() {
        this.setLayout(new GridLayout(0,3));
        this.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

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
        mappingTo = new JTextField();

        incomeMappingsTable = new JTable(incomeMappingsModel);
        expenseMappingsTable = new JTable(expenseMappingsModel);

        this.add(mappingFromLabel);
        this.add(mappingToLabel);
        this.add(blankLabel);
        this.add(mappingFrom);
        this.add(mappingTo);
        this.add(createMapping);

        this.add(incomeMappingsTable);
        this.add(expenseMappingsTable);
    }
    public void createMappingFunction() throws IOException {
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
        } else {
            System.out.println("Please Enter Audit ID and Income/Expense");
        }
        System.out.println(AuditAccountClass.getIncomeExpenseChar());
    }

    public void refreshMappingTable() throws SQLException {
        ResultSet temp = mappingTableSQLs.getMappings(AuditAccountClass.getAuditID(), true);
        incomeMappingsModel = buildTableModel(temp);

    }
}
