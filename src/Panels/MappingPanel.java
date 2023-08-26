package src.Panels;

import src.SQLFunctions.DatabaseConnection;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class MappingPanel extends JPanel {
    JLabel mappingFromLabel, mappingToLabel, blankLabel;
    JButton createMapping;
    JTextField mappingFrom, mappingTo;
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

        this.add(mappingFromLabel);
        this.add(mappingToLabel);
        this.add(blankLabel);
        this.add(mappingFrom);
        this.add(mappingTo);
        this.add(createMapping);
    }
    public void createMappingFunction() throws IOException {
        String mapFromText = mappingFrom.getText();
        String mapToText = mappingTo.getText();
        mappingFrom.setText("");
        mappingTo.setText("");

        DatabaseConnection newConnection = new DatabaseConnection();
        newConnection.executeSQL();
    }
}
