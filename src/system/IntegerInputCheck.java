package system;

import java.util.Scanner;

public class IntegerInputCheck {
	
	/**
	 * Returns integer from user input if it meets greater than zero condition.
	 * 
	 * @param scanner
	 * @return int
	 */
	public static int check(Scanner scanner) {
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
