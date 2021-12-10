package utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.logging.Logger;

/**
 * Class cung cap cac phuong thuc giup gui du lieu len server va nhan du lieu tra ve
 * Date: 8/12/2021
 * @author Nguyen Duc Tuan - 20180192
 * @version 1.1
 */
public class API {

	/**
	 * Thuoc tinh giup log thong tin ra console
	 */
	private static final Logger LOGGER = Utils.getLogger(Utils.class.getName());

	/**
	 * Phuong thuc goi cac api dang GET
	 * @param url: duong dan toi server can request
	 * @param token: daon ma bam can cung cap de xac thuc nguoi dung
	 * @return response: phan hoi tu server (dang string)
	 * @throws Exception
	 */
	public static String get(String url, String token) throws Exception {
		// Nguyen Duc Tuan - 20180192

		// phan 1: setup
		HttpURLConnection conn = setupConnection(url, "GET", token);

		// phan 2: doc du ieu tra ve tu server
		String response = readResponse(conn);

		return response;
	}

	/**
	 * Phuong thuc goi cac api dang POST (thanh toan, ...)
	 * @param url: duong dan toi server can request
	 * @param data: du lieu dua len server de xu ly (dang JSON)
	 * @return response: phan hoi tu server(dang string)
	 * @throws IOException
	 */

	public static String post(String url, String data, String token) throws IOException {
		// Nguyen Duc Tuan - 20180192
		allowMethods("PATCH");

		// phan 1: setup
		HttpURLConnection conn = setupConnection(url, "PATCH", token);
//		conn.setRequestProperty("Authorization", "Bearer " + token);

		// phan 2: gui du lieu
		Writer writer = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
		writer.write(data);
		writer.close();

		// phan 3: doc du lieu gui ve tu server
		return readResponse(conn);
	}

	/**
	 * Doc response tra ve
	 * @param conn: HttpURLConnection
	 * @return string
	 * @throws IOException
	 */
	private static String readResponse(HttpURLConnection conn) throws IOException {
		// Nguyen Duc Tuan - 20180192
		BufferedReader in;
		String inputLine;
		if (conn.getResponseCode() / 100 == 2) {
			in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		} else {
			in = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
		}
		StringBuilder response = new StringBuilder();
		while ((inputLine = in.readLine()) != null)
			response.append(inputLine);
		in.close();
		LOGGER.info("Response Info: " + response);
		return response.toString();
	}

	/**
	 * Khoi tao ket noi http den server
	 * @param url: duong dan den server
	 * @param method: http method
	 * @param token: authentication
	 * @return HttpURLConnection
	 * @throws IOException
	 */
	private static HttpURLConnection setupConnection(String url, String method, String token) throws IOException {
		// Nguyen Duc Tuan - 20180192
		LOGGER.info("Request URL: " + url + "\n");
		URL line_api_url = new URL(url);
		HttpURLConnection conn = (HttpURLConnection) line_api_url.openConnection();
		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.setRequestMethod(method);
		conn.setRequestProperty("Content-Type", "application/json");
		conn.setRequestProperty("Authorization", "Bearer " + token);
		return conn;
	}

	/**
	 * Phuong thuc cho phep goi cac loai giao thuc API khac nhau nhu PATCH, PUT, ... (chi hoat dong voi Java 11)
	 * @param methods: giao thuc can cho phep (PATCH, PUT)
	 */
	private static void allowMethods(String... methods) {
		// Nguyen Duc Tuan - 20180192
		try {
			Field methodsField = HttpURLConnection.class.getDeclaredField("methods");
			methodsField.setAccessible(true);

			Field modifiersField = Field.class.getDeclaredField("modifiers");
			modifiersField.setAccessible(true);
			modifiersField.setInt(methodsField, methodsField.getModifiers() & ~Modifier.FINAL);

			String[] oldMethods = (String[]) methodsField.get(null);
			Set<String> methodsSet = new LinkedHashSet<>(Arrays.asList(oldMethods));
			methodsSet.addAll(Arrays.asList(methods));
			String[] newMethods = methodsSet.toArray(new String[0]);

			methodsField.set(null/* static field */, newMethods);
		} catch (NoSuchFieldException | IllegalAccessException e) {
			throw new IllegalStateException(e);
		}
	}

}
