package tabels;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.mindrot.jbcrypt.BCrypt;

public class User {

	private int id = 0;
	private String username;
	 String password;
	private String email;
	private int userGroupId;

	// Wczytywanie z bazy
	public User() {
	}

	// Tworzenie nowego
	public User(String username, String password, String email, int userGroupId) {
		super();
		setUsername(username);
		setPassword(password);
		setEmail(email);
		setUserGroupId(userGroupId);
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

	User setId(int id) {
		this.id = id;
		return this;
	}

	public int getUserGroupId() {
		return userGroupId;
	}

	public void setUserGroupId(int userGroupId) {
		this.userGroupId = userGroupId;
	}

	public User saveToDB(Connection conn) throws SQLException {
		if (this.getId() == 0) {
			String[] generatedColumns = { "id" };
			PreparedStatement pst = conn.prepareStatement(
					"Insert into  user( username, password, email, user_group_id) Values( ?, ?, ?, ?)", generatedColumns);
			pst.setString(1, this.getUsername());
			pst.setString(2, this.getPassword());
			pst.setString(3, this.getEmail());
			pst.setInt(4, getUserGroupId());
			pst.executeUpdate();
			ResultSet rs = pst.getGeneratedKeys();
			if (rs.next()) {
				this.setId(rs.getInt(1));
			}
		} else {
			PreparedStatement pst = conn
					.prepareStatement("Update user Set username=?, password=?, email=?, user_group_id=? Where id = ?");
			pst.setString(1, this.getUsername());
			pst.setString(2, this.getPassword());
			pst.setString(3, this.getEmail());
			pst.setInt(4, getUserGroupId());
			pst.setInt(5, this.getId());
			pst.executeUpdate();
		}
		return this;
	}

	static public User[] loadAll(Connection conn) throws SQLException {
		ArrayList<User> users = new ArrayList<>();
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery("Select * from user");

		while (rs.next()) {
			User tempUser = new User();
			tempUser.setUsername(rs.getString("username"));
			tempUser.setEmail(rs.getString("email"));
			tempUser.password = rs.getString("password");
			tempUser.setUserGroupId(rs.getInt("user_group_id"));
			tempUser.setId(rs.getInt("id"));
			users.add(tempUser);
		}
		User[] userArray = new User[users.size()];
		users.toArray(userArray);
		return userArray;
	}
	static public User[] loadUsersByGroup(Connection conn, int id) throws SQLException {
		ArrayList<User> users = new ArrayList<>();
		PreparedStatement st = conn.prepareStatement("Select * from user where user_group_id=?");
		st.setInt(1, id);
		ResultSet rs = st.executeQuery();
		
		while (rs.next()) {
			User tempUser = new User();
			tempUser.setUsername(rs.getString("username"));
			tempUser.setEmail(rs.getString("email"));
			tempUser.password = rs.getString("password");
			tempUser.setId(rs.getInt("id"));
			users.add(tempUser);
		}
		User[] userArray = new User[users.size()];
		users.toArray(userArray);
		return userArray;
	}

	static public User loadById(Connection conn, int id) throws SQLException, NullPointerException {
		String sql = "SELECT * FROM user where id=?";
		PreparedStatement preparedStatement = conn.prepareStatement(sql);
		preparedStatement.setInt(1, id);
		ResultSet rs = preparedStatement.executeQuery();
		if (rs.next()) {
			User tempUser = new User();
			tempUser.id = rs.getInt("id");
			tempUser.username = rs.getString("username");
			tempUser.password = rs.getString("password");
			tempUser.email = rs.getString("email");
			tempUser.userGroupId = rs.getInt("user_group_id");
			return tempUser;
		}
		return null;
	}
	 public Solution[] loadSolutions(Connection conn) throws SQLException {
		ArrayList<Solution> solutions = new ArrayList<>();
		String sql = "SELECT * FROM solution where user_id=?";
		PreparedStatement st = conn.prepareStatement(sql);
		st.setInt(1, this.getId());
		ResultSet rs = st.executeQuery();
		
		while (rs.next()) {
			Solution tempSolution = new Solution();
			tempSolution.setCreated(rs.getDate("created"));
			tempSolution.setUpdated(rs.getDate("updated"));
			tempSolution.setDescription(rs.getString("description"));
			tempSolution.setExcerciseId(rs.getInt("excercise_id"));
			tempSolution.setUserId(rs.getInt("user_id"));
			tempSolution.setId(rs.getInt("id"));
			solutions.add(tempSolution);
		}
		Solution[] solutionArray = new Solution[solutions.size()];
		solutions.toArray(solutionArray);
		return solutionArray;
	}
	public void delete(Connection conn) throws SQLException {
		if (this.id != 0) {
			String sql = "DELETE FROM user WHERE id= ?";
			PreparedStatement preparedStatement = conn.prepareStatement(sql);
			preparedStatement.setInt(1, this.id);
			preparedStatement.executeUpdate();
			this.id = 0;
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.getUsername()).append(" ").append(this.getEmail());
		return sb.toString();
	}

}
