package ro.ugal.aciee.bettingagency.ui.dialogs;

import ro.ugal.aciee.bettingagency.service.AccountService;
import ro.ugal.aciee.bettingagency.ui.Session;

import javax.swing.*;
import java.awt.*;

public class ChangeUsernameDialog extends JDialog {
    private final AccountService accountService = new AccountService();
    private final JTextField newUsername;

    public ChangeUsernameDialog(JFrame parent) {
        super(parent, "Change username", true);

        setSize(300, 200);
        setLocationRelativeTo(parent);

        newUsername = new JTextField(10);

        JPanel changePanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        changePanel.add(new JLabel("Enter new username: "), gbc);

        gbc.gridx = 1;
        changePanel.add(newUsername, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> dispose());
        changePanel.add(cancelButton, gbc);

        gbc.gridx = 1;
        JButton applyButton = new JButton("Apply");
        applyButton.addActionListener(e -> changeName());
        changePanel.add(applyButton, gbc);

        add(changePanel);
    }

    private void changeName() {
        try {
            String newName = newUsername.getText();
            int userId = Session.getCurrentUser().getUserId();
            accountService.updateUsername(newName, userId);
            Session.login(accountService.getById(userId));
            JOptionPane.showMessageDialog(this, "Username changed successfully");
            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
}
