package Spider;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashSet;
import java.util.Set;

import MicrosoftImageCup.Book;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


public class QueryBook {
	public static Book queryBook(String url, Book book) {
	//public static void main(String[] args){
		
		//Book book = new Book();
		//book.setBookName("童年");
		//book.setAuthor("高尔基");
		//String url = "https://api.douban.com/v2/book/search?q="+book.getBookName();
		
		
		System.out.println("book url: "+ url);
		Book resultBook = null;
        String jsonString = "";
        BufferedReader in = null;
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.connect();
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                jsonString += line;
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        
        //Parse JSON Data
        //System.out.println(jsonString);
        JSONObject jsonObject = JSONObject.fromObject(jsonString);
        String count = jsonObject.getString("count");
        if(count == "0"){ // if could not find the book,then return null
        	return null;
        }
        
        JSONArray jsonArray = jsonObject.getJSONArray("books");
        
        for(int i=0;i < jsonArray.size();i++){
        	System.out.println("############################################");
        	
        	JSONObject jsonObjectRate = JSONObject.fromObject(jsonArray.getJSONObject(i).getString("rating"));
            String doubanRate = jsonObjectRate.getString("average");
        	System.out.println("Average rate:" + doubanRate);
            
            JSONArray jsonArrayTags = JSONArray.fromObject(jsonArray.getJSONObject(i).getString("tags"));
            Set<String> labels = new HashSet<String>();
    		
            for(int j=0;j<jsonArrayTags.size();j++){
            	//System.out.println(jsonArrayTags.getJSONObject(i).toString());
            	JSONObject jsonObjectTag = JSONObject.fromObject(jsonArrayTags.getJSONObject(j).toString());
            	String tag = jsonObjectTag.getString("name");
            	labels.add(tag);
            	System.out.println("Tag_"+j+jsonObjectTag.getString("name"));
            }
            
            //System.out.println(jsonArray.getJSONObject(0).getString("images").toString());
            JSONObject jsonObjectImages = JSONObject.fromObject(jsonArray.getJSONObject(i).getString("images").toString());
            String imgUrl = jsonObjectImages.getString("large");
            System.out.println("Img url:" + imgUrl);
        	
        	String author = jsonArray.getJSONObject(i).getString("author");
        	System.out.println("Author :" + author);
        	if(author.indexOf(book.getAuthor()) != -1){
        		resultBook = new Book(book.getBookName(), book.getAuthor(), 
        				imgUrl, doubanRate, labels);
        		break;
        	}    	
        	
        }
        
        return resultBook;
    }
	
}


/*
 ############################################
Average rate:7.6
Tag_0高尔基
Tag_1外国文学
Tag_2小说
Tag_3苏联
Tag_4名著
Tag_5童年
Tag_6经典
Tag_7外国名著
名著
童年
外国名著
外国文学
苏联
经典
高尔基
小说
Img url:https://img1.doubanio.com/lpic/s2263997.jpg
Author :["[苏] 高尔基"]
 * 
 * */
