package ro.ugal.aciee.bettingagency.service;

import ro.ugal.aciee.bettingagency.dao.BetDAO;
import ro.ugal.aciee.bettingagency.dao.BetRateDAO;
import ro.ugal.aciee.bettingagency.model.*;
import ro.ugal.aciee.bettingagency.model.enums.*;

import java.sql.SQLException;
import java.util.List;

public class BetService {
    private final BetDAO betDAO = new BetDAO();
    private final AccountService accountService = new AccountService();
    private final BetRateDAO betRateDAO = new BetRateDAO();
    private final RateService rateService = new RateService();
    private final MatchService matchService = new MatchService();

    public Bet save(int userId, double amount, List<Integer> rateId) throws SQLException {
        Account account = accountService.getById(userId);

        if (amount <= 0) {
            throw new IllegalArgumentException("The amount must be greater than 0");
        }

        if (account.getBalance() < amount) {
            throw new IllegalArgumentException("Insufficient balance to place a bet");
        }

        if (account.getAccountStatus() == AccountStatus.BANNED) {
            throw new IllegalArgumentException("The account is blocked and cannot place bets");
        }

        if (rateId.isEmpty()) {
            throw new IllegalArgumentException("Rate id list is empty");
        }

        double totalOdds = 1;

        for (int id : rateId) {
            Rate rate = rateService.getById(id);

            if (rate.getRateStatus() == RateStatus.CLOSED) {
                throw new IllegalArgumentException("Rate is closed");
            }

            Match match = matchService.getById(rate.getMatchId());

            if (match.getMatchStatus() == MatchStatus.FINISHED) {
                throw new IllegalArgumentException("Match is finished");
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

        accountService.updateBalance(userId, -amount);

        for (int id : rateId) {
            BetRate betRate = new BetRate(bet.getBetId(), id);
            betRateDAO.save(betRate);
        }

        return bet;
    }

    public Bet importer(Bet bet) {
        try{
            return betDAO.importer(bet);
        } catch (SQLException e){
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public List<Bet> getAll() throws SQLException {
        return betDAO.getAll();
    }

    public Bet getByBetId(int betId) throws SQLException {
        if (betId <= 0) {
            throw new IllegalArgumentException("Incorrect bet id");
        }

        Bet bet = betDAO.getByBetId(betId);

        if (bet == null) {
            throw new IllegalArgumentException("Bet not found");
        }

        return bet;
    }

    public List<Bet> getByUserId(int userId) throws SQLException {
        Account account = accountService.getById(userId);

        return betDAO.getByUserId(account.getUserId());
    }

    public List<Bet> getByStatus(BetStatus betStatus, int userId) throws SQLException {
        accountService.getById(userId);

        if (betStatus == null) {
            throw new IllegalArgumentException("Incorrect bet status");
        }

        return betDAO.getByStatus(betStatus, userId);
    }

    public List<Rate> getRateByBetId(int betId) throws SQLException {
        getByBetId(betId);

        List<Rate> rateList = betRateDAO.getByBetId(betId);

        if (rateList.isEmpty()) {
            throw new IllegalArgumentException("Rate list is empty");
        }

        return rateList;
    }

    public List<Bet> getAllBetByMatchId(int matchId) throws SQLException {
        matchService.getById(matchId);

        return betDAO.getAllBetByMatch(matchId);
    }

    public boolean updateStatus(int betId, BetStatus betStatus) throws SQLException {
        Bet bet = getByBetId(betId);

        if (betStatus == null) {
            throw new IllegalArgumentException("Incorrect bet status");
        }

        if (bet.getBetStatus() == betStatus) {
            throw new IllegalArgumentException("It is not possible to change the status because it is already set");
        }

        return betDAO.updateStatus(betStatus, betId);
    }

    public boolean delete(int betId) throws SQLException {
        getByBetId(betId);
        return betDAO.delete(betId);
    }

    public BetStatus calculateBetStatus(int betId) throws SQLException {
        getByBetId(betId);

        List<Rate> rateList = betRateDAO.getByBetId(betId);

        if (rateList.isEmpty()) {
            throw new IllegalArgumentException("Rate list is empty");
        }

        for (Rate rate : rateList) {
            Match match = matchService.getById(rate.getMatchId());

            if (match.getMatchStatus() == MatchStatus.CANCELLED) {
                return BetStatus.CANCELED;
            }

            if (match.getMatchStatus() != MatchStatus.FINISHED) {
                throw new IllegalArgumentException("Match must be finished");
            }

            RateType winner = matchService.getWinnerMatch(match.getMatchId());

            if (winner != rate.getType()) {
                return BetStatus.LOST;
            }
        }

        return BetStatus.WON;
    }

    public boolean settleBet(int betId) throws SQLException {
        Bet bet = getByBetId(betId);

        if (bet.getBetStatus() != BetStatus.PENDING) {
            throw new IllegalArgumentException("Bet has already been settled");
        }

        BetStatus betStatus = calculateBetStatus(betId);
        updateStatus(betId, betStatus);

        if (betStatus == BetStatus.WON) {
            return accountService.updateBalance(bet.getUserId(), bet.getPossibleWin());
        } else if (betStatus == BetStatus.CANCELED) {
            return accountService.updateBalance(bet.getUserId(), bet.getAmount());
        }

        return false;
    }

    public boolean settleBetCashOut(int betId) throws SQLException {
        Bet bet = getByBetId(betId);

        if (bet.getBetStatus() != BetStatus.PENDING) {
            throw new IllegalArgumentException("Bet has already been settled");
        }

        if (!betDAO.updateStatus(BetStatus.CASHED_OUT, betId)) {
            return false;
        }

        return accountService.updateBalance(bet.getUserId(), bet.getAmount() * 0.7);
    }
}
