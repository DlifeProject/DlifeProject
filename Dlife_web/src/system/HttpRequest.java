package system;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.List;

public class HttpRequest {

	public final static int MAXCONNECTTIMEOUT = 3000;
	public final static int MAXREADTIMEOUT = 6000;
	public final static String URLENCODER = "UTF-8";
	private String method = "POST";
	private String url;
	private Map<String, String> sendData;
	private Map<String, String> header;
	private String output = "";

	public HttpRequest(String url) {
		super();
		this.method = "GET";
		this.url = url;
	}

	public HttpRequest(String url, Map<String, String> header) {
		super();
		this.method = "GET";
		this.url = url;
		this.header = header;
	}

	public HttpRequest(String method, String url, Map<String, String> sendData) {
		super();
		this.method = method;
		this.url = url;
		this.sendData = sendData;
	}

	public HttpRequest(String method, String url, Map<String, String> sendData, Map<String, String> header) {
		super();
		this.method = method;
		this.url = url;
		this.sendData = sendData;
		this.header = header;
	}

	public HttpRequest doRequest() {

		url = defindURL();
		BufferedReader in = null;

		try {
			URL realUrl;
			realUrl = new URL(url);
			HttpURLConnection connection;
			connection = (HttpURLConnection)realUrl.openConnection();
			connection.setConnectTimeout(MAXCONNECTTIMEOUT);
			connection.setReadTimeout(MAXREADTIMEOUT);
			if (header != null) {
				Iterator<Entry<String, String>> it = header.entrySet().iterator();
				while (it.hasNext()) {
					Entry<String, String> entry = it.next();
					//System.out.println(entry.getKey() + ":::" + entry.getValue());
					connection.setRequestProperty(entry.getKey(), entry.getValue());
				}
			}
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			
			if(method.equals("GET")) {
				// optional default is GET
				connection.setRequestMethod("GET");
				connection.connect();
			}else {
				connection.setRequestMethod("POST");
				DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
				wr.writeBytes(definfParam());
				wr.flush();
				wr.close();
			}

			// 獲取所有響應頭字段
			Map<String, List<String>> map = connection.getHeaderFields();
			// 遍歷所有的響應頭字段
			for (String key : map.keySet()) {
				//System.out.println(key + "--->" + map.get(key));
			}

			in = new BufferedReader(new InputStreamReader(connection.getInputStream(), URLENCODER));
			String line;
			while ((line = in.readLine()) != null) {
				output += line;
			}
			if (in != null) {
				in.close();
			}

		} catch (IOException e) {
			System.out.println("connection error:" + url);
			e.printStackTrace();
		}

		return this;
	}

	private String defindURL() {
		String getData = definfParam();
		if(method.equals("GET") && !getData.equals("")) {
			return url + "?" + getData;
		}else {
			return url;
		}
	}
	
	private String definfParam() {
		int count = 0;
		String getData = "";
		for (Entry<String, String> entry : sendData.entrySet()) {
			if (count > 0) {
				getData = getData + "&";
			}
			getData = getData + entry.getKey() + "=";
			try {
				getData = getData + URLEncoder.encode(entry.getValue(), URLENCODER);
			} catch (IOException e) {
				System.out.println("URLEncoder err:" + entry.getValue() + " with " + URLENCODER);
			}
			count++;
		}
		return getData;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Map<String, String> getSendData() {
		return sendData;
	}

	public void setSendData(Map<String, String> sendData) {
		this.sendData = sendData;
	}

	public String getOutput() {
		return output;
	}

	public void setOutput(String output) {
		this.output = "";
	}

	public Map<String, String> getHeader() {
		return header;
	}

	public void setHeader(Map<String, String> header) {
		this.header = header;
	}

}
