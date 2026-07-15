package ro.ugal.aciee.bettingagency.ui.panels.player;

import ro.ugal.aciee.bettingagency.model.Bet;
import ro.ugal.aciee.bettingagency.model.Match;
import ro.ugal.aciee.bettingagency.model.Rate;
import ro.ugal.aciee.bettingagency.model.Team;
import ro.ugal.aciee.bettingagency.service.*;
import ro.ugal.aciee.bettingagency.ui.BetSlip;
import ro.ugal.aciee.bettingagency.ui.Session;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RatePanel extends JFrame {
    private final BetService betService = new BetService();
    private final AccountService accountService = new AccountService();
    private final JPanel ratesPanel;
    private final TeamService teamService = new TeamService();
    private final MatchService matchService = new MatchService();
    private final RateService rateService = new RateService();
    private final JLabel totalOddsLabel;
    private final JLabel totalAmountLabel;
    private final JTextField amountField;

    public RatePanel() {
        setTitle("Coupon");
        setSize(450, 400);
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        ratesPanel = new JPanel();
        ratesPanel.setLayout(new BoxLayout(ratesPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(ratesPanel);
        add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        amountField = new JTextField(10);
        totalOddsLabel = new JLabel("Total Odds: 0.00");
        totalAmountLabel = new JLabel("Win possible: 0.00");

        amountField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateRatesDisplay();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateRatesDisplay();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateRatesDisplay();
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 0;
        bottomPanel.add(new JLabel("Bet amount:"), gbc);

        gbc.gridx = 1;
        bottomPanel.add(amountField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        bottomPanel.add(totalOddsLabel, gbc);

        gbc.gridy = 2;
        bottomPanel.add(totalAmountLabel, gbc);

        gbc.gridy = 3;
        JButton cancelButton = new JButton("Clear All");
        cancelButton.addActionListener(e -> {
            BetSlip.clear();
            updateRatesDisplay();
        });
        bottomPanel.add(cancelButton, gbc);

        gbc.gridx = 1;
        JButton placeBetButton = new JButton("Create Bet");
        placeBetButton.addActionListener(e -> placeBet());
        bottomPanel.add(placeBetButton, gbc);

        add(bottomPanel, BorderLayout.SOUTH);

        updateRatesDisplay();
    }

    private JPanel createRatePanel(Rate rate) throws SQLException {
        JPanel ratePanel = new JPanel(new BorderLayout());
        ratePanel.setBorder(BorderFactory.createLineBorder(Color.CYAN));
        ratePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        Match match = matchService.getById(rate.getMatchId());
        Team team1 = teamService.getById(match.getTeam1Id());
        Team team2 = teamService.getById(match.getTeam2Id());

        JLabel info = new JLabel(team1.getTeamName() +
                " VS " + team2.getTeamName() +
                " | " + rate.getType());
        JLabel valueLabel = new JLabel(String.valueOf(rate.getValue()));

        JButton clearRateButton = new JButton("clear");
        clearRateButton.addActionListener(e -> {
            BetSlip.remove(rate.getRateId());
            updateRatesDisplay();
        });

        JPanel middle = new JPanel(new FlowLayout(FlowLayout.LEFT));
        middle.add(clearRateButton);

        ratePanel.add(info, BorderLayout.WEST);
        ratePanel.add(middle, BorderLayout.CENTER);
        ratePanel.add(valueLabel, BorderLayout.EAST);

        return ratePanel;
    }

    private void updateRatesDisplay() {
        try {
            List<Rate> rateList = new ArrayList<>();
            for (int id : BetSlip.getRateIdList()) {
                rateList.add(rateService.getById(id));
            }

            ratesPanel.removeAll();

            double totalOdds = 1.0;
            for (Rate rate : rateList) {
                ratesPanel.add(createRatePanel(rate));
                ratesPanel.add(Box.createVerticalStrut(5));
                totalOdds *= rate.getValue();
            }
            double amount = 0.0;
            try {
                amount = Double.parseDouble(amountField.getText().trim());
            } catch (NumberFormatException ignored) {

            }
            double winPossible = amount * totalOdds;

            totalOddsLabel.setText("Total Odds: " + String.format("%.2f", totalOdds));
            totalAmountLabel.setText("Win possible: " + String.format("%.2f", winPossible));

            ratesPanel.revalidate();
            ratesPanel.repaint();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void placeBet() {
        try {
            double amount = Double.parseDouble(amountField.getText());
            Bet bet = betService.save(Session.getCurrentUser().getUserId(), amount, BetSlip.getRateIdList());

            Session.login(accountService.getById(bet.getUserId()));

            BetSlip.clear();
            updateRatesDisplay();
            JOptionPane.showMessageDialog(this, "Bet placed successfully");
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
        updateRatesDisplay();
        SwingUtilities.invokeLater(() -> setVisible(true));
    }
}
