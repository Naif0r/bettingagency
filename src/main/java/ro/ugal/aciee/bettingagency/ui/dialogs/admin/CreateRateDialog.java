package ro.ugal.aciee.bettingagency.ui.dialogs.admin;

import ro.ugal.aciee.bettingagency.model.enums.RateType;
import ro.ugal.aciee.bettingagency.service.RateService;

import javax.swing.*;
import java.awt.*;

public class CreateRateDialog extends JDialog {
    private final RateService rateService = new RateService();
    private final JTextField rateValueField;
    private final JComboBox<String> typeBox;
    private int completed = 0;

    public CreateRateDialog(int matchId) {
        setTitle("Create rate");

        setSize(200, 140);
        setLocationRelativeTo(null);

        JPanel ratePanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        rateValueField = new JTextField(10);
        String[] type = {"WIN1", "DRAW", "WIN2"};
        typeBox = new JComboBox<>(type);

        gbc.gridx = 0;
        gbc.gridy = 0;
        ratePanel.add(new JLabel("Rate value: "), gbc);

        gbc.gridy = 1;
        ratePanel.add(new JLabel("Rate type: "), gbc);

        gbc.gridy = 2;
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> dispose());
        ratePanel.add(cancelButton, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        ratePanel.add(rateValueField, gbc);

        gbc.gridy = 1;
        ratePanel.add(typeBox, gbc);

        gbc.gridy = 2;
        JButton loginButton = new JButton("Apply");
        loginButton.addActionListener(e -> applyCheck(matchId));
        ratePanel.add(loginButton, gbc);

        add(ratePanel);
    }

    private void applyCheck(int matchId) {
        try {
            double value = Double.parseDouble(rateValueField.getText().trim());

            RateType rateType = RateType.valueOf(String.valueOf(typeBox.getSelectedItem()));

            rateService.save(matchId, rateType, value);
            completed++;

            rateValueField.setText("");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    public boolean isCompleted() {
        return completed >= 2;
    }
}
