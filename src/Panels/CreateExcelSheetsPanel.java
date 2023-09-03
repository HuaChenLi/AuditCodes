package src.Panels;

import src.SQLFunctions.DatabaseConnection;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CreateExcelSheetsPanel extends JPanel {
    JButton createExcelSheets, createIncomeExpenseCSVs;
    public CreateExcelSheetsPanel() {
        this.setLayout(new GridLayout(0,2));
        this.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

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

        this.add(createExcelSheets);
        this.add(createIncomeExpenseCSVs);
    }

    public void createIncomeExpenseCSVsFunction() throws IOException {
        ProcessBuilder pb = new ProcessBuilder("python","Business Audit\\create_income_expense_csv.py");
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

    public void createExcelSheetsFunction() throws IOException {
        ProcessBuilder pb = new ProcessBuilder("python","Business Audit\\create_folder_structure.py");
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


}
