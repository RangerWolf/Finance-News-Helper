package utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.horrabin.horrorss.RssParser;
import org.horrabin.horrorss.util.DateParser;

public class USDateParser implements DateParser{

	@Override
	public Date getDate(String date, int rssType) throws Exception {
		Date res = null;
		if(rssType != RssParser.TYPE_RSS) {
			System.out.println("Error Type, should be RSS only");
		} else {
			String pattern = "E, dd MMM yyy HH:mm:ss";
			try {
				SimpleDateFormat sd = new SimpleDateFormat(pattern, Locale.US);
				if(date.endsWith("GMT")) {
					sd.setTimeZone(TimeZone.getTimeZone("GMT"));
				}
				res = sd.parse(date);
//				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss E");
//				System.out.println(date + "\t" + sdf.format(res));
			} catch (Exception e) {
				System.out.println("Error parsing date: " + date + " [Type: " + rssType + "] --" + e.toString());
			}	
		}
		return res;
	}

}
