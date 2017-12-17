package data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.mindrot.jbcrypt.BCrypt;

public class User  {

	private int id = 0;
	private String username;
	 String password;
	private String email;
	private int userGroupId;

	// loading from DB
	public User() {
	}

	// creating new User
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
		this.password = BCrypt.hashpw(password, BCrypt.gensalt());		//hashing user password
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

	/**Inserts User to DB if it do not exists, otherwise updates the existing one.
	 * 
	 * @param connection
	 * @return User
	 * @throws SQLException
	 */
	public User saveToDB(Connection connection) throws SQLException {
		if (this.getId() == 0) {
			String[] generatedColumns = { "id" };
			PreparedStatement pst = connection.prepareStatement(
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
			PreparedStatement pst = connection
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
	

	 /**Deletes User from DB.
	  * 
	  * @param connection
	  * @throws SQLException
	  */
	public void delete(Connection connection) throws SQLException {
		if (this.id != 0) {
			String sql = "DELETE FROM user WHERE id= ?";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, this.id);
			preparedStatement.executeUpdate();
			this.id = 0;
		}
	}
	
	/**Returns list of all Users.
	 * 
	 * @param connection
	 * @return User[]
	 * @throws SQLException
	 */
	static public User[] loadAll(Connection connection) throws SQLException {
		ArrayList<User> users = new ArrayList<>();
		Statement st = connection.createStatement();
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
	
	/**Returns list of Users in specific User Group.
	 * 
	 * @param connection
	 * @param id
	 * @return User[]
	 * @throws SQLException
	 */
	static public User[] loadUsersByGroup(Connection connection, int id) throws SQLException {
		ArrayList<User> users = new ArrayList<>();
		PreparedStatement st = connection.prepareStatement("Select * from user where user_group_id=?");
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

	/**Returns User by given user.id.
	 * 
	 * @param connection
	 * @param id
	 * @return User
	 * @throws SQLException
	 * @throws NullPointerException
	 */
	static public User loadById(Connection connection, int id) throws SQLException {
		String sql = "SELECT * FROM user where id=?";
		PreparedStatement preparedStatement = connection.prepareStatement(sql);
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
	
	/**Returns all Solutions of User.
	 * 
	 * @param connection
	 * @return Solution[]
	 * @throws SQLException
	 */
	 public Solution[] loadSolutions(Connection connection) throws SQLException {
		ArrayList<Solution> solutions = new ArrayList<>();
		String sql = "SELECT * FROM solution where user_id=?";
		PreparedStatement st = connection.prepareStatement(sql);
		st.setInt(1, this.getId());
		ResultSet rs = st.executeQuery();
		
		while (rs.next()) {
			Solution tempSolution = new Solution();
			tempSolution.setCreated(rs.getString("created"));
			tempSolution.setUpdated(rs.getString("updated"));
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
	

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.getUsername()).append(", ").append(this.getEmail());
		return sb.toString();
	}

}
