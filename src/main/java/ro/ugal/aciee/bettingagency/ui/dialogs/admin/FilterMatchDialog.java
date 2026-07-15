package ro.ugal.aciee.bettingagency.ui.dialogs.admin;

import ro.ugal.aciee.bettingagency.model.enums.MatchStatus;
import ro.ugal.aciee.bettingagency.service.MatchService;
import ro.ugal.aciee.bettingagency.ui.BettingAgencyGUI;

import javax.swing.*;
import java.awt.*;

public class FilterMatchDialog extends JDialog {
    private final BettingAgencyGUI gui;
    private final MatchService matchService = new MatchService();
    private final JComboBox<String> filterBox;

    public FilterMatchDialog(BettingAgencyGUI gui) {
        this.gui = gui;

        setTitle("Filter matches");
        setSize(240, 180);
        setLocationRelativeTo(null);

        JPanel filterPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        String[] statusValues = {"UPCOMING", "LIVE", "FINISHED", "CANCELLED"};
        filterBox = new JComboBox<>(statusValues);

        gbc.gridx = 0;
        gbc.gridy = 0;
        filterPanel.add(new JLabel("Status match: "), gbc);

        gbc.gridx = 1;
        filterPanel.add(filterBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> dispose());
        filterPanel.add(cancelButton, gbc);

        gbc.gridx = 1;
        JButton applyButton = new JButton("Apply");
        applyButton.addActionListener(e -> filterMatchByStatus());
        filterPanel.add(applyButton, gbc);

        add(filterPanel);
    }

    private void filterMatchByStatus() {
        try {
            MatchStatus matchStatus = MatchStatus.valueOf(String.valueOf(filterBox.getSelectedItem()));
            gui.showHomePanel();
            gui.getHomeAdminPanel().updateMatchesDisplay(matchService.getByStatus(matchStatus));
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
