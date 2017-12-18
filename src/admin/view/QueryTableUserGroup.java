package admin.view;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Scanner;

import data.UserGroup;
import system.IntegerInputCheck;

public class QueryTableUserGroup {

	static private String tableName = "User Group";
	static private String tableDBName = "user_group";

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
				showAllUserGroups(connection);

			} else if (choice.matches("2|add")) {
				System.out.printf("Enter info about new %s:\n", tableName);
				createRecord(connection, scan);

			} else if (choice.matches("edit|3")) {
				System.out.printf("Enter updated info about %s:\n", tableName);
				updateRecord(connection, scan);
			} else if (choice.matches("delete|4")) {
				System.out.printf("Enter %s.id  to delete:\n", tableName);
				UserGroup.loadById(connection, IntegerInputCheck.check(scan)).delete(connection);

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
		String menu = MessageFormat.format("Choose option:\n" + "1. Show all {0}s \n" + "2. Add {0} \n"
				+ "3. Edit {0} \n" + "4. Delete {0} \n" + "5. Quit", tableName);
		System.out.println(menu);
	}
	
	/**Prints all User Groups in DB.
	 * 
	 * @param connection
	 * @throws SQLException
	 */
	static void showAllUserGroups(Connection connection) throws SQLException {
		System.out.println(MessageFormat.format("{0}s:", tableName));
		UserGroup[] dbRecord = UserGroup.loadAll(connection);
		for (int i = 0; i < dbRecord.length; i++) {
			System.out.println(dbRecord[i].getId() + ": " + dbRecord[i].toString());
		}
		System.out.println();
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
		ArrayList<String> input = getData(connection, scan, 2);
		UserGroup newDbRecord = new UserGroup(input.get(0));
		newDbRecord.saveToDB(connection);
	}

	/**
	 * Returns updated DB record.
	 * 
	 * @param connection
	 * @param scan
	 * @return
	 * @throws SQLException
	 */
	static void updateRecord(Connection connection, Scanner scan) throws SQLException {
		ArrayList<String> input = getData(connection, scan, 1);
		UserGroup updatedDbRecord = UserGroup.loadById(connection, Integer.parseInt(input.get(0)));
		updatedDbRecord.setName(input.get(1));
		updatedDbRecord.saveToDB(connection);
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
		for (int i = start; i <= columnsNumber; i++) {
			System.out.println("Type in " + rsmd.getColumnName(i) + ":");
			inputData.add(scan.nextLine());
		}
		return inputData;
	}
}
