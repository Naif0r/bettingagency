package ro.ugal.aciee.bettingagency.ui.dialogs;

import ro.ugal.aciee.bettingagency.model.Account;
import ro.ugal.aciee.bettingagency.model.enums.Role;
import ro.ugal.aciee.bettingagency.service.AccountService;

import javax.swing.*;
import java.awt.*;

public class RegisterDialog extends JDialog {
    private final AccountService accountService = new AccountService();
    private final JTextField usernameField;
    private final JPasswordField passwordField;
    private final JRadioButton player;
    private final JRadioButton admin;
    private final ButtonGroup group;

    public RegisterDialog(JFrame parent) {
        super(parent, "Register", true);

        setSize(300, 200);
        setLocationRelativeTo(parent);

        JPanel registerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        usernameField = new JTextField(10);
        passwordField = new JPasswordField(10);
        player = new JRadioButton(String.valueOf(Role.PLAYER));
        admin = new JRadioButton(String.valueOf(Role.ADMIN));
        player.setSelected(true);
        group = new ButtonGroup();
        group.add(player);
        group.add(admin);

        gbc.gridx = 0;
        gbc.gridy = 0;
        registerPanel.add(new JLabel("Username"), gbc);

        gbc.gridy = 1;
        registerPanel.add(new JLabel("Password"), gbc);

        gbc.gridy = 2;
        registerPanel.add(player, gbc);

        gbc.gridy = 3;
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> dispose());
        registerPanel.add(cancelButton, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        registerPanel.add(usernameField, gbc);

        gbc.gridy = 1;
        registerPanel.add(passwordField, gbc);

        gbc.gridy = 2;
        registerPanel.add(admin, gbc);

        gbc.gridy = 3;
        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(e -> registerCheck());
        registerPanel.add(registerButton, gbc);

        add(registerPanel);
    }

    private void registerCheck() {
        try {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            Role role;
            if (player.isSelected()) {
                role = Role.PLAYER;
            } else if (admin.isSelected()) {
                role = Role.ADMIN;
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "Select a role",
                        "Register Error",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }
            Account account = accountService.register(username, password, role);
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
