package ro.ugal.aciee.bettingagency.ui.panels;

import ro.ugal.aciee.bettingagency.model.Match;
import ro.ugal.aciee.bettingagency.model.Rate;
import ro.ugal.aciee.bettingagency.model.Team;
import ro.ugal.aciee.bettingagency.service.MatchService;
import ro.ugal.aciee.bettingagency.service.RateService;
import ro.ugal.aciee.bettingagency.service.TeamService;
import ro.ugal.aciee.bettingagency.ui.BetSlip;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class HomePanel extends JPanel {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("d.M.yyyy, HH:mm");
    private final MatchService matchService = new MatchService();
    private final RatePanel ratePanel = new RatePanel();
    private final JPanel centerMatchesPanel;
    private final TeamService teamService = new TeamService();
    private final RateService rateService = new RateService();

    public HomePanel() {
        setLayout(new BorderLayout());

        centerMatchesPanel = new JPanel();
        centerMatchesPanel.setLayout(new GridLayout(0, 3, 15, 15));

        JScrollPane scrollPane = new JScrollPane(centerMatchesPanel);
        add(scrollPane, BorderLayout.CENTER);

        defaulMatches();
    }

    public void updateMatchesDisplay(List<Match> matches) {
        try {
            centerMatchesPanel.removeAll();
            centerMatchesPanel.setLayout(new BoxLayout(centerMatchesPanel, BoxLayout.Y_AXIS));

            int perRow = 3;
            JPanel currentRow = null;

            for (int i = 0; i < matches.size(); i++) {
                if (i % perRow == 0) {
                    currentRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
                    centerMatchesPanel.add(currentRow);
                }
                currentRow.add(createMatchPanel(matches.get(i)));
            }

            centerMatchesPanel.revalidate();
            centerMatchesPanel.repaint();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private JPanel createMatchPanel(Match match) throws SQLException {
        JPanel matchPanel = new JPanel(new BorderLayout());
        matchPanel.setBorder(BorderFactory.createLineBorder(Color.BLUE));
        matchPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        matchPanel.setPreferredSize(new Dimension(300, 120));

        JPanel top = new JPanel(new BorderLayout());

        top.add(new JLabel(match.getMatchStatus().toString()), BorderLayout.WEST);
        top.add(new JLabel(match.getMatchDate().format(DATE_TIME_FORMATTER)), BorderLayout.EAST);

        Team team1 = teamService.getById(match.getTeam1Id());
        Team team2 = teamService.getById(match.getTeam2Id());

        JPanel centerPanel = new JPanel(new BorderLayout());

        JLabel team = new JLabel("<html>" +
                team1.getTeamName() +
                "<br>" +
                team2.getTeamName() +
                "</html>");
        JLabel score = new JLabel("<html>" +
                match.getTeam1Score() +
                "<br>" +
                match.getTeam2Score() +
                "</html>");

        team.setFont(new Font("Arial", Font.BOLD, 18));
        score.setFont(new Font("Arial", Font.BOLD, 18));
        centerPanel.add(team, BorderLayout.WEST);
        centerPanel.add(score, BorderLayout.EAST);

        JPanel bottom = new JPanel(new FlowLayout());
        List<Rate> rates = rateService.getByMatchId(match.getMatchId());
        for (Rate rate : rates) {
            JButton button = new JButton(rate.getType() + "  " + rate.getValue());
            button.addActionListener(e -> {
                try {
                    boolean added = BetSlip.add(rate.getRateId());
                    if (!added) {
                        JOptionPane.showMessageDialog(this, "It is impossible to add this odds to the bet");
                    }
                    ratePanel.launch();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, ex.getMessage());
                }
            });
            bottom.add(button);
        }

        matchPanel.add(top, BorderLayout.NORTH);
        matchPanel.add(centerPanel, BorderLayout.CENTER);
        matchPanel.add(bottom, BorderLayout.SOUTH);

        return matchPanel;
    }

    private void defaulMatches(){
        try {
            updateMatchesDisplay(matchService.getByExistsStatus());
        }catch (Exception e){
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
}