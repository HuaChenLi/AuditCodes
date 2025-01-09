package src.Panels;

import src.SQLFunctions.AuditIDSQLs;

public class AuditAccountClass {
    static int auditID;
    static char incomeExpenseChar = 'I';

    public static int getAuditID() {
        return auditID;
    }

    public static void setAuditID(int auditID_2) {
        auditID = auditID_2;
    }

    public static String getAccountName() {
        AuditIDSQLs auditIDSQLs = new AuditIDSQLs();
        return auditIDSQLs.getAuditName(auditID);
    }
    public static char getIncomeExpenseChar() {
        return incomeExpenseChar;
    }

    public static void setIncomeExpenseChar(char incomeExpenseChar) {
        AuditAccountClass.incomeExpenseChar = incomeExpenseChar;
    }

    public static boolean isAuditIDEntered() {
        return (auditID > 0);
    }

    public static boolean isIncomeExpenseEntered() {
        return (incomeExpenseChar > 0);
    }

    public static boolean isIncome() {
        return incomeExpenseChar == 'I' || incomeExpenseChar == 'B';
    }

    public static boolean isExpense() {
        return incomeExpenseChar == 'E' || incomeExpenseChar == 'B';
    }
}
