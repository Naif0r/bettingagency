package ro.ugal.aciee.bettingagency.ui;

import ro.ugal.aciee.bettingagency.model.Rate;
import ro.ugal.aciee.bettingagency.service.RateService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BetSlip {
    private static final List<Integer> rateIdList = new ArrayList<>();
    private static final RateService rateService = new RateService();

    public static boolean add(int id) throws SQLException {
        Rate rate = rateService.getById(id);

        for (int checkId : rateIdList) {
            Rate checkRate = rateService.getById(checkId);
            if (checkRate.getMatchId() == rate.getMatchId()) {
                return false;
            }
        }

        rateIdList.add(id);
        return true;
    }

    public static void remove(int id) {
        rateIdList.remove(Integer.valueOf(id));
    }

    public static List<Integer> getRateIdList() {
        return rateIdList;
    }

    public static void clear() {
        rateIdList.clear();
    }
}
