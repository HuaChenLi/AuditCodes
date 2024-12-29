package src.Panels;

import javax.swing.*;
import java.awt.*;

public class IncomeExpenseIndicatorPanel extends JPanel {
    JLabel incomeExpenseLabel;
    IncomeExpenseCombobox incomeExpenseCombobox;

    public IncomeExpenseIndicatorPanel() {
        this.setLayout(new GridLayout(0,2));
        this.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        incomeExpenseLabel = new JLabel("Income and/or Expense");
        incomeExpenseCombobox = new IncomeExpenseCombobox();

        this.add(incomeExpenseLabel);
        this.add(incomeExpenseCombobox);
    }

    public class IncomeExpenseCombobox extends JComboBox {
        public IncomeExpenseCombobox() {
            AuditAccountClass.incomeExpenseChar = 'I';

            this.addItem(new IncomeExpenseCodeDescription('I', "Income"));
            this.addItem(new IncomeExpenseCodeDescription('E', "Expense"));
            this.addItem(new IncomeExpenseCodeDescription('B', "Both"));

            this.addActionListener(e -> {
                IncomeExpenseCodeDescription temp = (IncomeExpenseCodeDescription) this.getSelectedItem();
                if (temp.getId() == 'E') {
                    AuditAccountClass.setIncomeExpenseChar('E');
                } else if (temp.getId() == 'I') {
                    AuditAccountClass.setIncomeExpenseChar('I');
                } else if (temp.getId() == 'B') {
                    AuditAccountClass.setIncomeExpenseChar('B');
                }
            });
        }
    }

    public class IncomeExpenseCodeDescription {
        char id;
        String description;

        public IncomeExpenseCodeDescription(char id, String description) {
            this.id = id;
            this.description = description;
        }

        public String getDescription() {
            return description;
        }

        public char getId() {
            return id;
        }

        @Override
        public String toString() {
            return description;
        }
    }
}