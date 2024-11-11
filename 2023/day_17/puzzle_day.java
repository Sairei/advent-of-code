import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.stream.Collectors;
import java.util.*;

public class PuzzleDay {

	public static class Pos {
		int row, col;
		String direction;
    	long totalHeatLoss;
    	int straightSteps;

		public Pos(int row, int col) {
			this.row = row;
			this.col = col;
		}

		public Pos(int row, int col, String direction, long totalLoss, int step) {
			this.row = row;
			this.col = col;
			this.direction = direction;
			this.totalHeatLoss = totalLoss;
			this.straightSteps = step;
		}

		public Long getTotalHeatLoss() {
			return this.totalHeatLoss;
		}

		public Pos getAdjacent(String direction) {
			switch(direction) {
				case "Right": 
					return new Pos(this.row, this.col+1);
				case "Down": 
					return new Pos(this.row+1, this.col);
				case "Left": 
					return new Pos(this.row, this.col-1);
				case "Up": 
					return new Pos(this.row-1, this.col);
			}
			return this;
		}

		public String getKey() {
			return this.row + "_" + this.col + "_" + this.direction + "_" + this.straightSteps;
		}
	}

	public static ArrayList<ArrayList<Long>> map = new ArrayList<>();
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

	private static void contructMap(ArrayList<String> reader) {
		map = new ArrayList<>();
		for (String line : reader) {
			ArrayList<Long> mapLine = new ArrayList<Long>();
			for (String c : line.split("")) {
				mapLine.add(Long.parseLong(c));
			}
			map.add(mapLine);
		}
	}

	private static long runCrucible(int maxStep, int minStep) {
		PriorityQueue<Pos> queue = new PriorityQueue<>(Comparator.comparing(Pos::getTotalHeatLoss));
		Pos startDown = new Pos(1, 0, "Down", getHeatFromPos(new Pos(1, 0)), 1);
		Pos startRight = new Pos(0, 1, "Right", getHeatFromPos(new Pos(0, 1)), 1);
		queue.add(startDown);
		queue.add(startRight);
		
        var visited = new HashMap<String, Long>();
        visited.put(startRight.getKey(), 0L);
        visited.put(startDown.getKey(), 0L);
		long minHeatLoss = Long.MAX_VALUE;
        while(!queue.isEmpty()) {
			Pos actualPos = queue.remove();
			if (isAtEnd(actualPos) && actualPos.straightSteps >= minStep) {
                minHeatLoss = Math.min(minHeatLoss, actualPos.getTotalHeatLoss());
                continue;
            }
			
			switch (actualPos.direction) {
				case "Right":
					if (actualPos.straightSteps >= minStep) {
						addIfOk(queue, visited, actualPos, "Down", true);
						addIfOk(queue, visited, actualPos, "Up", true);
					}
					if (actualPos.straightSteps < maxStep) {
						addIfOk(queue, visited, actualPos, "Right", false);
					}
					break;
				case "Down":
					if (actualPos.straightSteps >= minStep) {
						addIfOk(queue, visited, actualPos, "Right", true);
						addIfOk(queue, visited, actualPos, "Left", true);
					}
					if (actualPos.straightSteps < maxStep) {
						addIfOk(queue, visited, actualPos, "Down", false);
					}
					break;
				case "Left":
					if (actualPos.straightSteps >= minStep) {
						addIfOk(queue, visited, actualPos, "Down", true);
						addIfOk(queue, visited, actualPos, "Up", true);
					}
					if (actualPos.straightSteps < maxStep) {
						addIfOk(queue, visited, actualPos, "Left", false);
					}
					break;
				case "Up":
					if (actualPos.straightSteps >= minStep) {
						addIfOk(queue, visited, actualPos, "Right", true);
						addIfOk(queue, visited, actualPos, "Left", true);
					}
					if (actualPos.straightSteps < maxStep) {
						addIfOk(queue, visited, actualPos, "Up", false);
					}
					break;
			}
		}
		return minHeatLoss;
	}

	private static void addIfOk(PriorityQueue<Pos> queue, HashMap<String, Long> visited, Pos pos, String direction, boolean isTurn) {
		Pos point = pos.getAdjacent(direction);
		if (isOnGrid(point)) {
			Long newHeat = pos.getTotalHeatLoss() + getHeatFromPos(point);
			int newStep = isTurn ? 1 : pos.straightSteps+1;
			Pos newPos = new Pos(point.row, point.col, direction, newHeat, newStep);
			String key = newPos.getKey();
			if (!alreadyVisited(visited, key, newHeat)) {
				visited.put(key, newHeat);
				queue.add(newPos);
			}
		}
	}

	private static boolean alreadyVisited(HashMap<String, Long> visited, String key, long heatLoss) {
        return visited.containsKey(key) && visited.get(key) <= heatLoss;
    }

	private static boolean isAtEnd(Pos pos) {
        return pos.row == map.size()-1 && pos.col == map.get(0).size()-1;
    }

	private static boolean isOnGrid(Pos pos) {
        return pos.row < map.size() && pos.col < map.get(0).size() && pos.row >= 0 && pos.col >= 0;
    }

	private static Long getHeatFromPos(Pos pos) {
		return map.get(pos.row).get(pos.col);
	}

	/********************/
	/** The first part **/
	/********************/

	private static long solvePuzzle1() {
		long res = 0;
		ArrayList<String> reader = fileToArrayList("puzzle_data.txt");

		contructMap(reader);

		res = runCrucible(3, 0);
		
		return res;
	}

	/*********************/
	/** The second part **/
	/*********************/

	private static long solvePuzzle2() {
		long res = 0;
		ArrayList<String> reader = fileToArrayList("puzzle_data.txt");

		contructMap(reader);

		res = runCrucible(10, 4);
		
		return res;
	}
}