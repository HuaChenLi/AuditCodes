package src;

import src.Panels.*;
import src.SQLFunctions.AuditIDSQLs;
import src.SQLFunctions.CreateNewColumns;
import src.SQLFunctions.MappingTableSQLs;

import javax.swing.*;
import java.awt.*;

public class Main {
    JFrame frame;
    JPanel mainPanel;

    public Main() {
//        Setting the UI to look more like Windows 11
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        }

        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

//        Load Database
        AuditIDSQLs auditIDSQLs = new AuditIDSQLs();
        auditIDSQLs.createAuditTable();

        CreateNewColumns createNewColumns = new CreateNewColumns();
        createNewColumns.createExcelColumnTables();
        createNewColumns.createExcelColumnSelectionTable();
        createNewColumns.createCategoryMappings();

        MappingTableSQLs mappingTableSQLs = new MappingTableSQLs();
        mappingTableSQLs.createMappingTable();
        mappingTableSQLs.createMappingSelectionTable();

        ExcelColumnViewPanel excelColumnViewPanel = new ExcelColumnViewPanel();

//        Account Creation Panel
        CreateAccount createAccount = new CreateAccount(excelColumnViewPanel);
        mainPanel.add(createAccount);

//        Title Panel
        TitlePanel titlePanel = new TitlePanel();
        mainPanel.add(titlePanel);

//        Financial Year Panel
        FinancialYearPanel financialYearPanel = new FinancialYearPanel();
        mainPanel.add(financialYearPanel);

//        Create Excel Spreadsheets Panel
        CreateExcelSheetsPanel createExcelSpreadsheetsPanel = new CreateExcelSheetsPanel(AuditAccountClass.getAuditID(), AuditAccountClass.getAccountName());
        mainPanel.add(createExcelSpreadsheetsPanel);

//        Income Expense Indicator Panel
        IncomeExpenseIndicatorPanel incomeExpenseIndicatorPanel = new IncomeExpenseIndicatorPanel();
        mainPanel.add(incomeExpenseIndicatorPanel);

//        Mapping Panel
        MappingPanel mappingPanel = new MappingPanel();
        mainPanel.add(mappingPanel);

//        Column Creation Panel
        CreateCategoryPanel columnCreationPanel = new CreateCategoryPanel(excelColumnViewPanel);
        mainPanel.add(columnCreationPanel);

//        Category Creation Panel
        KnownDescriptionPanel createCategoryPanel = new KnownDescriptionPanel(excelColumnViewPanel);
        mainPanel.add(createCategoryPanel);

//        Categorise Panel
        CategoriseValuesPanel categoriseValuesPanel = new CategoriseValuesPanel();
        excelColumnViewPanel.setCategoriseValuesPanel(categoriseValuesPanel);
        mainPanel.add(categoriseValuesPanel);

        excelColumnViewPanel.refreshAll();
        mainPanel.add(excelColumnViewPanel);

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