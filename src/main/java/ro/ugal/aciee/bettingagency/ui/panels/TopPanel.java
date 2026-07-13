package ro.ugal.aciee.bettingagency.ui.panels;

import ro.ugal.aciee.bettingagency.ui.Session;
import ro.ugal.aciee.bettingagency.ui.dialogs.*;

import javax.swing.*;
import java.awt.*;

public class TopPanel extends JPanel {
    private final JFrame parentFrame;
    private final JButton loginButton;
    private final JButton registerButton;
    private final JButton depositButton;
    private final JButton statisticButton;
    private final JLabel userLabel;
    private final JLabel userBalanceLabel;

    public TopPanel(JFrame parentFrame) {
        this.parentFrame = parentFrame;

        setLayout(new FlowLayout(FlowLayout.RIGHT));

        loginButton = new JButton("Login");
        registerButton = new JButton("Register");
        depositButton = new JButton("Deposit");
        statisticButton = new JButton("Statistic");
        userLabel = new JLabel();
        userBalanceLabel = new JLabel();

        add(loginButton);
        add(registerButton);

        loginButton.addActionListener(e -> {
            LoginDialog dialog = new LoginDialog(parentFrame);
            dialog.setVisible(true);
            refreshState();
            updateMenuBar();
        });

        registerButton.addActionListener(e -> {
            RegisterDialog dialog = new RegisterDialog(parentFrame);
            dialog.setVisible(true);
            refreshState();
        });

        depositButton.addActionListener(e -> {
            DepositDialog dialog = new DepositDialog(parentFrame);
            dialog.setVisible(true);
            refreshState();
        });

        statisticButton.addActionListener(e -> {
            StatisticAccountDialog dialog = new StatisticAccountDialog(parentFrame);
            dialog.setVisible(true);
            refreshState();
        });

        Session.setOnChange(() -> {
            refreshState();
            updateMenuBar();
        });
        refreshState();
        updateMenuBar();
    }

    private void updateMenuBar() {
        if (Session.isLoggedIn()) {
            JMenuBar menuBar = new JMenuBar();
            JMenu settings = new JMenu("Settings");

            JMenuItem changeUsername = new JMenuItem("Change username");
            changeUsername.addActionListener(e -> {
                ChangeUsernameDialog dialog = new ChangeUsernameDialog(parentFrame);
                dialog.setVisible(true);
                dialog.dispose();
            });

            JMenuItem changePassword = new JMenuItem("Change password");
            changePassword.addActionListener(e -> {
                ChangePasswordDialog dialog = new ChangePasswordDialog(parentFrame);
                dialog.setVisible(true);
                dialog.dispose();
            });

            JMenuItem logout = new JMenuItem("Logout");
            logout.addActionListener(e -> {
                Session.logout();
                JOptionPane.showMessageDialog(this, "Successful account logout");
                refreshState();
                updateMenuBar();
            });

            settings.add(changeUsername);
            settings.add(changePassword);
            settings.addSeparator();
            settings.add(logout);

            menuBar.add(settings);
            parentFrame.setJMenuBar(menuBar);
        } else {
            parentFrame.setJMenuBar(null);
        }

        parentFrame.revalidate();
        parentFrame.repaint();
    }

    private void refreshState() {
        removeAll();
        if (Session.isLoggedIn()) {
            userLabel.setText(Session.getCurrentUser().getUsername());
            userBalanceLabel.setText(String.valueOf(Session.getCurrentUser().getBalance()));
            add(userLabel);
            add(userBalanceLabel);
            add(depositButton);
            add(statisticButton);
        } else {
            add(loginButton);
            add(registerButton);
        }
        revalidate();
        repaint();
    }
}
