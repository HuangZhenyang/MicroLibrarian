package MicrosoftImageCup;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.sf.json.JSONObject;

import Spider.QueryBook;

public class RecordBook {
	@SuppressWarnings("resource")
	public static void handleRecordBook(JSONObject jsonObject, Connection cn, PrintWriter pr)
			throws SQLException, ClassNotFoundException {
		Book book;
		String bookName = jsonObject.getString("bookName");
		String author = jsonObject.getString("author");
		book = new Book(bookName, author);
		String bookid = "";
		Boolean isRead = false;
		String userName = jsonObject.getString("userName");
		String status = jsonObject.getString("status");
		User user = null;
		JSONObject resultJsonObject = new JSONObject();

		PreparedStatement ps = cn.prepareStatement("SELECT id,passwd FROM user WHERE username=?");
		ps.setString(1, userName);
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			int userId = Integer.parseInt(rs.getString(1));
			String passwd = rs.getString(2);
			user = new User(userName, passwd);
			user.setId(userId);
		}
		// Database Options
		// 1.see if book has been stored in TABLE Book 2.if not, call crawler
		// and store.
		ps = cn.prepareStatement("SELECT id FROM book WHERE bookname=? and author=?");
		ps.setString(1, book.getBookName());
		ps.setString(2, book.getAuthor());
		rs = ps.executeQuery();
		if (rs.next()) {
			bookid = rs.getString(1);

		} else {
			/* Call Crawler and Store */
			System.out.println("数据库里没有这本书，开始启动爬虫");
			String bookUrl = "https://api.douban.com/v2/book/search?q=" + bookName;
			Book resultBook = QueryBook.queryBook(bookUrl, book);
			if (resultBook == null) {
				resultJsonObject.put("result", "false");
				pr.println(resultJsonObject.toString());
				pr.flush();
			} else if (resultBook != null) {
				ps = cn.prepareStatement(
						"INSERT INTO book(bookname,author,imgurl,doubanrate,label)" + "VALUES(?,?,?,?,?)");
				ps.setString(1, resultBook.getBookName());
				ps.setString(2, resultBook.getAuthor());
				ps.setString(3, resultBook.getImgurl());
				ps.setString(4, resultBook.getDoubanRate());
				ps.setString(5, resultBook.getResultLabel());
				ps.executeUpdate();

				// Get Book's id
				ps = cn.prepareStatement("SELECT id FROM book WHERE bookname=? and author=?");
				ps.setString(1, book.getBookName());
				ps.setString(2, book.getAuthor());
				rs = ps.executeQuery();
				if (rs.next()) {
					bookid = rs.getString(1);
				}

				
			}

		}
		
		
		System.out.println("status " + status);
		// And then see if TABLE HaveRead has this book use bookid
		if (status.equals("have")) {
			System.out.println("判断用户是否已经读过这本书（查看Haveread表的书籍id是否有该用户输入的id）");
			ps = cn.prepareStatement("SELECT bookid FROM HaveRead WHERE userid=?");
			ps.setString(1, user.getId() + "");
			rs = ps.executeQuery();

			while (rs.next()) {// if user has read any book ever
				if (rs.getString(1) == bookid) {
					isRead = true;
					break;
				}
			}

			if (!isRead) { // if user hasn't read this book. Store it!
				System.out.println("用户还没读过这本书，插入HaveRead的书籍id");
				String userRate = jsonObject.getString("userRate");
				ps = cn.prepareStatement("INSERT INTO HavaRead(userid, bookid, userrate)" + "VALUES(?, ?, ?)");
				ps.setString(1, user.getId() + "");
				ps.setString(2, bookid);
				ps.setString(3, userRate);
				ps.executeUpdate();

			}
		} else if (status.equals("never")) {
			String plan = jsonObject.getString("bookPlan");
			ps = cn.prepareStatement("INSERT INTO BookPlan(userid, bookid, plan)" + "VALUES(?, ?, ?)");
			ps.setString(1, user.getId() + "");
			ps.setString(2, bookid);
			ps.setString(3, plan);
			ps.executeUpdate();

		}

		resultJsonObject.put("result", "true");
		pr.println(resultJsonObject.toString());
		pr.flush();

		ps.close();
	}
}
