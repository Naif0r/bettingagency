package ro.ugal.aciee.bettingagency.utils.file.export;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import ro.ugal.aciee.bettingagency.dao.BetRateDAO;
import ro.ugal.aciee.bettingagency.model.*;
import ro.ugal.aciee.bettingagency.service.*;

import java.sql.SQLException;
import java.util.List;

public class WriteExcelFormula {
    private final AccountService accountService = new AccountService();
    private final SportService sportService = new SportService();
    private final TeamService teamService = new TeamService();
    private final MatchService matchService = new MatchService();
    private final RateService rateService = new RateService();
    private final BetService betService = new BetService();
    private final BetRateDAO betRateDAO = new BetRateDAO();

    public void exportAccount(Workbook workbook) throws SQLException {
        Sheet sheet = workbook.createSheet("ACCOUNT");

        Row header = sheet.createRow(0);

        header.createCell(0).setCellValue("user_id");
        header.createCell(1).setCellValue("username");
        header.createCell(2).setCellValue("password");
        header.createCell(3).setCellValue("role");
        header.createCell(4).setCellValue("account_status");
        header.createCell(5).setCellValue("balance");

        List<Account> accountList = accountService.getAll();

        int rowIndex = 1;

        for (Account account : accountList) {
            Row row = sheet.createRow(rowIndex++);

            row.createCell(0).setCellValue(account.getUserId());
            row.createCell(1).setCellValue(account.getUsername());
            row.createCell(2).setCellValue(account.getPassword());
            row.createCell(3).setCellValue(account.getRole().name());
            row.createCell(4).setCellValue(account.getAccountStatus().name());
            row.createCell(5).setCellValue(account.getBalance());
        }

        autoSize(sheet, 6);
    }

    public void exportSport(Workbook workbook) throws SQLException {
        Sheet sheet = workbook.createSheet("SPORT");

        Row header = sheet.createRow(0);

        header.createCell(0).setCellValue("sport_id");
        header.createCell(1).setCellValue("sport_name");

        List<Sport> sportList = sportService.getAll();

        int rowIndex = 1;

        for (Sport sport : sportList) {
            Row row = sheet.createRow(rowIndex++);

            row.createCell(0).setCellValue(sport.getSportId());
            row.createCell(1).setCellValue(sport.getSportName());
        }

        autoSize(sheet, 2);
    }

    public void exportTeam(Workbook workbook) throws SQLException {
        Sheet sheet = workbook.createSheet("TEAM");

        Row header = sheet.createRow(0);

        header.createCell(0).setCellValue("team_id");
        header.createCell(1).setCellValue("sport_id");
        header.createCell(2).setCellValue("team_name");

        List<Team> teamList = teamService.getAll();

        int rowIndex = 1;

        for (Team team : teamList) {
            Row row = sheet.createRow(rowIndex++);

            row.createCell(0).setCellValue(team.getTeamId());
            row.createCell(1).setCellValue(team.getSportId());
            row.createCell(2).setCellValue(team.getTeamName());
        }

        autoSize(sheet, 3);
    }

    public void exportMatch(Workbook workbook) throws SQLException {
        Sheet sheet = workbook.createSheet("MATCH");

        Row header = sheet.createRow(0);

        header.createCell(0).setCellValue("match_id");
        header.createCell(1).setCellValue("sport_id");
        header.createCell(2).setCellValue("team1_id");
        header.createCell(3).setCellValue("team2_id");
        header.createCell(4).setCellValue("match_date");
        header.createCell(5).setCellValue("match_status");
        header.createCell(6).setCellValue("team1_score");
        header.createCell(7).setCellValue("team2_score");

        List<Match> matchList = matchService.getAll();

        int rowIndex = 1;

        for (Match match : matchList) {
            Row row = sheet.createRow(rowIndex++);

            row.createCell(0).setCellValue(match.getMatchId());
            row.createCell(1).setCellValue(match.getSportId());
            row.createCell(2).setCellValue(match.getTeam1Id());
            row.createCell(3).setCellValue(match.getTeam2Id());
            row.createCell(4).setCellValue(match.getMatchDate());
            row.createCell(5).setCellValue(match.getMatchStatus().name());
            row.createCell(6).setCellValue(match.getTeam1Score());
            row.createCell(7).setCellValue(match.getTeam2Score());
        }

        autoSize(sheet, 8);
    }

    public void exportRate(Workbook workbook) throws SQLException {
        Sheet sheet = workbook.createSheet("RATE");

        Row header = sheet.createRow(0);

        header.createCell(0).setCellValue("rate_id");
        header.createCell(1).setCellValue("match_id");
        header.createCell(2).setCellValue("type");
        header.createCell(3).setCellValue("value");
        header.createCell(4).setCellValue("rate_status");

        List<Rate> rateList = rateService.getAll();

        int rowIndex = 1;

        for (Rate rate : rateList) {
            Row row = sheet.createRow(rowIndex++);

            row.createCell(0).setCellValue(rate.getRateId());
            row.createCell(1).setCellValue(rate.getMatchId());
            row.createCell(2).setCellValue(rate.getType().name());
            row.createCell(3).setCellValue(rate.getValue());
            row.createCell(4).setCellValue(rate.getRateStatus().name());
        }

        autoSize(sheet, 5);
    }

    public void exportBet(Workbook workbook) throws SQLException {
        Sheet sheet = workbook.createSheet("BET");

        Row header = sheet.createRow(0);

        header.createCell(0).setCellValue("bet_id");
        header.createCell(1).setCellValue("user_id");
        header.createCell(2).setCellValue("amount");
        header.createCell(3).setCellValue("bet_status");
        header.createCell(4).setCellValue("created_at");
        header.createCell(5).setCellValue("total_odds");
        header.createCell(6).setCellValue("possible_win");

        List<Bet> betList = betService.getAll();

        int rowIndex = 1;

        for (Bet bet : betList) {
            Row row = sheet.createRow(rowIndex++);

            row.createCell(0).setCellValue(bet.getBetId());
            row.createCell(1).setCellValue(bet.getUserId());
            row.createCell(2).setCellValue(bet.getAmount());
            row.createCell(3).setCellValue(bet.getBetStatus().name());
            row.createCell(4).setCellValue(bet.getCreatedAt());
            row.createCell(5).setCellValue(bet.getTotalOdds());
            row.createCell(6).setCellValue(bet.getPossibleWin());
        }

        autoSize(sheet, 7);
    }

    public void exportBetRate(Workbook workbook) throws SQLException {
        Sheet sheet = workbook.createSheet("BET_RATE");

        Row header = sheet.createRow(0);

        header.createCell(0).setCellValue("bet_id");
        header.createCell(1).setCellValue("rate_id");

        List<BetRate> betRateList = betRateDAO.getAll();

        int rowIndex = 1;

        for (BetRate betRate : betRateList) {
            Row row = sheet.createRow(rowIndex++);

            row.createCell(0).setCellValue(betRate.getBetId());
            row.createCell(1).setCellValue(betRate.getRateId());
        }

        autoSize(sheet, 2);
    }

    private void autoSize(Sheet sheet, int size) {
        for (int i = 0; i < size; i++) {
            sheet.autoSizeColumn(i);
        }
    }
}
