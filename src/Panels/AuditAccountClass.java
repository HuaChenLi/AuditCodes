package src.Panels;

public class AuditAccountClass {
    static int auditID;
    static boolean incomeExpenseEntered;
    static char incomeExpenseChar;

    public static int getAuditID() {
        return auditID;
    }

    public static void setAuditID(int auditID_2) {
        auditID = auditID_2;
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
