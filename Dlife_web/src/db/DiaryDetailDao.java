package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import api.CategorySum;
import api.PiechartData;
import api.SelectDate;
import system.Common;

public class DiaryDetailDao {

	public int memberSK;
	DiaryDetail diaryDetail;
	Connection conn = null;
	PreparedStatement ps = null;

	public DiaryDetailDao(int memberSK) {
		super();
		this.memberSK = memberSK;
		Common.initDB();
	}

	public DiaryDetailDao(DiaryDetail diaryDetail) {
		super();
		this.diaryDetail = diaryDetail;
		Common.initDB();
	}

	public DiaryDetailDao close() {
		if (ps != null) {
			try {
				ps.close();
			} catch (SQLException e1) {
				System.out.println("DiaryDetailDao ps close");
				e1.printStackTrace();
			}
		}
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				System.out.println("DiaryDetailDao conn close");
				e.printStackTrace();
			}
		}
		return this;
	}

	public List<DiaryDetail> autoDiary() {
		List<DiaryDetail> ltDiary = new ArrayList<DiaryDetail>();
		int timesCount = 0;
		double sumLongitude = 0.0;
		double sumLatitude = 0.0;
		double sumAltitude = 0.0;
		LocationTrace ltLast = new LocationTrace();
		LocationTrace ltforward = new LocationTrace();

		LocationTraceDao ltGetAll = new LocationTraceDao(memberSK);
		List<LocationTrace> ltLocationTrace = ltGetAll.getAllLocationTrace();
		System.out.println(ltLocationTrace);

		for (LocationTrace l : ltLocationTrace) {

			if (l.getForward_sk() == 0) {
				if (timesCount > 1) {
					ltDiary.add(autoDiary(ltLast, ltforward, timesCount, sumLongitude, sumLatitude, sumAltitude));
				}
				ltLast = new LocationTrace(l);
				ltforward = new LocationTrace(l);
				timesCount = 1;
				sumLongitude = l.getLongitude();
				sumLatitude = l.getLatitude();
				sumAltitude = l.getAltitude();
			} else if (l.getForward_sk() > 0) {
				ltLast = new LocationTrace(l);
				timesCount++;
				sumLongitude += l.getLongitude();
				sumLatitude += l.getLatitude();
				sumAltitude += l.getAltitude();
			} else {
				if (timesCount > 1) {
					ltDiary.add(autoDiary(ltLast, ltforward, timesCount, sumLongitude, sumLatitude, sumAltitude));
				}
				timesCount = 0;
				ltLast = null;
				ltforward = null;
				sumLongitude = 0.0;
				sumLatitude = 0.0;
				sumAltitude = 0.0;
			}
			System.out.println("timesCount : " + timesCount);
			System.out.println("locationTrace sk : " + l.getSk() + " => ");
		}
		return ltDiary;
	}

	private DiaryDetail autoDiary(LocationTrace ltLast, LocationTrace ltforward, int timesCount, double sumLongitude,
			double sumLatitude, double sumAltitude) {

		DiaryDetail newDiary = new DiaryDetail();
		newDiary.setMember_sk(memberSK);
		newDiary.setStart_stamp(ltforward.getPost_stamp());
		newDiary.setStart_date(ltforward.getPost_date());
		newDiary.setEnd_stamp(ltLast.getPost_stamp());
		newDiary.setEnd_date(ltLast.getPost_date());
		newDiary.setPost_date(Common.getNowDateTimeString());
		newDiary.setPost_day(Common.getNowDayString());
		newDiary.setLongitude(sumLongitude / timesCount);
		newDiary.setLatitude(sumLatitude / timesCount);
		newDiary.setAltitude(sumAltitude / timesCount);

		return newDiary;
	}

	public int insert() {
		int insertCount = 0;
		memberSK = diaryDetail.getMember_sk();
		String sql = "insert into diary_detail" + "(member_sk, top_category_sk, member_location_sk, note, start_stamp,"
				+ " end_stamp, start_date, end_date, post_day, post_date," + " longitude, latitude, altitude)"
				+ " VALUES (" + " ?,?,?,?,?," + " ?,?,?,?,?," + " ?,?,?)";
		try {
			conn = DriverManager.getConnection(Common.DBURL, Common.DBACCOUNT, Common.DBPWD);
			ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			ps.setInt(1, diaryDetail.getMember_sk());
			ps.setInt(2, diaryDetail.getTop_category_sk());
			ps.setInt(3, diaryDetail.getMember_location_sk());
			ps.setString(4, diaryDetail.getNote());
			ps.setString(5, diaryDetail.getStart_stamp());
			ps.setString(6, diaryDetail.getEnd_stamp());
			ps.setString(7, diaryDetail.getStart_date());
			ps.setString(8, diaryDetail.getEnd_date());
			ps.setString(9, Common.getNowDayString());
			ps.setString(10, Common.getNowDateTimeString());
			ps.setDouble(11, diaryDetail.getLongitude());
			ps.setDouble(12, diaryDetail.getLatitude());
			ps.setDouble(13, diaryDetail.getAltitude());
			ps.executeUpdate();

			ResultSet tableKeys = ps.getGeneratedKeys();
			tableKeys.next();
			insertCount = tableKeys.getInt(1);

		} catch (SQLException e) {
			e.printStackTrace();
		}

		close();
		return insertCount;
	}

	public int upload() {
		int count = 0;
		memberSK = diaryDetail.getMember_sk();

		String sql = "update diary_detail set top_category_sk=?, note=? where sk = ?";
		try {
			conn = DriverManager.getConnection(Common.DBURL, Common.DBACCOUNT, Common.DBPWD);
			ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			ps.setInt(1, diaryDetail.getTop_category_sk());
			ps.setString(2, diaryDetail.getNote());
			ps.setInt(3, diaryDetail.getSk());
			count = ps.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		close();
		if (count > 0) {

			CategoryDao categoryDao = new CategoryDao();
			
			DiaryCategoryDao diaryCategoryDao = new DiaryCategoryDao(memberSK);
			diaryCategoryDao.updateDiaryCategory(diaryDetail.getSk(), diaryDetail.getTop_category_sk(),
					categoryDao.getCategoryType(diaryDetail.getTop_category_sk()));

			System.out.println("diaryDetail.getSk() : " + memberSK);

			CategoryMatchDao categoryMatchDao = new CategoryMatchDao(memberSK);
			categoryMatchDao.updateCategoryMatch(getCategoryMatch(Common.CATEGORYMATCHDAY));
		}
		return count;
	}

	public CategorySum getfinalDiary(String categoryType) {

		CategorySum categorySum = new CategorySum();
		int categorySK = new CategoryDao().getCategory_sk(categoryType);

		String sql = "select sk, post_day, note from diary_detail" + " where member_sk = ? "
				+ " and top_category_sk = ?" + " order by sk desc limit 1";

		try {
			conn = DriverManager.getConnection(Common.DBURL, Common.DBACCOUNT, Common.DBPWD);
			ps = conn.prepareStatement(sql);
			ps.setInt(1, memberSK);
			ps.setInt(2, categorySK);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				int diarySK = rs.getInt(1);
				String diaryDay = rs.getString(2);
				String note = rs.getString(3);
				
				System.out.println("diaryDay : " + diaryDay);
				int a = new DiaryPhotoDao(diarySK).getMainPhotoSK();
				categorySum.setDiaryPhotoSK(new DiaryPhotoDao(diarySK).getMainPhotoSK());
				categorySum.setYear(Common.stringToYear(diaryDay));
				categorySum.setMonth(Common.stringToMonth(diaryDay));
				categorySum.setDay(Common.stringToDay(diaryDay));
				categorySum.setNote(note);

			} else {
				categorySum.setDiaryPhotoSK(0);
				categorySum.setYear("00");
				categorySum.setMonth("00");
				categorySum.setDay("00");
				categorySum.setNote("");
			}
		} catch (SQLException e) {
			categorySum.setDiaryPhotoSK(0);
			categorySum.setYear("00");
			categorySum.setMonth("00");
			categorySum.setDay("00");
			categorySum.setNote(""); // TODO Auto-generated catch block
			e.printStackTrace();
		}

		close();
		return categorySum;
	}

	public int getDayDiaryCount(int day) {
		day = day * (-1);
		int diarycount = 0;

		String sql = "select count(sk) as data_count from diary_detail" + " where member_sk = ? "
				+ " and post_day >= ?";

		try {
			conn = DriverManager.getConnection(Common.DBURL, Common.DBACCOUNT, Common.DBPWD);
			ps = conn.prepareStatement(sql);
			ps.setInt(1, memberSK);
			ps.setString(2, Common.getTimeString(day));
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				diarycount = rs.getInt(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		close();
		return diarycount;
	}

	// 第一頁RecyclerView全取
	public List<DiaryDetail> getRecyclerViewDiary() {

		List<DiaryDetail> ltDiaryDetail = new ArrayList<DiaryDetail>();

		ResultSet rs = null;

		String sql = "select " + " sk, member_sk, top_category_sk, member_location_sk, note"
				+ ",start_stamp, end_stamp, start_date, end_date, post_day"
				+ ",post_date, longitude, latitude, altitude" + " from diary_detail" + " where member_sk = ? ";
		try {
			conn = DriverManager.getConnection(Common.DBURL, Common.DBACCOUNT, Common.DBPWD);
			ps = conn.prepareStatement(sql);
			ps.setInt(1, memberSK);
			rs = ps.executeQuery();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			while (rs.next()) {
				DiaryDetail diaryDetail = new DiaryDetail();
				diaryDetail.setSk(rs.getInt(1));
				diaryDetail.setMember_sk(rs.getInt(2));
				diaryDetail.setTop_category_sk(rs.getInt(3));
				diaryDetail.setMember_location_sk(rs.getInt(4));
				diaryDetail.setNote(rs.getString(5));
				diaryDetail.setStart_stamp(rs.getString(6));
				diaryDetail.setEnd_stamp(rs.getString(7));
				diaryDetail.setStart_date(rs.getString(8));
				diaryDetail.setEnd_date(rs.getString(9));
				diaryDetail.setPost_day(rs.getString(10));
				diaryDetail.setPost_date(rs.getString(11));
				diaryDetail.setLongitude(rs.getDouble(12));
				diaryDetail.setLatitude(rs.getDouble(13));
				diaryDetail.setAltitude(rs.getDouble(14));
				
				DiaryLocationDao diaryLocationDao = new DiaryLocationDao(rs.getInt(2));
				diaryDetail.setPlaceID(diaryLocationDao.getDiaryPlaceID(rs.getInt(1))); 
				
				
				ltDiaryDetail.add(diaryDetail);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		close();
		return ltDiaryDetail;
	}

	public List<DiaryDetail> getDiaryByCategoryTypeBySK(int categorySK) {
		CategoryDao categoryDao = new CategoryDao();
		List<DiaryDetail> ltDiaryDetail = getDiaryByCategoryType(categoryDao.getCategoryType(categorySK));
		return ltDiaryDetail;
	}

	public List<DiaryDetail> getDiaryByCategoryType(String categoryType) {

		List<DiaryDetail> ltDiaryDetail = new ArrayList<DiaryDetail>();

		System.out.println(categoryType);

		int top_category_sk = 0;
		ResultSet rs = null;
		if (categoryType.equals("nonCategory")) {
			String sql = "select " + " sk, member_sk, top_category_sk, member_location_sk, note"
					+ ",start_stamp, end_stamp, start_date, end_date, post_day"
					+ ",post_date, longitude, latitude, altitude" + " from diary_detail" + " where member_sk = ? "
					+ " order by sk desc limit 5";

			try {
				conn = DriverManager.getConnection(Common.DBURL, Common.DBACCOUNT, Common.DBPWD);
				ps = conn.prepareStatement(sql);
				ps.setInt(1, memberSK);
				rs = ps.executeQuery();

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {
			top_category_sk = new CategoryDao().getCategory_sk(categoryType);
			String sql = "select " + " sk, member_sk, top_category_sk, member_location_sk, note"
					+ ",start_stamp, end_stamp, start_date, end_date, post_day"
					+ ",post_date, longitude, latitude, altitude" + " from diary_detail" + " where member_sk = ? "
					+ " and top_category_sk = ?" + " order by sk  desc limit 5";
			try {
				conn = DriverManager.getConnection(Common.DBURL, Common.DBACCOUNT, Common.DBPWD);
				ps = conn.prepareStatement(sql);
				ps.setInt(1, memberSK);
				ps.setInt(2, top_category_sk);
				rs = ps.executeQuery();

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		try {
			while (rs.next()) {
				DiaryDetail diaryDetail = new DiaryDetail();
				diaryDetail.setSk(rs.getInt(1));
				diaryDetail.setMember_sk(rs.getInt(2));
				diaryDetail.setTop_category_sk(rs.getInt(3));
				diaryDetail.setMember_location_sk(rs.getInt(4));
				diaryDetail.setNote(rs.getString(5));
				diaryDetail.setStart_stamp(rs.getString(6));
				diaryDetail.setEnd_stamp(rs.getString(7));
				diaryDetail.setStart_date(rs.getString(8));
				diaryDetail.setEnd_date(rs.getString(9));
				diaryDetail.setPost_day(rs.getString(10));
				diaryDetail.setPost_date(rs.getString(11));
				diaryDetail.setLongitude(rs.getDouble(12));
				diaryDetail.setLatitude(rs.getDouble(13));
				diaryDetail.setAltitude(rs.getDouble(14));
				ltDiaryDetail.add(diaryDetail);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		close();
		return ltDiaryDetail;
	}

	public int getDayDiaryCategotyCount(int day, int topCategorySK) {
		day = day * (-1);
		int diarycount = 0;

		String sql = "select count(sk) as data_count from diary_detail" + " where member_sk = ? " + " and post_day >= ?"
				+ " and top_category_sk = ?";

		try {
			conn = DriverManager.getConnection(Common.DBURL, Common.DBACCOUNT, Common.DBPWD);
			ps = conn.prepareStatement(sql);
			ps.setInt(1, memberSK);
			ps.setString(2, Common.getTimeString(day));
			ps.setInt(3, topCategorySK);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				diarycount = rs.getInt(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		close();
		return diarycount;

	}

	public ArrayList<CategoryMatchFormat> getCategoryMatch(int days) {

		ArrayList<CategoryMatchFormat> summary = new ArrayList<CategoryMatchFormat>();

		String sql = "select " + "category.category_type as category_type"
				+ ",max(diary_detail.sk) as max_diary_detail_sk" + ",min(diary_detail.sk) as min_diary_detail_sk"
				+ ",sum(diary_detail.start_stamp) as sum_start_stamp" + ",sum(diary_detail.end_stamp) as sum_end_stamp"
				+ ",(sum(diary_detail.start_stamp) - sum(diary_detail.end_stamp)) as total_stamp"
				+ ",category.sk as top_category_sk" + ",count(diary_detail.sk) as diary_count"
				+ " from diary_detail, category" + " where" + " diary_detail.top_category_sk = category.sk"
				+ " and category.is_shareable = 1" + " and category.is_useful = 1" + " and diary_detail.member_sk = ?"
				+ " and diary_detail.post_day >= ?" + " group by diary_detail.top_category_sk"
				+ " order by total_stamp desc";

		System.out.println(sql);
		System.out.println("memberSK " + memberSK);
		System.out.println("Common.getTimeString(Common.CATEGORYMATCHDAY) " + Common.getTimeString(Common.CATEGORYMATCHDAY));

		try {
			conn = (Connection) DriverManager.getConnection(Common.DBURL, Common.DBACCOUNT, Common.DBPWD);
			ps = (PreparedStatement) conn.prepareStatement(sql);
			ps.setInt(1, memberSK);
			ps.setString(2, Common.getTimeString(Common.CATEGORYMATCHDAY));
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				System.out.println("category_type:"+ rs.getString(1)+ " diary_count:" + rs.getInt(8));
				CategoryMatchFormat categoryMatchFormat = new CategoryMatchFormat();
				categoryMatchFormat.category_type = rs.getString(1);
				categoryMatchFormat.max_diary_detail_sk = rs.getInt(2);
				categoryMatchFormat.min_diary_detail_sk = rs.getInt(3);
				categoryMatchFormat.sum_start_stamp = Long.valueOf(rs.getString(4));
				categoryMatchFormat.sum_end_stamp = Long.valueOf(rs.getString(5));
				categoryMatchFormat.top_category_sk = rs.getInt(7);
				categoryMatchFormat.diary_count = rs.getInt(8);
				summary.add(categoryMatchFormat);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		close();
		return summary;
	}

	public boolean isMemberOwn(int dieayDetailSK) {

		int realMemberOwnSK = 0;
		String sql = "select sk, member_sk " + " from diary_detail" + " where sk = ? ";
		try {
			conn = DriverManager.getConnection(Common.DBURL, Common.DBACCOUNT, Common.DBPWD);
			ps = conn.prepareStatement(sql);
			ps.setInt(1, dieayDetailSK);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				realMemberOwnSK = rs.getInt("member_sk");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		close();
		if (realMemberOwnSK == memberSK) {
			return true;
		} else {
			return false;
		}
	}

	public boolean deleteDiaryDetail(int dieayDetailSK) {

		// delete photo
		DiaryPhotoDao diaryPhotoDao = new DiaryPhotoDao();
		if (diaryPhotoDao.deleteDiaryPhoto(memberSK, dieayDetailSK)) {
			// delete diary_category
			DiaryCategoryDao diaryCategoryDao = new DiaryCategoryDao(memberSK);
			if (diaryCategoryDao.deleteDiaryCategoryDao(dieayDetailSK)) {
				// delete diary_detail
				if (doDeleteDiaryDetail(dieayDetailSK)) {
					// recalculate category_match
					CategoryMatchDao categoryMatchDao = new CategoryMatchDao(memberSK);
					categoryMatchDao.updateCategoryMatch(getCategoryMatch(Common.CATEGORYMATCHDAY));
					
					DiaryLocationDao diaryLocationDao = new DiaryLocationDao(memberSK);
					diaryLocationDao.deleteByDiaryDetailSK(dieayDetailSK);

				} else {
					System.out.println("deleteDiaryDetail doDeleteDiaryDetail err!");
					return false;
				}

			} else {
				System.out.println("deleteDiaryDetail deleteDiaryCategoryDao err!");
				return false;
			}

		} else {
			System.out.println("deleteDiaryDetail deleteDiaryPhoto err!");
			return false;
		}

		return false;
	}

	private boolean doDeleteDiaryDetail(int dieayDetailSK) {
		int rowCount = 0;
		String sql = "delete from diary_detail" + " where sk = ?";
		try {
			conn = DriverManager.getConnection(Common.DBURL, Common.DBACCOUNT, Common.DBPWD);
			ps = conn.prepareStatement(sql);
			ps.setInt(1, dieayDetailSK);
			rowCount = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("doDeleteDiaryDetail fail sk:" + dieayDetailSK);
		}
		close();

		if (rowCount > 0) {
			return true;
		} else {
			return false;
		}
	}

	// 大程
	public PiechartData getPiechartDate(String categoryType, String startDay, String endDay) {
		int category_sk = new CategoryDao().getCategory_sk(categoryType);
		String sql = "select sum(end_stamp - start_stamp) as sumTimestamp, count(*) as diaryCount"
				+ " from diary_detail"
				+ " where"
				+ " post_day >= ? and post_day <= ? and top_category_sk = ? and member_sk = ?"
				+ " group by top_category_sk;";
		double categoryTimeHr = 0;
		long categoryTimestamp = 0;
		int diaryCount = 0;
		try {
			conn = (Connection) DriverManager.getConnection(Common.DBURL, Common.DBACCOUNT, Common.DBPWD);
			ps = (PreparedStatement) conn.prepareStatement(sql);
			ps.setString(1, startDay);
			ps.setString(2, endDay);
			ps.setInt(3, category_sk);
			ps.setInt(4, memberSK);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				categoryTimestamp = rs.getLong(1);
				diaryCount = rs.getInt(2);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		categoryTimeHr = Common.timestampToHr(categoryTimestamp);
		return new PiechartData(categoryType, categoryTimeHr,diaryCount);
	}
	
	public DiaryDetail getDiaryBySK(int diaryDetailSK) {
		DiaryDetail diaryDetail = new DiaryDetail();
		ResultSet rs = null;
		String sql = "select " 
				+ " sk, member_sk, top_category_sk, member_location_sk, note"
				+ ",start_stamp, end_stamp, start_date, end_date, post_day"
				+ ",post_date, longitude, latitude, altitude" 
				+ " from diary_detail" 
				+ " where sk = ? ";

		try {
			conn = DriverManager.getConnection(Common.DBURL, Common.DBACCOUNT, Common.DBPWD);
			ps = conn.prepareStatement(sql);
			ps.setInt(1, diaryDetailSK);
			rs = ps.executeQuery();
			if (rs.next()) {
				diaryDetail.setSk(rs.getInt(1));
				diaryDetail.setMember_sk(rs.getInt(2));
				diaryDetail.setTop_category_sk(rs.getInt(3));
				diaryDetail.setMember_location_sk(rs.getInt(4));
				diaryDetail.setNote(rs.getString(5));
				diaryDetail.setStart_stamp(rs.getString(6));
				diaryDetail.setEnd_stamp(rs.getString(7));
				diaryDetail.setStart_date(rs.getString(8));
				diaryDetail.setEnd_date(rs.getString(9));
				diaryDetail.setPost_day(rs.getString(10));
				diaryDetail.setPost_date(rs.getString(11));
				diaryDetail.setLongitude(rs.getDouble(12));
				diaryDetail.setLatitude(rs.getDouble(13));
				diaryDetail.setAltitude(rs.getDouble(14));
			}

		} catch (SQLException e) {
			System.out.println("getDiaryBySK error sk: " + diaryDetailSK);
			e.printStackTrace();
		}
		return diaryDetail;
	}

	public List<DiaryDetail> getDiaryBetweenDays(String startDay, String endDay, int categoryListIndex) {
		List<DiaryDetail> ltDiaryDetail = new ArrayList<DiaryDetail>();
		ResultSet rs = null;
		if (categoryListIndex == -1) {
			String sql = "select " 
					+ " sk, member_sk, top_category_sk, member_location_sk, note"
					+ ",start_stamp, end_stamp, start_date, end_date, post_day"
					+ ",post_date, longitude, latitude, altitude" 
					+ " from diary_detail" 
					+ " where member_sk = ? "
					+ " and post_day >= ?"
					+ " and post_day <= ?"
					+ " order by sk desc limit 5";

			try {
				conn = DriverManager.getConnection(Common.DBURL, Common.DBACCOUNT, Common.DBPWD);
				ps = conn.prepareStatement(sql);
				ps.setInt(1, memberSK);
				ps.setString(2, startDay);
				ps.setString(3, endDay);
				rs = ps.executeQuery();

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {
			String sql = "select " 
					+ " sk, member_sk, top_category_sk, member_location_sk, note"
					+ ",start_stamp, end_stamp, start_date, end_date, post_day"
					+ ",post_date, longitude, latitude, altitude" 
					+ " from diary_detail" 
					+ " where member_sk = ? "
					+ " and post_day >= ?"
					+ " and post_day <= ?"
					+ " and top_category_sk = ?" 
					+ " order by sk  desc limit 5";
			try {
				conn = DriverManager.getConnection(Common.DBURL, Common.DBACCOUNT, Common.DBPWD);
				ps = conn.prepareStatement(sql);
				ps.setInt(1, memberSK);
				ps.setString(2, startDay);
				ps.setString(3, endDay);
				ps.setInt(4, categoryListIndex );
				rs = ps.executeQuery();
				System.out.println(sql);
				System.out.println(memberSK);
				System.out.println(startDay);
				System.out.println(endDay);
				System.out.println("categoryListIndex : " + categoryListIndex );

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		try {
			while (rs.next()) {
				DiaryDetail diaryDetail = new DiaryDetail();
				diaryDetail.setSk(rs.getInt(1));
				diaryDetail.setMember_sk(rs.getInt(2));
				diaryDetail.setTop_category_sk(rs.getInt(3));
				diaryDetail.setMember_location_sk(rs.getInt(4));
				diaryDetail.setNote(rs.getString(5));
				diaryDetail.setStart_stamp(rs.getString(6));
				diaryDetail.setEnd_stamp(rs.getString(7));
				diaryDetail.setStart_date(rs.getString(8));
				diaryDetail.setEnd_date(rs.getString(9));
				diaryDetail.setPost_day(rs.getString(10));
				diaryDetail.setPost_date(rs.getString(11));
				diaryDetail.setLongitude(rs.getDouble(12));
				diaryDetail.setLatitude(rs.getDouble(13));
				diaryDetail.setAltitude(rs.getDouble(14));
				ltDiaryDetail.add(diaryDetail);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		close();
		return ltDiaryDetail;
	}

}
