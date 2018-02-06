package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import system.Common;

public class LocationTraceDao {
	
	public LocationTrace locationTrace;
	public int memberSK;
	Connection conn = null;
	PreparedStatement ps = null;
	
	public LocationTraceDao(int memberSK) {
		super();
		this.memberSK = memberSK;
		Common.initDB();
	}
	
	public LocationTraceDao(LocationTrace locationTrace, int memberSK) {
		super();
		this.locationTrace = locationTrace;
		this.memberSK = memberSK;
		Common.initDB();
	}
	
	public LocationTraceDao close() {
		if(ps != null) {
			try {
				ps.close();
			} catch (SQLException e1) {
				System.out.println("LocationTraceDao ps close");
				e1.printStackTrace();
			}
		}
		if(conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				System.out.println("LocationTraceDao conn close");
				e.printStackTrace();
			}
		}
		return this;
	}
	
	public int insert() {
		int row = 0;
		
		String sql = "insert into location_trace"
				+ "(mobile_sk, member_sk, longitude, latitude, altitude"
				+ ",	speed, accuracy, forward_sk, distance, post_stamp"
				+ ",post_date"
				+ ") VALUES ("
				+ "?,?,?,?,?"
				+ ",?,?,?,?,?"
				+ ",?)";
		
		try {
			conn = DriverManager.getConnection(Common.DBURL, Common.DBACCOUNT,
					Common.DBPWD);
			ps = conn.prepareStatement(sql);
			ps.setInt(1, locationTrace.getSk());
			ps.setInt(2, memberSK);
			ps.setDouble(3, locationTrace.getLongitude());
			ps.setDouble(4, locationTrace.getLatitude());
			ps.setDouble(5, locationTrace.getAltitude());
			ps.setDouble(6, locationTrace.getSpeed());
			ps.setDouble(7, locationTrace.getAccuracy());
			ps.setInt(8, locationTrace.getForward_sk());
			ps.setDouble(9, locationTrace.getDistance());
			ps.setString(10, locationTrace.getPost_stamp());
			ps.setString(11, locationTrace.getPost_date());
			
			row = ps.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		close();
		return locationTrace.getSk();
	}

	public List<LocationTrace> getAllLocationTrace() {
		ResultSet rs = null;
		String sql = "select"
				+ " sk, mobile_sk, member_sk, longitude, latitude"
				+ ", altitude, speed, accuracy, forward_sk, distance"
				+ ", is_update, update_date, post_stamp, post_date"
				+ " from location_trace";
		

		List<LocationTrace> ltLocationTrace = new ArrayList<LocationTrace>();
		
		try {
			conn = DriverManager.getConnection(Common.DBURL, Common.DBACCOUNT,
					Common.DBPWD);
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			
			while(rs.next()) {
				
				LocationTrace locationTrace = new LocationTrace();
				
				locationTrace.setSk(rs.getInt(1));
				locationTrace.setMobile_sk(rs.getInt(2));
				locationTrace.setMember_sk(rs.getInt(3));
				locationTrace.setLongitude(rs.getDouble(4));
				locationTrace.setLatitude(rs.getDouble(5));
				
				locationTrace.setAltitude(rs.getDouble(6));
				locationTrace.setSpeed(rs.getDouble(7));
				locationTrace.setAccuracy(rs.getDouble(8));
				locationTrace.setForward_sk(rs.getInt(9));
				locationTrace.setDistance(rs.getDouble(10));
				
				locationTrace.setIs_update(rs.getInt(11));
				locationTrace.setUpdate_date(rs.getDate(12).toString());
				locationTrace.setPost_stamp(rs.getString(13).toString());
				locationTrace.setPost_date(rs.getDate(14).toString());
				
				ltLocationTrace.add(locationTrace);
				
				System.out.println("locationTrace sk : " + rs.getInt(1) + " => " + rs.getString(13).toString());
				System.out.println("locationTrace sk : " + rs.getInt(1) + " => " + locationTrace.getPost_stamp());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(ltLocationTrace);
		
		close();
		return ltLocationTrace;
	}


}
