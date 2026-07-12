package ro.ugal.aciee.bettingagency.ui.dialogs;

import ro.ugal.aciee.bettingagency.service.AccountService;
import ro.ugal.aciee.bettingagency.ui.Session;

import javax.swing.*;
import java.awt.*;

public class DepositDialog extends JDialog {
    private final AccountService accountService = new AccountService();
    private final JTextField depositField;

    public DepositDialog(JFrame parent) {
        super(parent, "Deposit", true);

        setSize(240, 300);
        setLocationRelativeTo(parent);

        JPanel depositPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        depositField = new JTextField(10);

        gbc.gridx = 0;
        gbc.gridy = 0;
        depositPanel.add(new JLabel("Deposit: "), gbc);

        gbc.gridx = 1;
        depositPanel.add(depositField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> dispose());
        depositPanel.add(cancelButton, gbc);

        gbc.gridx = 1;
        JButton replenishButton = new JButton("Replenish");
        replenishButton.addActionListener(e -> replenishBalance());
        depositPanel.add(replenishButton, gbc);

        add(depositPanel);
    }

    private void replenishBalance() {
        try {
            accountService.deposit(Session.getCurrentUser().getUserId(),
                    Double.parseDouble(depositField.getText()));
            Session.login(accountService.getById(Session.getCurrentUser().getUserId()));
            JOptionPane.showMessageDialog(this, "The replenishment was successful");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Invalid deposit: " + e.getMessage(),
                    "Deposit Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
