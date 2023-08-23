import javax.swing.*;
import java.awt.*;
import java.io.*;


public class Main {
    JFrame frame;
    JPanel actionPanel, titlePanel, mainPanel, mappingPanel, financialYearPanel, incomeExpensePanel, accountSelectionPanel;
    JLabel financialYearLabel, financialYearIndicator, titleLabel, mappingFromLabel, mappingToLabel, blankLabel, incomeExpenseLabel, incomeExpenseIndicator, accountSelectionLabel;
    JButton createExcelSheets, createIncomeExpenseCSVs, createMapping, setFinancialYear, incomeButton, expenseButton, accountSelectionButton;
    JTextField financialYearText, mappingFrom, mappingTo, accountSelectionText;



    public Main() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));


//        Title Panel
        titlePanel = new JPanel();
        titleLabel = new JLabel("One Stop Shop for Excel Shenanigans");
        titlePanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        titlePanel.add(titleLabel);

        mainPanel.add(titlePanel);

//        Financial Year Panel
        financialYearLabel = new JLabel("Financial Year");
        financialYearIndicator = new JLabel();
        financialYearText = new JTextField();
        setFinancialYear = new JButton("Set Financial Year");
        financialYearPanel = new JPanel();
        financialYearPanel.setLayout(new GridLayout(0,4));
        financialYearPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        financialYearPanel.add(financialYearLabel);
        financialYearPanel.add(financialYearIndicator);
        financialYearPanel.add(financialYearText);
        financialYearPanel.add(setFinancialYear);

        mainPanel.add(financialYearPanel);

//        Account Selection
        accountSelectionPanel = new JPanel();
        accountSelectionPanel.setLayout(new GridLayout(0,3));
        accountSelectionPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        accountSelectionLabel = new JLabel();
        accountSelectionText = new JTextField();
        accountSelectionButton = new JButton("Set Account");
        accountSelectionPanel.add(accountSelectionLabel);
        accountSelectionPanel.add(accountSelectionText);
        accountSelectionPanel.add(accountSelectionButton);

        mainPanel.add(accountSelectionPanel);

//        Action Panel
        actionPanel = new JPanel();
        actionPanel.setLayout(new GridLayout(0,2));
        actionPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        createExcelSheets = new JButton("Create New Excel Sheets");
        createIncomeExpenseCSVs = new JButton("Create Income and Expense CSVs");

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


        actionPanel.add(createExcelSheets);
        actionPanel.add(createIncomeExpenseCSVs);

        mainPanel.add(actionPanel);

//        Income Expense Indicator Panel
        incomeExpensePanel = new JPanel();
        incomeExpensePanel.setLayout(new GridLayout(0,4));
        incomeExpensePanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        incomeExpenseLabel = new JLabel("Income and/or Expense");
        incomeExpenseIndicator = new JLabel();
        incomeButton = new JButton("Income");
        expenseButton = new JButton("Expense");
        incomeExpensePanel.add(incomeExpenseLabel);
        incomeExpensePanel.add(incomeExpenseIndicator);
        incomeExpensePanel.add(incomeButton);
        incomeExpensePanel.add(expenseButton);

        mainPanel.add(incomeExpensePanel);

//        Mapping Panel
        mappingPanel = new JPanel();
        mappingPanel.setLayout(new GridLayout(0,3));
        mappingPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

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

        mappingPanel.add(mappingFromLabel);
        mappingPanel.add(mappingToLabel);
        mappingPanel.add(blankLabel);
        mappingPanel.add(mappingFrom);
        mappingPanel.add(mappingTo);
        mappingPanel.add(createMapping);

        mainPanel.add(mappingPanel);

//        Setting the GUI Frame
        frame = new JFrame();
        frame.add(mainPanel, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Bank Account Organisation");
        frame.pack();
        frame.setVisible(true);




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
