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
//    static String[] header = {"Hospital","Contact","Phone No","Normal Bed","Oxygen Bed","Address","Found Useful(1hr)","Notes","Comments","Up Votes","Total Beds","Updated On"};
    static String[] header = {"Hospital","Contact","Phone No","Normal Bed","Oxygen Bed","Address","Total Beds","Updated On"};
    static int[] hiddenCols = {5};

	public static void writeFile(String fileName,String [] headers,int []hiddenColumns,List<HospitalBed> beds,boolean append){
		System.out.println("CHECKING FILE: "+fileName);
		header=headers;
		hiddenCols=hiddenColumns;
		 if(new File(fileName).exists() )
        {
        	System.out.println("EXCEL Exists: "+fileName);
        	 if(append==false)
    			 createExcel(fileName, beds);
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
		    
		       	CellStyle cellStyle = studentsSheet.createCellStyle();
	            cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
	            cellStyle.setFillForegroundColor( IndexedColors.LIGHT_GREEN.getIndex());
	            cellStyle.setFillBackgroundColor(IndexedColors.DARK_YELLOW.getIndex());
		       
		       lastRow++;
		       System.out.println("WRITING DATA");
		       //Row row = worksheet.createRow(++lastRow);
		       for (HospitalBed bed : beds){
	            	
	                Row row = worksheet.createRow(lastRow++);

	                row.createCell(0).setCellValue(bed.getHospitalName());
	                row.createCell(1).setCellValue(bed.getContact());
	                row.createCell(2).setCellValue(bed.getPhone());
    
//	                System.out.println("#### WRITING NORMAL BED: "+bed.getNormalBeds());
//	                System.out.println("#### WRITING OXYGEN BED: "+bed.getOxygenBeds());
	                
	                Cell nbCell = row.createCell(3);nbCell.setCellValue(bed.getNormalBeds());nbCell.setCellStyle(cellStyle);
	                Cell oxCell = row.createCell(4);oxCell.setCellValue(bed.getOxygenBeds());oxCell.setCellStyle(cellStyle);
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
		       System.out.println("WRITING DATA COMPLETD: "+fileName);
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
            //String[] header = {"Hospital","Contact","Phone No","Normal Bed","Oxygen Bed","Address","Found Useful(1hr)","Notes","Comments","Up Votes","Total Beds","Updated On"};
            //            String[] header = {"Hospital","Contact","Phone No","Normal Bed","Oxygen Bed","Address","Total Beds","Updated On"};
            sheet.createFreezePane( 0, 2, 0, 2 );

            Row headerRow = sheet.createRow(0);
            System.out.println("CREATING HEADERS");
            Font headerFont = wbk.createFont();
            headerFont.setColor(IndexedColors.YELLOW.getIndex());
            headerFont.setFontHeight((short) 330);
            short bold = 14;
            headerFont.setBoldweight(bold);
            CellStyle headerCellStyle = wbk.createCellStyle();
            headerCellStyle.setFont(headerFont);
            headerCellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
            headerCellStyle.setFillForegroundColor( IndexedColors.BLACK.getIndex());
            headerCellStyle.setFillBackgroundColor(IndexedColors.YELLOW.getIndex());
           // headerCellStyle.setC
            for(int i = 0; i < header.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(header[i]);
                cell.setCellStyle(headerCellStyle);
                //sheet.autoSizeColumn(i);
            }
           // sheet.setColumnWidth(0,160);
            sheet.setColumnWidth(0,16000);
            sheet.setColumnWidth(1,6000);
            sheet.setColumnWidth(2,6000);
            sheet.setColumnWidth(3,6000);//Normal Bed
            sheet.setColumnWidth(4,6000);//Oxygen Beds
            sheet.setColumnWidth(5,12000);//Address
            //Hide 
            for(int col:hiddenCols)
            {
            	sheet.setColumnWidth(col,1);	
            }
            sheet.setColumnWidth(11,6000);//Normal Bed

            FileOutputStream fos = new FileOutputStream(fileName);
            wbk.write(fos);
            fos.close();



        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
