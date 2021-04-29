package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import entities.HospitalBed;

public class AppendExcel {
    static String[] header = {"Hospital","Contact","Phone No","Normal Bed","Oxygen Bed","Address","Found Useful(1hr)","Notes","Comments","Up Votes","Total Beds","Updated On"};

	public static void append(String fileName,List<HospitalBed> beds){
		 if(new File(fileName).exists())
        {
        	System.out.println("EXCEL Exists: "+fileName);
        }
		 else
			 createExcel(fileName, beds);
         try
		   {
		       FileInputStream myxls = new FileInputStream(fileName);
		       XSSFWorkbook studentsSheet = new XSSFWorkbook(myxls);
		       XSSFSheet worksheet = studentsSheet.getSheet("Beds");
		       int lastRow=worksheet.getLastRowNum();
		       System.out.println("lastRow: "+lastRow);
		       
		       lastRow++;
		       System.out.println("WRITING DATA");
		       //Row row = worksheet.createRow(++lastRow);
		       for (HospitalBed bed : beds){
	            	
	                Row row = worksheet.createRow(lastRow++);

	                row.createCell(0).setCellValue(bed.getHospitalName());
	                row.createCell(1).setCellValue(bed.getContact());
	                row.createCell(2).setCellValue(bed.getPhone());
	                row.createCell(3).setCellValue(bed.getNormalBeds());
	                row.createCell(4).setCellValue(bed.getOxygenBeds());
	                row.createCell(5).setCellValue(bed.getAddress());
	                row.createCell(6).setCellValue(bed.getFoundUseful());
	                row.createCell(7).setCellValue(bed.getNotes());
	                row.createCell(8).setCellValue(bed.getComments());
	                row.createCell(9).setCellValue(bed.getVotes());
	                row.createCell(10).setCellValue(bed.getTotalBeds());
	                row.createCell(11).setCellValue(bed.getLastUpdateTime());
	            }
		       //row.createCell(0).setCellValue("Dr.Hola");
		       myxls.close();
		       FileOutputStream output_file =new FileOutputStream(new File(fileName));  
		       //write changes
		       studentsSheet.write(output_file);
		       output_file.close();
		       System.out.println(" is successfully written");
		    }
		    catch(Exception e)
		    {
		    	System.out.println(e.getMessage());
		    }
		}
	public static void createExcel(String fileName,List<HospitalBed> beds){//
        try {
            //Workbook wbk = WorkbookFactory.create(new FileInputStream(new File("CovidResources.xlsx")));
            Workbook wbk = new XSSFWorkbook();
            Sheet sheet = null ;
            int rowNum = 1;

          	System.out.println("EXCEL Doesn't Exist Creating: "+fileName);
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

            FileOutputStream fos = new FileOutputStream(fileName);
            wbk.write(fos);
            fos.close();



        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
