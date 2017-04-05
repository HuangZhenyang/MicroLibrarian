package MicrosoftImageCup;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.sf.json.JSONObject;

import java.util.Date;
import java.text.SimpleDateFormat;

public class SignIn {
	public static void signIn(JSONObject jsonObject, Connection cn, PrintWriter pr) throws SQLException{
		System.out.println("Signindata: " + jsonObject.toString());
		String userName = jsonObject.getString("username");
		String userId = "";
		PreparedStatement ps = cn.prepareStatement("SELECT id FROM user WHERE username=?");
		ps.setString(1, userName);
		ResultSet rs = ps.executeQuery();
		JSONObject resultJsonObject = new JSONObject();
		if(rs.next()){
			userId = rs.getString(1);
		}
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy_MM_dd");//设置日期格式
		String date = df.format(new Date());
		
		
		
		
		ps = cn.prepareStatement("INSERT INTO signin(userid, date)" + 
													"VALUES(?, ?)");
		ps.setString(1, userId);
		ps.setString(2, date);//jsonObject.getString("date")
		ps.executeUpdate();
		
		resultJsonObject.put("result", "true");
		pr.println(resultJsonObject);
		pr.flush();
		
		ps.close();
	}
}