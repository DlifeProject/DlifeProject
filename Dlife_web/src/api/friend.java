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

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import db.DiaryDetail;
import db.DiaryDetailDao;
import db.Member;
import db.MemberDao;
import db.MemberShareRelationDao;
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
			if (action.equals("getFriendList")) {
				MemberShareRelationDao memberShareRelationDao = new MemberShareRelationDao(memberSK);
				ArrayList<MatchFriendItem> shareList = memberShareRelationDao.getMemberShareRelationList();
				
				JsonObject outJsonObject = new JsonObject();
				outJsonObject.addProperty("friendList", new Gson().toJson(shareList));
				
				System.out.println("friend friendList outStr: " + outJsonObject.toString());
				response.getWriter().println(outJsonObject.toString());				
				
			} else if ( action.equals("getFriendDiary")) {
				
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
				
				
			} else {
				response.getWriter().println("actionError");
			}
		}else {	
			response.getWriter().println("accountError");
		}
		
	}
}
