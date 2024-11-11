import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.Math;
import java.util.*;

public class PuzzleDay {

    public static void main(String[] args) {
		int res_1 = solvePuzzle1();
		System.out.println("Result for puzzle 1 = " + res_1);

		int res_2 = solvePuzzle2();
		System.out.println("Result for puzzle 2 = " + res_2);
    }

	private static int solvePuzzle1() {
		int res = 0;

		ArrayList<String> reader = fileToArrayListOfLines("puzzle_data.txt");

		for (String line : reader) {
			int nbWin = 0;
			ArrayList<String> array = splitNumberOnCard(line);
			String winningNumbers = array.get(0);
			String[] numbers = array.get(1).split(" ");
			
			for(String n : numbers) {
				if(Arrays.asList(winningNumbers.split(" ")).contains(n)) {
					nbWin++;
				}
			}
							
			res += Math.pow(2, nbWin-1);
		}

		return res;
	}

	private static int solvePuzzle2() {
		int res = 0;

		ArrayList<String> reader = fileToArrayListOfLines("puzzle_data.txt");
		int[] copy = new int[reader.size()];

		for (int i=0; i<reader.size(); i++) {
			String line = reader.get(i);
			ArrayList<String> array = splitNumberOnCard(line);
			String winningNumbers = array.get(0);
			String[] numbers = array.get(1).split(" ");
			
			int nbWin = 0;
			for(String n : numbers) {
				if(Arrays.asList(winningNumbers.split(" ")).contains(n)) {
					nbWin++;
					if ((i+nbWin) < reader.size()) {
						copy[i+nbWin] += copy[i]+1;
					}
				}
			}
		}
		for (int nbCopy : copy) {
			res += (nbCopy+1);
		}

		return res;
	}

	// Split winning number of number we have
	private static ArrayList<String> splitNumberOnCard(String line) {
		// Replace double space to simple one
		line = line.replace("  ", " ");

		String game = line.split(": ")[1];
		String[] splitNumbers = game.split(" \\| ");

		ArrayList<String> res = new ArrayList<String>();
		res.add(splitNumbers[0]);
		res.add(splitNumbers[1]);

		return res;
	}

	// Transform file into ArrayList
	private static ArrayList<String> fileToArrayListOfLines(String fileName) {
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
}