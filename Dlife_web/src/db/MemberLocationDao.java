package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import system.Common;

public class MemberLocationDao {
	
	public int memberSK = 0;
	public MemberLocation memberLocation;
	Connection conn = null;
	PreparedStatement ps = null;	
	
	public MemberLocationDao(int memberSK) {
		super();
		this.memberSK = memberSK;
		Common.initDB();
	}
	
	public MemberLocationDao(MemberLocation memberLocation) {
		super();
		this.memberLocation = memberLocation;
		Common.initDB();
	}
	
	public MemberLocationDao close() {
		if(ps != null) {
			try {
				ps.close();
			} catch (SQLException e1) {
				System.out.println("MemberLocationDao ps close");
				e1.printStackTrace();
			}
		}
		if(conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				System.out.println("MemberLocationDao conn close");
				e.printStackTrace();
			}
		}
		return this;
	}
	
	public int addNew() {
		
		int count = 0;
		
		String sql = "insert into member_location"
				+ "(member_sk, google_title, user_title, longitude, latitude,"
				+ " latest_date, post_date )"
				+ " VALUES (?,?,?,?,?,"
				+ "?,?)";
		try {
			conn = DriverManager.getConnection(Common.DBURL, Common.DBACCOUNT,
					Common.DBPWD);
			ps = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
			
			System.out.println(sql);
			
			ps.setInt(1, 1);
			ps.setString(2, "test google");
			ps.setString(3, "user title");
			ps.setDouble(4, 121.19182735193802);
			ps.setDouble(5, 24.967921295607205);
			ps.setString(6, "2018-02-03 06:13:32");
			ps.setString(7, "2018-04-07 07:17:37");
			count = ps.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
			
		}
		close();
		return count;
	}

}
