package ro.ugal.aciee.bettingagency.ui.panels;

import ro.ugal.aciee.bettingagency.model.Account;
import ro.ugal.aciee.bettingagency.model.enums.Role;
import ro.ugal.aciee.bettingagency.service.AccountService;
import ro.ugal.aciee.bettingagency.ui.Session;
import ro.ugal.aciee.bettingagency.ui.dialogs.ChangePasswordDialog;
import ro.ugal.aciee.bettingagency.ui.dialogs.ChangeUsernameDialog;
import ro.ugal.aciee.bettingagency.ui.dialogs.LoginDialog;
import ro.ugal.aciee.bettingagency.ui.dialogs.RegisterDialog;
import ro.ugal.aciee.bettingagency.ui.dialogs.player.DepositDialog;
import ro.ugal.aciee.bettingagency.ui.dialogs.player.StatisticAccountDialog;
import ro.ugal.aciee.bettingagency.utils.database.DatabaseManager;
import ro.ugal.aciee.bettingagency.utils.database.ImportAndExportManager;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class TopPanel extends JPanel {
    private final AccountService accountService = new AccountService();
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

        Session.addListener(() -> {
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

            JMenuItem refresh = new JMenuItem("Refresh");
            refresh.addActionListener(e -> {
                try {
                    Session.login(accountService.getById(Session.getCurrentUser().getUserId()));
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, ex.getMessage());
                }
            });

            JMenuItem logout = new JMenuItem("Logout");
            logout.addActionListener(e -> {
                Session.logout();

                JOptionPane.showMessageDialog(
                        this,
                        "Successful account logout",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);

                refreshState();
                updateMenuBar();
            });

            if (Session.getCurrentUser().getRole() == Role.ADMIN) {
                JMenu loadAndSave = new JMenu("Load & Save");

                JMenuItem save = new JMenuItem("Save");
                save.addActionListener(e -> saveDate());

                JMenuItem load = new JMenuItem("Load");
                load.addActionListener(e -> loadData());

                loadAndSave.add(save);
                loadAndSave.add(load);

                menuBar.add(loadAndSave);
            }

            settings.add(changeUsername);
            settings.add(changePassword);
            settings.add(refresh);
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
            Account user = Session.getCurrentUser();
            userLabel.setText(user.getUsername());
            add(userLabel);

            if (user.getRole() == Role.PLAYER) {
                userBalanceLabel.setText(String.valueOf(user.getBalance()));
                add(userBalanceLabel);
                add(depositButton);
                add(statisticButton);
            }
        } else {
            add(loginButton);
            add(registerButton);
        }
        revalidate();
        repaint();
    }

    private void saveDate() {

        JFileChooser chooser = new JFileChooser();

        if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {

            String filePath = chooser.getSelectedFile().getAbsolutePath().trim();

            if (!filePath.endsWith(".xlsx")) {
                filePath += ".xlsx";
            }

            if (ImportAndExportManager.exportExcelFile(filePath)) {
                JOptionPane.showMessageDialog(
                        this,
                        "Data saved successfully",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }
        }

    }

    private void loadData() {
        JFileChooser chooser = new JFileChooser();

        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();

            if (!DatabaseManager.delete()) {
                JOptionPane.showMessageDialog(
                        this,
                        "Date not delete",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            if (ImportAndExportManager.importExcelFile(file)) {
                if (!DatabaseManager.resetSequences()) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Sequences not reset",
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }
                JOptionPane.showMessageDialog(
                        this,
                        "Date loaded successfully",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "Date not load",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }
}

