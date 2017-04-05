package MicrosoftImageCup;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.sf.json.JSONObject;

public class Register {
	public static void handleRegister(JSONObject jsonObject, Connection cn, PrintWriter pr) throws SQLException{
		User user;
		String userName = jsonObject.getString("username");
		String passwd = jsonObject.getString("password");
		String sex = jsonObject.getString("sex");
		int age = Integer.parseInt(jsonObject.getString("age"));
		String email = jsonObject.getString("email");
		user = new User(userName, passwd, sex, age, email);

		// 执行数据库操作： 1.如果已有用户名，返回false; 2.如果没有，就插入数据库
		PreparedStatement ps = cn.prepareStatement("SELECT userName FROM user WHERE userName=?");
		ps.setString(1, user.getUserName());
		ResultSet rs = ps.executeQuery(); // rs就是SQL查询语句返回的结果集
		if (rs.next()) { // 如果已有用户名，返回false
			// JSON格式数据解析对象
			JSONObject resultJsonObject = new JSONObject();
			resultJsonObject.put("result", "false");
			pr.println(resultJsonObject.toString());
			pr.flush();
		} else { // 如果没有查询到，就写入数据库
			ps = cn.prepareStatement(
					"INSERT INTO user(userName, passwd, sex, age, email) " + "values(?,?,?,?,?)");
			ps.setString(1, user.getUserName());
			ps.setString(2, user.getPasswd());
			ps.setString(3, user.getSex());
			ps.setString(4, user.getAge() + "");
			ps.setString(5, user.getEmail());
			ps.executeUpdate();
			// JSON格式数据解析对象
			JSONObject resultJsonObject = new JSONObject();
			resultJsonObject.put("result", "true");
			pr.println(resultJsonObject.toString());
			pr.flush();
		}
		
		ps.close();
	}
	
}