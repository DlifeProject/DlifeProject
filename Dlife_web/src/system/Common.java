package system;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Common {

//	public final static String DBNAME = "dlife";
//	public final static String DBURLGETDATA = "useUnicode=true&characterEncoding=UTF8&zeroDateTimeBehavior=convertToNull";
//	public final static String DBURL = "jdbc:mysql://localhost:8889/" + DBNAME + "?" + DBURLGETDATA;
//	public final static String DBACCOUNT = "root";
//	public final static String DBPWD = "root";

	 public final static String DBNAME = "dlife";
	 public final static String DBURLGETDATA = "useUnicode=true&characterEncoding=UTF8&zeroDateTimeBehavior=convertToNull";
	 //public final static String DBURL = "jdbc:mysql://192.168.0.205:3306/" + DBNAME + "?" + DBURLGETDATA;
	 //public final static String DBURL = "jdbc:mysql://localhost:3306/" + DBNAME + "?" + DBURLGETDATA;
	 public final static String DBURL = "jdbc:mysql://114.34.110.248:7060/" + DBNAME + "?" + DBURLGETDATA;
	 public final static String DBACCOUNT = "dlife";
	 public final static String DBPWD = "dlife";

	public final static String DBKEY = "sk";
	public final static String[] DEFAULTCATE = { "Shopping", "Hobby", "Learning", "Travel", "Work" };
	public final static String[] NONSHARECATE = { "Work" };
	public final static int CATEGORYMATCHDAY = -100;
	public final static int SHAREABELDIARYCOUNT = 1;

	public static void initDB() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static String getNowDateTimeString() {
		// 目前時間
		Date date = new Date();
		// 設定日期格式
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// 進行轉換
		String dateString = sdf.format(date);
		return dateString;
	}

	public static String getNowDayString() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String day = sdf.format(date);
		return day;
	}

	public static String getNowTimestampString() {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		return timestamp.toString();
	}

	public static String stringToYear(String diaryDay) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String year = "0000";
		try {
			Date today = dateFormat.parse(diaryDay);
			DateFormat chtDateFormat = new SimpleDateFormat("yyyy");
			year = chtDateFormat.format(today);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return year;
	}

	public static String stringToMonth(String diaryDay) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String month = "00";
		try {
			Date today = dateFormat.parse(diaryDay);
			DateFormat chtDateFormat = new SimpleDateFormat("MM");
			month = chtDateFormat.format(today);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return month;
	}

	public static String stringToDay(String diaryDay) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String day = "00";
		try {
			Date today = dateFormat.parse(diaryDay);
			DateFormat chtDateFormat = new SimpleDateFormat("dd");
			day = chtDateFormat.format(today);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return day;
	}

	public static String getTimeString(int i) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, i);
		Date date = cal.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String timestring = sdf.format(date);
		return timestring;
	}

	public static String setDateFormat(int year, int month, int dayOfMonth) {
		String momthString = "";
		if (month < 10) {
			momthString = "0" + String.valueOf(month);
		} else {
			momthString = String.valueOf(month);
		}
		String dayString = "";
		if (dayOfMonth < 10) {
			dayString = "0" + String.valueOf(dayOfMonth);
		} else {
			dayString = String.valueOf(dayOfMonth);
		}
		return String.valueOf(year) + "-" + momthString + "-" + dayString;
	}

	public static double timestampToHr(long categoryTimestamp) {
		long hr = categoryTimestamp / 100000;
		double doubleHr = (double) hr / 36;
		DecimalFormat df = new DecimalFormat("##.0");
		doubleHr = Double.parseDouble(df.format(doubleHr));
		return doubleHr;
	}

	public static List<Integer> removieListIntegerElement(List<Integer> matchLocationFreindList, int parseInt) {
		for (int i = 0, len = matchLocationFreindList.size(); i < len; i++) {
			if (matchLocationFreindList.get(i) == parseInt) {
				matchLocationFreindList.remove(i);
				len--;
				i--;
			}
		}
		return matchLocationFreindList;
	}
}
