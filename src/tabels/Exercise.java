package tabels;

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

	public Exercise saveToDB(Connection conn) throws SQLException {
		if (this.getId() == 0) {
			String[] generatedColumns = { "id" };
			PreparedStatement pst = conn.prepareStatement(
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
			PreparedStatement pst = conn
					.prepareStatement("Update exercise Set title=?, description=? Where id = ?");
			pst.setString(1, this.getTitle());
			pst.setString(2, this.getDescription());
			pst.setInt(3, this.getId());
			pst.executeUpdate();
		}
		return this;
	}

	static public Exercise[] loadAll(Connection conn) throws SQLException {
		ArrayList<Exercise> excercises = new ArrayList<>();
		Statement st = conn.createStatement();
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

	static public Exercise loadById(Connection conn, int id) throws SQLException, NullPointerException {
		String sql = "SELECT * FROM exercise where id=?";
		PreparedStatement preparedStatement = conn.prepareStatement(sql);
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

	public Solution[] loadSolutions(Connection conn) throws SQLException {
		ArrayList<Solution> solutions = new ArrayList<>();
		String sql = "SELECT * FROM solution where exercise_id=?";
		PreparedStatement st = conn.prepareStatement(sql);
		st.setInt(1, this.getId());
		ResultSet rs = st.executeQuery();

		while (rs.next()) {
			Solution tempSolution = new Solution();
			tempSolution.setCreated(rs.getDate("created"));
			tempSolution.setUpdated(rs.getDate("updated"));
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

	public void delete(Connection conn) throws SQLException {
		if (this.id != 0) {
			String sql = "DELETE FROM exercise WHERE id= ?";
			PreparedStatement preparedStatement = conn.prepareStatement(sql);
			preparedStatement.setInt(1, this.id);
			preparedStatement.executeUpdate();
			this.id = 0;
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.getTitle()).append(" ").append(this.getDescription());
		return sb.toString();
	}

}
