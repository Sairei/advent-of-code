import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class PuzzleDay {
    public static void main(String[] args) {
		int res_1 = solvePuzzle1();
		System.out.println("Result for puzzle 1 = " + res_1);
		
		int res_2 = solvePuzzle2();
		System.out.println("Result for puzzle 2 = " + res_2);
    }

	private static int solvePuzzle1() {
		int res = 0;
		BufferedReader reader;

		try {
			reader = new BufferedReader(new FileReader("puzzle_day_1.txt"));
			String line = reader.readLine();

			while (line != null) {
				String withoutLetters = line.replaceAll("[a-zA-Z]", "");
				String nb = withoutLetters.charAt(0) + "" + withoutLetters.charAt(withoutLetters.length()-1);
				res += Integer.valueOf(nb);

				line = reader.readLine();
			}

			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return res;
	}

	private static int solvePuzzle2() {
		int res = 0;
		BufferedReader reader;

		try {
			reader = new BufferedReader(new FileReader("puzzle_day_1.txt"));
			String line = reader.readLine();

			while (line != null) {
				String withoutNumbers = line.replaceAll("[0-9]", "_");
				String convertZeroToNumber = withoutNumbers.replaceAll("zero", "0");
				String convertOneToNumber = convertZeroToNumber.replaceAll("one", "1");
				String convertTwoToNumber = convertOneToNumber.replaceAll("two", "2");
				String convertThreeToNumber = convertTwoToNumber.replaceAll("three", "3");
				String convertFourToNumber = convertThreeToNumber.replaceAll("four", "4");
				String convertFiveToNumber = convertFourToNumber.replaceAll("five", "5");
				String convertSixToNumber = convertFiveToNumber.replaceAll("six", "6");
				String convertSevenToNumber = convertSixToNumber.replaceAll("seven", "7");
				String convertEightToNumber = convertSevenToNumber.replaceAll("eight", "8");
				String convertNineToNumber = convertEightToNumber.replaceAll("nine", "9");
				
				String withOnlyNumber = convertNineToNumber.replaceAll("[a-zA-Z_]", "");
				String nb = "0";
				if (withOnlyNumber.length() > 0) {
					nb = withOnlyNumber.charAt(0) + "" + withOnlyNumber.charAt(withOnlyNumber.length()-1);
				}
				res += Integer.valueOf(nb);

				line = reader.readLine();
			}

			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return res;
	}
}