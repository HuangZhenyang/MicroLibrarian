package Spider;

import java.sql.Connection;
//import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.Set;
import java.util.HashSet;

import MicrosoftImageCup.Book;
import MicrosoftImageCup.ConnectDB;

public class StoreBookToDB {
	public static void storeBook(Book book) throws ClassNotFoundException, SQLException{
		//Database 
		Connection cn = ConnectDB.connectDB();
		//Statement st = cn.createStatement(); // state1ment用来执行SQL语句
		//DB operation
		PreparedStatement ps = cn.prepareStatement("SELECT id FROM book WHERE bookname=?");
		ps.setString(1, book.getBookName());
		//ps.setString(2, book.getAuthor());
		ResultSet rs = ps.executeQuery();
		if(rs.next()){ // if this book has been in DB,then pass 
			
		}else{ // else store it into DB
			//Get Book label in Table BookLabel
			Set<String> labelIDSet = new HashSet<String>();
			String tags = "";
			for(String each : book.getLabel()){
				ps = cn.prepareStatement("SELECT labelid FROM booklabel WHERE label=?");
				ps.setString(1, each);
				rs = ps.executeQuery();
				if(rs.next()){ // if label has been in booklabel DB, get its labelid
					labelIDSet.add(rs.getString(1));
				}else{ // if not,store it into booklabel DB and then get its labelid
					//store
					ps = cn.prepareStatement("INSERT INTO booklabel(label) " + "VALUES(?)");
					ps.setString(1, each);
					ps.executeUpdate();
					//get labelid
					ps = cn.prepareStatement("SELECT labelid FROM booklabel WHERE label=?");
					ps.setString(1, each);
					rs = ps.executeQuery();
					// create labelIDSet
					if(rs.next()){
						String result = rs.getString(1);
						labelIDSet.add(result);
					}
					
				}
			}
			for(String each:labelIDSet){
				tags += (each+"_");
			}
			tags = tags.substring(0, tags.length()-1); //remove the last "_"
			
			
			String sqlQuery = "INSERT INTO book(bookname, author, imgurl, doubanrate, label)" + 
								"VALUES(?, ?, ?, ?, ?)";
			ps = cn.prepareStatement(sqlQuery);
			ps.setString(1, book.getBookName());
			ps.setString(2, book.getAuthor());
			ps.setString(3, book.getImgurl());
			if((book.getDoubanRate()+"").equals("")){
				ps.setString(4, "0.0");
			}else{
				ps.setString(4, book.getDoubanRate()+"");
			}
			
			ps.setString(5, tags);
			ps.executeUpdate();
		}
	ps.close();
	cn.close();
		
	}
}
