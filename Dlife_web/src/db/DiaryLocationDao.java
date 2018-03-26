package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.ArrayList;
import java.util.Hashtable;

import system.Common;

public class DiaryLocationDao {
	
	public int memberSK;
	public DiaryLocation diaryLocation;
	Connection conn = null;
	PreparedStatement ps = null;
	
	public DiaryLocationDao(int memberSK) {
		super();
		this.memberSK = memberSK;
		Common.initDB();
	}
	
	public DiaryLocationDao(DiaryLocation diaryLocation) {
		super();
		this.diaryLocation = diaryLocation;
		Common.initDB();
	}
	public DiaryLocationDao close() {
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
	public void insert() {
		
		int insertCount = 0;
		if(!isDiaryDetailSKExist()) {
			String sql = "insert into diary_location" 
					+ "(member_sk, diary_detail_sk, google_id, google_name, longitude,"
					+ " latitude, diary_day, post_date)"
					+ " VALUES (" + " ?,?,?,?,?," + " ?,?,?" + ")";
			
			try {
				conn = DriverManager.getConnection(Common.DBURL, Common.DBACCOUNT, Common.DBPWD);
				ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				ps.setInt(1, diaryLocation.getMember_sk());
				ps.setInt(2, diaryLocation.getDiary_detail_sk());
				ps.setString(3, diaryLocation.getGoogle_id());
				ps.setString(4, diaryLocation.getGoogle_name());
				ps.setDouble(5, diaryLocation.getLongitude());
				ps.setDouble(6, diaryLocation.getLatitude());
				ps.setString(7, diaryLocation.getDiary_day());
				ps.setString(8, Common.getNowDateTimeString());
				ps.executeUpdate();

				ResultSet tableKeys = ps.getGeneratedKeys();
				tableKeys.next();
				insertCount = tableKeys.getInt(1);

			} catch (SQLException e) {
				e.printStackTrace();
			}

			close();	
		}
		if(insertCount > 0) {
			System.out.println("diary_location insert success");
		}else {
			System.out.println("diary_location insert fail : " + diaryLocation.getDiary_detail_sk());
		}
	}
	public boolean isDiaryDetailSKExist() {
		int diaryLocationSK = 0;
		ResultSet rs = null;
		String sql = "select sk from diary_location where diary_detail_sk = ? ";
		try {
			conn = DriverManager.getConnection(Common.DBURL, Common.DBACCOUNT, Common.DBPWD);
			ps = conn.prepareStatement(sql);
			ps.setInt(1, diaryLocation.getDiary_detail_sk());
			rs = ps.executeQuery();
			if (rs.next()) {
				diaryLocationSK = rs.getInt(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		close();
		if(diaryLocationSK > 0) {
			return true;
		}else {
			return false;
		}
	}

	public List<Integer> getLocationFriendList(List<Integer> avoidMemberSKList) {
		
		List<Integer> allLocationOfFriend  = new ArrayList<Integer>();
		Hashtable<Integer, Integer> friendSCcountMap = new Hashtable<Integer, Integer>();
		String avoidMemberSK = String.valueOf(memberSK);
		for(Integer avoidSK:avoidMemberSKList) {
			if(avoidSK > 0) {
				avoidMemberSK = avoidMemberSK + "," +  String.valueOf(avoidSK);
			}
		}
		try {
			ResultSet rs = null;
			conn = DriverManager.getConnection(Common.DBURL, Common.DBACCOUNT, Common.DBPWD);
			for(int i=0; i<= 7; i++) {
				String checkDay = Common.getTimeString (i * (-1));
				String sql = "select google_id from diary_location"
						+ " where member_sk = ?"
						+ " and diary_day = ?";
				ps = conn.prepareStatement(sql);
				ps.setInt(1, memberSK);
				ps.setString(2, checkDay);
				rs = ps.executeQuery();
				while (rs.next()) {
					String googleID = rs.getString(1);
					DiaryLocationDao thisDiaryLocationDao = new DiaryLocationDao(memberSK);
					List<Integer> locationOfFriend = thisDiaryLocationDao.aLocationFreindMatch(googleID,checkDay,avoidMemberSK);
					if(locationOfFriend.size() > 0) {
						for(int tempSK: locationOfFriend) {
							if(tempSK > 0) {
								if(friendSCcountMap.containsKey(tempSK)) {
									int tempCount = friendSCcountMap.get(tempSK);
									tempCount = tempCount + 1;
									friendSCcountMap.remove(tempSK);
									friendSCcountMap.put(tempSK, tempCount);								
								}else {
									friendSCcountMap.put(tempSK, 1);
								}
							}
						}
					}
				}					
			}	
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		for (Integer key : friendSCcountMap.keySet()) {
			allLocationOfFriend.add(key);
	    }
		return allLocationOfFriend;
	}

	public List<Integer> aLocationFreindMatch(String googleID, String checkDay, String avoidMemberSK) {
		
		List<Integer> locationOfFriend = new ArrayList<Integer>();
		String skListString = "";
		String sql = "select group_concat( member_sk ) as member_sk_list from diary_location"
				+ " where google_id = ?"
				+ " and diary_day = ?"
				+ " and member_sk not in ( " + avoidMemberSK + ")";
		
		ResultSet rs = null;
		try {
			conn = DriverManager.getConnection(Common.DBURL, Common.DBACCOUNT, Common.DBPWD);
			ps = conn.prepareStatement(sql);
			ps.setInt(1, memberSK);
			ps.setString(2, checkDay);
			rs = ps.executeQuery();
			if(rs.next()) {
				skListString = rs.getString(1);
			}
			if(!skListString.equals("")) {
				String[] skArray = skListString.split(",");
				for(String skString:skArray) {
					locationOfFriend.add(Integer.valueOf(skString));
				}
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return locationOfFriend;
	}

	public void deleteByDiaryDetailSK(int dieayDetailSK) {
		int rowCount = 0;
		String sql = "delete from diary_location" + " where diary_detail_sk = ?";
		try {
			conn = DriverManager.getConnection(Common.DBURL, Common.DBACCOUNT, Common.DBPWD);
			ps = conn.prepareStatement(sql);
			ps.setInt(1, dieayDetailSK);
			rowCount = ps.executeUpdate();
			if(rowCount > 0) {
				System.out.println("deleteByDiaryDetailSK success sk:" + dieayDetailSK);
			}else {
				System.out.println("deleteByDiaryDetailSK fail 1 sk:" + dieayDetailSK);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("deleteByDiaryDetailSK fail 2 sk:" + dieayDetailSK);
		}
		close();
		
	}
	

}
