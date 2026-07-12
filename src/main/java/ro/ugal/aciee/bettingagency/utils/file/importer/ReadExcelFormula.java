package ro.ugal.aciee.bettingagency.utils.file.importer;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import ro.ugal.aciee.bettingagency.model.Account;
import ro.ugal.aciee.bettingagency.model.enums.AccountStatus;
import ro.ugal.aciee.bettingagency.model.enums.Role;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReadExcelFormula {
    public boolean importAccounts(Sheet sheet) throws IOException {
        List<Account> accounts = new ArrayList<>();
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {

            Row row = sheet.getRow(i);

            Account account = new Account();

            account.setUserId((int) row.getCell(0).getNumericCellValue());
            account.setUsername(row.getCell(1).getStringCellValue());
            account.setPassword(row.getCell(2).getStringCellValue());
            account.setRole(Role.valueOf(row.getCell(3).getStringCellValue()));
            account.setAccountStatus(AccountStatus.valueOf(row.getCell(4).getStringCellValue()));
            account.setBalance(row.getCell(5).getNumericCellValue());

            accounts.add(account);
        }
        return true;
    }
}
