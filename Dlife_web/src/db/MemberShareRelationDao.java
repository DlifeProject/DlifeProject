package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import system.Common;
import api.MatchFriendItem;

public class MemberShareRelationDao {

	public int memberSK = 0;
	public Hashtable<Integer,ShareItem> fromMeToFriendHash = new Hashtable<Integer,ShareItem>();
	public Hashtable<Integer,ShareItem> fromFriendToMeHash = new Hashtable<Integer,ShareItem>();
	public Hashtable<Integer,String> topCategoryHash = new Hashtable<Integer,String>();
	
	class ShareItem {
		public String topCategory = "";
		public int topCategorySK = 0;
		public int isShareable = 0;
		public String name = "";
		public String postDay = "0000-00-00";
	}
	
	Connection conn = null;
	PreparedStatement ps = null;
	
	public MemberShareRelationDao(int memberSK) {
		super();
		this.memberSK = memberSK;
		Common.initDB();
	}
	
	public MemberShareRelationDao close() {
		if(ps != null) {
			try {
				ps.close();
			} catch (SQLException e1) {
				System.out.println("MemberShareRelationDao ps close");
				e1.printStackTrace();
			}
		}
		if(conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				System.out.println("MemberShareRelationDao conn close");
				e.printStackTrace();
			}
		}
		return this;
	}
	
	public ArrayList<MatchFriendItem> getMemberShareRelationList(){
		
		ArrayList<MatchFriendItem> shareList = new ArrayList<MatchFriendItem>();
		CategoryDao categoryDao = new CategoryDao();
		topCategoryHash = categoryDao.getTopCategoryHash();
		setFromMeToFriendHash();
		setFromFriendToMeHash();
		
		for(int i: fromMeToFriendHash.keySet()) {
			if(fromFriendToMeHash.get(i) != null) {
				MatchFriendItem matchFriendItem = new MatchFriendItem();
				matchFriendItem.setMyCategory(fromMeToFriendHash.get(i).topCategory );
				matchFriendItem.setMyCategorySK(fromMeToFriendHash.get(i).topCategorySK);
				matchFriendItem.setMyFriendCategory(fromFriendToMeHash.get(i).topCategory);
				matchFriendItem.setMyFriendCategorySK(fromFriendToMeHash.get(i).topCategorySK);
				matchFriendItem.setMyFriendName(fromMeToFriendHash.get(i).name);
				matchFriendItem.setMyFriendSK(i);
				if(fromMeToFriendHash.get(i).isShareable == 0 || fromFriendToMeHash.get(i).isShareable == 0) {
					matchFriendItem.setIsShareable(0);
				}else {
					matchFriendItem.setIsShareable(1);
				}
				matchFriendItem.setPostDay(fromMeToFriendHash.get(i).postDay);
				shareList.add(matchFriendItem);
			}
		}
		
		return shareList;
	}

	private void setFromMeToFriendHash() {
		String sql = "select " 
		+ " member_share_relation.sk, member_share_relation.from_member_sk, member_share_relation.to_member_sk, member_share_relation.category_sk, member_share_relation.is_shareable"
		+ ",member_share_relation.post_day, member_share_relation.post_date, member.nick_name"
		+ " from member_share_relation, member" 
		+ " where member_share_relation.to_member_sk = member.sk "
		+ " and member_share_relation.from_member_sk = ? "
		+ " order by member_share_relation.sk desc";	
		
//		System.out.println("setFromMeToFriendHash");
//		System.out.println(sql);
//		System.out.println(memberSK);
				
		try {
			conn = DriverManager.getConnection(Common.DBURL, Common.DBACCOUNT, Common.DBPWD);
			ps = conn.prepareStatement(sql);
			ps.setInt(1, memberSK);
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
				
				int friendSK = rs.getInt("member_share_relation.to_member_sk");
				ShareItem shareItem = new ShareItem();
				shareItem.topCategorySK = rs.getInt("member_share_relation.category_sk");
				shareItem.topCategory = topCategoryHash.get(rs.getInt("member_share_relation.category_sk"));
				shareItem.isShareable = rs.getInt("member_share_relation.is_shareable");
				shareItem.name = rs.getString("member.nick_name");
				shareItem.postDay = rs.getString("member_share_relation.post_day");
				fromMeToFriendHash.put(friendSK, shareItem);		
			}
		
		} catch (SQLException e) {
			e.printStackTrace();
		}
		close();	
	}
	
	private void setFromFriendToMeHash() {
		String sql = "select " 
		+ " member_share_relation.sk, member_share_relation.from_member_sk, member_share_relation.to_member_sk, member_share_relation.category_sk, member_share_relation.is_shareable"
		+ ",member_share_relation.post_day, member_share_relation.post_date, member.nick_name"
		+ " from member_share_relation, member" 
		+ " where member_share_relation.from_member_sk = member.sk "
		+ " and member_share_relation.to_member_sk = ? "
		+ " order by member_share_relation.sk desc";
		
//		System.out.println("setFromMeToFriendHash");
//		System.out.println(sql);
//		System.out.println(memberSK);
		
		try {
			conn = DriverManager.getConnection(Common.DBURL, Common.DBACCOUNT, Common.DBPWD);
			ps = conn.prepareStatement(sql);
			ps.setInt(1, memberSK);
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
				int friendSK = rs.getInt("member_share_relation.from_member_sk");
				ShareItem shareItem = new ShareItem();
				shareItem.topCategorySK = rs.getInt("member_share_relation.category_sk");
				shareItem.topCategory = topCategoryHash.get(rs.getInt("member_share_relation.category_sk"));
				shareItem.isShareable = rs.getInt("member_share_relation.is_shareable");
				shareItem.name = rs.getString("member.nick_name");
				shareItem.postDay = rs.getString("member_share_relation.post_day");
				fromFriendToMeHash.put(friendSK, shareItem);		
			}
		
		} catch (SQLException e) {
			e.printStackTrace();
		}
		close();	
	}

	public boolean isMyFriendShareCategory(int friendSK, int friendCategorySK) {
		if(isMyShareFriend(friendSK) && isFriendShare(friendSK,friendCategorySK)) {
			return true;
		}else {
			return false;
		}
	}


	private boolean isMyShareFriend(int friendSK) {
		int sk = 0;
		String sql = "select " 
		+ " sk"
		+ " from member_share_relation" 
		+ " where from_member_sk = ? "
		+ " and to_member_sk = ?"
		+ " and is_shareable = 1";
		try {
			conn = DriverManager.getConnection(Common.DBURL, Common.DBACCOUNT, Common.DBPWD);
			ps = conn.prepareStatement(sql);
			ps.setInt(1, memberSK);
			ps.setInt(2, friendSK);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				sk = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		close();	
		
		if(sk > 0) {
			return true;
		}else {
			System.out.println("isMyShareFriend : " + sql);
			return false;
		}
	}
	
	private boolean isFriendShare(int friendSK, int friendCategorySK) {
		int sk = 0;
		String sql = "select " 
		+ " sk"
		+ " from member_share_relation" 
		+ " where from_member_sk = ? "
		+ " and to_member_sk = ?"
		+ " and category_sk = ?"
		+ " and is_shareable = 1";
		try {
			conn = DriverManager.getConnection(Common.DBURL, Common.DBACCOUNT, Common.DBPWD);
			ps = conn.prepareStatement(sql);
			ps.setInt(1, friendSK);
			ps.setInt(2, memberSK);
			ps.setInt(3, friendCategorySK);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				sk = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		close();	
		
		if(sk > 0) {
			return true;
		}else {
			return false;
		}
	}

	public List<Integer> myfriendSKList() {
		List<Integer> myfriendList = new ArrayList<Integer>();
		String sql = "select to_member_sk"
				+ " from member_share_relation" 
				+ " where from_member_sk = ? ";
		try {
			conn = DriverManager.getConnection(Common.DBURL, Common.DBACCOUNT, Common.DBPWD);
			ps = conn.prepareStatement(sql);
			ps.setInt(1, memberSK);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				myfriendList.add(rs.getInt(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		close();	
		return myfriendList;
	}

	
	
}
