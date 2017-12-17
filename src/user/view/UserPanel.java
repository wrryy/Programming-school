package user.view;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Scanner;

import data.Solution;
import system.IntegerInputCheck;

public class UserPanel {
	

	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		String url = "jdbc:mysql://localhost:3306/school?useSSL=false";
		try (Connection conn = DriverManager.getConnection(url, "root", "coderslab")) {
			menu(conn, scan);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	static void menu(Connection connection, Scanner scan) throws SQLException{
		System.out.println("Enter Your User Id:");
		int userId = IntegerInputCheck.check(scan);
		boolean check = true;
		while (check) {
			showMenu();
			String choice = scan.next().toLowerCase();
			if (choice.matches("1|show")) {
				showAllSolutionsByUser(connection, userId);
			} else if (choice.matches("2|add")) {
				showNullSolutionsByUser(connection, userId);
				System.out.println("Choose Exercise ID to add Solution to:");
				int exId = IntegerInputCheck.check(scan); scan.nextLine();
				System.out.println("Add Solution description:");
				String desc = scan.nextLine();
				AddSolution(connection, userId, exId, desc);
//			} else if (choice.matches("2|add")) {
//				System.out.println("Enter Solution ID to add Comment:");
//				AddComment(connection, scan);
			} else if (choice.matches("delete|3")) {
				System.out.printf("Enter Solutions ID to delete:\n");
				Solution.loadById(connection, IntegerInputCheck.check(scan)).delete(connection);
			} else if (choice.matches("4")) {
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
		String menu = MessageFormat
				.format("Choose option:\n" + "1. Show all {0}s \n" + "2. Add {0} \n" + "3. Delete {0} \n"
		// + "3. Edit {0} \n"
					 + "4. Quit", "Solution");
		System.out.println(menu);
	}

	static void showAllSolutionsByUser(Connection connection, int userId) throws SQLException {
		Solution[] solutions = Solution.loadSolutionsByUser(connection, userId);
		for (Solution solution : solutions) {
			System.out.println(solution.toString());
		}System.out.println();
	}

	static void showNullSolutionsByUser(Connection connection, int userId) throws SQLException {
		Solution[] solutions = Solution.loadSolutionsByUser(connection, userId);
		for (Solution solution : solutions) {
			if (solution.getUpdated().equals(null)) {
				System.out.println(solution.toString());
			}
		}System.out.println();
	}

	static void AddSolution(Connection connection, int userId, int exId, String description) throws SQLException {
		String updated = LocalDateTime.now().toString();
		Solution newSolution = new Solution(updated, description, exId, userId);
		newSolution.saveToDB(connection);
	}
}
