package api;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import db.CategoryDao;
import db.DiaryDetail;
import db.DiaryDetailDao;
import db.FriendRelation;
import db.FriendRelationDao;
import db.Member;
import db.MemberDao;
import db.MemberMatchDao;
import db.MemberShareRelationDao;
import system.Common;

@WebServlet("/friend")
public class friend extends HttpServlet{

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
		System.out.println("friend inStr: " + sb.toString());
		
		Gson gson = new Gson();
		JsonObject jsonObject = gson.fromJson(sb.toString(), JsonObject.class);
		String action = jsonObject.get("action").getAsString();
		System.out.println("Start friend !! " + action);
		Member member = new Member(jsonObject.get("account").getAsString(), jsonObject.get("password").getAsString());
		MemberDao memberDao = new MemberDao(member);
		int memberSK = memberDao.getMemberSK();
		String msg = "";
		if (memberSK > 0) {
			System.out.println("memberSK : " + memberSK);
			if (action.equals("getFriendList")) {
				MemberShareRelationDao memberShareRelationDao = new MemberShareRelationDao(memberSK);
				ArrayList<MatchFriendItem> shareList = memberShareRelationDao.getMemberShareRelationList();
				
				JsonObject outJsonObject = new JsonObject();
				outJsonObject.addProperty("getFriendList", new Gson().toJson(shareList));
				
				System.out.println("friend friendList outStr: " + outJsonObject.toString());
				response.getWriter().println(outJsonObject.toString());				
				
			} else if (action.equals("getFriendDiary")) {
				
				int friendSK = jsonObject.get("MyFriendSK").getAsInt();
				int friendCategorySK = jsonObject.get("MyFriendCategorySK").getAsInt();
				MemberShareRelationDao memberShareRelationDao = new MemberShareRelationDao(memberSK);
				
				if(memberShareRelationDao.isMyFriendShareCategory(friendSK,friendCategorySK )) {

					DiaryDetailDao diaryDetailDao = new DiaryDetailDao(friendSK);
					List<DiaryDetail> ltDiaryDetail = new ArrayList<DiaryDetail>();
					ltDiaryDetail = diaryDetailDao.getDiaryByCategoryTypeBySK(friendCategorySK);
					JsonObject outJsonObject = new JsonObject();

					if (ltDiaryDetail.size() > 0) {

						outJsonObject.addProperty("getFriendDiary", new Gson().toJson(ltDiaryDetail));
						response.getWriter().println(outJsonObject.toString());
						System.out.println("outStr: my friend's diary" + outJsonObject.toString());

					} else {
						response.getWriter().println("getfriendDiaryError");
						System.out.println("outStr: my friend has no diary");
					}
					
				}else {
					System.out.println("friend share err get non shareale friend categoty: mySk-> " 
																				+ memberSK 
																				+ " friendSk->"
																				+ friendSK
																				+ " friendCategorySK->"
																				+ friendCategorySK);
					response.getWriter().println("getfriendDiaryError");				
				}
				
				
			} else if (action.equals("MyShareAbleCateList")) {
				String[] cateArray = Common.DEFAULTCATE;
				List<CategorySum> ltCategorySum = new ArrayList<CategorySum>();
				System.out.println("MyShareAbleCateList start!!");
				for(String categoryType:cateArray) {
					
					if(!categoryType.equals(Common.NONSHARECATE[0])) {
						CategorySum categorySum = new CategorySum();
						CategoryDao categoryDao = new CategoryDao(memberSK);
						categorySum = categoryDao.getSummaryByType(categoryType);
						System.out.println(categoryType + " -> 7days" + categorySum.getSeven_day() + " photoSK->" + categorySum.getDiaryPhotoSK());
						if(categorySum.getSeven_day() > Common.SHAREABELDIARYCOUNT && categorySum.getDiaryPhotoSK() > 0) {
							ltCategorySum.add(categorySum);
						}
					}
				}

	            JsonObject outJsonObject = new JsonObject();
	            outJsonObject.addProperty("MyShareAbleCateList", new Gson().toJson(ltCategorySum));
	            response.getWriter().println(outJsonObject.toString());	
				System.out.println("Start summary !! outPut " + outJsonObject.toString());
				
			} else if (action.equals("toRequestShare")) {
				
				//inStr: {"action":"toRequestShare","account":"irv278@gmail.com","password":"Regan","shareCategory":"Hobby
				CategoryDao categoryDao = new CategoryDao();				
				int shareTopCateorySk = categoryDao.getCategory_sk(jsonObject.get("shareCategory").getAsString());
				MemberMatchDao memberMatchDao = new MemberMatchDao(memberSK);
				memberMatchDao.toMatch(shareTopCateorySk);
				
				
			} else if (action.equals("updateFBList")) {
				
				MemberDao fbMemberDao = new MemberDao(memberSK);
				fbMemberDao.updateFBid(jsonObject.get("FBid").getAsString());
				
				JSONArray jsonArray = new JSONArray( jsonObject.get("FBList").getAsString());
				for(int i=0;i<jsonArray.length();i++) {
					JSONObject jsonObjectItem = jsonArray.getJSONObject(i);
					FriendRelation friendRelation = new FriendRelation();
					friendRelation.setMember_sk(memberSK);
					friendRelation.setFriend_type("facebook");
					friendRelation.setFriend_account(jsonObjectItem.get("id").toString());
					friendRelation.setIs_shareable(0);
					friendRelation.setPost_date(Common.getNowDateTimeString());

					FriendRelationDao friendRelationDao = new FriendRelationDao(friendRelation);
					friendRelationDao.insert();
					
				}									
		
			} else if (action.equals("getFacebookFriendCount")) {
				FriendRelationDao friendRelationDao = new FriendRelationDao(memberSK);
				int facebookFriendCount = friendRelationDao.getFacebookCount();
				System.out.println("getFacebookFriendCount : " + facebookFriendCount);
				response.getWriter().println(facebookFriendCount);
			}else {
				response.getWriter().println("actionError");
			}
			
		} else {
			response.getWriter().println("accountError");
		}
	}
		
}
