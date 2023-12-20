import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.stream.Collectors;
import java.util.*;

public class PuzzleDay {

    public static void main(String[] args) {
		long res_1 = solvePuzzle1();
		System.out.println("Result for puzzle 1 = " + res_1);

		// long res_2 = solvePuzzle2();
		// System.out.println("Result for puzzle 2 = " + res_2);
    }

	// Transform file into ArrayList
	private static ArrayList<String> fileToArrayList(String fileName) {
		BufferedReader reader;

		ArrayList<String> lines = new ArrayList<String>();
		try {
			reader = new BufferedReader(new FileReader(fileName));
			String line = reader.readLine();

			while (line != null) {
				lines.add(line);
				line = reader.readLine();
			}

			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return lines;
	}

	private static void slideNorth(ArrayList<String> map) {
		for (int i=0; i<map.get(0).length(); i++) {
			int firstEmptySpace = 0;
			for (int j=0; j<map.size(); j++) {
				String[] cLine = map.get(j).split("");
				if (cLine[i].equals("O")) {
					cLine[i] = ".";
					map.set(j, String.join("", cLine));
					String[] rollLine = map.get(firstEmptySpace).split("");
					rollLine[i] = "O";
					map.set(firstEmptySpace, String.join("", rollLine));

					firstEmptySpace++;
				} else if (cLine[i].equals("#")) {
					firstEmptySpace = j+1;
				}
			}
		}
	}

	/********************/
	/** The first part **/
	/********************/

	private static long solvePuzzle1() {
		long res = 0;
		ArrayList<String> reader = fileToArrayList("puzzle_day_14.txt");

		slideNorth(reader);
		for (int i=0; i<reader.size(); i++) {
			String line = reader.get(i);
			int height = reader.size() - i;

			res += height * line.chars().filter(ch -> ch == 'O').count();
		}
		
		return res;
	}

	/*********************/
	/** The second part **/
	/*********************/

	private static long solvePuzzle2() {
		long res = 0;
		ArrayList<String> reader = fileToArrayList("data_test.txt");
		
		return res;
	}
}