package db;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import db.Member;
import system.Common;

public class MemberDao {
	
	public int memberSK = 0;
	public Member member;
	Connection conn = null;
	PreparedStatement ps = null;	

	public MemberDao(int memberSK) {
		super();
		this.memberSK = memberSK;
		Common.initDB();
	}
	
	public MemberDao(Member member) {
		super();
		this.member = member;
		Common.initDB();
	}
	
	public MemberDao close() {
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
	
	public int getMemberSK() {
		int sk = 0;
		String sql = "select sk from member"
				+ " where app_account = ? "
				+ " and app_pwd = ?";
		try {
			conn = DriverManager.getConnection(Common.DBURL, Common.DBACCOUNT,
					Common.DBPWD);
			ps = conn.prepareStatement(sql);
			ps.setString(1, member.getApp_account());
			ps.setString(2, member.getApp_pwd());
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
		return sk;	
	}
	
	public String checkAccount() {
		String sql = "select sk, app_account, app_pwd, android_user_id from member"
				+ " where app_account = ?";
		String returnMsg = "";
		
		try {
			conn = DriverManager.getConnection(Common.DBURL, Common.DBACCOUNT,
					Common.DBPWD);
			ps = conn.prepareStatement(sql);
			ps.setString(1, member.getApp_account());
			ResultSet rs = ps.executeQuery();
			
			int rsCount = 0;
			int sk = 0;
			String android_user_id = "";
			String app_account = "";
			String app_pwd = "";
			
			while (rs.next()) {
				rsCount++;
				sk = rs.getInt(1);
				app_account = rs.getString(2);
				app_pwd = rs.getString(3);
				android_user_id = rs.getString(4);
			}
			
			if(rsCount > 1) {
				returnMsg = "doubleAccount";
			}else {
				if(sk > 0) {
					if(app_pwd.equals(member.getApp_pwd())) {
						
						if(!android_user_id.equals(member.getAndroid_user_id())) {
							// update android user id
							updateMobileUUID(app_account, member.getAndroid_user_id());
						}
						
						// need to update login date
						updateLoginDate();
						returnMsg = "login";
						
					}else {
						returnMsg = "pwdError";
					}
					
				}else {
					returnMsg = "needAddAcount";
				}	
			}
		} catch (SQLException e) {
			e.printStackTrace();
			returnMsg = "error";
		}
		
		close();
		return returnMsg;
	}
	
	public String doMemberLogin() {
		String checkResult = checkAccount();
		if(checkResult.equals("needAddAcount")) {
			updateLoginDate();
		}
		System.out.println("doMemberLogin !");
		close();
		return checkResult;
	}

	
	public void updateMobileUUID(String app_account, String android_user_id) {
		String sql = "update member set android_user_id = ? where app_account = ? ";
		try {
			conn = DriverManager.getConnection(Common.DBURL, Common.DBACCOUNT,
					Common.DBPWD);
			ps = conn.prepareStatement(sql);
			ps.setString(1, android_user_id);
			ps.setString(2, app_account);
			ps.executeUpdate();
			ps.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		close();
	}
	
	public String addNewAccount() {
		String sql = "insert into member"
				+ "(app_account, app_pwd, login_date, post_date, android_user_id) VALUES (?,?,?,?,?)";		
		try {
			conn = DriverManager.getConnection(Common.DBURL, Common.DBACCOUNT,
					Common.DBPWD);
			ps = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
			
			System.out.println("App_account " + member.getApp_account());
			
			ps.setString(1, member.getApp_account());
			ps.setString(2, member.getApp_pwd());
			ps.setString(3, Common.getNowDateTimeString());
			ps.setString(4, Common.getNowDateTimeString());
			ps.setString(5, member.getAndroid_user_id());
			int count = ps.executeUpdate();
			
			if(count > 0) {
				close();
				return "addSuccess";
			}else {
				close();
				return "addFail";
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			close();
			return "addFail";
		}
	}
	
	public void updateLoginDate() {
		String sql = "update member set login_date = ? where app_account = ? ";
		try {
			conn = DriverManager.getConnection(Common.DBURL, Common.DBACCOUNT,
					Common.DBPWD);
			ps = conn.prepareStatement(sql);
			ps.setString(1, Common.getNowDateTimeString());
			ps.setString(2, member.getApp_account());
			ps.executeUpdate();
			ps.close();			
			
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("updateLoginDate err = " + sql);
		}
		close();
	}
	
	public Member getMemberProfileBySK(int memberSK) {
		
		String sql = "select"
				+ "  sk, app_account, app_pwd, fb_account, google_account"
				+ " ,nick_name ,sex ,birthday ,birth_year ,login_date"
				+ " from member"
				+ " where sk = ? ";
		
		try {
			conn = DriverManager.getConnection(Common.DBURL, Common.DBACCOUNT,
					Common.DBPWD);
			ps = conn.prepareStatement(sql);	
			ps.setInt(1, memberSK);
			ResultSet rs = ps.executeQuery();
			
			if(rs.next()) {
				member.setSk(rs.getInt(1));
				member.setApp_account(rs.getString(2));
				member.setApp_pwd(rs.getString(3));
				member.setFb_account(rs.getString(4));
				member.setGoogle_account(rs.getString(5));
				member.setNick_name(rs.getString(6));
				member.setSex(rs.getInt(7));
				member.setBirthday(rs.getString(8));
				member.setBirth_year(rs.getString(9));
				member.setLogin_date(rs.getString(10));
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		close();
		return member;
	}

	public int updatePassword(String asString) {
		int count = 0;
		String sql = "update member set app_pwd = ? where sk = ? ";
		try {
			conn = DriverManager.getConnection(Common.DBURL, Common.DBACCOUNT,
					Common.DBPWD);
			ps = conn.prepareStatement(sql);
			ps.setString(1, asString);
			ps.setInt(2, memberSK);
			ps.executeUpdate();
			count = ps.executeUpdate();		
			
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("updateLoginDate err = " + sql);
		}
		close();	
		return count;
	}

	public int updateNickname(String asString) {
		int count = 0;
		String sql = "update member set nick_name = ? where sk = ? ";
		try {
			conn = DriverManager.getConnection(Common.DBURL, Common.DBACCOUNT,
					Common.DBPWD);
			ps = conn.prepareStatement(sql);
			ps.setString(1, asString);
			ps.setInt(2, memberSK);
			ps.executeUpdate();
			count = ps.executeUpdate();		
			
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("updateLoginDate err = " + sql);
		}
		close();	
		return count;
	}

	public int updateBirthday(String asString) {
		int count = 0;
		String[] birthdayArray = asString.split("-");
		int birthdayYear = Integer.parseInt(birthdayArray[0]);
		
		String sql = "update member set birthday = ?, birth_year = ? where sk = ? ";
		try {
			conn = DriverManager.getConnection(Common.DBURL, Common.DBACCOUNT,
					Common.DBPWD);
			ps = conn.prepareStatement(sql);
			ps.setString(1, asString);
			ps.setInt(2, birthdayYear);
			ps.setInt(3, memberSK);
			ps.executeUpdate();
			count = ps.executeUpdate();		
			
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("updateLoginDate err = " + sql);
		}
		close();	
		return count;
	}

	public int updateBirthday(int asInt) {
		
		int count = 0;
		
		String sql = "update member set sex = ? where sk = ? ";
		try {
			conn = DriverManager.getConnection(Common.DBURL, Common.DBACCOUNT,
					Common.DBPWD);
			ps = conn.prepareStatement(sql);
			ps.setInt(1, asInt);
			ps.setInt(2, memberSK);
			ps.executeUpdate();
			count = ps.executeUpdate();		
			
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("updateLoginDate err = " + sql);
		}
		close();	
		return count;
	}


}
