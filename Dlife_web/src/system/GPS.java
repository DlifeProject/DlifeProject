package system;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import db.LocationTrace;
import db.LocationTraceDao;
import db.Member;
import db.MemberDao;



@WebServlet("/gps")
public class GPS extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

    public GPS() { super(); }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		BufferedReader br = request.getReader();
		StringBuffer sb = new StringBuffer();
		String text = "";
		while((text = br.readLine()) != null) {
			sb.append(text);
		}
		System.out.println("inStr: " + sb.toString());
		Gson gson = new Gson();
		JsonObject jsonObject = gson.fromJson(sb.toString(),JsonObject.class);
		String action = jsonObject.get("action").getAsString();
		String exeString = "";
		List<Integer> ltCount = new ArrayList<Integer>();
		
		if(action.equals("uploadGPS")) {
			Member member = new Member(jsonObject.get("account").getAsString()
					,jsonObject.get("password").getAsString());
			MemberDao memberDao = new MemberDao(member);
			int memberSK = memberDao.getMemberSK();
			if(memberSK == 0) {
				exeString = "accountError";
			}else {
				String ltJson = jsonObject.get("locationTrace").getAsString();
				List<LocationTrace> ltList = null;
				Type listType = new TypeToken<List<LocationTrace>>(){ }.getType();
				ltList = new Gson().fromJson(ltJson, listType);
				
				for(LocationTrace t:ltList) {
					LocationTraceDao inserLocationTraceDao = new LocationTraceDao(t,memberSK);
					ltCount.add(inserLocationTraceDao.insert());
				}
				
			}
			
		}
        JsonObject outJsonObject = new JsonObject();
        outJsonObject.addProperty("action", "updateSK");
        outJsonObject.addProperty("updateSK", new Gson().toJson(ltCount));
		
        exeString = outJsonObject.toString();
		response.getWriter().println(exeString);
		System.out.println("loginStatus = " + exeString);
	}

}
