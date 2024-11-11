import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
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
		BufferedReader reader;
		Map<String, Integer> bag = new HashMap<String, Integer>();
		bag.put("red", 12);
		bag.put("green", 13);
		bag.put("blue", 14);

		try {
			reader = new BufferedReader(new FileReader("puzzle_data.txt"));
			String line = reader.readLine();

			while (line != null) {
				boolean gameIsPossible = true;
				String[] tmp = line.split(": ");
				int idGame = Integer.valueOf(tmp[0].split(" ")[1]);

				String[] reveals = tmp[1].split("; ");
				for(String r : reveals) {
					String[] colorsPick = r.split(", ");
					for(String nbByColor : colorsPick) {
						String[] splitNbPick = nbByColor.split(" ");
						if(Integer.valueOf(splitNbPick[0]) > bag.get(splitNbPick[1])) {
							gameIsPossible = false;
						}
					}
				}

				if(gameIsPossible) {
					res += idGame;
				}
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
				String[] tmp = line.split(": ");
				
				Map<String, Integer> bag = new HashMap<String, Integer>();
				bag.put("red", 0);
				bag.put("green", 0);
				bag.put("blue", 0);

				String[] reveals = tmp[1].split("; ");
				for(String r : reveals) {
					String[] colorsPick = r.split(", ");
					for(String nbByColor : colorsPick) {
						String[] splitNbPick = nbByColor.split(" ");
						if(Integer.valueOf(splitNbPick[0]) > bag.get(splitNbPick[1])) {
							bag.replace(splitNbPick[1], Integer.valueOf(splitNbPick[0]));
						}
					}
				}
				
				int mult = 1;
				for(String i : bag.keySet()) {
					mult *= bag.get(i);
				}
				res += mult;

				line = reader.readLine();
			}

			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return res;
	}
}