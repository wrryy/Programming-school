package view;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Scanner;

import data.User;

public class QueryDB {

	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		String url = "jdbc:mysql://localhost:3306/school?useSSL=false";
		try (Connection conn = DriverManager.getConnection(url, "root", "coderslab")) {
			menu(scan, conn);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	static private String tableName = "User";
	static private String tableDBName = "user";

	/**
	 * Enables interaction with DB user table.
	 * 
	 * @param scan
	 * @param connection
	 * @throws SQLException
	 */
	static void menu(Scanner scan, Connection connection) throws SQLException {
		boolean check = true;
		while (check) {
			showMenu();
			String choice = scan.next();
			if (choice.equals("1") || choice.toLowerCase().equals("show")) {
				System.out.println(MessageFormat.format("{0}s:", tableName));
				User[] dbRecord = User.loadAll(connection);
				for (int i = 0; i < dbRecord.length; i++) {
					System.out.println(dbRecord[i].getId() + ": " + dbRecord[i].toString());
				}
				System.out.println();

			} else if (choice.equals("2") || choice.toLowerCase().equals("add")) {
				System.out.printf("Enter info about new %s\n", tableName);
				ArrayList<String> input = getData(connection, scan, 2);
				User newDbRecord = new User(input.get(0), input.get(2), input.get(1), Integer.parseInt(input.get(3)));
				newDbRecord.saveToDB(connection);

			} else if (choice.equals("3") || choice.toLowerCase().equals("edit")) {
				System.out.printf("Enter updated info about %s", tableName);
				ArrayList<String> input = getData(connection, scan, 1);
				User updatedDbRecord = User.loadById(connection, Integer.parseInt(input.get(0)));
				updatedDbRecord.setUsername(input.get(1));
				updatedDbRecord.setEmail(input.get(2));
				updatedDbRecord.setPassword(input.get(3));
				updatedDbRecord.setUserGroupId(Integer.parseInt(input.get(4)));
				updatedDbRecord.saveToDB(connection);
			} else if (choice.equals("4") || choice.toLowerCase().equals("delete")) {
				System.out.printf("Enter %s.id  to delete:", tableName);
				User.loadById(connection, intInputCheck0(scan)).delete(connection);

			} else if (choice.equals("5") || choice.toLowerCase().equals("quit")) {
				System.out.println("Quiting program.");
				check = false;
			}
		}
	}

	/**
	 * Prints menu options.
	 * 
	 */
	static public void showMenu() {
		String menu = MessageFormat.format("Choose option:\n" + "1. Show all {0}s \n" + "2. Add {0} \n"
				+ "3. Edit {0} \n" + "4. Delete {0} \n" + "5. Quit", tableName);
		System.out.println(menu);
	}

	/**
	 * Returns list of String user data input.
	 * 
	 * @param connection
	 * @param scan
	 * @param start
	 * @return ArrayList<String>
	 * @throws SQLException
	 */
	static ArrayList<String> getData(Connection connection, Scanner scan, int start) throws SQLException {
		Statement statement = connection.createStatement();
		ResultSet rs = statement.executeQuery("select * from " + tableDBName);
		ResultSetMetaData rsmd = rs.getMetaData();
		int columnsNumber = rsmd.getColumnCount();
		ArrayList<String> inputData = new ArrayList<>();
		if (rs.next()) {
			for (int i = start; i <= columnsNumber; i++) {
				System.out.println("Type in " + rsmd.getColumnName(i) + ":");
				inputData.add(scan.next());
			}
		}
		return inputData;
	}

	/**
	 * Returns integer from user input if it meets certain conditions.
	 * 
	 * @param scanner
	 * @return int
	 */
	static int intInputCheck0(Scanner scanner) {
		int number;
		do {
			while (!scanner.hasNextInt()) {
				System.out.println("That's not a number!");
				scanner.next(); // this is important!
			}
			number = scanner.nextInt();
		} while (number <= 0);
		return number;
	}
}
