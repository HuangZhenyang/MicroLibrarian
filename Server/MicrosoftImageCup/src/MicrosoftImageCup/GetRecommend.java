package MicrosoftImageCup;

import org.apache.mahout.cf.taste.common.TasteException;
//import org.apache.mahout.cf.taste.impl.common.LongPrimitiveIterator;
import org.apache.mahout.cf.taste.impl.model.file.*;  
import org.apache.mahout.cf.taste.impl.neighborhood.*;  
//import org.apache.mahout.cf.taste.impl.recommender.*;  
import org.apache.mahout.cf.taste.impl.similarity.*;  
import org.apache.mahout.cf.taste.model.*;   
import org.apache.mahout.cf.taste.recommender.*;  
import org.apache.mahout.cf.taste.similarity.*;

import net.sf.json.JSONObject;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;  
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;

public class GetRecommend {
	public static void handleGetRecommend(JSONObject jsonObject, Connection cn, PrintWriter pr) throws IOException, SQLException, ClassNotFoundException{
		System.out.println("将数据从数据库取出来存到mydata.csv");
		String filePath = "src/data/mydata.csv";
        File file=new File(filePath);
        if(!file.exists()){
        	file.createNewFile();
        	System.out.println("创建了file");
        }	
		int nearCnt = 5;
		int recCnt = 3;
		
		String userName = jsonObject.getString("username");
		//Get user's id, *** IMPORTANT  ***
		PreparedStatement ps = cn.prepareStatement("SELECT id FROM user WHERE userName=?");
		ps.setString(1, userName);
		ResultSet rs = ps.executeQuery();
		int userId  = 0;
		if(rs.next()){
			userId = Integer.parseInt(rs.getString(1));
		}
		
		// Get data from HaveRead and store into mydata.csv
		List<String> dataList=new ArrayList<String>();
		ps = cn.prepareStatement("SELECT userid,bookid,userrate FROM HaveRead");
		rs = ps.executeQuery();
		String eachRecord = "", eachUserId = "", eachBookId="", eachUserRate="";
		Map<Long,Float> resultMap = new HashMap<Long,Float>();
		@SuppressWarnings("rawtypes")
		List<Map> recommendBooks = new ArrayList<Map>();
		
		JSONObject resultJsonObject = new JSONObject();
		
		
		while(rs.next()){
			eachUserId = rs.getString(1);
			eachBookId = rs.getString(2);
			eachUserRate = rs.getString(3);
			eachRecord = eachUserId + "," + eachBookId + "," + eachUserRate;
			dataList.add(eachRecord);
		}
		
		//for(String each:dataList){
		//	System.out.println(each);
		//}
		boolean isSuccess=CSVUtils.importCsv(file, dataList);
		if(isSuccess){
			System.out.println("Success!");
		}
		
		try {
			DataModel model = new FileDataModel(file);
			System.out.println("1");
			UserSimilarity similarity = new EuclideanDistanceSimilarity(model);
			System.out.println("2");
			NearestNUserNeighborhood neighborhood = new NearestNUserNeighborhood(nearCnt, similarity, model);
			System.out.println("3");
			Recommender recommender = new GenericUserBasedRecommender(model, neighborhood, similarity);
			System.out.println("4");
			List<RecommendedItem> recommendations = recommender.recommend(userId, recCnt);
			System.out.println("5 " + recommendations.size());
			
			for (RecommendedItem rItem : recommendations) {
				resultMap.put(rItem.getItemID(), rItem.getValue());
	            System.out.printf("(%s,%f)", rItem.getItemID(), rItem.getValue());
	        }
			
			//Get book's info
			for (Long key : resultMap.keySet()) {  
				ps = cn.prepareStatement("SELECT bookname,author,imgurl,doubanrate FROM Book WHERE id=?");
				ps.setString(1, key+"");
				rs = ps.executeQuery();
			    if(rs.next()){
			    	Map<String, String> eachRecordBookAttr = new HashMap<String, String>();
			    	//book name
					eachRecordBookAttr.put("bookname", rs.getString(1));
					//author
					eachRecordBookAttr.put("author", rs.getString(2));
					//imgurl
					eachRecordBookAttr.put("imgurl", rs.getString(3));
					//douban rate
					eachRecordBookAttr.put("doubanrate", rs.getString(4));
					
					recommendBooks.add(eachRecordBookAttr);
			    }
			} 
			
			resultJsonObject.put("result", "true");
			resultJsonObject.put("recommend", recommendBooks);
			// {"result":"true","recommend":[{"bookname":"XX","author":"XX","imgurl":"XX","doubanrate":"XX"},{......},{......}]}
			System.out.println("推荐结果： " + resultJsonObject.toString());
			pr.println(resultJsonObject.toString());
			pr.flush();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TasteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (file.isFile() && file.exists()) {  
	        
			if(file.delete()){
				System.out.println("mydata.csv has been deleted.");
			}
	        
	    }  
	}
}
