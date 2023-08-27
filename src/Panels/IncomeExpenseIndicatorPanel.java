package src.Panels;

import javax.swing.*;
import java.awt.*;

public class IncomeExpenseIndicatorPanel extends JPanel {
    JLabel incomeExpenseLabel, incomeExpenseIndicator;
    JButton incomeButton, expenseButton;
    boolean isIncome, isExpense;
    char incomeExpenseChar;
    public IncomeExpenseIndicatorPanel() {
        this.setLayout(new GridLayout(0,4));
        this.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        incomeExpenseLabel = new JLabel("Income and/or Expense");
        incomeExpenseIndicator = new JLabel("", SwingConstants.CENTER);
        incomeButton = new JButton("Income");
        expenseButton = new JButton("Expense");

        incomeButton.addActionListener(e1 -> {
            isIncome = !isIncome;
            incomeExpenseIndicator.setText(getIncomeExpenseChar());
            this.revalidate();
            this.validate();
        });

        expenseButton.addActionListener(e1 -> {
            isExpense = !isExpense;
            incomeExpenseIndicator.setText(getIncomeExpenseChar());
            this.revalidate();
            this.validate();
        });

        this.add(incomeExpenseLabel);
        this.add(incomeExpenseIndicator);
        this.add(incomeButton);
        this.add(expenseButton);
    }
    public String getIncomeExpenseChar() {
        AuditAccountClass.setIncomeExpenseEntered(true);

        if (isIncome && !isExpense) {
            incomeExpenseChar = 'I';
        } else if (!isIncome && isExpense) {
            incomeExpenseChar = 'E';
        } else {
            incomeExpenseChar = 'B';
        }

        AuditAccountClass.setIncomeExpenseChar(incomeExpenseChar);
        return String.valueOf(incomeExpenseChar);
    }

}
