package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import system.Common;

public class FriendRelationDao {
	public int memberSK = 0;
	public FriendRelation friendRelation;
	Connection conn = null;
	PreparedStatement ps = null;	
	
	public FriendRelationDao(int memberSK) {
		super();
		this.memberSK = memberSK;
		Common.initDB();
	}
	
	public FriendRelationDao(FriendRelation friendRelation) {
		super();
		this.friendRelation = friendRelation;
		Common.initDB();
	}
	
	public FriendRelationDao close() {
		if(ps != null) {
			try {
				ps.close();
			} catch (SQLException e1) {
				System.out.println("MemberDao ps close");
				e1.printStackTrace();
			}
		}
		if(conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				System.out.println("MemberDao conn close");
				e.printStackTrace();
			}
		}
		return this;
	}

	public void insert() {
		if(!isExist()) {
			String sql = "insert into friend_relation"
					+ "(member_sk, friend_type, friend_account, is_shareable, post_date) VALUES (?,?,?,?,?)";		
			try {
				conn = DriverManager.getConnection(Common.DBURL, Common.DBACCOUNT,
						Common.DBPWD);
				ps = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
				ps.setInt(1, friendRelation.getMember_sk());
				ps.setString(2, friendRelation.getFriend_type());
				ps.setString(3, friendRelation.getFriend_account());
				ps.setInt(4, 0);
				ps.setString(5, friendRelation.getPost_date());
				int count = ps.executeUpdate();
				if(count > 0) {
					System.out.println("friend_relation add success");
				}else {
					System.out.println("friend_relation add fail");
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println("friend_relation add err");
			}
			close();
			
		}
	}

	public boolean isExist() {
		int sk = 0;
		String sql = "select sk from friend_relation"
				+ " where member_sk = ?"
				+ " and friend_type = ?"
				+ " and friend_account = ?";
		try {
			conn = DriverManager.getConnection(Common.DBURL, Common.DBACCOUNT,
					Common.DBPWD);
			ps = conn.prepareStatement(sql);
			ps.setInt(1,friendRelation.getMember_sk());
			ps.setString(2, friendRelation.getFriend_type());
			ps.setString(3, friendRelation.getFriend_account());
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				sk = rs.getInt(1);
			}else {
				sk = 0;
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		close();
		
		if(sk > 0) {
			return true;
		}else {
			return false;
		}
	}

	public List<String> getAvoidFBIDList() {
		List<String> avoidFBIDList = new ArrayList<String>();
		String sql = "select friend_account from friend_relation"
				+ " where member_sk = ?"
				+ " and friend_type = ?";

		try {
			conn = DriverManager.getConnection(Common.DBURL, Common.DBACCOUNT,
					Common.DBPWD);
			ps = conn.prepareStatement(sql);
			ps.setInt(1,memberSK);
			ps.setString(2, "facebook");
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				avoidFBIDList.add(rs.getString(1));
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		close();
		return avoidFBIDList;
	}
	
	
	

}
