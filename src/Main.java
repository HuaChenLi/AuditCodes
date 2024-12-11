package src;

import src.Panels.*;
import src.SQLFunctions.AuditIDSQLs;
import src.SQLFunctions.DatabaseConnection;

import javax.swing.*;
import java.awt.*;
import java.io.*;


public class Main {
    JFrame frame;
    JPanel mainPanel;
    AuditAccountClass auditAccountClass;

    public Main() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

//        Load Database
        AuditIDSQLs auditIDSQLs = new AuditIDSQLs();
        auditIDSQLs.createAuditTable();

//        Account Creation Panel
        CreateAccount createAccount = new CreateAccount();
        mainPanel.add(createAccount);

//        Title Panel
        TitlePanel titlePanel = new TitlePanel();
        mainPanel.add(titlePanel);

//        Financial Year Panel
        FinancialYearPanel financialYearPanel = new FinancialYearPanel();
        mainPanel.add(financialYearPanel);

//        Account Selection
        AccountSelectionPanel accountSelectionPanel = new AccountSelectionPanel();
        mainPanel.add(accountSelectionPanel);

//        Create Excel Spreadsheets Panel
        CreateExcelSheetsPanel createExcelSpreadsheetsPanel = new CreateExcelSheetsPanel();
        mainPanel.add(createExcelSpreadsheetsPanel);

//        Income Expense Indicator Panel
        IncomeExpenseIndicatorPanel incomeExpenseIndicatorPanel = new IncomeExpenseIndicatorPanel();
        mainPanel.add(incomeExpenseIndicatorPanel);

//        Mapping Panel
        MappingPanel mappingPanel = new MappingPanel();
        mainPanel.add(mappingPanel);

//        Column Creation Panel
        ColumnCreationPanel columnCreationPanel = new ColumnCreationPanel();
        mainPanel.add(columnCreationPanel);

//        Categorise Panel
        CategoriseValuesPanel categoriseValuesPanel = new CategoriseValuesPanel();
        mainPanel.add(categoriseValuesPanel);

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
}