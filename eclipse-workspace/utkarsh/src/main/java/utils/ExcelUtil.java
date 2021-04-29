package utils;

import org.apache.poi.hssf.util.PaneInformation;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import entities.HospitalBed;
public class ExcelUtil {
	
    public static void createExcel(String fileName,List<HospitalBed> beds){//
        try {
            //Workbook wbk = WorkbookFactory.create(new FileInputStream(new File("CovidResources.xlsx")));
            Workbook wbk = new XSSFWorkbook();
            Sheet sheet = null ;
            int rowNum = 1;

            if(new File(fileName).exists())
            {
            	System.out.println("EXCEL Exists: "+fileName);
            	FileInputStream myxls = new FileInputStream(fileName);
            	Workbook mySheet = new XSSFWorkbook(myxls);
            	Sheet worksheet = mySheet.getSheet("Beds");
                rowNum=worksheet.getLastRowNum();
                System.out.println("GOT LAST ROW: "+rowNum++);
                myxls.close();
                sheet =wbk.createSheet("Beds");
            }
            else
            {	System.out.println("EXCEL Doesn't Exist Creating: "+fileName);
            	sheet = wbk.createSheet("Beds");
                String[] header = {"Hospital","Contact","Phone No","Normal Bed","Oxygen Bed","Address","Found Useful(1hr)","Notes","Comments","Up Votes","Total Beds","Updated On"};
                Row headerRow = sheet.createRow(0);
                System.out.println("CREATING HEADERS");
                Font headerFont = wbk.createFont();
                headerFont.setColor(IndexedColors.DARK_BLUE.getIndex());
                short bold = 8;
                headerFont.setBoldweight(bold);
                CellStyle headerCellStyle = wbk.createCellStyle();
                headerCellStyle.setFont(headerFont);
                headerCellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
                headerCellStyle.setFillForegroundColor( IndexedColors.LIGHT_GREEN.getIndex());
                headerCellStyle.setFillBackgroundColor(IndexedColors.DARK_YELLOW.getIndex());

                for(int i = 0; i < header.length; i++) {
                    Cell cell = headerRow.createCell(i);
                    cell.setCellValue(header[i]);
                    cell.setCellStyle(headerCellStyle);
                }

            }

            for (HospitalBed bed : beds){
            	System.out.println("WRITING DATA");
                Row dataRow = sheet.createRow(rowNum++);

                dataRow.createCell(0).setCellValue(bed.getHospitalName());
                dataRow.createCell(1).setCellValue(bed.getContact());
                dataRow.createCell(2).setCellValue(bed.getPhone());
                dataRow.createCell(3).setCellValue(bed.getNormalBeds());
                dataRow.createCell(4).setCellValue(bed.getOxygenBeds());
                dataRow.createCell(5).setCellValue(bed.getAddress());
                dataRow.createCell(6).setCellValue(bed.getFoundUseful());
                dataRow.createCell(7).setCellValue(bed.getNotes());
                dataRow.createCell(8).setCellValue(bed.getComments());
                dataRow.createCell(9).setCellValue(bed.getVotes());
                dataRow.createCell(10).setCellValue(bed.getTotalBeds());
                dataRow.createCell(11).setCellValue(bed.getLastUpdateTime());
            }


            FileOutputStream fos = new FileOutputStream(fileName);
            wbk.write(fos);
            fos.close();



        }catch (Exception e){
            e.printStackTrace();
        }
    }
  
}
