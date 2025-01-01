package src.Panels;

import javax.swing.*;

public class FinancialYearPanel extends JPanel {
    JLabel financialYearLabel;
    YearComboBox yearComboBox;

    static int financialYearValue;
    public FinancialYearPanel() {
        financialYearLabel = new JLabel("Year");
        yearComboBox = new YearComboBox();

        financialYearValue = 2024;

        this.add(financialYearLabel);
        this.add(yearComboBox);
    }

    public class YearComboBox extends JComboBox {
        public YearComboBox() {
//            this.addItem(2023);
            this.addItem(2024);
            this.addItem(2025);

            this.addActionListener(e -> {
                financialYearValue = (int) this.getSelectedItem();
            });
        }

        public void setValue(int year) {
//            this.;
        }
    }
}
