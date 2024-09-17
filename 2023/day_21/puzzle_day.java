import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.stream.Collectors;
import java.util.*;

public class PuzzleDay {

	public final static String ROCK = "rock";
	public final static String START = "start";
	public final static String EMPTY = "empty";
	public final static String[] DIRECTIONS = {"U", "D", "L", "R"};

	public static class Pos {
		int x, y;

		public Pos(int x, int y) {
			this.x = x;
			this.y = y;
		}

		public Pos(String pos) {
			String[] coord = pos.substring(1, pos.length()-1).split(",");
			this.x = Integer.parseInt(coord[0]);
			this.y = Integer.parseInt(coord[1]);
		}

		public Pos getAdjacent(String direction) {
			switch (direction) {
				case "U":
				 	return new Pos(x-1, y);
				case "D":
					return new Pos(x+1, y);
				case "L":
					return new Pos(x, y-1);
				case "R":
					return new Pos(x, y+1);
			}
			return null;
		}

		public String toString() {
			return "[" + x + "," + y + "]";
		}
	}

	public static Map<String, String> map = new HashMap<>();
	public static Pos startPos = new Pos(0, 0);
	public static int maxX = 0;
	public static int maxY = 0;
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

	public static void constructMap(ArrayList<String> reader) {
		maxX = reader.size();
		maxY = reader.get(0).length();
		for (int i=0; i<reader.size(); i++) {
			String line = reader.get(i);
			for (int j = 0; j<line.length(); j++) {
				Pos p = new Pos(i, j);
				String type = "";
				if (line.charAt(j) == '#') {
					type = ROCK;
				 } else if (line.charAt(j) == 'S') { 
					type = START;
					startPos = p;
				} else {
					type = EMPTY;
				}
				map.put(p.toString(), type);
			}
		}
	}

	public static long walkNbStep(int nbStep, int part) {
		Set<String> odd = new HashSet<>();
		odd.add(startPos.toString());
		Set<String> even = new HashSet<>();
		
		Map<String, Set<String>> mapStep = new HashMap<>();
		mapStep.put("odd", odd);
		mapStep.put("even", even);

		for (int i=1; i<=nbStep; i++) {
			String typeToCheck = i%2 != 0 ? "odd" : "even";
			String typeToAdd = i%2 == 0 ? "odd" : "even";

			List<String> listInGrid = new ArrayList<>();
			for (String stringPos : mapStep.get(typeToCheck)) {
				Pos pos = new Pos(stringPos);
				for (String d : DIRECTIONS) {
					Pos p = pos.getAdjacent(d);
					if (conditionByPuzzlePart(part, p)) {
						listInGrid.add(p.toString());
					}
				}
			}
			List<String> posToAdd = listInGrid.stream().map(p -> p.toString()).collect(Collectors.toList());
			mapStep.get(typeToAdd).addAll(posToAdd);
		}
		return nbStep%2==0 ? odd.size() : even.size();
	}

	public static boolean conditionByPuzzlePart(int part, Pos pos) {
		if (part == 1) {
			return isInGrid(pos) && !map.get(pos.toString()).equals(ROCK);
		} else {
			int newX = pos.x%maxX < 0 ? maxX + pos.x%maxX : pos.x%maxX;
			int newY = pos.y%maxY < 0 ? maxY + pos.y%maxY : pos.y%maxY;
			Pos inGridPos = new Pos(newX, newY);
			return !ROCK.equals(map.get(inGridPos.toString()));
		}
	}

	public static boolean isInGrid(Pos pos) {
		return pos.x >= 0 && pos.x < maxX && pos.y >= 0 && pos.y < maxY;
	}

	/********************/
	/** The first part **/
	/********************/

	private static long solvePuzzle1() {
		long res = 0;
		ArrayList<String> reader = fileToArrayList("puzzle_day_21.txt");

		constructMap(reader);

		res = walkNbStep(64, 1);

		return res;
	}

	/*********************/
	/** The second part **/
	/*********************/

	
	private static long solvePuzzle2() {
		long res = 0;
		// ArrayList<String> reader = fileToArrayList("data_test.txt");
		ArrayList<String> reader = fileToArrayList("puzzle_day_21.txt");

		constructMap(reader);

		// Thanks to Reddit AOC thread for this equation
		int stepsToTake = 26501365;
        int width = maxX;
        int x = stepsToTake % width;
        int d = stepsToTake / width;
        long y = walkNbStep(x, 2);
        long y1 = walkNbStep(x + width, 2);
        long y2 = walkNbStep(x + (width * 2), 2);
        long c = y;
        long a = (y2 + c - (2 * y1)) / 2;
        long b = y1 - y - a;
        res = (a * d * d) + (b * d) + c;

		return res;
	}
}