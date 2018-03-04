package db;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;

import api.CategorySum;
import api.MatchFriendItem;
import system.Common;
import java.util.Hashtable;

public class CategoryDao {
	public int memberSK;
	Category category;
	Connection conn = null;
	PreparedStatement ps = null;	
	

	public CategoryDao() {
		super();
		Common.initDB();
	}
	
	public CategoryDao(int memberSK) {
		super();
		this.memberSK = memberSK;
		Common.initDB();
	}
	
	public CategoryDao close() {
		if(ps != null) {
			try {
				ps.close();
			} catch (SQLException e1) {
				System.out.println("CategoryDao ps close");
				e1.printStackTrace();
			}
		}
		if(conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				System.out.println("CategoryDao conn close");
				e.printStackTrace();
			}
		}
		return this;
	}
	
	public int insert() {
		int insertCount = 0;
		String sql = "insert into Category"
				+ "(member_sk, tag_title , category_type, is_shareable, is_top_category,"
				+ " is_default, is_useful, belong_category_sk)"
				+ " VALUES ("
				+ " ?,?,?,?,?,"
				+ " ?,?,?)";
		try {
			conn = DriverManager.getConnection(Common.DBURL, Common.DBACCOUNT,
					Common.DBPWD);
			ps =  conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
			ps.setInt(1, category.getMember_sk());
			ps.setString(2, category.getTag_title());
			ps.setString(3, category.getCategory_type());
			ps.setString(4, category.getIs_shareable());
			ps.setString(5, category.getIs_top_category());
			ps.setString(6, category.getIs_default());
			ps.setString(7, category.getIs_useful());
			ps.setInt(8, category.getBelong_category_sk());
		
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		close();
		return insertCount;
	}

	public String getCategoryType(int sk) {
		String sql = " select category_type from category"
				   + " where sk = ?"
				   + " and is_default = 1"
				   + " and is_useful = 1"
				   + " and is_top_category = 1"
				   + " and is_shareable = 1";
		String category_type = "";
		try {
			conn = (Connection) DriverManager.getConnection(Common.DBURL, Common.DBACCOUNT,
					Common.DBPWD);
			ps = (PreparedStatement) conn.prepareStatement(sql);
			ps.setInt(1, sk);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				category_type = rs.getString("category_type");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		close();
		return category_type;	
	}	
	
	public int getCategory_sk(String category_type) {
		String sql = " select sk from category"
				   + " where category_type = ?"
				   + " and is_default = 1";
		int sk = 0;
		try {
			conn = (Connection) DriverManager.getConnection(Common.DBURL, Common.DBACCOUNT,
					Common.DBPWD);
			ps = (PreparedStatement) conn.prepareStatement(sql);
			ps.setString(1, category_type);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				sk = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		close();
		return sk;	
	}
	
	public CategorySum getSummaryByType(String categoryType) {
		CategorySum categorySum = new CategorySum();
		
		categorySum.setCategoryType(categoryType);
		DiaryDetailDao diaryDetailDao = new DiaryDetailDao(memberSK);
		
		CategorySum finalCategorySum = new CategorySum();
		finalCategorySum = diaryDetailDao.getfinalDiary(categoryType);

		categorySum.setDiaryPhotoSK(finalCategorySum.getDiaryPhotoSK());
		categorySum.setYear(finalCategorySum.getYear());
		categorySum.setMonth(finalCategorySum.getMonth());
		categorySum.setDay(finalCategorySum.getDay());
		categorySum.setNote(finalCategorySum.getNote());
		int top_category_sk = getCategory_sk(categoryType);
		categorySum.setThree_day(new DiaryDetailDao(memberSK).getDayDiaryCategotyCount(3,top_category_sk));
		categorySum.setSeven_day(new DiaryDetailDao(memberSK).getDayDiaryCategotyCount(7,top_category_sk));
				
		return categorySum;
	}
	
	public Hashtable<Integer,String> getTopCategoryHash(){
		
		Hashtable<Integer,String> topCategoryHash = new Hashtable<Integer,String>();
		
		String sql = " select "
				   + " sk, category_type"
				   + " from category"
				   + " where is_top_category = 1";
				   
		try {
			conn = DriverManager.getConnection(Common.DBURL, Common.DBACCOUNT, Common.DBPWD);
			ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				topCategoryHash.put(rs.getInt("sk"), rs.getString("category_type"));
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		close();
		return topCategoryHash;
	}

}
