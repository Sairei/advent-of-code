import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class PuzzleDay {

    public static void main(String[] args) {
		long res_1 = solvePuzzle1();
		System.out.println("Result for puzzle 1 = " + res_1);

		long res_2 = solvePuzzle2();
		System.out.println("Result for puzzle 2 = " + res_2);
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

	private static long optimalRun(String time, String dist) {
		long maxTime = Long.valueOf(time);
		long distanceToBeat = Long.valueOf(dist);

		long nbTimePressBtn = 1;
		long nbTimeBoatMove = maxTime - nbTimePressBtn;
		long distTraveled = nbTimePressBtn * nbTimeBoatMove;
		while ( distTraveled <= distanceToBeat && nbTimePressBtn < maxTime) {
			nbTimePressBtn += 1;
			nbTimeBoatMove = maxTime - nbTimePressBtn;
			distTraveled = nbTimePressBtn * nbTimeBoatMove;
		}

		return maxTime - (2*(nbTimePressBtn-1)) - 1;
	}

	/********************/
	/** The first part **/
	/********************/

	private static long solvePuzzle1() {
		long res = 1;
		ArrayList<String> reader = fileToArrayListOfLines("puzzle_data.txt");

		String[] times = reader.get(0).replace("Time:", "").trim().replaceAll(" +", " ").split(" ");
		String[] distances = reader.get(1).replace("Distance:", "").trim().replaceAll(" +", " ").split(" ");
		for (int run=0; run<times.length; run++) {
			long nbOptiRun = optimalRun(times[run], distances[run]);
			res *= nbOptiRun;
		}

		return res;
	}

	/*********************/
	/** The second part **/
	/*********************/

	private static long solvePuzzle2() {
 		long res = 1;
		ArrayList<String> reader = fileToArrayListOfLines("puzzle_data.txt");

		String[] times = reader.get(0).replace("Time:", "").trim().replaceAll(" +", "").split(" ");
		String[] distances = reader.get(1).replace("Distance:", "").trim().replaceAll(" +", "").split(" ");
		for (int run=0; run<times.length; run++) {
			long nbOptiRun = optimalRun(times[run], distances[run]);
			res *= nbOptiRun;
		}

		return res;
   }
}