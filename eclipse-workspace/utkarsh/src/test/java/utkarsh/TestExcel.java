package utkarsh;

import utils.AppendExcel;

import java.util.ArrayList;
import java.util.List;

import entities.HospitalBed;

public class TestExcel {

	public static void main(String[] args) {
		HospitalBed s1= new HospitalBed(" 1#" ," #" ," #" ," 3" ," 4" ," #" ," #" ," #" ," #" ," #" ," #" ," #" );
		HospitalBed s2= new HospitalBed(" 2#" ," #" ," #" ," 33" ,"44" ," #" ," #" ," #" ," #" ," #" ," #" ," #" );
		HospitalBed s3= new HospitalBed(" 3#" ," #" ," #" ," 333" ,"444" ," #" ," #" ," #" ," #" ," #" ," #" ," #" );
List <HospitalBed> s = new ArrayList<HospitalBed>(); 
		s.add(s1);
		s.add(s2);
		s.add(s3);
	    String[] header = {"Hospital","TEST","Phone No","Normal Bed","Oxygen Bed","Address","Total Beds","Updated On"};
	    int [] hiddenHeader= {4};
        AppendExcel.writeFile("TestExcel.xlsx",header,hiddenHeader,s,false);

	}

}
