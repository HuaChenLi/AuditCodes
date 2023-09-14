package src.Panels;

import javax.swing.*;
import java.awt.*;

public class FinancialYearPanel extends JPanel {
    JLabel financialYearLabel, financialQuarterLabel;
    JButton financialYearButton, financialQuarterButton;
    JTextField financialYearText, financialQuarterText;
    static int financialYearValue, financialQuarterValue;
    public FinancialYearPanel() {
        this.setLayout(new GridLayout(0,3));

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


        financialQuarterLabel = new JLabel("Quarter");
        financialQuarterText = new JTextField();
        financialQuarterButton = new JButton("Set Quarter");

        financialQuarterButton.addActionListener(e1 -> {
            String tempString = financialQuarterText.getText();
            try {
                financialQuarterValue = Integer.parseInt(tempString);
                financialQuarterLabel.setText("Quarter " + financialQuarterValue);
            } catch (Exception e){
                e.printStackTrace();
            }
            financialQuarterText.setText("");

            this.revalidate();
            this.validate();
        });

        this.add(financialYearLabel);
        this.add(financialYearText);
        this.add(financialYearButton);

        this.add(financialQuarterLabel);
        this.add(financialQuarterText);
        this.add(financialQuarterButton);

    }

}
