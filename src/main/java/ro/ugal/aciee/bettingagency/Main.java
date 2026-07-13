package ro.ugal.aciee.bettingagency;

import ro.ugal.aciee.bettingagency.dao.*;
import ro.ugal.aciee.bettingagency.model.Bet;
import ro.ugal.aciee.bettingagency.model.BetRate;
import ro.ugal.aciee.bettingagency.model.Match;
import ro.ugal.aciee.bettingagency.model.Rate;
import ro.ugal.aciee.bettingagency.model.enums.*;
import ro.ugal.aciee.bettingagency.service.*;
import ro.ugal.aciee.bettingagency.ui.BettingAgencyGUI;
import ro.ugal.aciee.bettingagency.utils.database.DatabaseInitializer;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;


public class Main {
    public static void main(String[] args) throws SQLException {

        DatabaseInitializer.initialize();

        BettingAgencyGUI gui = new BettingAgencyGUI();
        gui.launch();







    }
}