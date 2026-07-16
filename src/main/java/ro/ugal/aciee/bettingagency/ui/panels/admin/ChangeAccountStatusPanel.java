package ro.ugal.aciee.bettingagency.ui.panels.admin;

import ro.ugal.aciee.bettingagency.model.Account;
import ro.ugal.aciee.bettingagency.model.enums.AccountStatus;
import ro.ugal.aciee.bettingagency.service.AccountService;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ChangeAccountStatusPanel extends JFrame {
    private final AccountService accountService = new AccountService();
    private final JPanel accountsPanel;

    public ChangeAccountStatusPanel() {
        setTitle("Manager Account Status");
        setSize(380, 400);
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        accountsPanel = new JPanel();
        accountsPanel.setLayout(new BoxLayout(accountsPanel, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(accountsPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(24);
        add(scrollPane, BorderLayout.CENTER);
    }


    private JPanel createAccountPanel(Account account) {
        JPanel accountPanel = new JPanel(new BorderLayout());
        accountPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        accountPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        JLabel userLabel = new JLabel("Username: " + account.getUsername());

        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusPanel.add(new JLabel("Account status: "));
        JLabel statusLabel = new JLabel(String.valueOf(account.getAccountStatus()));
        if (account.getAccountStatus() == AccountStatus.ACTIVE) {
            statusLabel.setForeground(Color.GREEN);
        } else {
            statusLabel.setForeground(Color.RED);
        }
        statusPanel.add(statusLabel);

        JButton changeStatus = new JButton("Change status");
        changeStatus.addActionListener(e -> changeStatusByUser(account));

        accountPanel.add(userLabel, BorderLayout.NORTH);
        accountPanel.add(statusPanel, BorderLayout.CENTER);
        accountPanel.add(changeStatus, BorderLayout.EAST);

        return accountPanel;
    }

    private void updateAccountDisplay(List<Account> accounts) {
        try {
            accountsPanel.removeAll();

            for (Account account : accounts) {
                accountsPanel.add(createAccountPanel(account));
            }

            accountsPanel.revalidate();
            accountsPanel.repaint();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void changeStatusByUser(Account account) {
        try {
            if (account.getAccountStatus() == AccountStatus.ACTIVE) {
                accountService.updateStatus(account.getUserId(), AccountStatus.BANNED);
            } else {
                accountService.updateStatus(account.getUserId(), AccountStatus.ACTIVE);
            }

            updateAccountDisplay(accountService.getAll());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    public void launch() {
        try {
            updateAccountDisplay(accountService.getAll());
            SwingUtilities.invokeLater(() -> setVisible(true));
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
