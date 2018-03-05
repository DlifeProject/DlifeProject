package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import system.Common;

public class DiaryPhotoDao {
	public int memberSK;
	public int diarySK;
	DiaryPhoto diaryphoto;
	Connection conn = null;
	PreparedStatement ps = null;

	public DiaryPhotoDao() {
		super();
		Common.initDB();
	}

	public DiaryPhotoDao(int diarySK) {
		super();
		this.diarySK = diarySK;
		Common.initDB();
	}

	public DiaryPhotoDao(int memberSK, int diarySK) {
		super();
		this.memberSK = memberSK;
		this.diarySK = diarySK;
		Common.initDB();
	}

	public DiaryPhotoDao close() {
		if (ps != null) {
			try {
				ps.close();
			} catch (SQLException e1) {
				System.out.println("DiaryPhotoDao ps close");
				e1.printStackTrace();
			}
		}
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				System.out.println("DiaryPhotoDao conn close");
				e.printStackTrace();
			}
		}
		return this;
	}

	public int insert(byte[] image) {
		int insertCount = 0;
		String sql = "insert into diary_photo" + " (member_sk, diary_sk, photo_img, post_day, post_date)" + " VALUES ("
				+ " ?,?,?,?,?)";
		try {
			conn = DriverManager.getConnection(Common.DBURL, Common.DBACCOUNT, Common.DBPWD);
			ps = conn.prepareStatement(sql);
			ps.setInt(1, memberSK);
			ps.setInt(2, diarySK);
			ps.setBytes(3, image);
			ps.setString(4, Common.getNowDayString());
			ps.setString(5, Common.getNowDateTimeString());
			insertCount = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		close();
		return insertCount;
	}

	public int getMainPhotoSK() {
		int diaryPhotoSK = 0;
		String sql = "select sk from diary_photo" + " where diary_sk = ? " + " order by sk asc limit 1";
		try {
			conn = DriverManager.getConnection(Common.DBURL, Common.DBACCOUNT, Common.DBPWD);
			ps = conn.prepareStatement(sql);
			ps.setInt(1, diarySK);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				diaryPhotoSK = rs.getInt(1);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		close();
		return diaryPhotoSK;
	}

	public List<DiaryPhoto> getPhotoSKList(int diarySK) {
		List<DiaryPhoto> photoSKList = new ArrayList<DiaryPhoto>();

		String sql = "select sk from diary_photo" + " where diary_sk = ? " + " order by sk asc";

		try {
			conn = DriverManager.getConnection(Common.DBURL, Common.DBACCOUNT, Common.DBPWD);
			ps = conn.prepareStatement(sql);
			ps.setInt(1, diarySK);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				int sk = rs.getInt("sk");
				DiaryPhoto photoSk = new DiaryPhoto(sk);
				photoSKList.add(photoSk);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		close();

		return photoSKList;
	}
	
	public boolean deleteDiaryPhoto(int memberSK, int dieayDetailSK) {
		String sql = "delete from diary_photo"
				+ " where member_sk = ?"
				+ " and diary_sk";
		try {
			conn = DriverManager.getConnection(Common.DBURL, Common.DBACCOUNT, Common.DBPWD);
			ps = conn.prepareStatement(sql);
			ps.setInt(1, memberSK);
			ps.setInt(2, dieayDetailSK);
			ps.executeUpdate();
			close();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("deleteDiaryPhoto fail memberSK:" + memberSK + " dieayDetailSK:" + dieayDetailSK);
			close();
			return false;
		}
	}

	public byte[] getImage(int id) {

		String sql = "SELECT photo_img FROM diary_photo WHERE sk = ?;";
		Connection connection = null;
		PreparedStatement ps = null;
		byte[] image = null;

		try {
			connection = DriverManager.getConnection(Common.DBURL, Common.DBACCOUNT,Common.DBPWD);
			ps = connection.prepareStatement(sql);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				image = rs.getBytes(1);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		close();

		return image;

	}

}
