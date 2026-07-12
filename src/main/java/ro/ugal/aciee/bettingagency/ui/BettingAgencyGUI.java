package ro.ugal.aciee.bettingagency.ui;

import ro.ugal.aciee.bettingagency.ui.panels.BetUserPanel;
import ro.ugal.aciee.bettingagency.ui.panels.HomePanel;
import ro.ugal.aciee.bettingagency.ui.panels.NavPanel;
import ro.ugal.aciee.bettingagency.ui.panels.TopPanel;

import javax.swing.*;
import java.awt.*;

public class BettingAgencyGUI extends JFrame {
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel centerContainer = new JPanel();
    private final HomePanel homePanel = new HomePanel();
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
        centerContainer.add(betUserPanel, "BETS");

        add(topContainer, BorderLayout.NORTH);
        add(centerContainer, BorderLayout.CENTER);

        cardLayout.show(centerContainer, "HOME");
    }

    public HomePanel getHomePanel() {
        return homePanel;
    }

    public void showBetsPanel() {
        betUserPanel.updateBetsDisplay();
        cardLayout.show(centerContainer, "BETS");
    }

    public void showHomePanel() {
        cardLayout.show(centerContainer, "HOME");
    }

    public void launch() {
        SwingUtilities.invokeLater(() -> setVisible(true));
    }
}