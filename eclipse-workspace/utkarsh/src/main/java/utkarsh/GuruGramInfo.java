package utkarsh;

import utils.AppendExcel;
import utils.GuruGramCovidSite;

public class GuruGramInfo {

	public static void main (String args[])
	{//            bedList.add(new HospitalBed(hospitalName,"108, 1075",phoneNumber,NormalBeds,oxygenBeds,icuBeds,ventilatorBeds,"","","",totalBeds,getTimeStamp()));
        String[] header = {"Hospital","HelpLine","Phone No","Normal Bed","Oxygen Bed","ICU Beds","Ventilator Beds","Notes","Comments","Up Votes","Total Beds","Updated On"};
        int[] hiddenCols = {};
        AppendExcel.writeFile("TestExcel.xlsx",header,hiddenCols,new GuruGramCovidSite().performRowOperation(),false);
	}
	
}
