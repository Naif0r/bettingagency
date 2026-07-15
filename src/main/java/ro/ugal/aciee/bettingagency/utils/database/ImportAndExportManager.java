package ro.ugal.aciee.bettingagency.utils.database;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import ro.ugal.aciee.bettingagency.utils.file.export.WriteExcelFormula;
import ro.ugal.aciee.bettingagency.utils.file.importer.ReadExcelFormula;

import java.io.File;
import java.io.FileOutputStream;

public class ImportAndExportManager {
    private static final ReadExcelFormula readExcelFormula = new ReadExcelFormula();
    private static final WriteExcelFormula writeExcelFormula = new WriteExcelFormula();

    public static boolean importExcelFile(File file)  {
        try {
            Workbook workbook = WorkbookFactory.create(file);

            readExcelFormula.importAccounts(workbook.getSheet("ACCOUNT"));
            readExcelFormula.importSport(workbook.getSheet("SPORT"));
            readExcelFormula.importTeam(workbook.getSheet("TEAM"));
            readExcelFormula.importMatch(workbook.getSheet("MATCH"));
            readExcelFormula.importRate(workbook.getSheet("RATE"));
            readExcelFormula.importBet(workbook.getSheet("BET"));
            readExcelFormula.importBetRate(workbook.getSheet("BET_RATE"));

            workbook.close();

            return true;
        } catch (Exception e){
            return false;
        }
    }

    public static boolean exportExcelFile(String file)  {
        try {
            Workbook workbook = new XSSFWorkbook();

            writeExcelFormula.exportAccount(workbook);
            writeExcelFormula.exportSport(workbook);
            writeExcelFormula.exportTeam(workbook);
            writeExcelFormula.exportMatch(workbook);
            writeExcelFormula.exportRate(workbook);
            writeExcelFormula.exportBet(workbook);
            writeExcelFormula.exportBetRate(workbook);

            try (FileOutputStream out = new FileOutputStream(file)) {
                workbook.write(out);
            }

            workbook.close();

            return true;
        } catch (Exception e){
            return false;
        }
    }
}
