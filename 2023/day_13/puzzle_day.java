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

	private static int findMirror(ArrayList<String> pattern) {
		int mirrorLine = (-1);

		int evenOdd = pattern.size() % 2;

		// Start on top
		int i = 0;
		int startIndex = pattern.size() - 1 - evenOdd;
		for (int j=startIndex; j>i; j--) {
			String l1 = pattern.get(i);
			String l2 = pattern.get(j);

			if (l1.equals(l2)) {
				System.out.println(l1 + "(" + i + ")" + " => " + l2  + "(" + j + ")");
				if (j == (i+1)) {
					mirrorLine = j;
					break;
				} else {
					i++;
				}
			}
		}

		i = pattern.size()-1;
		for (int j=evenOdd; j<i; j++) {
			String l1 = pattern.get(i);
			String l2 = pattern.get(j);

			if (l1.equals(l2)) {
				System.out.println(l1 + "(" + i + ")" + " => " + l2  + "(" + j + ")");
				if (j == (i-1)) {
					mirrorLine = i;
					break;
				} else {
					i--;
				}
			}
		}

		return mirrorLine;
	}

	private static ArrayList<String> rotatePattern(ArrayList<String> pattern) {
		ArrayList<String> rotate = new ArrayList<>();

		for (int i=0; i<pattern.size(); i++) {
			String[] splitLine = pattern.get(i).split("");
			for (int j=0; j<splitLine.length; j++) {
				if (i == 0) {
					rotate.add(splitLine[j]);
				} else {
					rotate.set(j, rotate.get(j) + splitLine[j]);
				}
			}
		}

		return rotate;
	}

	/********************/
	/** The first part **/
	/********************/

	private static long solvePuzzle1() {
		long res = 0;
		ArrayList<String> reader = fileToArrayList("puzzle_day_13.txt");

		ArrayList<String> pattern = new ArrayList<>();
		for (int l=0; l<reader.size(); l++) {
			String line = reader.get(l);

			if (line.length() > 0) {
				pattern.add(line);
			}
			
			if (line.isEmpty() || l == reader.size()-1) {
				System.out.println("----------------");
				int mirrorLine = findMirror(pattern);
				if (mirrorLine == -1) {
					mirrorLine = findMirror(rotatePattern(pattern));
				} else {
					mirrorLine *= 100;
				}

				res += mirrorLine;
				pattern = new ArrayList<>();
				System.out.println("----------------");
			}
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