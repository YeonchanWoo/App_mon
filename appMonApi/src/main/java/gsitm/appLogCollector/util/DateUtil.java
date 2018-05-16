package gsitm.appLogCollector.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DateUtil {

	public static String getcurrentDate() {
		Calendar cal = Calendar.getInstance();
		
		int year = cal.get(Calendar.YEAR);
		int month = (cal.get(Calendar.MONTH) + 1);;
		int date = cal.get(Calendar.DATE);
		
		String strDate = Integer.toString(year) +
				((month<10)?"0"+Integer.toString(month):Integer.toString(month)) +
				((date<10)?"0"+Integer.toBinaryString(date):Integer.toString(date));
		
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd",Locale.KOREA);
//		sdf.format(strDate);
		
		return strDate;
		
	}
}
