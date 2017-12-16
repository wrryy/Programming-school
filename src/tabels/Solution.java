package tabels;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Solution {
	private int id = 0;
	private Date created;
	private Date updated;
	private String description;
	private int excerciseId;
	private int userId;

	public Solution() {
	}

	public Solution(Date created, Date updated, String description, int excerciseId, int userId) {
		super();
		setCreated(created);
		setUpdated(updated);
		setDescription(description);
		setExcerciseId(excerciseId);
		setUserId(userId);
	}

	public int getId() {
		return id;
	}

	void setId(int id) {
		this.id = id;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getUpdated() {
		return updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getExcerciseId() {
		return excerciseId;
	}

	public void setExcerciseId(int excerciseId) {
		this.excerciseId = excerciseId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public Solution saveToDB(Connection conn) throws SQLException {
		if (this.getId() == 0) {
			String[] generatedColumns = { "id" };
			PreparedStatement pst = conn.prepareStatement(
					"Insert into  solution(created, updated, description, excercise_id, user_id) Values(?, ?, ?, ?, ?)",
					generatedColumns);
			pst.setDate(1, this.getCreated());
			pst.setDate(2, this.getUpdated());
			pst.setString(3, this.getDescription());
			pst.setInt(4, this.getExcerciseId());
			pst.setInt(5, this.getUserId());
			pst.executeUpdate();
			ResultSet rs = pst.getGeneratedKeys();
			if (rs.next()) {
				this.setId(rs.getInt(1));
			}
		} else {
			PreparedStatement pst = conn.prepareStatement(
					"Update solution Set created=?, updated=?, description=?, excercise_id=?, user_id=? Where id = ?");
			pst.setDate(1, this.getCreated());
			pst.setDate(2, this.getUpdated());
			pst.setString(3, this.getDescription());
			pst.setInt(4, this.getExcerciseId());
			pst.setInt(5, this.getUserId());
			pst.setInt(6, this.getId());
			pst.executeUpdate();
		}
		return this;
	}

	static public Solution[] loadAll(Connection conn) throws SQLException {
		ArrayList<Solution> solutions = new ArrayList<>();
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery("Select * from solution");

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
	static public Solution[] loadSolutionsByUser(Connection conn, int id) throws SQLException {
		ArrayList<Solution> solutions = new ArrayList<>();
		String sql = "SELECT * FROM solution where user_id=?";
		PreparedStatement st = conn.prepareStatement(sql);
		st.setInt(1, id);
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

	static public Solution loadById(Connection conn, int id) throws SQLException, NullPointerException {
		String sql = "SELECT * FROM solution where id=?";
		PreparedStatement preparedStatement = conn.prepareStatement(sql);
		preparedStatement.setInt(1, id);
		ResultSet rs = preparedStatement.executeQuery();
		if (rs.next()) {
			Solution tempSolution = new Solution();
			tempSolution.setCreated(rs.getDate("created"));
			tempSolution.setUpdated(rs.getDate("updated"));
			tempSolution.setDescription(rs.getString("description"));
			tempSolution.setExcerciseId(rs.getInt("excercise_id"));
			tempSolution.setUserId(rs.getInt("user_id"));
			tempSolution.setId(rs.getInt("id"));
			return tempSolution;
		}
		return null;
	}

	public void delete(Connection conn) throws SQLException {
		if (this.id != 0) {
			String sql = "DELETE FROM solution WHERE id= ?";
			PreparedStatement preparedStatement = conn.prepareStatement(sql);
			preparedStatement.setInt(1, this.id);
			preparedStatement.executeUpdate();
			this.id = 0;
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.getExcerciseId()).append(" ").append(this.getDescription()).append(" ").append(this.getUserId());
		return sb.toString();
	}

}
