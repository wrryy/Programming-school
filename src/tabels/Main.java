package tabels;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {

	public static void main(String[] args) {
		
		try(Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/school?useSSL=false", "root", "coderslab")){
			User[] users = User.loadAllUsers(conn);
			for(User u:users) {
//				System.out.println(u.toString());
			}
			users[0].setEmail("nowy@mail.com");
			users[0].saveToDB(conn);
			users[2].delete(conn);
//			System.out.println(User.loadUserById(conn, 2).toString());
//			User user = new User("test123","12345","mail123@mail.pl");
//			user.saveToDB(conn);
//			User user1 = new User("test124","12345","mail124@mail.pl");
//			user1.saveToDB(conn);
//			User user2 = new User("test125","12345","mail125@mail.pl");
//			user2.saveToDB(conn);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
