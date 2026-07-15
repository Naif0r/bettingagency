package ro.ugal.aciee.bettingagency.ui.panels.player;

import ro.ugal.aciee.bettingagency.dto.MatchOddDTO;
import ro.ugal.aciee.bettingagency.model.Match;
import ro.ugal.aciee.bettingagency.service.MatchService;
import ro.ugal.aciee.bettingagency.service.RateService;
import ro.ugal.aciee.bettingagency.ui.BetSlip;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class HomePanel extends JPanel {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("d.M.yyyy, HH:mm");
    private final MatchService matchService = new MatchService();
    private final RateService rateService = new RateService();
    private final RatePanel ratePanel = new RatePanel();
    private final JPanel centerMatchesPanel;
    private final JPanel wrapperPanel;

    public HomePanel() {
        setLayout(new BorderLayout());

        centerMatchesPanel = new JPanel();

        wrapperPanel = new JPanel(new BorderLayout());
        wrapperPanel.add(centerMatchesPanel, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(wrapperPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
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
                    centerMatchesPanel.add(createMatchPlayerPanel(match));
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

    private JPanel createMatchPlayerPanel(Match match) throws SQLException {
        JPanel matchPanel = new JPanel(new BorderLayout());
        matchPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        matchPanel.setPreferredSize(new Dimension(Integer.MAX_VALUE, 120));

        JPanel top = new JPanel(new BorderLayout());

        top.add(new JLabel(match.getMatchStatus().toString()), BorderLayout.WEST);
        top.add(new JLabel(match.getMatchDate().format(DATE_TIME_FORMATTER)), BorderLayout.EAST);

        List<MatchOddDTO> matchOddDTOList = rateService.getRatesWithTeamNames(match.getMatchId());

        String team1 = matchOddDTOList.getFirst().team1Name();
        String team2 = matchOddDTOList.getFirst().team2Name();

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

        for (MatchOddDTO matchOddDTO : matchOddDTOList) {
            JButton button = new JButton(matchOddDTO.rate().getType() + "  " + matchOddDTO.rate().getValue());
            button.addActionListener(e -> {
                try {
                    boolean added = BetSlip.add(matchOddDTO.rate().getRateId());
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