package ro.ugal.aciee.bettingagency.ui.dialogs;

import ro.ugal.aciee.bettingagency.model.Account;
import ro.ugal.aciee.bettingagency.service.AccountService;
import ro.ugal.aciee.bettingagency.ui.Session;

import javax.swing.*;
import java.awt.*;

public class LoginDialog extends JDialog {
    private final AccountService accountService = new AccountService();
    private final JTextField usernameField;
    private final JPasswordField passwordField;

    public LoginDialog(JFrame parent) {

        super(parent, "Login", true);

        setSize(300, 200);
        setLocationRelativeTo(parent);

        JPanel loginPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        usernameField = new JTextField(10);
        passwordField = new JPasswordField(10);

        gbc.gridx = 0;
        gbc.gridy = 0;
        loginPanel.add(new JLabel("Username"), gbc);

        gbc.gridy = 1;
        loginPanel.add(new JLabel("Password"), gbc);

        gbc.gridy = 2;
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> dispose());
        loginPanel.add(cancelButton, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        loginPanel.add(usernameField, gbc);

        gbc.gridy = 1;
        loginPanel.add(passwordField, gbc);

        gbc.gridy = 2;
        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(e -> loginCheck());
        loginPanel.add(loginButton, gbc);

        add(loginPanel);
    }

    private void loginCheck() {
        try {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            Account account = accountService.login(username, password);
            Session.login(account);

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
