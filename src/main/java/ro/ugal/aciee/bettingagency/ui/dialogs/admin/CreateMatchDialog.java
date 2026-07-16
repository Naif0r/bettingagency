package ro.ugal.aciee.bettingagency.ui.dialogs.admin;

import ro.ugal.aciee.bettingagency.model.Match;
import ro.ugal.aciee.bettingagency.model.Sport;
import ro.ugal.aciee.bettingagency.model.Team;
import ro.ugal.aciee.bettingagency.service.MatchService;
import ro.ugal.aciee.bettingagency.service.SportService;
import ro.ugal.aciee.bettingagency.service.TeamService;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.List;

public class CreateMatchDialog extends JDialog {
    private final SportService sportService = new SportService();
    private final TeamService teamService = new TeamService();
    private final MatchService matchService = new MatchService();
    private final JComboBox<String> sportBox;
    private final JComboBox<String> team1Box;
    private final JComboBox<String> team2Box;
    private final JSpinner yearSpinner;
    private final JSpinner monthSpinner;
    private final JSpinner daySpinner;
    private final JSpinner hourSpinner;
    private final JSpinner minuteSpinner;

    public CreateMatchDialog() {
        setTitle("Create Match");

        setSize(480, 250);
        setLocationRelativeTo(null);

        JPanel createMatchPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        sportBox = new JComboBox<>();
        team1Box = new JComboBox<>();
        team2Box = new JComboBox<>();

        team1Box.setPreferredSize(new Dimension(160, 25));
        team2Box.setPreferredSize(new Dimension(160, 25));
        sportBox.setPreferredSize(new Dimension(120, 25));

        yearSpinner = new JSpinner(new SpinnerNumberModel(2026, 2024, 2100, 1));
        monthSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 12, 1));
        daySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 31, 1));
        hourSpinner = new JSpinner(new SpinnerNumberModel(12, 0, 23, 1));
        minuteSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 59, 1));

        loadSports();

        gbc.gridx = 0;
        gbc.gridy = 0;
        createMatchPanel.add(new JLabel("TEAM1"), gbc);

        gbc.gridx = 1;
        createMatchPanel.add(new JLabel("SPORT"), gbc);

        gbc.gridx = 2;
        createMatchPanel.add(new JLabel("TEAM2"), gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        createMatchPanel.add(team1Box, gbc);

        gbc.gridx = 1;
        sportBox.addActionListener(e -> loadTeams());
        createMatchPanel.add(sportBox, gbc);

        gbc.gridx = 2;
        createMatchPanel.add(team2Box, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        createMatchPanel.add(new JLabel("Date:"), gbc);

        gbc.gridy = 3;
        JPanel datePanel = new JPanel();
        datePanel.setLayout(new BoxLayout(datePanel, BoxLayout.X_AXIS));
        datePanel.add(daySpinner);
        datePanel.add(new JLabel("/"));
        datePanel.add(monthSpinner);
        datePanel.add(new JLabel("/"));
        datePanel.add(yearSpinner);
        createMatchPanel.add(datePanel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        createMatchPanel.add(new JLabel("Time:"), gbc);

        gbc.gridy = 3;
        JPanel timePanel = new JPanel();
        timePanel.setLayout(new BoxLayout(timePanel, BoxLayout.X_AXIS));
        timePanel.add(hourSpinner);
        timePanel.add(new JLabel(":"));
        timePanel.add(minuteSpinner);
        createMatchPanel.add(timePanel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> dispose());
        createMatchPanel.add(cancelButton, gbc);

        gbc.gridx = 2;
        gbc.gridy = 4;
        JButton createButton = new JButton("Create");
        createButton.addActionListener(e -> createMatch());
        createMatchPanel.add(createButton, gbc);


        add(createMatchPanel);
    }

    private void loadSports() {
        try {
            sportBox.removeAllItems();

            List<Sport> sports = sportService.getAll();

            for (Sport sport : sports)
                sportBox.addItem(sport.getSportName());

            loadTeams();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void loadTeams() {

        try {

            team1Box.removeAllItems();
            team2Box.removeAllItems();

            String sport = (String) sportBox.getSelectedItem();

            if (sport == null)
                return;

            List<Team> teams = teamService.getBySport(sport);

            for (Team team : teams) {
                team1Box.addItem(team.getTeamName());
                team2Box.addItem(team.getTeamName());
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }

    }

    private void createMatch() {
        try {
            String sportName = (String) sportBox.getSelectedItem();
            String team1Name = (String) team1Box.getSelectedItem();
            String team2Name = (String) team2Box.getSelectedItem();

            Team team1 = teamService.getByTeamAndSport(team1Name, sportName);
            Team team2 = teamService.getByTeamAndSport(team2Name, sportName);

            if (team1 == null || team2 == null) {
                return;
            }

            LocalDateTime date = LocalDateTime.of(
                    (Integer) yearSpinner.getValue(),
                    (Integer) monthSpinner.getValue(),
                    (Integer) daySpinner.getValue(),
                    (Integer) hourSpinner.getValue(),
                    (Integer) minuteSpinner.getValue()
            );

            Match match = matchService.save(team1.getSportId(),
                    team1.getTeamId(),
                    team2.getTeamId(),
                    date);

            CreateRateDialog dialog = new CreateRateDialog(match.getMatchId());
            dialog.setModal(true);
            dialog.setVisible(true);

            if (!dialog.isCompleted()) {
                matchService.delete(match.getMatchId());
            }

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
