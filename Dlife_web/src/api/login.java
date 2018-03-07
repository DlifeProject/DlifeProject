package api;

import java.io.BufferedReader;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import db.Member;
import db.MemberDao;

@WebServlet("/login")
public class login extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.setCharacterEncoding("utf-8");
		response.getWriter().append("Served at: ").append(request.getContextPath());
		
		Member member = new Member();
		member.setApp_account("irv278@gmail.com");
		member.setApp_pwd("Regan");
		MemberDao memberDaoB = new MemberDao(member);
		
		
		
		String datetime = String.valueOf(System.currentTimeMillis());
		response.getWriter().append(memberDaoB.getMemberSK() + "<br>");
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
		System.out.println("login inStr: " + sb.toString());
		Gson gson = new Gson();
		JsonObject jsonObject = gson.fromJson(sb.toString(),JsonObject.class);
		String action = jsonObject.get("action").getAsString();
		String loginStatus = "";
		if(action.equals("login")) {
			
			String spotJson = jsonObject.get("member").getAsString();
			Member memberLogin = gson.fromJson(spotJson, Member.class);
			MemberDao memberDao = new MemberDao(memberLogin);
			System.out.println("doMemberLogin Login Login !");
			loginStatus = memberDao.doMemberLogin();
			if(loginStatus.equals("needAddAcount")) {
				loginStatus = memberDao.addNewAccount();
				memberDao.updateLoginDate();
			}
			System.out.println("loginStatus = " + loginStatus);
			response.getWriter().println(loginStatus);
			
		}else if(action.equals("memberProfile")) {
			
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
			}else {
				System.out.println("memberProfile : member account notfound ");
			}
			
		}else if(action.equals("changePassword")) {
			Member member = new Member(jsonObject.get("account").getAsString()
					,jsonObject.get("password").getAsString());
			MemberDao memberDao = new MemberDao(member);
			int memberSK = memberDao.getMemberSK();
			if(memberSK > 0) {
				memberDao.memberSK = memberSK;
				int checkcount = memberDao.updatePassword(jsonObject.get("newPassword").getAsString());
				if(checkcount > 0) {
					response.getWriter().println("passwordUpdateSuccess");
				}else {
					response.getWriter().println("passwordUpdateError");
				}
				
			}else {
				response.getWriter().println("passwordUpdateError");
			}
			
		}else if(action.equals("changeNickname")) {
			Member member = new Member(jsonObject.get("account").getAsString()
					,jsonObject.get("password").getAsString());
			MemberDao memberDao = new MemberDao(member);
			int memberSK = memberDao.getMemberSK();
			if(memberSK > 0) {
				memberDao.memberSK = memberSK;
				int checkcount = memberDao.updateNickname(jsonObject.get("newNickname").getAsString());
				if(checkcount > 0) {
					response.getWriter().println("nicknameUpdateSuccess");
				}else {
					response.getWriter().println("nicknameUpdateError");
				}
				
			}else {
				response.getWriter().println("nicknameUpdateError");
			}
		}else if(action.equals("changeBirthday")) {
			Member member = new Member(jsonObject.get("account").getAsString()
					,jsonObject.get("password").getAsString());
			MemberDao memberDao = new MemberDao(member);
			int memberSK = memberDao.getMemberSK();
			if(memberSK > 0) {
				memberDao.memberSK = memberSK;
				int checkcount = memberDao.updateBirthday(jsonObject.get("newBirthday").getAsString());
				if(checkcount > 0) {
					response.getWriter().println("birthdayUpdateSuccess");
				}else {
					response.getWriter().println("birthdayUpdateError");
				}
				
			}else {
				response.getWriter().println("birthdayUpdateError");
			}
		}else if(action.equals("changeGender")) {
			Member member = new Member(jsonObject.get("account").getAsString()
					,jsonObject.get("password").getAsString());
			MemberDao memberDao = new MemberDao(member);
			int memberSK = memberDao.getMemberSK();
			if(memberSK > 0) {
				memberDao.memberSK = memberSK;
				int checkcount = memberDao.updateBirthday(jsonObject.get("newGender").getAsInt());
				if(checkcount > 0) {
					response.getWriter().println("genderUpdateSuccess");
				}else {
					response.getWriter().println("genderUpdateError");
				}
				
			}else {
				response.getWriter().println("genderUpdateError");
			}
		}
		
		
	}
}

