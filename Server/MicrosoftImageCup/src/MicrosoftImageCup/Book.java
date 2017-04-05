package MicrosoftImageCup;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class Book {
	private String bookName;
	private String author;
	private String imgurl;
	private String doubanRate;
	private Set<String> labels;

	public Book(){
		
	}
	public Book(String initBookName, String initAuthor){
		this.bookName = initBookName;
		this.author = initAuthor;
	}
	
	public Book(String initBookName, String initAuthor, String initImgurl, 
			String initDoubanRate, Set<String> initLabels){
		this.bookName = initBookName;
		this.author = initAuthor;
		this.imgurl = initImgurl;
		this.doubanRate = initDoubanRate;
		this.labels = initLabels;
	}
	
	
	// Setter and Getter
	public String getBookName() {
		return bookName;
	}
	public void setBookName(String bookName) {
		this.bookName = bookName;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getImgurl() {
		return imgurl;
	}
	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}
	public String getDoubanRate() {
		return doubanRate;
	}
	public void setDoubanRate(String doubanRate) {
		this.doubanRate = doubanRate;
	}
	public Set<String> getLabel() {
		
		return labels;
	}
	public void setLabel(Set<String> label) {
		this.labels = label;
	}
	
	public String getResultLabel() throws ClassNotFoundException, SQLException{

		String tags = "";
		Set<String> labelIDSet = new HashSet<String>();
		PreparedStatement ps = null;
		ResultSet rs = null; 
		Connection cn = ConnectDB.connectDB();
		for(String each : this.getLabel()){
			ps = cn.prepareStatement("SELECT labelid FROM booklabel WHERE label=?");
			ps.setString(1, each);
			rs = ps.executeQuery();
			if(rs.next()){ // if label has been in booklabel DB, get its labelid
				labelIDSet.add(rs.getString(1));
			}else{ // if not,store it into booklabel DB and then get its labelid
				//store
				ps = cn.prepareStatement("INSERT INTO booklabel(label) " + "VALUES(?)");
				System.out.println("这是在Book.getResultLabel()里,每一个标签是： " + each);
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
		return tags;
	}
	
}
