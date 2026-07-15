package ro.ugal.aciee.bettingagency;

import ro.ugal.aciee.bettingagency.ui.BettingAgencyGUI;
import ro.ugal.aciee.bettingagency.utils.database.DatabaseManager;

import java.sql.SQLException;


public class Main {
    public static void main(String[] args) throws SQLException {

        DatabaseManager.initialize();

        BettingAgencyGUI gui = new BettingAgencyGUI();
        gui.launch();

    }
}