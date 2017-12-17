package admin.view;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.Scanner;

import data.Solution;

public class QueryTableSolution {

	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		String url = "jdbc:mysql://localhost:3306/school?useSSL=false";
		try (Connection conn = DriverManager.getConnection(url, "root", "coderslab")) {
			menu(scan, conn);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	static private String tableName = "Solution";
//	static private String tableDBName = "solution";

	/**
	 * Enables interaction with DB user table.
	 * 
	 * @param scan
	 * @param connection
	 * @throws SQLException
	 */
	public static void menu(Scanner scan, Connection connection) throws SQLException {
		boolean check = true;
		while (check) {
			showMenu();
			String choice = scan.next().toLowerCase();
			if (choice.matches("1|show")) {
				showAllSolutions(connection);

			} else if (choice.matches("2")) {
				System.out.printf("Enter User id to view %s:\n", tableName);
				showSolutionsByUser(connection, scan);
			} else if (choice.matches("3|add")) {
				System.out.printf("Enter info about new %s:\n", tableName);
				addRecord(connection, scan);
//			} else if (choice.matches("edit|3")) {
//				System.out.printf("Enter updated info about %s", tableName);
//				updateRecord(connection, scan);

			} else if (choice.matches("delete|4")) {
				System.out.printf("Enter %s.id  to delete:\n", tableName);
				Solution.loadById(connection, intInputCheck0(scan)).delete(connection);

			} else if (choice.matches("5|quit")) {
				System.out.println("Quiting program.");
				check = false;
			}
		}
	}

	/**
	 * Prints menu options.
	 * 
	 */
	static void showMenu() {
		String menu = MessageFormat.format("Choose option:\n" 
				+ "1. Show all {0}s \n"
				+ "2. Show {0}s by User \n" 
				+ "3. Assign {0} \n"
//				+ "3. Edit {0} \n" 
				+ "4. Delete {0} \n" 
				+ "5. Quit", tableName);
		System.out.println(menu);
	}

	static void showAllSolutions(Connection connection) throws SQLException {
		System.out.println(MessageFormat.format("{0}s:", tableName));
		Solution[] dbRecord = Solution.loadAll(connection);
		for (int i = 0; i < dbRecord.length; i++) {
			System.out.println(dbRecord[i].getId() + ": " + dbRecord[i].toString());
		}
		System.out.println();
	}
	
	static void showSolutionsByUser(Connection connection, Scanner scan) throws SQLException {
		int userId = intInputCheck0(scan);
		Solution[] solutions = Solution.loadSolutionsByUser(connection, userId);
		for(Solution solution: solutions) {
			System.out.println(solution.toString());
		}
	}

	static void addRecord(Connection connection, Scanner scan) throws SQLException {
		QueryTableUser.showAllUsers(connection);
		createRecord(connection, scan);
	}

	/**
	 * Returns new DB record created from user input.
	 * 
	 * @param connection
	 * @param scan
	 * @return
	 * @throws SQLException
	 */
	static void createRecord(Connection connection, Scanner scan) throws SQLException {
		System.out.println("Enter User id:");
		int userId = intInputCheck0(scan);
		System.out.println("Enter Exercise id:");
		int exerciseId = intInputCheck0(scan);
		String created = LocalDateTime.now().toString();
		Solution newDbRecord = new Solution(created, exerciseId, userId);
		newDbRecord.saveToDB(connection);
	}
	

//	/**
//	 * Returns updated DB record.
//	 * 
//	 * @param connection
//	 * @param scan
//	 * @return
//	 * @throws SQLException
//	 */
//	static void updateRecord(Connection connection, Scanner scan) throws SQLException {
//		ArrayList<String> input = getData(connection, scan, 1);
//		Solution updatedDbRecord = Solution.loadById(connection, Integer.parseInt(input.get(0)));
//		updatedDbRecord.setCreated(input.get(1));
//		updatedDbRecord.setUpdated(input.get(2));
//		updatedDbRecord.setDescription(input.get(3));
//		updatedDbRecord.setExcerciseId((Integer.parseInt(input.get(4))));
//		updatedDbRecord.setUserId((Integer.parseInt(input.get(5))));
//		updatedDbRecord.saveToDB(connection);
//	}

//	/**
//	 * Returns list of String user data input.
//	 * 
//	 * @param connection
//	 * @param scan
//	 * @param start
//	 * @return ArrayList<String>
//	 * @throws SQLException
//	 */
//	static ArrayList<String> getData(Connection connection, Scanner scan, int start) throws SQLException {
//		Statement statement = connection.createStatement();
//		ResultSet rs = statement.executeQuery("select * from " + tableDBName);
//		ResultSetMetaData rsmd = rs.getMetaData();
//		int columnsNumber = rsmd.getColumnCount();
//		ArrayList<String> inputData = new ArrayList<>();
//		for (int i = start; i <= columnsNumber; i++) {
//			System.out.println("Type in " + rsmd.getColumnName(i) + ":");
//			if (i == 1) {
//				inputData.add(LocalDateTime.now().toString());
//			} else if (i == 4 || i == 3) {
//			} else if (i == 5 || i == 6) {
//				inputData.add(intInputCheck0(scan) + "");
//			}
//		}
//		return inputData;
//	}

	/**
	 * Returns integer from user input if it meets greater than zero condition.
	 * 
	 * @param scanner
	 * @return int
	 */
	static int intInputCheck0(Scanner scanner) {
		int number;
		do {
			while (!scanner.hasNextInt()) {
				System.out.println("That is not a number!");
				scanner.next();
			}
			number = scanner.nextInt();
		} while (number <= 0);
		return number;
	}
}
