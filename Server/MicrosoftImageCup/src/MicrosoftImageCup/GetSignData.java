package MicrosoftImageCup;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

public class GetSignData {
	public static void getSignData(JSONObject jsonObject, Connection cn, PrintWriter pr) throws SQLException{
		String userName = jsonObject.getString("username");
		User user = null;
		
		PreparedStatement ps = cn.prepareStatement("SELECT id,passwd FROM user WHERE username=?");
		ps.setString(1, userName);
		ResultSet rs = ps.executeQuery();
		if(rs.next()){
			int userId = Integer.parseInt(rs.getString(1));
			String passwd = rs.getString(2);
			user = new User(userName, passwd);
			user.setId(userId);
		
		}
		
		int userId = user.getId();
		ps = cn.prepareStatement("SELECT date FROM SignIn WHERE userid=?");
		ps.setString(1, userId+"");
	    rs = ps.executeQuery(); 
	    JSONObject resultJsonObject=new JSONObject();
	    Boolean isNull = true; // true means rs is null
	    
	    @SuppressWarnings("rawtypes")
		List<Map> signInData = new ArrayList<Map>();
		while(rs.next()){
			isNull = false;
			Map<String,String> signInDataMap = new HashMap<String,String>();
			//for(int i=0;i<rs.; rs.next(),i++){
			System.out.println("rs的數據：" + rs.getString(1));
			signInDataMap.put("eachData", rs.getString(1));
			//resultJsonObject.put(i, rs.getString(1)); //{"1":"2017_02_04",}
			//}
			signInData.add(signInDataMap);
		}
		if(!isNull){
			resultJsonObject.put("result", "true");
			resultJsonObject.put("signInData", signInData);
			pr.println(resultJsonObject.toString());
			pr.flush();	
		}else{
			resultJsonObject.put("result", "false");
			pr.println(resultJsonObject.toString());
			pr.flush();	
		}
		
		
	}
}
