package MicrosoftImageCup;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.sf.json.JSONObject;

public class Login {
	
	public static void handleLogin(JSONObject jsonObject, Connection cn, PrintWriter pr) throws SQLException{
		//User tempUser = null;
		String userName = jsonObject.getString("username");
		//String passwd = jsonObject.getString("password");
		//tempUser = new User(userName, passwd);
		// 执行数据库操作：如果有该用户就返回登陆成功：true,否则返回false
		PreparedStatement ps = cn.prepareStatement("SELECT id FROM user WHERE userName=?");
		ps.setString(1, userName);
		ResultSet rs = ps.executeQuery(); // rs就是SQL查询语句返回的结果集
		if (rs.next()) { // 登陆成功
			//tempUser.setId(Integer.parseInt(rs.getString(1)));
			//user = tempUser;
			// JSON格式数据解析对象
			JSONObject resultJsonObject = new JSONObject();
			resultJsonObject.put("result", "true");
			pr.println(resultJsonObject.toString() + "\n");
			pr.flush();
			System.out.println("登录成功  " + resultJsonObject.toString());
		} else { // 登陆失败
			// JSON格式数据解析对象
			JSONObject resultJsonObject = new JSONObject();
			resultJsonObject.put("result", "false");
			pr.println(resultJsonObject.toString() + "\n");
			pr.flush();
		}
		
		ps.close();
	}
	
}
