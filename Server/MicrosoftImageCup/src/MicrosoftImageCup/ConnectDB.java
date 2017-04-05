package MicrosoftImageCup;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectDB {
	public static Connection connectDB() throws ClassNotFoundException, SQLException{
		String dbDriver = "com.mysql.jdbc.Driver"; // org.gjt.mm.mysql.Driver
		String dbName = "msimagecup";
		String dbUrl = "jdbc:mysql://127.0.0.1:3306/"+dbName; //// URL指向要访问的数据库名
		String dbUser = "root";
		String dbPassword = "123456";
		Class.forName(dbDriver); // 加载驱动程序
		Connection cn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
		return cn;
	}
}
