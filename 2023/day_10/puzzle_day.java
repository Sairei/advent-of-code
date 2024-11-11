import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.stream.Collectors;
import java.util.*;

public class PuzzleDay {

	public static Pos start = new Pos(0, 0, "S", "");
	public static class Pos {
		public int x, y;
		public String from;
		public String name;
		
		public Pos(int x, int y, String name, String from) {
			this.x = x;
			this.y = y;
			this.name = name;
			this.from = from;
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

	private static void findStart(ArrayList<String> reader) {
		for (int i=0; i<reader.size(); i++) {
			String line = reader.get(i);
			if (line.contains("S")) {
				char[] lineToCharArray = line.toCharArray();
				for (int j=0; j<lineToCharArray.length; j++) {
					if (lineToCharArray[j] == 'S') {
						start = new Pos(i, j, "S", "");
						break;
					}
				}
				break;
			}
		}
	}

	private static Pos firstStepAndStartType(ArrayList<String> reader) {
		Pos firstStep = null;
		Pos lastStep = null;

		// Check North
		if (start.x > 0) {
			String[] northLine = reader.get(start.x-1).split("");
			if ("|7F".contains(northLine[start.y])) {
				Pos step = new Pos(start.x-1, start.y, northLine[start.y], "south");
				if (firstStep == null) { firstStep = step; } 
				else if (lastStep == null) { lastStep = step; }
			}
		}
		// Check South
		if (start.x < reader.size()-1) {
			String[] southLine = reader.get(start.x+1).split("");
			if ("|JL".contains(southLine[start.y])) {
				Pos step = new Pos(start.x+1, start.y, southLine[start.y], "north");
				if (firstStep == null) { firstStep = step; } 
				else if (lastStep == null) { lastStep = step; }
			}
		}
		// Check East
		if (start.y < reader.get(start.x).length()-2) {
			String[] startLine = reader.get(start.x).split("");
			if ("-J7".contains(startLine[start.y+1])) {
				Pos step = new Pos(start.x, start.y+1, startLine[start.y+1], "west");
				if (firstStep == null) { firstStep = step; } 
				else if (lastStep == null) { lastStep = step; }
			}
		}
		// Check West
		if (start.y > 0) {
			String[] startLine = reader.get(start.x).split("");
			if ("-LF".contains(startLine[start.y-1])) {
				Pos step = new Pos(start.x, start.y-1, startLine[start.y-1], "east");
				if (firstStep == null) { firstStep = step; } 
				else if (lastStep == null) { lastStep = step; }

			}
		}

		start.name = startType(firstStep, lastStep);
		return firstStep;
	}

	private static String startType(Pos from, Pos to) {
		return switch (from.from) {
			case "south" -> switch (to.from) {
					case "south" -> "\\|";
					case "west" -> "L";
					case "east" -> "J";
					default -> throw new IllegalStateException("Invalid point value");
				};
			case "north" -> switch (to.from) {
					case "north" -> "\\|";
					case "west" -> "F";
					case "east" -> "7";
					default -> throw new IllegalStateException("Invalid point value");
				};
			case "west" -> switch (to.from) {
					case "west" -> "-";
					case "south" -> "L";
					case "north" -> "F";
					default -> throw new IllegalStateException("Invalid point value");
				};
			case "east" -> switch (to.from) {
					case "east" -> "-";
					case "south" -> "J";
					case "north" -> "7";
					default -> throw new IllegalStateException("Invalid point value");
				};
			default -> throw new IllegalStateException("Invalid point value");
		};
	}

	private static Pos nextStep(Pos prevStep, ArrayList<String> reader) {
		Pos step = null;
		String[] line = {};

		switch (prevStep.from) {
			case "north":
				switch (prevStep.name) {
					case "|":
						line = reader.get(prevStep.x+1).split("");
						step = new Pos(prevStep.x+1, prevStep.y, line[prevStep.y], "north");
						break;
					case "J":
						line = reader.get(prevStep.x).split("");
						step = new Pos(prevStep.x, prevStep.y-1, line[prevStep.y-1], "east");
						break;
					case "L":
						line = reader.get(prevStep.x).split("");
						step = new Pos(prevStep.x, prevStep.y+1, line[prevStep.y+1], "west");
						break;
					default:
						break;
				}
				break;
			case "south":
				switch (prevStep.name) {
					case "|":
						line = reader.get(prevStep.x-1).split("");
						step = new Pos(prevStep.x-1, prevStep.y, line[prevStep.y], "south");
						break;
					case "7":
						line = reader.get(prevStep.x).split("");
						step = new Pos(prevStep.x, prevStep.y-1, line[prevStep.y-1], "east");
						break;
					case "F":
						line = reader.get(prevStep.x).split("");
						step = new Pos(prevStep.x, prevStep.y+1, line[prevStep.y+1], "west");
						break;
					default:
						break;
				}
				break;
			case "east":
				switch (prevStep.name) {
					case "-":
						line = reader.get(prevStep.x).split("");
						step = new Pos(prevStep.x, prevStep.y-1, line[prevStep.y-1], "east");
						break;
					case "L":
						line = reader.get(prevStep.x-1).split("");
						step = new Pos(prevStep.x-1, prevStep.y, line[prevStep.y], "south");
						break;
					case "F":
						line = reader.get(prevStep.x+1).split("");
						step = new Pos(prevStep.x+1, prevStep.y, line[prevStep.y], "north");
						break;
					default:
						break;
				}
				break;
			case "west":
				switch (prevStep.name) {
					case "-":
						line = reader.get(prevStep.x).split("");
						step = new Pos(prevStep.x, prevStep.y+1, line[prevStep.y+1], "west");
						break;
					case "J":
						line = reader.get(prevStep.x-1).split("");
						step = new Pos(prevStep.x-1, prevStep.y, line[prevStep.y], "south");
						break;
					case "7":
						line = reader.get(prevStep.x+1).split("");
						step = new Pos(prevStep.x+1, prevStep.y, line[prevStep.y], "north");
						break;
					default:
						break;
				}
				break;
		}

		return step;
	}

	/********************/
	/** The first part **/
	/********************/

	private static long solvePuzzle1() {
		long res = 0;
		ArrayList<String> reader = fileToArrayList("puzzle_data.txt");

		findStart(reader);
		Pos step = firstStepAndStartType(reader);
		if (step != null) {
			int nbStep = 1;
			while (!step.name.equals("S")) {
				step = nextStep(step, reader);
				nbStep++;
			}

			res = nbStep/2;
		}

		return res;
	}

	/*********************/
	/** The second part **/
	/*********************/

	private static long solvePuzzle2() {
 		long res = 0;
		ArrayList<String> reader = fileToArrayList("puzzle_data.txt");
		findStart(reader);

		ArrayList<String> map = new ArrayList<String>();
		for (String line : reader) {
			map.add(".".repeat(line.length()));
		}

		Pos step = firstStepAndStartType(reader);
		if (step != null) {
			String[] startLine = map.get(start.x).split("");
			startLine[start.y] = start.name;
			map.set(start.x, String.join("", startLine));

			int nbStep = 1;
			while (!step.name.equals("S")) {
				String[] mapLine = map.get(step.x).split("");
				mapLine[step.y] = step.name;
				map.set(step.x, String.join("", mapLine));
				
				step = nextStep(step, reader);
				nbStep++;
			}
		}

		map = inLoop(map);
		for (String line : map) {
			res += line.chars().filter(ch -> ch == '.').count();

			/** Print loop **/
			// line = line.replaceAll("7", "┐").replaceAll("F", "┌").replaceAll("L", "└").replaceAll("J", "┘").replaceAll("-", "─").replaceAll("\\|", "│");
			// System.out.println(line);
		}

		return res;
   	}

	private static ArrayList<String> inLoop(ArrayList<String> map) {
		for (int x=0; x<map.size(); x++) {
			String[] line = map.get(x).split("");

            String lastCurve = "*";
			boolean insideLoop = false;
			for (int y=0; y<line.length; y++) {
				String point = line[y];
				switch (point) {
                    case "." -> {
                        if (!insideLoop) {
							line[y] = " ";
						}
                    }
                    case "|" -> insideLoop = !insideLoop;
                    case "L", "F" -> lastCurve = point;
                    case "J" -> {
                        if (lastCurve.equals("F")) {
                            insideLoop = !insideLoop;
                        }
                    }
                    case "7" -> {
                        if (lastCurve.equals("L")) {
                            insideLoop = !insideLoop;
                        }
                    }
                }
			}
			
			map.set(x, String.join("", line));
		}

		return map;
	}
}