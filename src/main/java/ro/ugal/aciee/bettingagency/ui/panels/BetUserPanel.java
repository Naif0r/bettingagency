package ro.ugal.aciee.bettingagency.ui.panels;

import ro.ugal.aciee.bettingagency.model.Bet;
import ro.ugal.aciee.bettingagency.model.Match;
import ro.ugal.aciee.bettingagency.model.Rate;
import ro.ugal.aciee.bettingagency.model.Team;
import ro.ugal.aciee.bettingagency.model.enums.BetStatus;
import ro.ugal.aciee.bettingagency.model.enums.MatchStatus;
import ro.ugal.aciee.bettingagency.service.BetService;
import ro.ugal.aciee.bettingagency.service.MatchService;
import ro.ugal.aciee.bettingagency.service.RateService;
import ro.ugal.aciee.bettingagency.service.TeamService;
import ro.ugal.aciee.bettingagency.ui.Session;

import javax.swing.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


public class BetUserPanel extends JPanel {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("d.M.yyyy, HH:mm");
    private final BetService betService = new BetService();
    private final MatchService matchService = new MatchService();
    private final RateService rateService = new RateService();
    private final TeamService teamService = new TeamService();
    private final JPanel centerBetsPanel;
    private List<Bet> betsByStatus = new ArrayList<>();

    public BetUserPanel() {
        setLayout(new BorderLayout());

        centerBetsPanel = new JPanel();
        centerBetsPanel.setLayout(new GridLayout(0, 6, 15, 15));

        JScrollPane scrollPane = new JScrollPane(centerBetsPanel);
        add(createBetsTagsPanel(), BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel createBetsTagsPanel() {
        JPanel betsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        betsPanel.setBorder(BorderFactory.createTitledBorder("My Bets"));

        try {
            for (int i = 0; i < BetStatus.values().length; i++) {
                BetStatus betStatus = BetStatus.values()[i];
                JButton button = new JButton(String.valueOf(betStatus));
                button.addActionListener(e -> {
                    try {
                        betsByStatus = betService.getByStatus(betStatus, Session.getCurrentUser().getUserId());
                        updateBetsDisplay();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, ex.getMessage());
                    }
                });
                betsPanel.add(button);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
        return betsPanel;
    }

    public void updateBetsDisplay() {
        try {
            centerBetsPanel.removeAll();
            centerBetsPanel.setLayout(new BoxLayout(centerBetsPanel, BoxLayout.Y_AXIS));

            int perRow = 6;
            JPanel currentRow = null;

            for (int i = 0; i < betsByStatus.size(); i++) {
                if (i % perRow == 0) {
                    currentRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
                    centerBetsPanel.add(currentRow);
                }
                currentRow.add(createBetPanel(betsByStatus.get(i)));
            }

            centerBetsPanel.revalidate();
            centerBetsPanel.repaint();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private JPanel createBetPanel(Bet bet) {
        JPanel betPanel = new JPanel(new BorderLayout());
        betPanel.setBorder(BorderFactory.createLineBorder(Color.GREEN));
        betPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

        try {

            JPanel matchesInfo = new JPanel();
            matchesInfo.setLayout(new BoxLayout(matchesInfo, BoxLayout.Y_AXIS));

            List<Rate> rateList = betService.getRateByBetId(bet.getBetId());

            for (Rate rate : rateList) {
                Match match = matchService.getById(rate.getMatchId());
                Team team1 = teamService.getById(match.getTeam1Id());
                Team team2 = teamService.getById(match.getTeam2Id());

                String winnerInfo = match.getMatchStatus() == MatchStatus.FINISHED
                        ? String.valueOf(matchService.getWinnerMatch(match.getMatchId()))
                        : match.getMatchStatus().toString();

                JLabel mInfo = new JLabel("<html>" + team1.getTeamName() + " " + match.getTeam1Score() +
                        " : " +
                        match.getTeam2Score() + " " + team2.getTeamName() +
                        " | " + winnerInfo + "<br>" +
                        "Winner: " + rate.getType() + "</html>");
                matchesInfo.add(mInfo);
            }
            JLabel betInfo = new JLabel("<html>" +
                    "Betting time: " + bet.getCreatedAt().format(DATE_TIME_FORMATTER) + "<br>" +
                    "Amount: " + bet.getAmount() + "<br>" +
                    "Odds: " + bet.getTotalOdds() + "<br>" +
                    "Status: " + bet.getBetStatus() +
                    "</html>");

            betPanel.add(matchesInfo, BorderLayout.CENTER);
            betPanel.add(betInfo, BorderLayout.SOUTH);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
        return betPanel;
    }
}
