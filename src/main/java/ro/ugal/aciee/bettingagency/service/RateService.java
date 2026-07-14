package ro.ugal.aciee.bettingagency.service;

import ro.ugal.aciee.bettingagency.dao.RateDAO;
import ro.ugal.aciee.bettingagency.model.Rate;
import ro.ugal.aciee.bettingagency.model.enums.RateStatus;
import ro.ugal.aciee.bettingagency.model.enums.RateType;

import java.sql.SQLException;
import java.util.List;

public class RateService {
    private final RateDAO rateDAO = new RateDAO();
    private final MatchService matchService = new MatchService();

    public Rate save(int matchId, RateType type, double value) throws SQLException {
        matchService.getById(matchId);

        if (type == null) {
            throw new IllegalArgumentException("Type is empty");
        }

        if (value <= 1.00) {
            throw new IllegalArgumentException("The coefficient must be greater than 1.00");
        }

        List<Rate> rates = rateDAO.getByMatch(matchId);

        for (Rate rate : rates) {
            if (rate.getType() == type) {
                throw new IllegalArgumentException("This rate already exists for the match");
            }
        }

        Rate rate = new Rate();
        rate.setMatchId(matchId);
        rate.setType(type);
        rate.setValue(value);

        return rateDAO.save(rate);
    }

    public Rate importer(Rate rate) {
        try {
            return rateDAO.importer(rate);
        } catch (SQLException e){
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public List<Rate> getAll() throws SQLException {
        return rateDAO.getAll();
    }

    public List<Rate> getByMatchId(int matchId) throws SQLException {
        matchService.getById(matchId);
        return rateDAO.getByMatch(matchId);
    }

    public Rate getById(int rateId) throws SQLException {
        if (rateId <= 0) {
            throw new IllegalArgumentException("Incorrect rate id");
        }

        Rate rate = rateDAO.getById(rateId);

        if (rate == null) {
            throw new IllegalArgumentException("Rate not found");
        }

        return rate;
    }

    public boolean updateStatus(int rateId, RateStatus rateStatus) throws SQLException {
        Rate rate = getById(rateId);

        if (rateStatus == null) {
            throw new IllegalArgumentException("Rate status is empty");
        }

        if (rate.getRateStatus() == rateStatus) {
            throw new IllegalArgumentException("The status cannot be set because it is already set");
        }

        return rateDAO.updateStatus(rateId, rateStatus);
    }

    public boolean delete(int rateId) throws SQLException {
        getById(rateId);
        return rateDAO.delete(rateId);
    }

}
