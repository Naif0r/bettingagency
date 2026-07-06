package ro.ugal.aciee.bettingagency.service;

import ro.ugal.aciee.bettingagency.dao.AccountDAO;
import ro.ugal.aciee.bettingagency.dao.BetDAO;
import ro.ugal.aciee.bettingagency.dao.BetRateDAO;
import ro.ugal.aciee.bettingagency.dao.RateDAO;
import ro.ugal.aciee.bettingagency.model.Account;
import ro.ugal.aciee.bettingagency.model.Bet;
import ro.ugal.aciee.bettingagency.model.BetRate;
import ro.ugal.aciee.bettingagency.model.Rate;
import ro.ugal.aciee.bettingagency.model.enums.BetStatus;
import ro.ugal.aciee.bettingagency.model.enums.RateStatus;

import java.sql.SQLException;
import java.util.List;

public class BetService {
    private final BetDAO betDAO = new BetDAO();
    private final AccountDAO accountDAO = new AccountDAO();
    private final BetRateDAO betRateDAO = new BetRateDAO();
    private final RateDAO rateDAO = new RateDAO();

    public Bet save(int userId, double amount, List<Integer> rateId) throws SQLException {
        if (userId <= 0) {
            throw new IllegalArgumentException("Incorrect user id");
        }
        Account account = accountDAO.getById(userId);
        if (account == null) {
            throw new IllegalArgumentException("Account not found");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("The amount must be greater than 0");
        }
        if (account.getBalance() - amount < 0) {
            throw new IllegalArgumentException("Insufiicient balance to place a bet");
        }
        if (rateId.isEmpty()) {
            throw new IllegalArgumentException("Rate id list is empty");
        }
        double totalOdds = 1;
        for (int id : rateId) {
            Rate rate = rateDAO.getById(id);
            if (rate == null) {
                throw new IllegalArgumentException("Rate not found");
            }
            if (rate.getRateStatus().equals(RateStatus.CLOSED)) {
                throw new IllegalArgumentException("Rate is closed");
            }
            totalOdds *= rate.getValue();
        }
        double possibleWin = amount * totalOdds;
        Bet bet = new Bet();
        bet.setUserId(userId);
        bet.setAmount(amount);
        bet.setTotalOdds(totalOdds);
        bet.setPossibleWin(possibleWin);
        bet = betDAO.save(bet);
        accountDAO.updateBalance(-amount, userId);
        for (int id : rateId) {
            BetRate betRate = new BetRate(bet.getBetId(), id);
            betRateDAO.save(betRate);
        }
        return bet;
    }

    public List<Bet> getAll() throws SQLException {
        List<Bet> betList = betDAO.getAll();
        if (betList.isEmpty()) {
            throw new IllegalArgumentException("Bet list is empty");
        }
        return betList;
    }

    public List<Bet> getByUserId(int userId) throws SQLException {
        if (userId <= 0) {
            throw new IllegalArgumentException("Incorrect user id");
        }
        List<Bet> betList = betDAO.getByUserId(userId);
        if(betList.isEmpty()){
            throw new IllegalArgumentException("Bet list is empty");
        }
        return betList;
    }

    public List<Bet> getByStatus(BetStatus betStatus) throws SQLException {
        if (betStatus == null) {
            throw new IllegalArgumentException("Incorrect bet status");
        }
        List<Bet> betList = betDAO.getByStatus(betStatus);
        if(betList.isEmpty()){
            throw new IllegalArgumentException("Bet list is empty");
        }
        return betList;
    }

    public boolean updateStatus(int betId, BetStatus betStatus) throws SQLException {
        if (betId <= 0) {
            throw new IllegalArgumentException("Incorrect bet id");
        }
        Bet bet = betDAO.getByBetId(betId);
        if (bet == null) {
            throw new IllegalArgumentException("Bet not found");
        }
        if (betStatus == null) {
            throw new IllegalArgumentException("Incorrect bet status");
        }
        if (bet.getBetStatus().equals(betStatus)) {
            throw new IllegalArgumentException("It is not possible to change the status because it is already set");
        }

        return betDAO.updateStatus(betStatus, betId);
    }

    public boolean delete(int betId) throws SQLException {
        if (betId <= 0) {
            throw new IllegalArgumentException("Incorrect bet id");
        }
        Bet bet = betDAO.getByBetId(betId);
        if (bet == null) {
            throw new IllegalArgumentException("Bet not found");
        }
        return betDAO.delete(betId);
    }
}
