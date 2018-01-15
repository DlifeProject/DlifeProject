package system;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import db.DiaryDetail;
import db.DiaryDetailDao;

@WebServlet("/autoDiary")
public class autoDiary extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
       
    public autoDiary() { super(); }

	@SuppressWarnings("unused")
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.getWriter().append("Served at: ").append(request.getContextPath());
		
		DiaryDetailDao diaryDetailDao = new DiaryDetailDao(3);
		List<DiaryDetail> ltDiary = diaryDetailDao.autoDiary();
		
		int count = 0;
		for(DiaryDetail d:ltDiary) {
			count++;
			d.describe();
		}
		
		System.out.println(ltDiary);
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
	}

}
