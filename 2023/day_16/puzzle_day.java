import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.stream.Collectors;
import java.util.*;

public class PuzzleDay {

	public static class Pos {
		String type;
		Map<String, Boolean> activeDirection = new HashMap<>();
		boolean active = false;

		public Pos(String type) {
			this.type = type;
			this.activeDirection.put("up", false);
			this.activeDirection.put("down", false);
			this.activeDirection.put("right", false);
			this.activeDirection.put("left", false);
		}
	}

	public static ArrayList<ArrayList<Pos>> map = new ArrayList<>();
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

	private static void runBeam(int x, int y, String from) {
		if (x < 0 || x >= map.size()) { return; }
		if (y < 0 || y >= map.get(0).size()) { return; }

		while (!map.get(x).get(y).activeDirection.get(from)) {
			Pos actual = map.get(x).get(y);
			actual.active = true;
			actual.activeDirection.replace(from, true);

			if (actual.type.equals("|")) {
				switch (from) {
					case "right", "left":
						runBeam(x+1, y, "up");
						runBeam(x-1, y, "down");
						break;
					case "up":
						runBeam(x+1, y, "up");
						break;
					case "down":
						runBeam(x-1, y, "down");
						break;
				}
			} else if (actual.type.equals("-")) {
				switch (from) {
					case "up", "down":
						runBeam(x, y+1, "left");
						runBeam(x, y-1, "right");
						break;
					case "left":
						runBeam(x, y+1, "left");
						break;
					case "right":
						runBeam(x, y-1, "right");
						break;
				}
				
			} else if (actual.type.equals("\\")) {
				switch (from) {
					case "up":
						runBeam(x, y+1, "left");
						break;
					case "down":
						runBeam(x, y-1, "right");
						break;
					case "left":
						runBeam(x+1, y, "up");
						break;
					case "right":
						runBeam(x-1, y, "down");
						break;
				}
			} else if (actual.type.equals("/")) {
				switch (from) {
					case "up":
						runBeam(x, y-1, "right");
						break;
					case "down":
						runBeam(x, y+1, "left");
						break;
					case "left":
						runBeam(x-1, y, "down");
						break;
					case "right":
						runBeam(x+1, y, "up");
						break;
				}
			} else {
				actual.activeDirection.replace(from, true);
				switch (from) {
					case "up":
						runBeam(x+1, y, "up");
						break;
					case "down":
						runBeam(x-1, y, "down");
						break;
					case "left":
						runBeam(x, y+1, "left");
						break;
					case "right":
						runBeam(x, y-1, "right");
						break;
				}
			}
		}
	}

	private static ArrayList<String> possibleDirection(int x, int maxX, int y, int maxY) {
		ArrayList<String> res = new ArrayList<>();

		if (x == 0) { res.add("up"); }
		if (x == maxX) { res.add("down"); }
		if (y == 0) { res.add("left"); }
		if (y == maxY) { res.add("right"); }

		return res;
	}

	private static void contructMap(ArrayList<String> reader) {
		map = new ArrayList<>();
		for (String line : reader) {
			ArrayList<Pos> mapLine = new ArrayList<Pos>();
			for (String c : line.split("")) {
				mapLine.add(new Pos(c));
			}
			map.add(mapLine);
		}
	}

	/********************/
	/** The first part **/
	/********************/

	private static long solvePuzzle1() {
		long res = 0;
		ArrayList<String> reader = fileToArrayList("puzzle_day_16.txt");

		contructMap(reader);

		runBeam(0, 0, "left");

		for (ArrayList<Pos> mapLine : map) {
			for (Pos p : mapLine) {
				res += p.active ? 1 : 0;
			}
		}
		
		return res;
	}

	/*********************/
	/** The second part **/
	/*********************/

	private static long solvePuzzle2() {
		long res = 0;
		ArrayList<String> reader = fileToArrayList("puzzle_day_16.txt");

		contructMap(reader);

		for (int i=0; i<map.size(); i++) {
			for (int j=0; j<map.size(); j++) {	
			
				if (i == 0 || i == map.size()-1 || j == 0 || j == map.get(0).size()-1){
					for (String direction : possibleDirection(i, map.size()-1, j, map.get(0).size()-1)) {
						contructMap(reader);
						runBeam(i, j, direction);

						long tmpRes = 0;
						for (ArrayList<Pos> mapLine : map) {
							for (Pos p : mapLine) {
								tmpRes += p.active ? 1 : 0;
							}
						}
						
						res = (tmpRes > res) ? tmpRes : res;
						// System.out.println("x="+i+" y="+j+" dir="+direction+" active="+tmpRes);
					}
				}
			}
		}
		
		return res;
	}
}