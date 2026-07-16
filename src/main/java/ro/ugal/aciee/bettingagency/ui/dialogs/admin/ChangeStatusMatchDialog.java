package ro.ugal.aciee.bettingagency.ui.dialogs.admin;

import ro.ugal.aciee.bettingagency.model.Bet;
import ro.ugal.aciee.bettingagency.model.Rate;
import ro.ugal.aciee.bettingagency.model.enums.BetStatus;
import ro.ugal.aciee.bettingagency.model.enums.MatchStatus;
import ro.ugal.aciee.bettingagency.model.enums.RateStatus;
import ro.ugal.aciee.bettingagency.service.BetService;
import ro.ugal.aciee.bettingagency.service.MatchService;
import ro.ugal.aciee.bettingagency.service.RateService;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ChangeStatusMatchDialog extends JDialog {
    private final Runnable onSuccess;
    private final JComboBox<String> statusBox;
    private final int matchId;
    private final MatchService matchService = new MatchService();
    private final BetService betService = new BetService();
    private final RateService rateService = new RateService();

    public ChangeStatusMatchDialog(int matchId, Runnable onSuccess) {
        setTitle("Change status match");
        this.matchId = matchId;
        this.onSuccess = onSuccess;

        setSize(250, 150);
        setLocationRelativeTo(null);

        JPanel statusPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        statusPanel.add(new JLabel("Status: "), gbc);

        gbc.gridy = 1;
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> dispose());
        statusPanel.add(cancelButton, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        String[] status = {"UPCOMING", "LIVE", "FINISHED", "CANCELLED"};
        statusBox = new JComboBox<>(status);
        statusPanel.add(statusBox, gbc);

        gbc.gridy = 1;
        JButton apply = new JButton("Apply");
        apply.addActionListener(e -> applyStatus());
        statusPanel.add(apply, gbc);

        add(statusPanel);
    }

    private void applyStatus() {
        try {
            MatchStatus matchStatus = MatchStatus.valueOf(String.valueOf(statusBox.getSelectedItem()));

            matchService.updateStatus(matchStatus, matchId);

            if (matchStatus == MatchStatus.FINISHED) {
                matchService.getWinnerMatch(matchId);
            }

            if (matchStatus == MatchStatus.FINISHED || matchStatus == MatchStatus.CANCELLED) {
                List<Bet> betMatchList = betService.getAllBetByMatchId(matchId);
                List<Rate> rateList = rateService.getByMatchId(matchId);

                for (Rate rate : rateList) {
                    rateService.updateStatus(rate.getRateId(), RateStatus.CLOSED);
                }

                for (Bet bet : betMatchList) {
                    if (bet.getBetStatus() == BetStatus.PENDING) {
                        betService.settleBet(bet.getBetId());
                    }
                }
            }

            onSuccess.run();
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
