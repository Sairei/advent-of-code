import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.stream.Collectors;
import java.util.*;

public class PuzzleDay {

	public static class Pos {
		int row, col;

		public Pos(int row, int col) {
			this.row = row;
			this.col = col;
		}

		public void move(String direction) {
			switch(direction) {
				case "R": 
					col++;
					break;
				case "D": 
					row++;
					break;
				case "L": 
					col--;
					break;
				case "U": 
					row--;
					break;
			}
		}

		public void move(String direction, int step) {
			switch(direction) {
				case "R": 
					col+=step;
					break;
				case "D": 
					row+=step;
					break;
				case "L": 
					col-=step;
					break;
				case "U": 
					row-=step;
					break;
			}
		}
		
		public boolean equals(Object o) {
			if (this == o) {
				return true;
			}
			if (o == null || getClass() != o.getClass()) {
				return false;
			}
			Pos point = (Pos) o;
			return row == point.row && col == point.col;
		} 

		public int hashCode() {
			return Objects.hash(row, col);
		}

		public String toString() {
			return "[" + row + " | " + col + "]";
		}
	}

	public static class DigStep {
		String direction;
		int step;
		String color;

		public DigStep(String dir, int step, String color) {
			this.direction = dir;
			this.step = step;
			this.color = color;
		}

		public String toString() {
			return direction + " " + step + " " + color;
		}
	}

	public static ArrayList<DigStep> map = new ArrayList<>();
	public static ArrayList<String> digMap = new ArrayList<>();
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

	private static void constructMap(ArrayList<String> reader) {
		for (String line : reader) {
			String[] split = line.split(" ");
			String color = split[2].substring(1, split[2].length() - 1);
			map.add(new DigStep(split[0], Integer.parseInt(split[1]), color));
		}
	}

	private static void constructMapWithHexa(ArrayList<String> reader) {
		for (String line : reader) {
			String[] split = line.split(" ");
			String hex = split[2].substring(2, split[2].length() - 1);
			String direction = "";
			switch (hex.charAt(hex.length()-1)) {
				case '0': direction = "R"; break;
				case '1': direction = "D"; break;
				case '2': direction = "L"; break;
				case '3': direction = "U"; break;
			}
			map.add(new DigStep(direction, Integer.parseInt(hex, 16), "#"+hex));
		}
	}

	private static long dig() {
		Pos start = new Pos(0, 0);
		Set<Pos> digEdge = new HashSet<>();
		for (DigStep step : map) {
			for (int i=0; i<step.step; i++) {
				start.move(step.direction);
				digEdge.add(new Pos(start.row, start.col));
			}
		}

		int minRow = digEdge.stream().mapToInt(p -> p.row).min().getAsInt();
		int maxRow = digEdge.stream().mapToInt(p -> p.row).max().getAsInt();
        int minCol = digEdge.stream().mapToInt(p -> p.col).min().getAsInt();
        int maxCol = digEdge.stream().mapToInt(p -> p.col).max().getAsInt();

		long res = 0L;
		for (var r = minRow; r < maxRow + 1; r++) {
			String digRow = "";
            int crossed = 0;
            for (var c = minCol ; c < maxCol + 1; c++) {
				Pos actualPos = new Pos(r, c);
                if (digEdge.contains(new Pos(r, c))) {
                    res++;
					digRow += "#";
					if (!digEdge.contains(new Pos(r, c-1)) && !digEdge.contains(new Pos(r, c+1))
                        || (!digEdge.contains(new Pos(r, c-1)) && digEdge.contains(new Pos(r+1, c)) && digEdge.contains(new Pos(r, c+1)))
                        || (!digEdge.contains(new Pos(r, c+1)) && digEdge.contains(new Pos(r+1, c)) && digEdge.contains(new Pos(r, c-1)))) {
                        crossed++;
                    }
					continue;
                }
				if (crossed % 2 != 0) {
                    res++;
					digRow += ".";
                } else {
					digRow += " ";
				}
            }
			digMap.add(digRow);
        }
		return res;
	}

	private static double digWithCorner() {
		Pos start = new Pos(0, 0);
		List<Pos> digCorners = new ArrayList<>();
		for (DigStep step : map) {
			start.move(step.direction, step.step);
			for (int i=0; i<step.step; i++) {
				digCorners.add(new Pos(start.row, start.col));
			}
		}

		long maxRight = map.stream().filter(d -> d.direction.equals("R")).mapToLong(d -> d.step).sum();
		long maxDown = map.stream().filter(d -> d.direction.equals("D")).mapToLong(d -> d.step).sum();
		
        double area = 0.0;

		int n = digCorners.size();
        int j = n - 1;
        for (int i = 0; i < n; i++) {
            area += (digCorners.get(j).row + digCorners.get(i).row) * (digCorners.get(j).col - digCorners.get(i).col);
            j = i;
        }

        return Math.abs(area / 2.0) + (maxRight + maxDown + 1);
	}

	/********************/
	/** The first part **/
	/********************/

	private static long solvePuzzle1() {
		long res = 0;
		ArrayList<String> reader = fileToArrayList("puzzle_day_18.txt");
		
		constructMap(reader);

		res = dig();

		return res;
	}

	/*********************/
	/** The second part **/
	/*********************/

	private static long solvePuzzle2() {
		//** DOESN'T WORK !! **/
		long res = 0;
		ArrayList<String> reader = fileToArrayList("data_test.txt");
		// ArrayList<String> reader = fileToArrayList("puzzle_day_18.txt");
		
		constructMapWithHexa(reader);

		res = (long) digWithCorner();

		return res;
	}
}