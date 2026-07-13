package ro.ugal.aciee.bettingagency.ui;

import ro.ugal.aciee.bettingagency.model.enums.Role;
import ro.ugal.aciee.bettingagency.ui.panels.*;

import javax.swing.*;
import java.awt.*;

public class BettingAgencyGUI extends JFrame {
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel centerContainer = new JPanel();
    private final HomePanel homePanel = new HomePanel();
    private final HomeAdminPanel homeAdminPanel = new HomeAdminPanel();
    private final BetUserPanel betUserPanel = new BetUserPanel();

    public BettingAgencyGUI() {
        initializeGUI();
    }

    private void initializeGUI() {
        setTitle("Bet by Naifor");
        setSize(1200, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel topContainer = new JPanel(new BorderLayout());
        topContainer.add(new TopPanel(this), BorderLayout.NORTH);
        topContainer.add(new NavPanel(this), BorderLayout.SOUTH);

        centerContainer.setLayout(cardLayout);
        centerContainer.add(homePanel, "HOME");
        centerContainer.add(homeAdminPanel, "HOME_ADMIN");
        centerContainer.add(betUserPanel, "BETS");

        add(topContainer, BorderLayout.NORTH);
        add(centerContainer, BorderLayout.CENTER);

        Session.addListener(this::refreshView);
        refreshView();
    }

    private void refreshView() {
        if (Session.isLoggedIn() && Session.getCurrentUser().getRole() == Role.ADMIN) {
            cardLayout.show(centerContainer, "HOME_ADMIN");
        } else {
            cardLayout.show(centerContainer, "HOME");
        }
    }

    public HomePanel getHomePanel() {
        return homePanel;
    }

    public HomeAdminPanel getHomeAdminPanel() {
        return homeAdminPanel;
    }

    public void showBetsPanel() {
        betUserPanel.loadUserBets();
        cardLayout.show(centerContainer, "BETS");
    }

    public void showHomePanel() {
        refreshView();
    }

    public void launch() {
        SwingUtilities.invokeLater(() -> setVisible(true));
    }
}