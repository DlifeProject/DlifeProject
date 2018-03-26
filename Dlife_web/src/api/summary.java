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

import db.CategoryDao;
import db.Member;
import db.MemberDao;
import system.Common;

@WebServlet("/summary")
public class summary extends HttpServlet{
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
		Gson gson = new Gson();
		JsonObject jsonObject = gson.fromJson(sb.toString(), JsonObject.class);
		String action = jsonObject.get("action").getAsString();
		System.out.println("Start insert !! " + action);
		Member member = new Member(jsonObject.get("account").getAsString()
				,jsonObject.get("password").getAsString());
		MemberDao memberDao = new MemberDao(member);
		int memberSK = memberDao.getMemberSK();
		System.out.println("Start summary !! " + memberSK);
		
		String outPut = "";
		if(memberSK > 0) {
			if (action.equals("categorySum")) {
				System.out.println("text1");
				String[] cateArray = Common.DEFAULTCATE;
				List<CategorySum> ltCategorySum = new ArrayList<CategorySum>();
				for(String categoryType:cateArray) {
					CategorySum categorySum = new CategorySum();
					CategoryDao categoryDao = new CategoryDao(memberSK);
					categorySum = categoryDao.getSummaryByType(categoryType);
					ltCategorySum.add(categorySum);	
				}
                JsonObject outJsonObject = new JsonObject();
                outJsonObject.addProperty("categorySum", new Gson().toJson(ltCategorySum));
                outPut = outJsonObject.toString();
				System.out.println("Start summary !! outPut " + outPut);	
			}
		}
		
		response.getWriter().println(outPut);
		
		
	}
}
