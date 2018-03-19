package api;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import db.DiaryDetail;
import db.DiaryDetailDao;
import db.DiaryLocation;
import db.DiaryLocationDao;
import db.Member;
import db.MemberDao;
import system.Common;
import system.GoogleMapPlace;
import system.GoogleNearbyItem;

@WebServlet("/mapapi")
public class mapapi extends HttpServlet{
	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.setCharacterEncoding("utf-8");
		response.getWriter().append("Served at: ").append(request.getContextPath());
		
		String datetime = String.valueOf(System.currentTimeMillis());
		response.getWriter().append(datetime + "<br>");
		//do getdiary
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {		
		
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		
		BufferedReader br = request.getReader();
		StringBuffer sb = new StringBuffer();
		String text = "";
		while((text = br.readLine()) != null) {
			sb.append(text);
		}
		System.out.println("mapapi inStr: " + sb.toString());
		Gson gson = new Gson();
		JsonObject jsonObject = gson.fromJson(sb.toString(),JsonObject.class);
		String action = jsonObject.get("action").getAsString();
		
		if(action.equals("nearby")) {
			
			double latitude = jsonObject.get("latitude").getAsDouble();
			double longitude = jsonObject.get("longitude").getAsDouble();

			GoogleMapPlace googleMapPlace = new GoogleMapPlace(latitude,longitude);
			String json = googleMapPlace.getLocationJson();
			System.out.println("google nearby return json: " + json);
			ArrayList<GoogleNearbyItem> nearbyList = googleMapPlace.getNearbyItemList();
			
			JsonObject outJsonObject = new JsonObject();
			outJsonObject.addProperty("nearbyItems", new Gson().toJson(nearbyList));
			
			System.out.println("mapapi nearby outStr: " + outJsonObject.toString());
			response.getWriter().println(outJsonObject.toString());

		}else if(action.equals("nearBySelect")) {
			
			Member member = new Member(jsonObject.get("account").getAsString()
					,jsonObject.get("password").getAsString());
			MemberDao memberDao = new MemberDao(member);
			int memberSK = memberDao.getMemberSK();
			if(memberSK > 0) {
				
				DiaryDetailDao diaryDetailDao = new DiaryDetailDao(memberSK);
				if(diaryDetailDao.isMemberOwn(jsonObject.get("diaryDetailSK").getAsInt())) {
					
					DiaryDetail diaryDetail = diaryDetailDao.getDiaryBySK(jsonObject.get("diaryDetailSK").getAsInt());
					String[] diaryStartDayArray = diaryDetail.getPost_date().split(" ");
					
					String nearbyJson = jsonObject.get("nearBy").getAsString();
					JsonObject nearbyJsonObject = gson.fromJson(nearbyJson,JsonObject.class);
					
					String googleName = nearbyJsonObject.get("name").getAsString();
					String googlePlaceID = nearbyJsonObject.get("placeID").getAsString();
					double latitude = nearbyJsonObject.get("latitude").getAsDouble();
					double longitude = nearbyJsonObject.get("longitude").getAsDouble();
					
					DiaryLocation diaryLocation = new DiaryLocation(
							0
							,memberSK
							,jsonObject.get("diaryDetailSK").getAsInt()
							,googlePlaceID
							,googleName
							,longitude
							,latitude
							,diaryStartDayArray[0]
							,Common.getNowDateTimeString()
							);
					DiaryLocationDao diaryLocationDao = new DiaryLocationDao(diaryLocation);
					diaryLocationDao.insert();
										
				}else {
					System.out.println("nearBySelect : diary detial is not this Member Own ");
				}
				
			}else {
				System.out.println("nearBySelect : member account notfound ");
			}
	
		}    
		
	}
	
}
