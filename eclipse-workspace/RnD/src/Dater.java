import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Dater {

	public static void main(String[] args) {
		 System.out.println(System.getProperty("line.terminator"));

//System.out.println(getTimeStamp());
	}

	private static String getTimeStamp() {
		// TODO Auto-generated method stub
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
	    SimpleDateFormat sdf1 = new SimpleDateFormat("MM_dd_yyyy_HH-mm");
       return sdf1.format(timestamp);       
	}

}
