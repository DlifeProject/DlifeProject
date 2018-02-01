package api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import db.DiaryDetailDao;
import db.Member;
import db.MemberDao;
import system.Common;

@WebServlet("/PiechartServlet")
public class PiechartServlet extends HttpServlet {
	private final static String CONTENT_TYPE = "text/html; charset=utf-8";

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("utf-8");
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		
		BufferedReader br = new BufferedReader(request.getReader());
		StringBuffer inStr = new StringBuffer();
		String line = "";
		while ((line = br.readLine()) != null) {
			inStr.append(line);
		}
		System.out.println("inStr:" + inStr.toString());
		Gson gson = new Gson();
		JsonObject jsonObject = gson.fromJson(inStr.toString(), JsonObject.class);
		String action = jsonObject.get("action").getAsString();
		System.out.println("Start select !! " + action);
		Member member = new Member(jsonObject.get("account").getAsString(), jsonObject.get("password").getAsString());
		MemberDao memberDao = new MemberDao(member);
		int memberSK = memberDao.getMemberSK();
		System.out.println("Start PiechartServlet !! " + memberSK);
		if (action.equals("select")) {
			String selectDateJson = jsonObject.get("SelectDate").getAsString();
			SelectDate selectDate = gson.fromJson(selectDateJson.toString(), SelectDate.class);
			String[] cateArray = Common.DEFAULTCATE;
			List<PiechartData> ltPiechartData = new ArrayList<PiechartData>();
			for (String categoryType : cateArray) {
				// System.out.println(categoryType);
				PiechartData piechartData = new PiechartData();
				DiaryDetailDao dao = new DiaryDetailDao(memberSK);
				piechartData = dao.getPiechartDate(categoryType, selectDate);
				ltPiechartData.add(piechartData);
			}

			writeText(response, gson.toJson(ltPiechartData));
		}
	}

	private void writeText(HttpServletResponse response, String outText) throws IOException {
		response.setContentType(CONTENT_TYPE);
		PrintWriter out = response.getWriter();
		// System.out.println("outText: " + outText);
		out.print(outText);
		System.out.println("output: " + outText);
	}

}