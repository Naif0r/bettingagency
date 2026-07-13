package ro.ugal.aciee.bettingagency.ui.dialogs;

import ro.ugal.aciee.bettingagency.model.Account;
import ro.ugal.aciee.bettingagency.model.Bet;
import ro.ugal.aciee.bettingagency.model.enums.BetStatus;
import ro.ugal.aciee.bettingagency.service.BetService;
import ro.ugal.aciee.bettingagency.ui.Session;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class StatisticAccountDialog extends JDialog {
    private final JLabel winBetLabel;
    private final JLabel loseBetLabel;
    private final Account account;
    private final BetService betService = new BetService();

    public StatisticAccountDialog(JFrame parent) {
        super(parent, "Account statistic", true);

        setSize(240, 100);
        setLocationRelativeTo(parent);

        JPanel statisticPanel = new JPanel(new BorderLayout());
        statisticPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        statisticPanel.setPreferredSize(new Dimension(200, 80));

        account = Session.getCurrentUser();
        winBetLabel = new JLabel();
        loseBetLabel = new JLabel();
        betStatus();

        JPanel top = new JPanel(new BorderLayout());
        top.add(new JLabel("Username: " + account.getUsername()), BorderLayout.WEST);
        top.add(new JLabel("Bet statistic"), BorderLayout.EAST);

        JPanel center = new JPanel(new BorderLayout());
        center.add(new JLabel("Balance: " + account.getBalance()), BorderLayout.WEST);

        JPanel betStatPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 2, 0));
        betStatPanel.add(winBetLabel);
        betStatPanel.add(new JLabel("/"));
        betStatPanel.add(loseBetLabel);
        center.add(betStatPanel, BorderLayout.EAST);

        statisticPanel.add(top, BorderLayout.NORTH);
        statisticPanel.add(center, BorderLayout.CENTER);

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
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
}
