package data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class UserGroup {
	private int id = 0;
	private String name;
	
	public UserGroup() {
	}

	public UserGroup(String name) {
		setName(name);
	}

	public int getId() {
		return id;
	}

	private UserGroup setId(int id) {
		this.id = id;
		return this;
	}

	public String getName() {
		return name;
	}

	public UserGroup setName(String name) {
		this.name = name;
		return this;
	}
	/**Inserts UserGroup to DB if it do not exists, otherwise updates the existing one.
	 * 
	 * @param connection
	 * @return UserGroup
	 */
	public UserGroup saveToDB(Connection connection) throws SQLException {
		if (this.getId() == 0) {
			String[] generatedColumns = { "id" };
			PreparedStatement pst = connection.prepareStatement(
					"Insert into  user_group(name) Values(?)", generatedColumns);
			pst.setString(1, this.getName());
			pst.executeUpdate();
			ResultSet rs = pst.getGeneratedKeys();
			if (rs.next()) {
				this.setId(rs.getInt(1));
			}
		} else {
			PreparedStatement pst = connection
					.prepareStatement("Update user_group Set name=? Where id = ?");
			pst.setString(1, this.getName());
			pst.setInt(2, this.getId());
			pst.executeUpdate();
		}
		return this;
	}
	
	/**Deletes UserGroup from DB.
	  * 
	  * @param connection
	  * @throws SQLException
	  */
	public void delete(Connection connection) throws SQLException {
		if (this.id != 0) {
			String sql = "DELETE FROM user_group WHERE id= ?";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, this.id);
			preparedStatement.executeUpdate();
			this.id = 0;
		}
	}
	
	/**Returns list of all UserGroups.
	 * 
	 * @param Connection connection
	 * @return UserGroup[]
	 * @throws SQLException
	 */
	static public UserGroup[] loadAll(Connection connection) throws SQLException {
		ArrayList<UserGroup> userGroups = new ArrayList<>();
		Statement st = connection.createStatement();
		ResultSet rs = st.executeQuery("Select * from user_group");

		while (rs.next()) {
			UserGroup tempUserGroup = new UserGroup();
			tempUserGroup.setName(rs.getString("name"));
			tempUserGroup.setId(rs.getInt("id"));
			userGroups.add(tempUserGroup);
		}
		UserGroup[] userArray = new UserGroup[userGroups.size()];
		userGroups.toArray(userArray);
		return userArray;
	}
	
	/**Returns list of Users in User Group.
	 * 
	 * @param connection
	 * @return User[]
	 * @throws SQLException
	 */
	 public User[] loadUsers(Connection connection) throws SQLException {
		ArrayList<User> users = new ArrayList<>();
		PreparedStatement st = connection.prepareStatement("Select * from user where user_group_id=?");
		st.setInt(1, this.getId());
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
	 
	 /**Returns UserGroup by given user_group.id.
	 * 
	 * @param connection
	 * @param id
	 * @return UserGroup
	 * @throws SQLException
	 * @throws NullPointerException
	 */
	static public UserGroup loadById(Connection connection, int id) throws SQLException, NullPointerException {
		String sql = "SELECT * FROM user_group where id=?";
		PreparedStatement preparedStatement = connection.prepareStatement(sql);
		preparedStatement.setInt(1, id);
		ResultSet resultSet = preparedStatement.executeQuery();
		if (resultSet.next()) {
			UserGroup loadedUserGroup = new UserGroup();
			loadedUserGroup.id = resultSet.getInt("id");
			loadedUserGroup.name = resultSet.getString("name");
			return loadedUserGroup;
		} 
		return null;
	}
	

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.getName());
		return sb.toString();
	}

	
	
}