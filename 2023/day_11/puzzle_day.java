import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.stream.Collectors;
import java.util.*;

public class PuzzleDay {

	public static Set<Integer> noneEmptyRow;
	public static Set<Integer> noneEmptyCol;
	public static ArrayList<Pos> arrayOfGalaxies;

	public static class Pos {
		public int x, y;
		
		public Pos(int x, int y) {
			this.x = x;
			this.y = y;
		}

		@Override
		public String toString() {
			return "[" + this.x + ", " + this.y + "]"; 
		}
	}

    public static void main(String[] args) {
		long res_1 = solvePuzzle1();
		System.out.println("Result for puzzle 1 = " + res_1);

		long res_2 = solvePuzzle2();
		System.out.println("Result for puzzle 2 = " + res_2);
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

	private static void expendUnivers(ArrayList<String> map) {
		for (int i=0; i < map.size(); i++) {
			String[] line = map.get(i).split("");
			if (String.join("", line).contains("#")) { noneEmptyRow.add(i); }

			for (int j=0; j<line.length; j++) {
				if (line[j].equals("#")) { noneEmptyCol.add(j); }
			}
		}
	}

	/********************/
	/** The first part **/
	/********************/

	private static long solvePuzzle1() {
		noneEmptyRow = new HashSet<Integer>();
		noneEmptyCol = new HashSet<Integer>();
		arrayOfGalaxies = new ArrayList<Pos>();

		long res = 0;
		ArrayList<String> reader = fileToArrayList("puzzle_data.txt");
		expendUnivers(reader);

		int nbRowExpend = 0;
		for (int i=0; i<reader.size(); i++) {
			if (!noneEmptyRow.contains(i)) { nbRowExpend++; }

			String[] line = reader.get(i).split("");
			int nbColExpend = 0;
			for (int j=0; j<line.length; j++) {
				if (!noneEmptyCol.contains(j)) { nbColExpend++; }

				if (line[j].equals("#")) {
					Pos g = new Pos(i+(nbRowExpend), j+(nbColExpend));
					arrayOfGalaxies.add(g);
				}
			}
		}

		for (int i=0; i<arrayOfGalaxies.size()-1; i++) {
			Pos g1 = arrayOfGalaxies.get(i);
			for (int j=i+1; j<arrayOfGalaxies.size(); j++) {
				Pos g2 = arrayOfGalaxies.get(j);

				int absDist = Math.abs(g2.x - g1.x) + Math.abs(g2.y - g1.y);
				res += absDist;
			}
		}

		return res;
	}

	/*********************/
	/** The second part **/
	/*********************/

	private static long solvePuzzle2() {
		noneEmptyRow = new HashSet<Integer>();
		noneEmptyCol = new HashSet<Integer>();
		arrayOfGalaxies = new ArrayList<Pos>();
		
 		long res = 0;
		int expendSize = 999999;
		ArrayList<String> reader = fileToArrayList("puzzle_data.txt");
		expendUnivers(reader);

		int nbRowExpend = 0;
		for (int i=0; i<reader.size(); i++) {
			if (!noneEmptyRow.contains(i)) { nbRowExpend++; }

			String[] line = reader.get(i).split("");
			int nbColExpend = 0;
			for (int j=0; j<line.length; j++) {
				if (!noneEmptyCol.contains(j)) { nbColExpend++; }

				if (line[j].equals("#")) {
					Pos g = new Pos(i+(nbRowExpend*expendSize), j+(nbColExpend*expendSize));
					arrayOfGalaxies.add(g);
				}
			}
		}

		for (int i=0; i<arrayOfGalaxies.size()-1; i++) {
			Pos g1 = arrayOfGalaxies.get(i);
			for (int j=i+1; j<arrayOfGalaxies.size(); j++) {
				Pos g2 = arrayOfGalaxies.get(j);

				int absDist = Math.abs(g2.x - g1.x) + Math.abs(g2.y - g1.y);
				res += absDist;
			}
		}

		return res;
   	}
}