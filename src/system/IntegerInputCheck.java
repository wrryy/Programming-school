package system;

import java.util.Scanner;

public class IntegerInputCheck {
	
	/**
	 * Returns integer from user input if it meets greater than zero condition.
	 * 
	 * @param scanner
	 * @return int
	 */
	public static int check(Scanner scan) {
		int number;
		do {
			while (!scan.hasNextInt()) {
				System.out.println("That is not a number!");
				scan.next();
			}
			number = scan.nextInt();
		} while (number <= 0);
		return number;
	}

}
