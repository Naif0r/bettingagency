package ro.ugal.aciee.bettingagency.ui.panels.admin;

import ro.ugal.aciee.bettingagency.dto.MatchTeamNamesDTO;
import ro.ugal.aciee.bettingagency.model.Match;
import ro.ugal.aciee.bettingagency.model.enums.MatchTeam;
import ro.ugal.aciee.bettingagency.service.MatchService;
import ro.ugal.aciee.bettingagency.ui.dialogs.admin.ChangeStatusMatchDialog;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class HomeAdminPanel extends JPanel {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("d.M.yyyy, HH:mm");
    private final JPanel centerMatchesPanel;
    private final JPanel wrapperPanel;
    private final MatchService matchService = new MatchService();

    public HomeAdminPanel() {
        setLayout(new BorderLayout());

        centerMatchesPanel = new JPanel();

        wrapperPanel = new JPanel(new BorderLayout());
        wrapperPanel.add(centerMatchesPanel, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(wrapperPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(24);
        add(scrollPane, BorderLayout.CENTER);

        defaultMatches();
    }

    public void updateMatchesDisplay(List<Match> matches) {
        try {
            wrapperPanel.removeAll();
            centerMatchesPanel.removeAll();

            if (matches.isEmpty()) {
                JPanel emptyPanel = new JPanel(new GridBagLayout());
                JLabel emptyLabel = new JLabel("No matches found");
                emptyPanel.add(emptyLabel);

                wrapperPanel.add(emptyPanel, BorderLayout.CENTER);
            } else {
                wrapperPanel.add(centerMatchesPanel, BorderLayout.NORTH);
                centerMatchesPanel.setLayout(new GridLayout(0, 3, 15, 15));

                for (Match match : matches) {
                    centerMatchesPanel.add(createMatchAdminPanel(match));
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

    private JPanel createMatchAdminPanel(Match match) throws SQLException {
        JPanel matchPanel = new JPanel(new BorderLayout());
        matchPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        matchPanel.setPreferredSize(new Dimension(Integer.MAX_VALUE, 120));

        JPanel top = new JPanel(new BorderLayout());

        top.add(new JLabel(match.getMatchStatus().toString()), BorderLayout.WEST);
        top.add(new JLabel(match.getMatchDate().format(DATE_TIME_FORMATTER)), BorderLayout.EAST);

        MatchTeamNamesDTO matchTeamNamesDTO = matchService.getMatchTeams(match.getMatchId());

        String team1 = matchTeamNamesDTO.team1Name();
        String team2 = matchTeamNamesDTO.team2Name();

        JPanel centerPanel = new JPanel(new BorderLayout());

        JLabel team = new JLabel("<html>" +
                team1 +
                "<br>" +
                team2 +
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
