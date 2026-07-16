package ro.ugal.aciee.bettingagency;

import ro.ugal.aciee.bettingagency.ui.BettingAgencyGUI;
import ro.ugal.aciee.bettingagency.utils.database.DatabaseManager;

import javax.swing.*;


public class Main {
    public static void main(String[] args) {
        try {
            DatabaseManager.initialize();

            BettingAgencyGUI gui = new BettingAgencyGUI();
            gui.launch();

            System.out.println("====================================");
            System.out.println("          GUI LAUNCHED            ");
            System.out.println("====================================");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    null,
                    "Database connection failed: " + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}