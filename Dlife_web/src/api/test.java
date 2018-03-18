package api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import db.DiaryPhotoDao;
import db.Member;
import db.MemberDao;
import db.MemberLocationDao;
import system.Common;
import system.GoogleMapPlace;
import system.GoogleNearbyItem;
import system.ImageUtil;

@WebServlet("/test")
public class test extends HttpServlet{
	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		
//		BufferedReader br = request.getReader();
//		StringBuffer sb = new StringBuffer();
//		String text = "";
//		while((text = br.readLine()) != null) {
//			sb.append(text);
//		}
//		System.out.println("login inStr: " + sb.toString());
//		Gson gson = new Gson();
//		JsonObject jsonObject = gson.fromJson(sb.toString(),JsonObject.class);
//		String action = jsonObject.get("action").getAsString();
//		String loginStatus = "";
//		if(action.equals("login")) {
//			Member member = new Member(jsonObject.get("account").getAsString()
//					,jsonObject.get("password").getAsString());
//			MemberDao memberDao = new MemberDao(member);
//			int memberSK = memberDao.getMemberSK();
//			if(memberSK > 0) {
//				Member memberProfile = new Member(memberDao.getMemberProfileBySK(memberSK));
//				JsonObject outJsonObject = new JsonObject();
//				outJsonObject.addProperty("memberProfile", new Gson().toJson(memberProfile));
//				System.out.println("memberProfile = " + outJsonObject.toString());
//				response.getWriter().println(outJsonObject.toString());
//			}
//			
//		}else if (action.equals("photo")) {
//			int photoSK = jsonObject.get("sk").getAsInt();
//			int imageSize = jsonObject.get("imageSize").getAsInt();
//			DiaryPhotoDao diaryPhotoDao = new DiaryPhotoDao();
//			byte[] image = diaryPhotoDao.getImage(photoSK);
//			if (image != null) {
//				image = ImageUtil.shrink(image, imageSize);
//				response.setContentType("image/jpeg");
//				response.setContentLength(image.length);
//			}
//			OutputStream os = response.getOutputStream();
//			os.write(image);
//		}
		
		int photoSK = 1;
		int imageSize = 0;
		DiaryPhotoDao diaryPhotoDao = new DiaryPhotoDao();
		byte[] image = diaryPhotoDao.getImage(photoSK);
		if (image != null) {
			image = ImageUtil.shrink(image, imageSize);
			response.setContentType("image/jpeg");
			response.setContentLength(image.length);
		}
		OutputStream os = response.getOutputStream();
		os.write(image);
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
		System.out.println("login inStr: " + sb.toString());
		Gson gson = new Gson();
		JsonObject jsonObject = gson.fromJson(sb.toString(),JsonObject.class);
		String action = jsonObject.get("action").getAsString();
		String loginStatus = "";
		if(action.equals("login")) {
			Member member = new Member(jsonObject.get("account").getAsString()
					,jsonObject.get("password").getAsString());
			MemberDao memberDao = new MemberDao(member);
			int memberSK = memberDao.getMemberSK();
			if(memberSK > 0) {
				Member memberProfile = new Member(memberDao.getMemberProfileBySK(memberSK));
				JsonObject outJsonObject = new JsonObject();
				outJsonObject.addProperty("memberProfile", new Gson().toJson(memberProfile));
				System.out.println("memberProfile = " + outJsonObject.toString());
				response.getWriter().println(outJsonObject.toString());
			}
			
		}else if (action.equals("photo")) {
			
			OutputStream os = response.getOutputStream();
			int photoSK = jsonObject.get("sk").getAsInt();
			int imageSize = jsonObject.get("imageSize").getAsInt();
			DiaryPhotoDao diaryPhotoDao = new DiaryPhotoDao();
			byte[] image = diaryPhotoDao.getImage(photoSK);
			if (image != null) {
				image = ImageUtil.shrink(image, imageSize);
				response.setContentType("image/jpeg");
				response.setContentLength(image.length);
				os.write(image);
				//response.getWriter().println(image);
			}else {
				response.getWriter().println("no file");
			}
			
		}


	}
	

}

/*
帳號
curl -H "Content-Type: application/json"  -d '{"action":"login", "account":"irv278@gmail.com", "password":"Regan"}' http://114.34.110.248:7070/Dlife/test

        圖檔
        curl -H "Content-Type: application/json"  -d '{"action":"photo", "sk":1, "imageSize":0}' http://114.34.110.248:7070/Dlife/test
        curl -H "Content-Type: application/json"  -d '{"action":"photo", "sk":1, "imageSize":0}' http://localhost:8080/Dlife/test
        curl -H "Content-Type: application/json"  -d '{"action":"login", "account":"irv278@gmail.com", "password":"Regan"}' http://localhost:8080/Dlife/test

 */ 
