import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class PuzzleDay {

	public static class Pos {
		int x, y;

		public Pos(int x, int y) {
			this.x = x;
			this.y = y;
		}

		public Pos getAdjacentPoint(String direction) {
			Pos res = null;
			switch (direction) {
				case "U":
					res = new Pos(x - 1, y);
					break;
				case "D":
					res = new Pos(x + 1, y);
					break;
				case "L":
					res = new Pos(x, y - 1);
					break;
				case "R":
					res = new Pos(x, y + 1);
					break;
			};
			return res;
		}

		@Override
		public String toString() {
			return "[" + x + ", " + y + "]";
		}
	}

    public static record State(Pos position, long steps, Set<String> visited) {}
	public static List<List<Integer>> map = new ArrayList<>();
	public static Pos start, end;
	public static int maxRow, maxCol;
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

	public static void constructMap(ArrayList<String> reader) {
        maxRow = reader.size();
        maxCol = reader.get(0).length();

		start = new Pos(0, reader.get(0).indexOf("."));
		end = new Pos(maxRow-1, reader.get(maxRow-1).indexOf("."));

		for (String line : reader) {
			List<Integer> row = new ArrayList<>();
			for (String c : line.split("")) {
				switch (c) {
                    case "#":
                        row.add(-1);
                        break;
                    case ".":
                        row.add(0);
                        break;
                    case ">":
                        row.add(1);
                        break;
                    case "<":
                        row.add(2);
                        break;
                    case "^":
                        row.add(3);
                        break;
                    case "v":
                        row.add(4);
                        break;
                    default:
                        throw new RuntimeException("Unknown character: " + c);
				}
			}
			map.add(row);
		}
	}

	private static boolean isOnGrid(Pos p) {
		return p.x >= 0 && p.y >= 0 && p.x < maxRow && p.y < maxCol; 
	}

	private static boolean isNotForest(Pos p) {
		return map.get(p.x).get(p.y) != -1;
	}
	
	private static boolean isNotVisited(Pos p, Set<String> visited) {
		return !visited.contains(p.toString());
	}

	private static void addIfOk(List<Pos> nextPos, Pos p) {
		if (isOnGrid(p) && isNotForest(p)) {
			nextPos.add(p);
		}
	}

	public static long walk(boolean isPart2) {
		long res = 0;

		Queue<State> queue = new PriorityQueue<State>(Comparator.comparing(State::steps).reversed());
		queue.add(new State(start, 0, Set.of(start.toString())));
		while(!queue.isEmpty()) {
			State current = queue.poll();
			Pos point = current.position();
			int tileType = isPart2 ? 0 : map.get(point.x).get(point.y);
			long newStep = current.steps + 1;
			Set<String> visited = current.visited();

			if (point.toString().equals(end.toString())) {
				res = Math.max(res, current.steps);
				continue;
			}

			List<Pos> nextPos = new ArrayList<>();
			switch (tileType) {
				case 1:
					addIfOk(nextPos, point.getAdjacentPoint("R"));
					break;
				case 2:
					addIfOk(nextPos, point.getAdjacentPoint("L"));
					break;
				case 3:
					addIfOk(nextPos, point.getAdjacentPoint("U"));
					break;
				case 4:
					addIfOk(nextPos, point.getAdjacentPoint("D"));
					break;
				default:
					addIfOk(nextPos, point.getAdjacentPoint("R"));
					addIfOk(nextPos, point.getAdjacentPoint("L"));
					addIfOk(nextPos, point.getAdjacentPoint("U"));
					addIfOk(nextPos, point.getAdjacentPoint("D"));
					break;
			}

			for (Pos p : nextPos) {
				if (isNotVisited(p, visited)) {
					Set<String> copy = new HashSet<>(visited);
					copy.add(p.toString());
					queue.add(new State(p, newStep, copy));
				}
			}
		}

		return res;
	}

	/********************/
	/** The first part **/
	/********************/

	private static long solvePuzzle1() {
		ArrayList<String> reader = fileToArrayList("puzzle_day_23.txt");

		constructMap(reader);

		return walk(false);
	}

	/*********************/
	/** The second part **/
	/*********************/
	
	private static long solvePuzzle2() {
		ArrayList<String> reader = fileToArrayList("data_test.txt");
		// ArrayList<String> reader = fileToArrayList("puzzle_day_23.txt");

		constructMap(reader);

		return walk(true);
	}
}