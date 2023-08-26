package src.Panels;

import javax.swing.*;

public class TitlePanel extends JPanel {
    JLabel titleLabel;
    public TitlePanel() {

        titleLabel = new JLabel("One Stop Shop for Excel Shenanigans");
        this.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        this.add(titleLabel);
    }
}
