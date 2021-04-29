package utils;

import entities.HospitalBed;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.util.List;

public class ExcelUtil {

    public static void createExcel(List<HospitalBed> beds){

        try {
            //Workbook wbk = WorkbookFactory.create(new FileInputStream(new File("CovidResources.xlsx")));

            Workbook wbk = new XSSFWorkbook();
            Sheet sheet = wbk.createSheet("Beds");

            String[] header = {"Hospital Name","Hospital Address", "Contact","Total Beds","Vacant Beds","Last Updated"};

            //Create header row
            Row headerRow = sheet.createRow(0);

            Font headerFont = wbk.createFont();
            headerFont.setColor(IndexedColors.DARK_BLUE.getIndex());
            short bold = 8;
            headerFont.setBoldweight(bold);

            CellStyle headerCellStyle = wbk.createCellStyle();
            headerCellStyle.setFont(headerFont);
            headerCellStyle.setFillBackgroundColor(IndexedColors.DARK_YELLOW.getIndex());

            for(int i = 0; i < header.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(header[i]);
                cell.setCellStyle(headerCellStyle);
            }

            int rownum = 1;
            for (HospitalBed bed : beds){
                Row dataRow = sheet.createRow(rownum++);
                dataRow.createCell(0).setCellValue(bed.getHospitalName());
                dataRow.createCell(1).setCellValue(bed.getAddress());
                dataRow.createCell(2).setCellValue(bed.getContact());
                dataRow.createCell(3).setCellValue(bed.getTotalAvailability());
                dataRow.createCell(4).setCellValue(bed.getVacant());
                dataRow.createCell(5).setCellValue(bed.getLastUpdateTime());
            }


            FileOutputStream fos = new FileOutputStream("CovidData.xlsx");
            
            wbk.write(fos);
            fos.close();



        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
