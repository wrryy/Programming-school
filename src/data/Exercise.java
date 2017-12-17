package data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Exercise {
	private int id = 0;
	private String title;
	private String description;

	public Exercise() {
	}

	public Exercise(String title, String description) {
		super();
		setTitle(title);
		setDescription(description);
	}

	public int getId() {
		return id;
	}

	void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	/**Inserts Exercise to DB if it do not exists, otherwise updates the existing one.
	 * 
	 * @param connection
	 * @return Exercise
	 */
	public Exercise saveToDB(Connection connection) throws SQLException {
		if (this.getId() == 0) {
			String[] generatedColumns = { "id" };
			PreparedStatement pst = connection.prepareStatement(
					"Insert into  exercise(title, description) Values( ?, ?)",
					generatedColumns);
			pst.setString(1, this.getTitle());
			pst.setString(2, this.getDescription());
			pst.executeUpdate();
			ResultSet rs = pst.getGeneratedKeys();
			if (rs.next()) {
				this.setId(rs.getInt(1));
			}
		} else {
			PreparedStatement pst = connection
					.prepareStatement("Update exercise Set title=?, description=? Where id = ?");
			pst.setString(1, this.getTitle());
			pst.setString(2, this.getDescription());
			pst.setInt(3, this.getId());
			pst.executeUpdate();
		}
		return this;
	}
	
	/**Deletes Exercise from DB
	 * 
	 * @param connection
	 * @throws SQLException
	 */
	public void delete(Connection connection) throws SQLException {
		if (this.id != 0) {
			String sql = "DELETE FROM exercise WHERE id= ?";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, this.id);
			preparedStatement.executeUpdate();
			this.id = 0;
		}
	}
	
	/**Returns list of all Exercises.
	 * 
	 * @param connection
	 * @return Exercise[]
	 * @throws SQLException
	 */
	static public Exercise[] loadAll(Connection connection) throws SQLException {
		ArrayList<Exercise> excercises = new ArrayList<>();
		Statement st = connection.createStatement();
		ResultSet rs = st.executeQuery("Select * from exercise");

		while (rs.next()) {
			Exercise tempExercise = new Exercise();
			tempExercise.setTitle(rs.getString("title"));
			tempExercise.setDescription(rs.getString("description"));
			tempExercise.setId(rs.getInt("id"));
			excercises.add(tempExercise);
		}
		Exercise[] exerciseArray = new Exercise[excercises.size()];
		excercises.toArray(exerciseArray);
		return exerciseArray;
	}
	
	/**Returns Exercise by given exercise.id.
	 * 
	 * @param connection
	 * @param id
	 * @return Exercise
	 * @throws SQLException
	 * @throws NullPointerException
	*/
	static public Exercise loadById(Connection connection, int id) throws SQLException, NullPointerException {
		String sql = "SELECT * FROM exercise where id=?";
		PreparedStatement preparedStatement = connection.prepareStatement(sql);
		preparedStatement.setInt(1, id);
		ResultSet rs = preparedStatement.executeQuery();
		if (rs.next()) {
			Exercise tempExcercise = new Exercise();
			tempExcercise.id = rs.getInt("id");
			tempExcercise.title = rs.getString("title");
			tempExcercise.description = rs.getString("description");
			return tempExcercise;
		}
		return null;
	}

	/**Returns list of all User Solutions of the Exercise.
	 * 	
	 * @param connection
	 * @return Solution[]
	 * @throws SQLException
	 */
	 public Solution[] loadSolutions(Connection connection) throws SQLException {
		ArrayList<Solution> solutions = new ArrayList<>();
		String sql = "SELECT * FROM solution where exercise_id=? order by created desc";
		PreparedStatement st = connection.prepareStatement(sql);
		st.setInt(1, this.getId());
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
	 
	 /**Returns list of Solutions of specific User.
	  * 	
	  * @param connection
	  * @param id
	  * @return Solution[]
	  * @throws SQLException
	  */
	 public Solution loadSolutionByUser(Connection connection, int id) throws SQLException {
		 String sql = "SELECT * FROM solution where user_id=?";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, id);
			ResultSet rs = preparedStatement.executeQuery();
			if (rs.next()) {
				Solution tempSolution = new Solution();
				tempSolution.setCreated(rs.getString("created"));
				tempSolution.setUpdated(rs.getString("updated"));
				tempSolution.setDescription(rs.getString("description"));
				tempSolution.setExcerciseId(rs.getInt("excercise_id"));
				tempSolution.setUserId(rs.getInt("user_id"));
				tempSolution.setId(rs.getInt("id"));
				return tempSolution;
			}
			return null;
		}
	
	

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.getTitle()).append(" ").append(this.getDescription());
		return sb.toString();
	}

}
