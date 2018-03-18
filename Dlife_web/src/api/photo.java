package api;

import java.io.BufferedReader;

import java.io.IOException;

import java.io.OutputStream;
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

import db.DiaryPhoto;
import db.DiaryPhotoDao;
import java.util.Base64;
import db.Member;

import db.MemberDao;

import system.ImageUtil;

@WebServlet("/photo")

public class photo extends HttpServlet {
	private final static String CONTENT_TYPE = "text/html; charset=utf-8";

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
		System.out.println("inStr::: " + sb.toString());
		String exeString = "";
		String msg = "";
		Gson gson = new Gson();
		JsonObject jsonObject = gson.fromJson(sb.toString(), JsonObject.class);
		String action = jsonObject.get("action").getAsString();
		System.out.println("拿到請求 " + action);

		Member member = new Member(jsonObject.get("account").getAsString(), jsonObject.get("password").getAsString());
		MemberDao memberDao = new MemberDao(member);
		int memberSK = memberDao.getMemberSK();
		DiaryPhotoDao diaryPhotoDao = new DiaryPhotoDao();

		if (memberSK > 0) {
			if (action.equals("getDiaryPhotoSKList")) {
				int diarySK = jsonObject.get("diarySK").getAsInt();
				System.out.println(" diarySK:" + diarySK);
				List<DiaryPhoto> photoSKList = diaryPhotoDao.getPhotoSKList(diarySK);
				
				System.out.println(" photoSkList" + photoSKList);
				writeText(response, gson.toJson(photoSKList));
				
			} else if (action.equals("getImage")) {
				
				OutputStream os = response.getOutputStream();
				int id = jsonObject.get("id").getAsInt();
				System.out.println("startGetImage sk: " + id);
				int imageSize = jsonObject.get("imageSize").getAsInt();
				if(id != 0) {
					byte[] image = diaryPhotoDao.getImage(id);
					System.out.println("Start image !! outPut sk " + "111111");
					if (image != null) {
						image = ImageUtil.shrink(image, imageSize);
						response.setContentType("image/jpeg");
						response.setContentLength(image.length);
						//System.out.println("getImage : " + id);
					}
					os.write(image);
				}else {
					System.out.println("no getImage : " + id);
				}
				
			}
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