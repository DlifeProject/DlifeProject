package api;

import java.io.IOException;
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

import db.Member;
import db.MemberDao;
import db.MemberLocationDao;
import system.GoogleMapPlace;
import system.GoogleNearbyItem;

@WebServlet("/test")
public class test extends HttpServlet{
	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.setCharacterEncoding("utf-8");
		Member member = new Member();
		member.setApp_account("irv278@gmail.com");
		member.setApp_pwd("Regan");
		MemberDao memberDaoB = new MemberDao(member);
		
		request.setCharacterEncoding("utf-8");
		response.getWriter().append(memberDaoB.getMemberSK() + "<br>");
		response.getWriter().append("Served at: ").append(request.getContextPath());			
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {		
		
		request.setCharacterEncoding("utf-8");
		Member member = new Member();
		member.setApp_account("irv278@gmail.com");
		member.setApp_pwd("Regan");
		MemberDao memberDaoB = new MemberDao(member);
		
		request.setCharacterEncoding("utf-8");
		response.getWriter().append(memberDaoB.getMemberSK() + "<br>");
		response.getWriter().append("Served at: ").append(request.getContextPath());

	}
}
