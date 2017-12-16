package tabels;

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
	
	public UserGroup saveToDB(Connection conn) throws SQLException {
		if (this.getId() == 0) {
			String[] generatedColumns = { "id" };
			PreparedStatement pst = conn.prepareStatement(
					"Insert into  user_group(name) Values(?)", generatedColumns);
			pst.setString(1, this.getName());
			pst.executeUpdate();
			ResultSet rs = pst.getGeneratedKeys();
			if (rs.next()) {
				this.setId(rs.getInt(1));
			}
		} else {
			PreparedStatement pst = conn
					.prepareStatement("Update user_group Set name=? Where id = ?");
			pst.setString(1, this.getName());
			pst.setInt(2, this.getId());
			pst.executeUpdate();
		}
		return this;
	}

	static public UserGroup[] loadAll(Connection conn) throws SQLException {
		ArrayList<UserGroup> userGroups = new ArrayList<>();
		Statement st = conn.createStatement();
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
	 public User[] loadUsers(Connection conn) throws SQLException {
		ArrayList<User> users = new ArrayList<>();
		PreparedStatement st = conn.prepareStatement("Select * from user where user_group_id=?");
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
	static public UserGroup loadById(Connection conn, int id) throws SQLException, NullPointerException {
		String sql = "SELECT * FROM user_group where id=?";
		PreparedStatement preparedStatement = conn.prepareStatement(sql);
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

	public void delete(Connection conn) throws SQLException {
		if (this.id != 0) {
			String sql = "DELETE FROM user_group WHERE id= ?";
			PreparedStatement preparedStatement = conn.prepareStatement(sql);
			preparedStatement.setInt(1, this.id);
			preparedStatement.executeUpdate();
			this.id = 0;
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.getName());
		return sb.toString();
	}

	
	
}