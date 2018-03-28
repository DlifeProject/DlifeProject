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

public class CategoryMatchDao {

	public int memberSK = 0;
	public CategoryMatch categoryMatch;
	Connection conn = null;
	PreparedStatement ps = null;
	
	public CategoryMatchDao(int memberSK) {
		super();
		this.memberSK = memberSK;	
		categoryMatch = new CategoryMatch(this.memberSK);
		Common.initDB();
	}
	
	public CategoryMatchDao(CategoryMatch categoryMatch) {
		super();
		this.categoryMatch = categoryMatch;
		this.memberSK = categoryMatch.getMember_sk();
		Common.initDB();
	}
	
	public CategoryMatchDao close() {
		if(ps != null) {
			try {
				ps.close();
			} catch (SQLException e1) {
				System.out.println("CategoryMatchDao ps close");
				e1.printStackTrace();
			}
		}
		if(conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				System.out.println("CategoryMatchDao conn close");
				e.printStackTrace();
			}
		}
		return this;
	}	
	
	public int updateCategoryMatch(ArrayList<CategoryMatchFormat> summary) {
		int sk = 0;
		if(summary.size() == 0) {
			sk = getCategoryMatchSK();
			updateCategoryMatchToZero(sk);
			return sk;
		}else {
			//transfer to CategoryMatch
			int maxDiaryDetailSK = 0;
			int minDiaryDetailSK = 0;
			int shareableDiaryCount = 0;
			int loopCount = 0;
			for(CategoryMatchFormat item : summary) {
				
				loopCount++;
				if(loopCount == 1) {
					maxDiaryDetailSK = item.max_diary_detail_sk;
					minDiaryDetailSK = item.min_diary_detail_sk;
				}
				
				if(maxDiaryDetailSK < item.max_diary_detail_sk) {
					maxDiaryDetailSK = item.max_diary_detail_sk;
				}
				if(minDiaryDetailSK > item.min_diary_detail_sk) {
					minDiaryDetailSK = item.min_diary_detail_sk;
				}
				
				System.out.println("item.top_category_sk : " + item.top_category_sk);
				
				if(loopCount == 1) {
					categoryMatch.setTop_category_1_sk(item.top_category_sk);
					categoryMatch.setDiary_count_1(item.diary_count);
				}else if(loopCount == 2) {
					categoryMatch.setTop_category_2_sk(item.top_category_sk);
					categoryMatch.setDiary_count_2(item.diary_count);
				}else if(loopCount == 3) {
					categoryMatch.setTop_category_3_sk(item.top_category_sk);
					categoryMatch.setDiary_count_3(item.diary_count);
				}
				
				shareableDiaryCount = shareableDiaryCount + item.diary_count;
			}
			categoryMatch.setEnd_diary_detail_sk(maxDiaryDetailSK);
			categoryMatch.setStart_diary_detail_sk(minDiaryDetailSK);
			categoryMatch.setShareable_diary_count(shareableDiaryCount);
			categoryMatch.setUpdate_day(Common.getNowDayString());
			
			sk = getCategoryMatchSK();
			if(sk == 0) {
				sk = insert();
				return sk;
			}else {
				if(updateBySK(sk) == 0) {
					return 0;
				}else {
					return sk;
				}
			}
		}
	}
	
	private void updateCategoryMatchToZero(int sk) {
		int exeCount = 0;
		String sql = "update category_match"
				+ " set"
				+ " top_category_1_sk = 0, diary_count_1 = 0, top_category_2_sk = 0, diary_count_2 = 0, top_category_3_sk = 0"
				+ ",diary_count_3 = 0, start_diary_detail_sk = 0, end_diary_detail_sk = 0, update_day = '" + Common.getNowDayString() + "'"
				+ ", shareable_diary_count = 0"
				+ " where sk = ? ";
		System.out.println("updateCategoryMatchToZero sql:" + sql + " sk:" + sk );
		try {
			conn = DriverManager.getConnection(Common.DBURL, Common.DBACCOUNT,
					Common.DBPWD);
			ps = conn.prepareStatement(sql);
			ps.setInt(1, sk);
			exeCount = ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private int updateBySK(int sk) {
		
		int exeCount = 0;
		String sql = "update category_match"
				+ " set"
				+ " top_category_1_sk = ?, diary_count_1 = ?, top_category_2_sk = ?, diary_count_2 = ?, top_category_3_sk = ?"
				+ ",diary_count_3 = ?, start_diary_detail_sk = ?, end_diary_detail_sk = ?, row_data = ?, update_day = ?"
				+ ",post_date = ?, shareable_diary_count = ?"
				+ " where sk = ? ";
		try {
			conn = DriverManager.getConnection(Common.DBURL, Common.DBACCOUNT,
					Common.DBPWD);
			ps = conn.prepareStatement(sql);
			ps.setInt(1, categoryMatch.getTop_category_1_sk());
			ps.setInt(2, categoryMatch.getDiary_count_1());
			ps.setInt(3, categoryMatch.getTop_category_2_sk());
			ps.setInt(4, categoryMatch.getDiary_count_2());
			ps.setInt(5, categoryMatch.getTop_category_3_sk());
			ps.setInt(6, categoryMatch.getDiary_count_3());
			ps.setInt(7, categoryMatch.getStart_diary_detail_sk());
			ps.setInt(8, categoryMatch.getEnd_diary_detail_sk());
			ps.setString(9, "");
			ps.setString(10, categoryMatch.getUpdate_day());
			ps.setString(11, Common.getNowDateTimeString());
			ps.setInt(12, categoryMatch.getShareable_diary_count());
			ps.setInt(13, sk);
			exeCount = ps.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("updateCategory_match err = " + sql);
		}
		close();
		return exeCount;
	}

	private int insert() {
		int insertCount = 0;
		String sql = "insert into category_match"
				+ "(member_sk, top_category_1_sk, diary_count_1, top_category_2_sk, diary_count_2"
				+ ",top_category_3_sk, diary_count_3, start_diary_detail_sk, end_diary_detail_sk, row_data"
				+ ",update_day, post_date, shareable_diary_count)"
				+ " VALUES"
				+ " (?,?,?,?,?"
				+ " ,?,?,?,?,?"
				+ " ,?,?,?)";	
		
		try {
			conn = DriverManager.getConnection(Common.DBURL, Common.DBACCOUNT,
					Common.DBPWD);
			ps = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);

			ps.setInt(1, categoryMatch.getMember_sk());
			ps.setInt(2, categoryMatch.getTop_category_1_sk());
			ps.setInt(3, categoryMatch.getDiary_count_1());
			ps.setInt(4, categoryMatch.getTop_category_2_sk());
			ps.setInt(5, categoryMatch.getDiary_count_2());
			ps.setInt(6, categoryMatch.getTop_category_3_sk());
			ps.setInt(7, categoryMatch.getDiary_count_3());
			ps.setInt(8, categoryMatch.getStart_diary_detail_sk());
			ps.setInt(9, categoryMatch.getEnd_diary_detail_sk());
			ps.setString(10, "");
			ps.setString(11, categoryMatch.getUpdate_day());
			ps.setString(12, Common.getNowDateTimeString());
			ps.setInt(13, categoryMatch.getShareable_diary_count());
			ps.executeUpdate();
			ResultSet tableKeys = ps.getGeneratedKeys();
			tableKeys.next();
			insertCount = tableKeys.getInt(1);
			
			
		} catch (SQLException e) {
			e.printStackTrace();
			close();
		}
		return insertCount;
	}

	public int getCategoryMatchSK() {
		int sk = 0;
		String sql = "select sk from category_match"
				+ " where member_sk = ? ";
		try {
			conn = DriverManager.getConnection(Common.DBURL, Common.DBACCOUNT,
					Common.DBPWD);
			ps = conn.prepareStatement(sql);
			ps.setInt(1, memberSK);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				sk = rs.getInt(1);
			}else {
				sk = 0;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		close();
		return sk;	
	}

	public void getMyCategoryMatch() {
		String sql = "select"
				+ " sk, member_sk, top_category_1_sk, diary_count_1, top_category_2_sk"
				+ " ,diary_count_2, top_category_3_sk, diary_count_3, start_diary_detail_sk, end_diary_detail_sk"
				+ " from category_match"
				+ " where member_sk = ? ";
		try {
			conn = DriverManager.getConnection(Common.DBURL, Common.DBACCOUNT,
					Common.DBPWD);
			ps = conn.prepareStatement(sql);
			ps.setInt(1, memberSK);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				categoryMatch.setSk(rs.getInt(1));
				categoryMatch.setMember_sk(rs.getInt(2));
				categoryMatch.setTop_category_1_sk(rs.getInt(3));
				categoryMatch.setDiary_count_1(rs.getInt(4));
				categoryMatch.setTop_category_2_sk(rs.getInt(5));
				categoryMatch.setDiary_count_2(rs.getInt(6));
				categoryMatch.setTop_category_3_sk(rs.getInt(7));
				categoryMatch.setStart_diary_detail_sk(rs.getInt(8));
				categoryMatch.setEnd_diary_detail_sk(rs.getInt(9));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		close();
	}
	
	public CategoryMatch matchWithLocationFreind(List<Integer> matchLocationFreindList) {
		int myMatchFriend = 0;
		List<Integer> matchList = new ArrayList<Integer>();
		String matchMemberSKList = "0";
		for(Integer matchSK:matchLocationFreindList) {
			if(matchSK > 0) {
				matchMemberSKList = matchMemberSKList + "," +  String.valueOf(matchSK);
			}
		}
		CategoryMatch myCategoryMatch = new CategoryMatch(0);
		getMyCategoryMatch();
		CategoryMatchDao thisCategoryMarchDao = new CategoryMatchDao(memberSK);
		if(categoryMatch.getDiary_count_1() > 0) {
			myCategoryMatch = thisCategoryMarchDao.getMatchFromCategorySK(categoryMatch.getTop_category_1_sk(),matchMemberSKList);	
		}
		if(myCategoryMatch.getSk() == 0 && categoryMatch.getDiary_count_2() > 0) {
			myCategoryMatch = thisCategoryMarchDao.getMatchFromCategorySK(categoryMatch.getTop_category_2_sk(),matchMemberSKList);
		}
		if(myCategoryMatch.getSk() == 0  && categoryMatch.getDiary_count_3() > 0) {
			myCategoryMatch = thisCategoryMarchDao.getMatchFromCategorySK(categoryMatch.getTop_category_3_sk(),matchMemberSKList);
		}
		return myCategoryMatch;
	}

	private CategoryMatch getMatchFromCategorySK(int top_category_1_sk, String matchMemberSKList) {
		String sql = "select"
				+ " sk, member_sk, top_category_1_sk, diary_count_1, top_category_2_sk"
				+ " ,diary_count_2, top_category_3_sk, diary_count_3, start_diary_detail_sk, end_diary_detail_sk"
				+ " from category_match"
				+ " where member_sk in (" + matchMemberSKList + ") "
				+ " and top_category_1_sk = ?";
		try {
			conn = DriverManager.getConnection(Common.DBURL, Common.DBACCOUNT,
					Common.DBPWD);
			ps = conn.prepareStatement(sql);
			ps.setInt(1, top_category_1_sk);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				categoryMatch.setSk(rs.getInt(1));
				categoryMatch.setMember_sk(rs.getInt(2));
				categoryMatch.setTop_category_1_sk(rs.getInt(3));
				categoryMatch.setDiary_count_1(rs.getInt(4));
				categoryMatch.setTop_category_2_sk(rs.getInt(5));
				categoryMatch.setDiary_count_2(rs.getInt(6));
				categoryMatch.setTop_category_3_sk(rs.getInt(7));
				categoryMatch.setStart_diary_detail_sk(rs.getInt(8));
				categoryMatch.setEnd_diary_detail_sk(rs.getInt(9));
			}else {
				categoryMatch.setSk(0);
				categoryMatch.setMember_sk(0);
				categoryMatch.setTop_category_1_sk(0);
				categoryMatch.setDiary_count_1(0);
				categoryMatch.setTop_category_2_sk(0);
				categoryMatch.setDiary_count_2(0);
				categoryMatch.setTop_category_3_sk(0);
				categoryMatch.setStart_diary_detail_sk(0);
				categoryMatch.setEnd_diary_detail_sk(0);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		close();		
		return categoryMatch;
	}
}
