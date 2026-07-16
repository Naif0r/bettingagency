package ro.ugal.aciee.bettingagency.ui.dialogs.player;

import ro.ugal.aciee.bettingagency.model.Account;
import ro.ugal.aciee.bettingagency.model.Bet;
import ro.ugal.aciee.bettingagency.model.enums.AccountStatus;
import ro.ugal.aciee.bettingagency.model.enums.BetStatus;
import ro.ugal.aciee.bettingagency.service.AccountService;
import ro.ugal.aciee.bettingagency.service.BetService;
import ro.ugal.aciee.bettingagency.ui.Session;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class StatisticAccountDialog extends JDialog {
    private final JLabel winBetLabel;
    private final JLabel loseBetLabel;
    private final JLabel status;
    private Account account;
    private final BetService betService = new BetService();
    private final AccountService accountService = new AccountService();

    public StatisticAccountDialog(JFrame parent) {
        super(parent, "Account statistic", true);

        setSize(240, 100);
        setLocationRelativeTo(parent);

        JPanel statisticPanel = new JPanel(new BorderLayout());
        statisticPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        statisticPanel.setPreferredSize(new Dimension(200, 80));

        winBetLabel = new JLabel();
        loseBetLabel = new JLabel();
        status = new JLabel();

        refreshAccount();
        betStatus();

        JPanel top = new JPanel(new BorderLayout());
        top.add(new JLabel("Username: " + account.getUsername()), BorderLayout.WEST);
        top.add(new JLabel("Bet statistic"), BorderLayout.EAST);

        JPanel center = new JPanel(new BorderLayout());
        center.add(new JLabel("Balance: " + account.getBalance() + "$"), BorderLayout.WEST);

        JPanel betStatPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 2, 0));
        betStatPanel.add(winBetLabel);
        betStatPanel.add(new JLabel("/"));
        betStatPanel.add(loseBetLabel);
        center.add(betStatPanel, BorderLayout.EAST);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 0));
        bottom.add(new JLabel("Account status: "));
        bottom.add(status);

        statisticPanel.add(top, BorderLayout.NORTH);
        statisticPanel.add(center, BorderLayout.CENTER);
        statisticPanel.add(bottom, BorderLayout.SOUTH);

        add(statisticPanel);
    }

    private void betStatus() {
        try {
            List<Bet> betWinList = betService.getByStatus(BetStatus.WON, account.getUserId());
            int winCount = betWinList.size();
            winBetLabel.setText(String.valueOf(winCount));
            winBetLabel.setForeground(Color.GREEN);

            List<Bet> betLoseList = betService.getByStatus(BetStatus.LOST, account.getUserId());
            int loseCount = betLoseList.size();
            loseBetLabel.setText(String.valueOf(loseCount));
            loseBetLabel.setForeground(Color.RED);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void refreshAccount() {
        try {
            account = accountService.getById(Session.getCurrentUser().getUserId());
            Session.refreshCurrentUser(account);

            status.setText(account.getAccountStatus().toString());

            if (account.getAccountStatus() == AccountStatus.ACTIVE){
                status.setForeground(Color.GREEN);
            } else {
                status.setForeground(Color.RED);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
