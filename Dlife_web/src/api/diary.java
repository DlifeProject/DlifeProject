package api;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import db.CategoryDao;
import db.CategoryMatchDao;
import db.DiaryCategory;
import db.DiaryCategoryDao;
import db.DiaryDetail;
import db.DiaryDetailDao;
import db.DiaryPhotoDao;
import db.Member;
import db.MemberDao;
import system.Common;

@WebServlet("/diary")
public class diary extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("utf-8");

		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");

		BufferedReader br = request.getReader();
		StringBuffer sb = new StringBuffer();
		String text = "";
		while ((text = br.readLine()) != null) {
			sb.append(text);
		}
		System.out.println("inStr: " + sb.toString());

		String exeString = "";
		int insertCount = 0;

		Gson gson = new Gson();
		JsonObject jsonObject = gson.fromJson(sb.toString(), JsonObject.class);
		String action = jsonObject.get("action").getAsString();
		System.out.println("Start action !! " + action);
		Member member = new Member(jsonObject.get("account").getAsString(), jsonObject.get("password").getAsString());
		MemberDao memberDao = new MemberDao(member);
		int memberSK = memberDao.getMemberSK();
		String msg = "";

		System.out.println("memberSK !! " + memberSK);
		if (memberSK > 0) {
			if (action.equals("insertDiary")) {
				String categoryType = jsonObject.get("categoryType").getAsString();
				System.out.println("Start insert !! ");
				String diaryString = jsonObject.get("diaryDetail").getAsString();
				DiaryDetail diaryDetail = new DiaryDetail();
				diaryDetail = gson.fromJson(diaryString, DiaryDetail.class);
				diaryDetail.setMember_sk(memberSK);

				CategoryDao categoryDao = new CategoryDao();
				int categorySK = categoryDao.getCategory_sk(categoryType);
				diaryDetail.setTop_category_sk(categorySK);

				DiaryDetailDao diaryDetailDao = new DiaryDetailDao(diaryDetail);
				insertCount = diaryDetailDao.insert();

				if (insertCount > 0) {
					DiaryCategory diaryCategory = new DiaryCategory(0, memberSK, insertCount, categorySK, categorySK,
							categoryType, "0000-00-00 00:00:00");

					DiaryCategoryDao diaryCategoryDao = new DiaryCategoryDao(memberSK);

					int dCategory = diaryCategoryDao.insert(diaryCategory);

					if (dCategory > 0) {

						CategoryMatchDao categoryMatchDao = new CategoryMatchDao(memberSK);
						categoryMatchDao.updateCategoryMatch(diaryDetailDao.getCategoryMatch(Common.CATEGORYMATCHDAY));

						exeString = "inserDiarySuccess";
						System.out.println("Start insert num " + insertCount);
					} else {
						exeString = "inserDiaryCategoryError";
						System.out.println("Start inserDiaryCategoryError num " + dCategory);
					}

				} else {
					exeString = "insertDiaryError";
				}
			} else if (action.equals("insertDiaryPhoto")) {

				System.out.println("Start insert Photo!! ");
				String imageBase64 = jsonObject.get("imageBase64").getAsString();
				// java.util.Base64 (Java 8 supports)
				byte[] image = Base64.getMimeDecoder().decode(imageBase64);
				int diaryDetailSK = Integer.valueOf(jsonObject.get("diaryDetailSK").getAsString());

				DiaryPhotoDao diaryPhotoDao = new DiaryPhotoDao(memberSK, diaryDetailSK);
				insertCount = diaryPhotoDao.insert(image);
				if (insertCount > 0) {
					exeString = Integer.toString(insertCount);
				} else {
					exeString = "insertPhotoError";
				}

			} else if (action.equals("uploadDiary")) {

				String categoryType = jsonObject.get("uploadCategoryType").getAsString();
				System.out.println("Start upload !! ");
				String diaryString = jsonObject.get("uploadDiaryDetail").getAsString();
				DiaryDetail diaryDetail = new DiaryDetail();
				diaryDetail = gson.fromJson(diaryString, DiaryDetail.class);
				diaryDetail.setMember_sk(memberSK);

				CategoryDao categoryDao = new CategoryDao();
				int categorySK = categoryDao.getCategory_sk(categoryType);
				diaryDetail.setTop_category_sk(categorySK);

				DiaryDetailDao diaryDetailDao = new DiaryDetailDao(diaryDetail);
				int update_count = diaryDetailDao.upload();
				if (update_count == 1) {
					exeString = "uploadDiarySuccess";
				} else {
					exeString = "uploadDiaryFail";
				}

			} else if (action.equals("getDiaryBetweenDays")) {

				System.out.println("Start getDiary!! ");
				String startDay = jsonObject.get("startDay").getAsString();
				String endDay = jsonObject.get("endDay").getAsString();
				int categoryListIndex = jsonObject.get("categoryListIndex").getAsInt();
				String categoryType = "";
				if(categoryListIndex == 0) {
					categoryType = "all";
				}else {
					categoryType = Common.DEFAULTCATE[categoryListIndex + 1];
				}

				DiaryDetailDao diaryDetailDao = new DiaryDetailDao(memberSK);
				List<DiaryDetail> ltDiaryDetail = new ArrayList<DiaryDetail>();
				ltDiaryDetail = diaryDetailDao.getDiaryBetweenDays(startDay,endDay,categoryListIndex);
				JsonObject outJsonObject = new JsonObject();

				if (ltDiaryDetail.size() > 0) {
					exeString = "getDiarySuccess";
					outJsonObject.addProperty("getDiary", new Gson().toJson(ltDiaryDetail));
					msg = outJsonObject.toString();
					System.out.println("outStr: " + msg.toString());

				} else {
					exeString = "getDiaryNull";
					outJsonObject.addProperty("getDiary", "");
					msg = outJsonObject.toString();
					System.out.println("outStr: " + msg.toString());

				}

			} else if (action.equals("getRecyclerViewDiary")) {
				System.out.println("Start getDiary!! ");
				DiaryDetailDao diaryDetailDao = new DiaryDetailDao(memberSK);
				List<DiaryDetail> ltDiaryDetail = new ArrayList<DiaryDetail>();
				ltDiaryDetail = diaryDetailDao.getRecyclerViewDiary();
				JsonObject outJsonObject = new JsonObject();

				if (ltDiaryDetail.size() > 0) {
					exeString = "getDiarySuccess";
					outJsonObject.addProperty("getRecyclerViewDiary", new Gson().toJson(ltDiaryDetail));
					msg = outJsonObject.toString();
					System.out.println("outStr: " + msg.toString());

				} else {
					exeString = "getDiaryNull";
					outJsonObject.addProperty("getRecyclerViewDiary", "");
					msg = outJsonObject.toString();
					System.out.println("outStr: " + msg.toString());

				}

			} else if (action.equals("toDeleteDiary")) {
				System.out.println("Start toDeleteDiary!! ");
				int dieayDetailSK = jsonObject.get("diaryDetailSK").getAsInt();
				DiaryDetailDao diaryDetailDao = new DiaryDetailDao(memberSK);
				if (diaryDetailDao.isMemberOwn(dieayDetailSK)) {
					if (diaryDetailDao.deleteDiaryDetail(dieayDetailSK)) {
						exeString = "diaryDetailDeleteSuccess";
						System.out.println("outStr: diaryDetailDeleteSuccess");
					} else {
						exeString = "diaryDetailDeleteFail";
						System.out.println("outStr: deleteDiaryDetail fail");
					}
				} else {
					exeString = "diaryDetailDeleteFail";
					System.out.println("outStr: isMemberOwn fail");
				}

			} else {
				exeString = "accountError";
			}

			if (exeString.equals("inserDiarySuccess")) {
				response.getWriter().println(insertCount);
				System.out.println("Start insert num print " + insertCount);
			} else if (exeString.equals("insertDiaryError")) {
				response.getWriter().println("0");
			} else if (exeString.equals("inserPhotoSuccess")) {
				response.getWriter().println(insertCount);
			} else if (exeString.equals("insertPhotoError")) {
				response.getWriter().println("0");
			} else if (exeString.equals("getDiarySuccess")) {
				response.getWriter().println(msg);
			} else if (exeString.equals("getDiaryNull")) {
				response.getWriter().println(msg);
			} else if (exeString.equals("diaryDetailDeleteFail")) {
				response.getWriter().println("diaryDetailDeleteFail");
			} else if (exeString.equals("diaryDetailDeleteSuccess")) {
				response.getWriter().println("diaryDetailDeleteSuccess");
			} else {
				response.getWriter().println("0");
			}
		}

	}

}
