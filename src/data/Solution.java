package data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Solution {
	private int id = 0;
	private String created;
	private String updated;
	private String description;
	private int excerciseId;
	private int userId;

	public Solution() {
	}

	
	public Solution(String updated, String description, int excerciseId, int userId) {
		setCreated(created);
		setUpdated(null);
		setDescription(null);
		setExcerciseId(excerciseId);
		setUserId(userId);
	}
	public Solution(String created, int excerciseId, int userId) {
		setCreated(created);
		setUpdated(null);
		setDescription(null);
		setExcerciseId(excerciseId);
		setUserId(userId);
	}
	public Solution(String created, String updated, String description, int excerciseId, int userId) {
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

	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	public String getUpdated() {
		return updated;
	}

	public void setUpdated(String updated) {
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
	
	/**Inserts Solution to DB if it do not exists, otherwise updates the existing one.
	 * 
	 * @param connection
	 * @return Solution
	 */
	public Solution saveToDB(Connection connection) throws SQLException {
		if (this.getId() == 0) {
			String[] generatedColumns = { "id" };
			PreparedStatement pst = connection.prepareStatement(
					"Insert into  solution(created, updated, description, exercise_id, user_id) Values(?, ?, ?, ?, ?)",
					generatedColumns);
			pst.setString(1, this.getCreated());
			pst.setString(2, this.getUpdated());
			pst.setString(3, this.getDescription());
			pst.setInt(4, this.getExcerciseId());
			pst.setInt(5, this.getUserId());
			pst.executeUpdate();
			ResultSet rs = pst.getGeneratedKeys();
			if (rs.next()) {
				this.setId(rs.getInt(1));
			}
		} else {
			PreparedStatement pst = connection.prepareStatement(
					"Update solution Set created=?, updated=?, description=?, exercise_id=?, user_id=? Where id = ?");
			pst.setString(1, this.getCreated());
			pst.setString(2, this.getUpdated());
			pst.setString(3, this.getDescription());
			pst.setInt(4, this.getExcerciseId());
			pst.setInt(5, this.getUserId());
			pst.setInt(6, this.getId());
			pst.executeUpdate();
		}
		return this;
	}

	/**Deletes Solution from DB.
	  * 
	  * @param connection
	  * @throws SQLException
	  */
	public void delete(Connection connection) throws SQLException {
		if (this.id != 0) {
			String sql = "DELETE FROM solution WHERE id= ?";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, this.id);
			preparedStatement.executeUpdate();
			this.id = 0;
		}
	}

	
	/**Returns list of all Solutions.
	 * 
	 * @param connection
	 * @return Solution[]
	 * @throws SQLException
	 */
	static public Solution[] loadAll(Connection connection) throws SQLException {
		ArrayList<Solution> solutions = new ArrayList<>();
		Statement st = connection.createStatement();
		ResultSet rs = st.executeQuery("Select * from solution");

		while (rs.next()) {
			Solution tempSolution = new Solution();
			tempSolution.setCreated(rs.getString("created"));
			tempSolution.setUpdated(rs.getString("updated"));
			tempSolution.setDescription(rs.getString("description"));
			tempSolution.setExcerciseId(rs.getInt("exercise_id"));
			tempSolution.setUserId(rs.getInt("user_id"));
			tempSolution.setId(rs.getInt("id"));
			solutions.add(tempSolution);
		}
		Solution[] solutionArray = new Solution[solutions.size()];
		solutions.toArray(solutionArray);
		return solutionArray;
	}

	/**Returns Solution by given solution.id.
	 * 
	 * @param Connection connection
	 * @param id
	 * @return Solution
	 * @throws SQLException
	 * @throws NullPointerException
	*/
	static public Solution loadById(Connection conn, int id) throws SQLException, NullPointerException {
		String sql = "SELECT * FROM solution where id=?";
		PreparedStatement preparedStatement = conn.prepareStatement(sql);
		preparedStatement.setInt(1, id);
		ResultSet rs = preparedStatement.executeQuery();
		if (rs.next()) {
			Solution tempSolution = new Solution();
			tempSolution.setCreated(rs.getString("created"));
			tempSolution.setUpdated(rs.getString("updated"));
			tempSolution.setDescription(rs.getString("description"));
			tempSolution.setExcerciseId(rs.getInt("exercise_id"));
			tempSolution.setUserId(rs.getInt("user_id"));
			tempSolution.setId(rs.getInt("id"));
			return tempSolution;
		}
		return null;
	}

	/**Returns list of all Solutions connected to given User.
	 * 
	 * @param connection
	 * @param id
	 * @return Solution[]
	 * @throws SQLException
	 */
	static public Solution[] loadSolutionsByUser(Connection connection, int id) throws SQLException {
		ArrayList<Solution> solutions = new ArrayList<>();
		String sql = "SELECT * FROM solution where user_id=?";
		PreparedStatement st = connection.prepareStatement(sql);
		st.setInt(1, id);
		ResultSet rs = st.executeQuery();
		
		while (rs.next()) {
			Solution tempSolution = new Solution();
			tempSolution.setCreated(rs.getString("created"));
			tempSolution.setUpdated(rs.getString("updated"));
			tempSolution.setDescription(rs.getString("description"));
			tempSolution.setExcerciseId(rs.getInt("exercise_id"));
			tempSolution.setUserId(rs.getInt("user_id"));
			tempSolution.setId(rs.getInt("id"));
			solutions.add(tempSolution);
		}
		Solution[] solutionArray = new Solution[solutions.size()];
		solutions.toArray(solutionArray);
		return solutionArray;
	}
	
	/**Returns list of Solutions connected to the Exercise.
	 * 	
	 * @param connection
	 * @param id
	 * @return Solution[]
	 * @throws SQLException
	 */
	static public Solution[] loadSolutionsByExerciseId(Connection connection, int id) throws SQLException{
		ArrayList<Solution> solutions = new ArrayList<>();
		String sql = "SELECT * FROM solution where exercise_id=? order by created desc";
		PreparedStatement st = connection.prepareStatement(sql);
		st.setInt(1, id);
		ResultSet rs = st.executeQuery();

		while (rs.next()) {
			Solution tempSolution = new Solution();
			tempSolution.setCreated(rs.getString("created"));
			tempSolution.setUpdated(rs.getString("updated"));
			tempSolution.setDescription(rs.getString("description"));
			tempSolution.setExcerciseId(rs.getInt("exercise_id"));
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
		sb.append("Exercise: ").append(this.getExcerciseId()).append(", User: ").append(this.getUserId()).append(", Created: ").append(this.created.substring(0,10)).append("\n   ").append(this.getDescription()).append(" ");
		return sb.toString();
	}

}
