package ro.ugal.aciee.bettingagency.utils.file.importer;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import ro.ugal.aciee.bettingagency.dao.BetRateDAO;
import ro.ugal.aciee.bettingagency.model.*;
import ro.ugal.aciee.bettingagency.model.enums.*;
import ro.ugal.aciee.bettingagency.service.*;

public class ReadExcelFormula {
    private final AccountService accountService = new AccountService();
    private final SportService sportService = new SportService();
    private final TeamService teamService = new TeamService();
    private final MatchService matchService = new MatchService();
    private final RateService rateService = new RateService();
    private final BetService betService = new BetService();
    private final BetRateDAO betRateDAO = new BetRateDAO();

    public void importAccounts(Sheet sheet) {
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {

            Row row = sheet.getRow(i);

            Account account = new Account();

            account.setUserId((int) row.getCell(0).getNumericCellValue());
            account.setUsername(row.getCell(1).getStringCellValue());
            account.setPassword(row.getCell(2).getStringCellValue());
            account.setRole(Role.valueOf(row.getCell(3).getStringCellValue()));
            account.setAccountStatus(AccountStatus.valueOf(row.getCell(4).getStringCellValue()));
            account.setBalance(row.getCell(5).getNumericCellValue());

            accountService.importer(account);
        }
    }

    public void importSport(Sheet sheet) {
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);

            Sport sport = new Sport();

            sport.setSportId((int) row.getCell(0).getNumericCellValue());
            sport.setSportName(row.getCell(1).getStringCellValue());

            sportService.importer(sport);
        }
    }

    public void importTeam(Sheet sheet) {
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);

            Team team = new Team();

            team.setTeamId((int) row.getCell(0).getNumericCellValue());
            team.setSportId((int) row.getCell(1).getNumericCellValue());
            team.setTeamName(row.getCell(2).getStringCellValue());

            teamService.importer(team);
        }
    }

    public void importMatch(Sheet sheet) {
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);

            Match match = new Match();

            match.setMatchId((int) row.getCell(0).getNumericCellValue());
            match.setSportId((int) row.getCell(1).getNumericCellValue());
            match.setTeam1Id((int) row.getCell(2).getNumericCellValue());
            match.setTeam2Id((int) row.getCell(3).getNumericCellValue());
            match.setMatchDate(row.getCell(4).getLocalDateTimeCellValue());
            match.setMatchStatus(MatchStatus.valueOf(row.getCell(5).getStringCellValue()));
            match.setTeam1Score((int) row.getCell(6).getNumericCellValue());
            match.setTeam2Score((int) row.getCell(7).getNumericCellValue());

            matchService.importer(match);
        }
    }

    public void importRate(Sheet sheet) {
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);

            Rate rate = new Rate();

            rate.setRateId((int) row.getCell(0).getNumericCellValue());
            rate.setMatchId((int) row.getCell(1).getNumericCellValue());
            rate.setType(RateType.valueOf(row.getCell(2).getStringCellValue()));
            rate.setValue(row.getCell(3).getNumericCellValue());
            rate.setRateStatus(RateStatus.valueOf(row.getCell(4).getStringCellValue()));

            rateService.importer(rate);
        }
    }

    public void importBet(Sheet sheet) {
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);

            Bet bet = new Bet();

            bet.setBetId((int) row.getCell(0).getNumericCellValue());
            bet.setUserId((int) row.getCell(1).getNumericCellValue());
            bet.setAmount(row.getCell(2).getNumericCellValue());
            bet.setBetStatus(BetStatus.valueOf(row.getCell(3).getStringCellValue()));
            bet.setCreatedAt(row.getCell(4).getLocalDateTimeCellValue());
            bet.setTotalOdds(row.getCell(5).getNumericCellValue());
            bet.setPossibleWin(row.getCell(6).getNumericCellValue());

            betService.importer(bet);
        }
    }

    public void importBetRate(Sheet sheet) {
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);

            BetRate betRate = new BetRate();

            betRate.setBetId((int) row.getCell(0).getNumericCellValue());
            betRate.setRateId((int) row.getCell(1).getNumericCellValue());

            betRateDAO.save(betRate);
        }
    }
}
