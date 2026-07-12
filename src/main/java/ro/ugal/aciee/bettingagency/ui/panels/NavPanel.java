package ro.ugal.aciee.bettingagency.ui.panels;

import ro.ugal.aciee.bettingagency.model.Sport;
import ro.ugal.aciee.bettingagency.model.enums.MatchStatus;
import ro.ugal.aciee.bettingagency.service.MatchService;
import ro.ugal.aciee.bettingagency.service.SportService;
import ro.ugal.aciee.bettingagency.ui.BettingAgencyGUI;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class NavPanel extends JPanel {
    private final BettingAgencyGUI gui;
    private final SportService sportService = new SportService();
    private final MatchService matchService = new MatchService();

    public NavPanel(BettingAgencyGUI gui) {
        this.gui = gui;
        setLayout(new FlowLayout(FlowLayout.LEFT));

        try {
            JButton liveMatchButton = new JButton("LIVE");
            liveMatchButton.addActionListener(e -> {
                try {
                    gui.showHomePanel();
                    gui.getHomePanel().updateMatchesDisplay(matchService.getByStatus(MatchStatus.LIVE));
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, ex.getMessage());
                }
            });
            add(liveMatchButton);

            JButton betsUser = new JButton("Bets");
            betsUser.addActionListener(e -> gui.showBetsPanel());
            add(betsUser);

            List<Sport> sports = sportService.getAll();
            for (Sport sport : sports) {
                JButton button = new JButton(sport.getSportName());
                button.addActionListener(e -> {
                    try {
                        gui.showHomePanel();
                        gui.getHomePanel().updateMatchesDisplay(matchService.getBySport(sport.getSportName()));
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, ex.getMessage());
                    }
                });
                add(button);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
}