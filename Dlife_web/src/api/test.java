package api;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import db.MemberLocationDao;

@WebServlet("/test")
public class test extends HttpServlet{
	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.setCharacterEncoding("utf-8");
		response.getWriter().append("Served at: ").append(request.getContextPath());
		
		String datetime = String.valueOf(System.currentTimeMillis());
		response.getWriter().append(datetime + "<br>");
		//do getdiary
		
		double longitude = 121.19182735193802;
		double latitude = 24.967921295607205;
		response.getWriter().append(longitude + "<br>");
		response.getWriter().append(latitude + "<br>");
		
		MemberLocationDao memberLocationDao = new MemberLocationDao(1);
		int count = memberLocationDao.addNew();
		response.getWriter().append("count: " + count + "<br>");	 
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {		
		request.setCharacterEncoding("utf-8");

	}
}
