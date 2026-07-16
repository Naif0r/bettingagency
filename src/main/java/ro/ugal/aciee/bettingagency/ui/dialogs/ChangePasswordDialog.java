package ro.ugal.aciee.bettingagency.ui.dialogs;

import ro.ugal.aciee.bettingagency.service.AccountService;
import ro.ugal.aciee.bettingagency.ui.Session;

import javax.swing.*;
import java.awt.*;

public class ChangePasswordDialog extends JDialog {
    private final AccountService accountService = new AccountService();
    private final JPasswordField newPasswordField;

    public ChangePasswordDialog(JFrame parent) {
        super(parent, "Change password", true);

        setSize(300, 200);
        setLocationRelativeTo(parent);

        newPasswordField = new JPasswordField(10);

        JPanel changePanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        changePanel.add(new JLabel("Enter new password: "), gbc);

        gbc.gridx = 1;
        changePanel.add(newPasswordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> dispose());
        changePanel.add(cancelButton, gbc);

        gbc.gridx = 1;
        JButton applyButton = new JButton("Apply");
        applyButton.addActionListener(e -> changePass());
        changePanel.add(applyButton, gbc);

        add(changePanel);
    }

    private void changePass() {
        try {
            String newPass = newPasswordField.getText();
            int userId = Session.getCurrentUser().getUserId();

            accountService.updatePassword(newPass, userId);
            Session.refreshCurrentUser(accountService.getById(Session.getCurrentUser().getUserId()));

            JOptionPane.showMessageDialog(this, "Password changed successfully");

            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
