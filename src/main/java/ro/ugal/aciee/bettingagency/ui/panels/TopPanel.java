package ro.ugal.aciee.bettingagency.ui.panels;

import ro.ugal.aciee.bettingagency.model.Account;
import ro.ugal.aciee.bettingagency.model.enums.Role;
import ro.ugal.aciee.bettingagency.ui.Session;
import ro.ugal.aciee.bettingagency.ui.dialogs.*;
import ro.ugal.aciee.bettingagency.ui.dialogs.player.DepositDialog;
import ro.ugal.aciee.bettingagency.ui.dialogs.player.StatisticAccountDialog;
import ro.ugal.aciee.bettingagency.utils.database.ImportAndExportManager;
import ro.ugal.aciee.bettingagency.utils.database.DatabaseManager;

import javax.swing.*;
import java.awt.*;
import java.io.File;

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
                refreshState();
            });

            JMenuItem logout = new JMenuItem("Logout");
            logout.addActionListener(e -> {
                Session.logout();
                JOptionPane.showMessageDialog(this, "Successful account logout");
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

    }

    private void loadData() {
        try {
            JFileChooser chooser = new JFileChooser();

            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                File file = chooser.getSelectedFile();

                DatabaseManager.drop();
                DatabaseManager.initialize();
                ImportAndExportManager.importExcelFile(file);

                JOptionPane.showMessageDialog(this, "Date loaded successfully");
            }
        } catch (Exception e){
            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage(),
                    "Import error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
