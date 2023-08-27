package src.Panels;

import javax.swing.*;
import java.awt.*;

public class FinancialYearPanel extends JPanel {
    JLabel financialYearLabel;
    JButton financialYearButton;
    JTextField financialYearText;
    int financialYearValue;
    public FinancialYearPanel() {
        this.setLayout(new GridLayout());

        financialYearLabel = new JLabel("Financial Year");
        financialYearText = new JTextField();
        financialYearButton = new JButton("Set Financial Year");

        financialYearButton.addActionListener(e1 -> {
            String tempString = financialYearText.getText();
            try {
                financialYearValue = Integer.parseInt(tempString);
                financialYearLabel.setText("Financial Year " + financialYearValue);
            } catch (Exception e){
                e.printStackTrace();
            }
            financialYearText.setText("");


            this.revalidate();
            this.validate();
        });

        this.add(financialYearLabel);
        this.add(financialYearText);
        this.add(financialYearButton);

    }

}
