package src.Panels;

public class AuditAccountClass {
    static int auditID;
    static boolean auditIDEntered, incomeExpenseEntered;
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
        return auditIDEntered;
    }

    public static boolean isIncomeExpenseEntered() {
        return incomeExpenseEntered;
    }

    public static void setAuditIDEntered(boolean auditIDEntered) {
        AuditAccountClass.auditIDEntered = auditIDEntered;
    }

    public static void setIncomeExpenseEntered(boolean incomeExpenseEntered) {
        AuditAccountClass.incomeExpenseEntered = incomeExpenseEntered;
    }
}
