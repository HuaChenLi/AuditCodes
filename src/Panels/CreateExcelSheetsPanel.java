package src.Panels;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import static src.Panels.FinancialYearPanel.financialYearValue;

public class CreateExcelSheetsPanel extends JPanel {
    JButton createExcelSheets;
    JButton createIncomeExpenseCSVs;
    JPanel createCSVPanel;
    ArrayList<String> csvFiles = new ArrayList<>();
    public CreateExcelSheetsPanel(int accountID, String accountName) {
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
            if (csvFiles.size() > 0) {
                try {
                    createIncomeExpenseCSVsFunction(csvFiles);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("No CSVs selected");
            }
        });

        FileSelector fileSelector = new FileSelector();
        fileSelector.createPanel();

        createCSVPanel = new JPanel();
        createCSVPanel.add(fileSelector);
        createCSVPanel.add(createIncomeExpenseCSVs);

        this.add(createExcelSheets);
        this.add(createCSVPanel);
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


    public void createIncomeExpenseCSVsFunction(ArrayList<String> files) throws IOException {
        String arg = String.join("/", files);
        ProcessBuilder pb = new ProcessBuilder("python", "Business Audit\\create_income_expense_csv.py",
                String.valueOf(AuditAccountClass.getAuditID()),
                String.valueOf(AuditAccountClass.getAccountName()),
                String.valueOf(financialYearValue),
                arg);
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

        AlertMessage.infoBox("Excel Sheets Created", "Finished Process");
    }


    public class FileSelector extends JPanel {
        DefaultTableModel csvTableModel = new DefaultTableModel() {
            public int getColumnCount() { return 1; }
            @Override
            public boolean isCellEditable(int row, int column) {
                //all cells false
                return false;
            }
        };
        JTable csvTable = new JTable(csvTableModel);
        JButton openButton = new JButton("Select CSV file");

        public void createPanel() {
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
                            FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV Files (*.csv)", "csv");
                            chooser.setFileFilter(filter);
                            chooser.setMultiSelectionEnabled(true);

                            if (chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
                                File[] files = chooser.getSelectedFiles();
                                for (File f : files) {
                                    String file = f.getAbsolutePath();
                                    if (!isDuplicateFile(file)) {
                                        csvFiles.add(file);
                                        csvTableModel.addRow(new Object[]{file});
                                        csvTable.setModel(csvTableModel);
                                    } else {
                                        System.out.println("Duplicate File");
                                    }
                                }

                            }
                        }
                    });
                }
            });
            csvTable.getColumnModel().getColumn(0).setPreferredWidth(400);

            this.add(csvTable);
            this.add(openButton);
        }

        private boolean isDuplicateFile(String file) {
            for (String f : csvFiles) {
                if (file.equals(f)) {
                    return true;
                }
            }
            return false;
        }
    }
}
