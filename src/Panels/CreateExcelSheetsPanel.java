package src.Panels;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static src.Panels.FinancialYearPanel.financialQuarterValue;
import static src.Panels.FinancialYearPanel.financialYearValue;

public class CreateExcelSheetsPanel extends JPanel {
    JButton createExcelSheets, createIncomeExpenseCSVs;
    public CreateExcelSheetsPanel(int accountID, String accountName) {
        this.setLayout(new GridLayout(0,2));
        this.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        createExcelSheets = new JButton("Create New Excel Sheets");
        createIncomeExpenseCSVs = new JButton("Create Income and Expense CSVs");

        createExcelSheets.addActionListener(e1 -> {
            try {
                createExcelSheetsFunction(accountID, accountName);
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

        FileSelector fileSelector = new FileSelector();
        fileSelector.createButton();

        this.add(createExcelSheets);
        this.add(createIncomeExpenseCSVs);
        this.add(fileSelector);
    }


    public void createExcelSheetsFunction(int accountID, String accountName) throws IOException {
        ProcessBuilder pb = new ProcessBuilder("python","Business Audit\\create_folder_structure.py", String.valueOf(financialYearValue), String.valueOf(accountID), accountName);
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
        System.out.println(financialYearValue);
        ProcessBuilder pb = new ProcessBuilder("python", "Business Audit\\create_income_expense_csv.py", String.valueOf(financialYearValue), String.valueOf(financialQuarterValue));
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


    public class FileSelector extends JPanel {
        JButton openButton = new JButton("Open");

        public void createButton() {
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    final JFrame frame = new JFrame("Open File Example");
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    frame.setLayout(new BorderLayout());
                    openButton.addActionListener(new ActionListener() {

                        @Override
                        public void actionPerformed(ActionEvent e) {
                            JFileChooser chooser = new JFileChooser();
                            if (chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
                                // do something
                            }
                        }
                    });
                }
            });

            this.add(openButton);
        }
    }
}
