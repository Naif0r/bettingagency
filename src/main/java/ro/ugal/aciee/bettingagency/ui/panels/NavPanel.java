package ro.ugal.aciee.bettingagency.ui.panels;

import ro.ugal.aciee.bettingagency.model.Match;
import ro.ugal.aciee.bettingagency.model.Sport;
import ro.ugal.aciee.bettingagency.model.enums.MatchStatus;
import ro.ugal.aciee.bettingagency.model.enums.Role;
import ro.ugal.aciee.bettingagency.service.MatchService;
import ro.ugal.aciee.bettingagency.service.SportService;
import ro.ugal.aciee.bettingagency.ui.BettingAgencyGUI;
import ro.ugal.aciee.bettingagency.ui.Session;
import ro.ugal.aciee.bettingagency.ui.dialogs.admin.CreateMatchDialog;
import ro.ugal.aciee.bettingagency.ui.dialogs.admin.FilterMatchDialog;
import ro.ugal.aciee.bettingagency.ui.panels.admin.ChangeAccountStatusPanel;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class NavPanel extends JPanel {
    private final BettingAgencyGUI gui;
    private final ChangeAccountStatusPanel changeAccountStatusPanel = new ChangeAccountStatusPanel();
    private final SportService sportService = new SportService();
    private final MatchService matchService = new MatchService();

    public NavPanel(BettingAgencyGUI gui) {
        this.gui = gui;
        setLayout(new FlowLayout(FlowLayout.LEFT));
        Session.addListener(this::rebuild);
        rebuild();
    }

    private void rebuild() {
        removeAll();
        if (Session.isLoggedIn() && Session.getCurrentUser().getRole() == Role.ADMIN) {
            buildAdminButtons();
        } else {
            buildPlayerButtons();
        }
        revalidate();
        repaint();
    }

    private void buildPlayerButtons() {
        try {
            JButton homePageMatchButton = new JButton("HOME");
            homePageMatchButton.addActionListener(e -> {
                try {
                    updatePlayerDisplay(matchService.getByExistsStatus());
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            });
            add(homePageMatchButton);

            JButton liveMatchButton = new JButton("LIVE");
            liveMatchButton.addActionListener(e -> {
                try {
                    updatePlayerDisplay(matchService.getByStatus(MatchStatus.LIVE));
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
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
                        updatePlayerDisplay(matchService.getBySport(sport.getSportName()));
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                });
                add(button);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void buildAdminButtons() {
        JButton createMatchButton = new JButton("Create Match");
        createMatchButton.addActionListener(e -> {
            CreateMatchDialog dialog = new CreateMatchDialog();
            dialog.setModal(true);
            dialog.setVisible(true);
        });
        add(createMatchButton);

        JButton manageUsersStatusButton = new JButton("Manage Account Status");
        manageUsersStatusButton.addActionListener(e -> changeAccountStatusPanel.launch());
        add(manageUsersStatusButton);

        try {
            List<Sport> sports = sportService.getAll();
            for (Sport sport : sports) {
                JButton button = new JButton(sport.getSportName());
                button.addActionListener(e -> {
                    try {
                        gui.showHomePanel();
                        gui.getHomeAdminPanel().updateMatchesDisplay(matchService.getBySport(sport.getSportName()));
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(
                                this,
                                ex.getMessage(),
                                "Error",
                                JOptionPane.ERROR_MESSAGE
                        );
                    }
                });
                add(button);
            }
            JButton filterMatchByStatus = new JButton("Filter match");
            filterMatchByStatus.addActionListener(e -> {
                FilterMatchDialog dialog = new FilterMatchDialog(gui);
                dialog.setModal(true);
                dialog.setVisible(true);
            });
            add(filterMatchByStatus);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void updatePlayerDisplay(List<Match> matches) {
        try {
            gui.showHomePanel();
            gui.getHomePanel().updateMatchesDisplay(matches);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }


}