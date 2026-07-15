package ro.ugal.aciee.bettingagency.ui.panels.player;

import ro.ugal.aciee.bettingagency.model.Bet;
import ro.ugal.aciee.bettingagency.model.Match;
import ro.ugal.aciee.bettingagency.model.Rate;
import ro.ugal.aciee.bettingagency.model.Team;
import ro.ugal.aciee.bettingagency.model.enums.BetStatus;
import ro.ugal.aciee.bettingagency.model.enums.MatchStatus;
import ro.ugal.aciee.bettingagency.service.*;
import ro.ugal.aciee.bettingagency.ui.Session;

import javax.swing.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


public class BetUserPanel extends JPanel {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("d.M.yyyy, HH:mm");
    private final AccountService accountService = new AccountService();
    private final BetService betService = new BetService();
    private final MatchService matchService = new MatchService();
    private final TeamService teamService = new TeamService();
    private final JPanel centerBetsPanel;
    private final JPanel wrapperPanel;
    private List<Bet> betsByStatus = new ArrayList<>();

    public BetUserPanel() {
        setLayout(new BorderLayout());

        centerBetsPanel = new JPanel();
        centerBetsPanel.setLayout(new GridLayout(0, 3, 15, 15));

        wrapperPanel = new JPanel(new BorderLayout());
        wrapperPanel.add(centerBetsPanel, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(wrapperPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

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
            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
        return betsPanel;
    }

    public void updateBetsDisplay() {
        try {
            wrapperPanel.removeAll();
            centerBetsPanel.removeAll();

            if (betsByStatus.isEmpty()) {
                JPanel emptyPanel = new JPanel(new GridBagLayout());
                JLabel emptyLabel = new JLabel("No bets found");
                emptyPanel.add(emptyLabel);

                wrapperPanel.add(emptyPanel, BorderLayout.CENTER);
            }else {
                wrapperPanel.add(centerBetsPanel, BorderLayout.NORTH);
                centerBetsPanel.setLayout(new GridLayout(0, 3, 15, 15));

                for (Bet bet : betsByStatus) {
                    centerBetsPanel.add(createBetPanel(bet));
                }
            }

            wrapperPanel.revalidate();
            wrapperPanel.repaint();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private JPanel createBetPanel(Bet bet) {
        JPanel betPanel = new JPanel(new BorderLayout());
        betPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        betPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 300));
        betPanel.setPreferredSize(new Dimension(Integer.MAX_VALUE, 200));

        try {

            JPanel matchesInfo = new JPanel();
            matchesInfo.setLayout(new BoxLayout(matchesInfo, BoxLayout.Y_AXIS));

            List<Rate> rateList = betService.getRateByBetId(bet.getBetId());
            List<Match> matchList = new ArrayList<>();

            for (Rate rate : rateList) {
                Match match = matchService.getById(rate.getMatchId());
                matchList.add(match);
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

            JPanel southPanel = new JPanel(new BorderLayout());

            JLabel betInfo = new JLabel("<html>" +
                    "Betting time: " + bet.getCreatedAt().format(DATE_TIME_FORMATTER) + "<br>" +
                    "Amount: " + bet.getAmount() + "<br>" +
                    "Odds: " + bet.getTotalOdds() + "<br>" +
                    "Status: " + bet.getBetStatus() +
                    "</html>");

            for (Match match : matchList) {
                if(match.getMatchStatus() == MatchStatus.UPCOMING || match.getMatchStatus() == MatchStatus.LIVE) {
                    if (bet.getBetStatus() == BetStatus.PENDING){
                        JButton cashOutButton = new JButton("Cash out");
                        cashOutButton.addActionListener(e -> cashOut(bet));
                        southPanel.add(cashOutButton, BorderLayout.EAST);
                    }
                }
            }

            southPanel.add(betInfo, BorderLayout.WEST);

            betPanel.add(matchesInfo, BorderLayout.CENTER);
            betPanel.add(southPanel, BorderLayout.SOUTH);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
        return betPanel;
    }

    public void loadUserBets() {
        if (!Session.isLoggedIn()) {
            return;
        }

        try {
            betsByStatus = betService.getByUserId(Session.getCurrentUser().getUserId());
            updateBetsDisplay();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void cashOut(Bet bet){
        try{
            betService.settleBetCashOut(bet.getBetId());
            Session.login(accountService.getById(Session.getCurrentUser().getUserId()));
            loadUserBets();
        }catch (Exception e){
            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }

    }
}
