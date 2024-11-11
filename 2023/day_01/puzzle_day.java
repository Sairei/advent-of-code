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
			reader = new BufferedReader(new FileReader("puzzle_data.txt"));
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
			reader = new BufferedReader(new FileReader("puzzle_data.txt"));
			String line = reader.readLine();

			while (line != null) {
				String convertZeroToNumber = line.replaceAll("zero", "z0o");
				String convertOneToNumber = convertZeroToNumber.replaceAll("one", "o1e");
				String convertTwoToNumber = convertOneToNumber.replaceAll("two", "t2w");
				String convertThreeToNumber = convertTwoToNumber.replaceAll("three", "t3e");
				String convertFourToNumber = convertThreeToNumber.replaceAll("four", "f4r");
				String convertFiveToNumber = convertFourToNumber.replaceAll("five", "f5e");
				String convertSixToNumber = convertFiveToNumber.replaceAll("six", "s6x");
				String convertSevenToNumber = convertSixToNumber.replaceAll("seven", "s7x");
				String convertEightToNumber = convertSevenToNumber.replaceAll("eight", "e8t");
				String convertNineToNumber = convertEightToNumber.replaceAll("nine", "n9e");
				
				String withOnlyNumber = convertNineToNumber.replaceAll("[a-zA-Z]", "");
				String nb = withOnlyNumber.charAt(0) + "" + withOnlyNumber.charAt(withOnlyNumber.length()-1);
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