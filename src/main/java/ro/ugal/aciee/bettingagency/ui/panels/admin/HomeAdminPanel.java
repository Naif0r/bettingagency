package ro.ugal.aciee.bettingagency.ui.panels.admin;

import ro.ugal.aciee.bettingagency.model.Match;
import ro.ugal.aciee.bettingagency.model.Team;
import ro.ugal.aciee.bettingagency.model.enums.MatchTeam;
import ro.ugal.aciee.bettingagency.service.MatchService;
import ro.ugal.aciee.bettingagency.service.RateService;
import ro.ugal.aciee.bettingagency.service.TeamService;
import ro.ugal.aciee.bettingagency.ui.dialogs.admin.ChangeStatusMatchDialog;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class HomeAdminPanel extends JPanel {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("d.M.yyyy, HH:mm");
    private final JPanel centerMatchesPanel;
    private final TeamService teamService = new TeamService();
    private final RateService rateService = new RateService();
    private final MatchService matchService = new MatchService();

    public HomeAdminPanel() {
        setLayout(new BorderLayout());

        centerMatchesPanel = new JPanel();
        centerMatchesPanel.setLayout(new GridLayout(0, 3, 15, 15));

        JScrollPane scrollPane = new JScrollPane(centerMatchesPanel);
        add(scrollPane, BorderLayout.CENTER);

        defaultMatches();
    }

    public void updateMatchesDisplay(List<Match> matches) {
        try {
            centerMatchesPanel.removeAll();

            if (matches.isEmpty()) {
                centerMatchesPanel.setLayout(new BorderLayout());
                centerMatchesPanel.add(new JLabel("No matches found for this sport", SwingConstants.CENTER), BorderLayout.CENTER);
                centerMatchesPanel.revalidate();
                centerMatchesPanel.repaint();
                return;
            }

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
            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
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

        JButton addScoreTeam1 = new JButton("+TEAM1");
        addScoreTeam1.addActionListener(e -> addScore(match.getMatchId(), MatchTeam.TEAM1));
        bottom.add(addScoreTeam1);

        JButton updateStatusMatch = new JButton("Update Status");
        updateStatusMatch.addActionListener(e -> updateStatusMatchById(match.getMatchId()));
        bottom.add(updateStatusMatch);

        JButton addScoreTeam2 = new JButton("TEAM2+");
        addScoreTeam2.addActionListener(e -> addScore(match.getMatchId(), MatchTeam.TEAM2));
        bottom.add(addScoreTeam2);

        matchPanel.add(top, BorderLayout.NORTH);
        matchPanel.add(centerPanel, BorderLayout.CENTER);
        matchPanel.add(bottom, BorderLayout.SOUTH);

        return matchPanel;
    }

    private void addScore(int matchId, MatchTeam matchTeam) {
        try {
            matchService.updateScoreTeam(matchId, matchTeam);
            defaultMatches();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void updateStatusMatchById(int matchId) {
        ChangeStatusMatchDialog dialog = new ChangeStatusMatchDialog(matchId, this::defaultMatches);
        dialog.setVisible(true);
    }

    private void defaultMatches() {
        try {
            updateMatchesDisplay(matchService.getByExistsStatus());
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
