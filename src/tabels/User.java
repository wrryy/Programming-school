package tabels;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.mindrot.jbcrypt.BCrypt;
//import com.sun.org.apache.bcel.internal.util.BCELifier;

public class User {

	private int id = 0;
	private String username;
	private String password;
	private String email;

	// Wczytywanie z bazy
	public User() {
	}

	// Tworzenie nowego
	public User(String username, String password, String email) {
		super();
		setUsername(username);
		setPassword(password);
		setEmail(email);
	}

	public String getUsername() {
		return username;
	}

	public User setUsername(String username) {
		this.username = username;
		return this;
	}

	public String getPassword() {
		return password;
	}

	public User setPassword(String password) {
		this.password = BCrypt.hashpw(password, BCrypt.gensalt());
		return this;
	}

	public String getEmail() {
		return email;
	}

	public User setEmail(String email) {
		this.email = email;
		return this;
	}

	public int getId() {
		return id;
	}

	private User setId(int id) {
		this.id = id;
		return this;
	}

	public User saveToDB(Connection conn) throws SQLException {
		if (this.getId() == 0) {
			String[] generatedColumns = { "id" };
			PreparedStatement pst = conn.prepareStatement(
					"Insert into  user( username, password, email) Values( ?, ?, ?)", generatedColumns);
			pst.setString(1, this.getUsername());
			pst.setString(2, this.getPassword());
			pst.setString(3, this.getEmail());
			pst.executeUpdate();
			ResultSet rs = pst.getGeneratedKeys();
			if (rs.next()) {
				this.setId(rs.getInt(1));
			}
		} else {
			PreparedStatement pst = conn
					.prepareStatement("Update user Set username=?, password=?, email=? Where id = ?");
			pst.setString(1, this.getUsername());
			pst.setString(2, this.getPassword());
			pst.setString(3, this.getEmail());
			pst.setInt(4, this.getId());
			pst.executeUpdate();
		}
		return this;
	}

	static public User[] loadAllUsers(Connection conn) throws SQLException {
		ArrayList<User> users = new ArrayList<>();
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery("Select * from user");

		while (rs.next()) {
			User tmpUSer = new User();
			tmpUSer.setUsername(rs.getString("username"));
			tmpUSer.setEmail(rs.getString("email"));
			tmpUSer.password = rs.getString("password");
			tmpUSer.setId(rs.getInt("id"));
			users.add(tmpUSer);
		}
		User[] userArray = new User[users.size()];
		users.toArray(userArray);
		return userArray;
	}

	static public User loadUserById(Connection conn, int id) throws SQLException, NullPointerException {
		String sql = "SELECT * FROM user where id=?";
		PreparedStatement preparedStatement = conn.prepareStatement(sql);
		preparedStatement.setInt(1, id);
		ResultSet resultSet = preparedStatement.executeQuery();
		if (resultSet.next()) {
			User loadedUser = new User();
			loadedUser.id = resultSet.getInt("id");
			loadedUser.username = resultSet.getString("username");
			loadedUser.password = resultSet.getString("password");
			loadedUser.email = resultSet.getString("email");
			return loadedUser;
		} 
		return null;
	}

	public void delete(Connection conn) throws SQLException {
		if (this.id != 0) {
			String sql = "DELETE FROM user WHERE id= ?";
			PreparedStatement preparedStatement;
			preparedStatement = conn.prepareStatement(sql);
			preparedStatement.setInt(1, this.id);
			preparedStatement.executeUpdate();
			this.id = 0;
		}else {
			System.out.println("No user with this id!");
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.getUsername()).append(" ").append(this.getEmail());
		return sb.toString();
	}

}
