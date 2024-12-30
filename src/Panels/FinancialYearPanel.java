package src.Panels;

import javax.swing.*;
import java.awt.*;
import java.time.Year;

public class FinancialYearPanel extends JPanel {
    JLabel financialYearLabel;
    YearComboBox yearComboBox;

    static int financialYearValue, financialQuarterValue;
    public FinancialYearPanel() {
        this.setLayout(new GridLayout(0,2));

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
