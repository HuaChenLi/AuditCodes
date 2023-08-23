import javax.swing.*;
import java.awt.*;
import java.io.*;


public class Main {
    JFrame frame;
    JPanel actionPanel, titlePanel, mainPanel, mappingPanel;
    JLabel titleLabel, mappingFromLabel, mappingToLabel, blankLabel;
    JButton createExcelSheets, createIncomeExpenseCSVs, createMapping;
    JTextField financialYear, mappingFrom, mappingTo;



    public Main() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));




        titlePanel = new JPanel();
        titleLabel = new JLabel("One Stop Shop for Excel Shenanigans");
        titlePanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        titlePanel.add(titleLabel);




        actionPanel = new JPanel();
        actionPanel.setLayout(new GridLayout(0,2));
        actionPanel.setBorder(BorderFactory.createEmptyBorder(10,10,100,10));

        frame = new JFrame();

        createExcelSheets = new JButton("Create New Excel Sheets");
        createIncomeExpenseCSVs = new JButton("Create Income and Expense CSVs");

        actionPanel.add(createExcelSheets);
        actionPanel.add(createIncomeExpenseCSVs);

        mainPanel.add(titlePanel);
        mainPanel.add(actionPanel);


//        Mapping Panel
        mappingPanel = new JPanel();
        mappingPanel.setLayout(new GridLayout(0,3));
        mappingPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        mappingFromLabel = new JLabel("Map From");
        mappingToLabel = new JLabel("Map To");
        blankLabel = new JLabel();

        createMapping = new JButton("Create Mapping");
        mappingFrom = new JTextField();
        mappingTo = new JTextField();

        mappingPanel.add(mappingFromLabel);
        mappingPanel.add(mappingToLabel);
        mappingPanel.add(blankLabel);
        mappingPanel.add(mappingFrom);
        mappingPanel.add(mappingTo);
        mappingPanel.add(createMapping);

        mainPanel.add(mappingPanel);


        frame.add(mainPanel, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Bank Account Organisation");
        frame.pack();
        frame.setVisible(true);

        createExcelSheets.addActionListener(e1 -> {
            try {
                createExcelSheetsFunction();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        createIncomeExpenseCSVs.addActionListener(e1 -> {
            try {
                createIncomeExpenseCSVsFunction();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        createMapping.addActionListener(e1 -> {
            try{
                createMappingFunction();
            } catch (Exception e) {
                e.printStackTrace();
            }

        });

    }

    public static void main(String[] args) {
        new Main();
    }


    public void createExcelSheetsFunction() throws IOException {
        ProcessBuilder pb = new ProcessBuilder("python","Business Audit\\create_excel_files_purely_from_code_with_libraries.py");
        Process process = pb.start();

        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        BufferedReader readers = new BufferedReader(new InputStreamReader(process.getErrorStream()));

        String lines = null;
        while ((lines=reader.readLine())!=null) {
            System.out.println("lines " + lines);
        }

        while ((lines=readers.readLine())!=null) {
            System.out.println("Error lines " + lines);
        }
    }

    public void createIncomeExpenseCSVsFunction() throws IOException {
        ProcessBuilder pb = new ProcessBuilder("python","Business Audit\\move_deets_from_bank_csv_to_excel_sheets.py");
        Process process = pb.start();

        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        BufferedReader readers = new BufferedReader(new InputStreamReader(process.getErrorStream()));

        String lines = null;
        while ((lines=reader.readLine())!=null) {
            System.out.println("lines " + lines);
        }

        while ((lines=readers.readLine())!=null) {
            System.out.println("Error lines " + lines);
        }
    }


    public void createMappingFunction() throws  IOException {

    }


}
