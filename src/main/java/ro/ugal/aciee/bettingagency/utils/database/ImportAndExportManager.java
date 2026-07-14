package ro.ugal.aciee.bettingagency.utils.database;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import ro.ugal.aciee.bettingagency.utils.file.importer.ReadExcelFormula;

import java.io.File;
import java.io.IOException;

public class ImportAndExportManager {
    private static final ReadExcelFormula readExcelFormula = new ReadExcelFormula();

    public static void importExcelFile(File file) throws IOException {
        Workbook workbook = WorkbookFactory.create(file);

        readExcelFormula.importAccounts(workbook.getSheet("ACCOUNT"));
        readExcelFormula.importSport(workbook.getSheet("SPORT"));
        readExcelFormula.importTeam(workbook.getSheet("TEAM"));
        readExcelFormula.importMatch(workbook.getSheet("MATCH"));
        readExcelFormula.importRate(workbook.getSheet("RATE"));
        readExcelFormula.importBet(workbook.getSheet("BET"));
        readExcelFormula.importBetRate(workbook.getSheet("BET_RATE"));

        workbook.close();
    }
}
