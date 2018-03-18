package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import system.Common;

public class MemberMatchDao {
	public int memberSK;
	public MemberMatch memberMatch;
	Connection conn = null;
	PreparedStatement ps = null;
	
	public MemberMatchDao(int memberSK) {
		super();
		this.memberSK = memberSK;
		Common.initDB();
	}
	
	public MemberMatchDao(MemberMatch memberMatch) {
		super();
		this.memberMatch = memberMatch;
		Common.initDB();
	}
	public MemberMatchDao close() {
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

	public void toMatch(int shareTopCateorySk) {
		if(!isAccept(shareTopCateorySk)) {
			if(todaymatch()) {
				
			}else {
				getAcceoptMatch(memberSK);
			}
		}
	}

	public boolean isAccept(int shareTopCateorySk) {
		int sk = 0;
		int acceptTopCategorySK = 0;
		String sql = "select sk, accept_top_category_sk from member_match"
				+ " where accept_member_sk = ? "
				+ " and post_day = ?";
		try {
			conn = DriverManager.getConnection(Common.DBURL, Common.DBACCOUNT,
					Common.DBPWD);
			ps = conn.prepareStatement(sql);
			ps.setInt(1, memberSK);
			ps.setInt(2, shareTopCateorySk);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				sk = rs.getInt(1);
				acceptTopCategorySK = rs.getInt(2);
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		close();
		if(sk > 0) {
			if(acceptTopCategorySK != shareTopCateorySk) {
				updateAcceptTopCategorySK(acceptTopCategorySK,sk);
			}
			return true;
		}else {
			return false;
		}
	}

	public void updateAcceptTopCategorySK(int acceptTopCategorySK, int SK) {
		int count = 0;
		String sql = "update member_match set accept_top_category_sk = ? where sk = ? ";
		try {
			conn = DriverManager.getConnection(Common.DBURL, Common.DBACCOUNT,
					Common.DBPWD);
			ps = conn.prepareStatement(sql);
			ps.setInt(1, acceptTopCategorySK);
			ps.setInt(2, SK);
			ps.executeUpdate();
			count = ps.executeUpdate();		
			
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("updateLoginDate err = " + sql);
		}
		close();	
		if(count == 0 ) {
			System.out.println("updateAcceptTopCategorySK err sk: " + SK);
		}
	}
	
	public int getAcceoptMatch(int memberSK) {
		
		FriendRelationDao friendRelationDao = new FriendRelationDao(memberSK);
		List<String> avoidFBIDList = friendRelationDao.getAvoidFBIDList();
		
		MemberDao memberDao = new MemberDao(memberSK);
		List<Integer> avoidMemberSKList = memberDao.getAvoidMatchMemberList(avoidFBIDList);
		
		DiaryLocationDao diaryLocationDao = new DiaryLocationDao(memberSK);
		List<Integer> matchLocationFreindList = diaryLocationDao.getLocationFriendList(avoidMemberSKList);
		
		matchLocationFreindList = avoidTodayMatch(matchLocationFreindList);
		
		CategoryMatchDao categoryMatchDao = new CategoryMatchDao(memberSK);
		List<Integer> matchLocationFreindWithCategoryMatchList = categoryMatchDao.matchWithLocationFreind(matchLocationFreindList);
		
		
		return 0;
	}

	public List<Integer> avoidTodayMatch(List<Integer> matchLocationFreindList) {
		String testMemberSK = String.valueOf(memberSK);
		for(Integer testSK:matchLocationFreindList) {
			if(testSK > 0) {
				testMemberSK = testMemberSK + "," +  String.valueOf(testSK);
			}
		}
		
		String sql = "select group_concat( request_member_sk ) as request_member_sk_list from member_match"
				+ " where request_member_sk in ( " + testMemberSK +") "
				+ " and post_day = ?";
		
		try {
			conn = DriverManager.getConnection(Common.DBURL, Common.DBACCOUNT,
					Common.DBPWD);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, Common.getNowDayString());
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				String skList = rs.getString(1);
				if(!skList.equals("") || !skList.isEmpty()) {
					String[] skArray = skList.split(",");
					for(String tempSK:skArray) {
						matchLocationFreindList = Common.removieListIntegerElement(matchLocationFreindList,Integer.parseInt(tempSK) );
					}
				}
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		testMemberSK = String.valueOf(memberSK);
		for(Integer testSK:matchLocationFreindList) {
			if(testSK > 0) {
				testMemberSK = testMemberSK + "," +  String.valueOf(testSK);
			}
		}
		sql = "select group_concat( accept_member_sk ) as accept_member_sk_list from member_match"
				+ " where accept_member_sk in ( " + testMemberSK +") "
				+ " and post_day = ?";
		
		try {
			conn = DriverManager.getConnection(Common.DBURL, Common.DBACCOUNT,
					Common.DBPWD);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, Common.getNowDayString());
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				String skList = rs.getString(1);
				if(!skList.equals("") || !skList.isEmpty()) {
					String[] skArray = skList.split(",");
					for(String tempSK:skArray) {
						matchLocationFreindList = Common.removieListIntegerElement(matchLocationFreindList,Integer.parseInt(tempSK) );
					}
				}
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		return matchLocationFreindList;
	}
	
	public boolean todaymatch() {
		// TODO Auto-generated method stub
		return false;
	}


}
