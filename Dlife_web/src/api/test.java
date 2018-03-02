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
import system.GoogleMapPlace;

@WebServlet("/test")
public class test extends HttpServlet{
	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.setCharacterEncoding("utf-8");
		response.getWriter().append("Served at: ").append(request.getContextPath());
		
		String datetime = String.valueOf(System.currentTimeMillis());
		response.getWriter().append(datetime + "<br>");
		//do getdiary
		
		GoogleMapPlace googleMapPlace = new GoogleMapPlace("25.0487345","121.51423060000002");
		String json = googleMapPlace.getLocationJson();
		
		System.out.println(json);
		googleMapPlace.parserJson();
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {		
		request.setCharacterEncoding("utf-8");

	}
}
