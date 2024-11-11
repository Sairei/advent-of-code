import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class PuzzleDay {
	final static String numbers = "0123456789";
	final static String charNoApplied = "." + numbers;

    public static void main(String[] args) {
		int res_1 = solvePuzzle1();
		System.out.println("Result for puzzle 1 = " + res_1);

		int res_2 = solvePuzzle2();
		System.out.println("Result for puzzle 2 = " + res_2);
    }

	private static int solvePuzzle1() {
		ArrayList<String> lines = fileToArrayListOfLines("puzzle_data.txt");
		int res = 0;

		for(int i=0; i<lines.size(); i++) {
			String line = lines.get(i);
			
			for(int j=0; j<line.length(); j++) {
				if (!charNoApplied.contains(line.charAt(j)+"")) {
					// Top line search
					if( i >= 0 ){
						int[] tab = findFromAnotherLine(lines.get(i-1), j);
						for(int r : tab) {
							res += r;
						}
					}

					// Same line search
					res += convertStringToInt(findRight(line, j));
					res += convertStringToInt(findLeft(line, j));

					// Bottom line search
					if ( i < lines.size()) {
						int[] tab = findFromAnotherLine(lines.get(i+1), j);
						for(int r : tab) {
							res += r;
						}
					}
				}
			}
		}

		return res;
	}

	private static int solvePuzzle2() {
		ArrayList<String> lines = fileToArrayListOfLines("puzzle_data.txt");
		int res = 0;

		for(int i=0; i<lines.size(); i++) {
			String line = lines.get(i);
			
			for(int j=0; j<line.length(); j++) {
				if (line.charAt(j) == '*') {
					ArrayList<Integer> tmp = new ArrayList<Integer>();
					ArrayList<Integer> tabRes = new ArrayList<Integer>();

					// Top line search
					if( i >= 0 ){
						for(int r : findFromAnotherLine(lines.get(i-1), j)) {
							tmp.add(r);
						}
					}

					// Same line search
					tmp.add(convertStringToInt(findRight(line, j)));
					tmp.add(convertStringToInt(findLeft(line, j)));

					// Bottom line search
					if ( i < lines.size()) {
						for(int r : findFromAnotherLine(lines.get(i+1), j)) {
							tmp.add(r);
						}
					}

					for(int r : tmp) {
						if (r != 0) { tabRes.add(r); }
					}

					if (tabRes.size() == 2) {
						res += tabRes.get(0) * tabRes.get(1);
					}
				}
			}
		}

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

	// Find number to the left
	private static String findRight(String line, int startIndex) {
		String res = "";

		int i = startIndex+1;
		while (i < line.length() && numbers.contains(line.charAt(i)+"")) {
			res += line.charAt(i);
			i++;
		}

		return res;
	}

	// Find number to the left
	private static String findLeft(String line, int startIndex) {
		String res = "";

		int i = startIndex-1;
		while (i >= 0 && numbers.contains(line.charAt(i)+"")) {
			res = line.charAt(i) + res;
			i--;
		}

		return res;
	}

	// Find number from another line
	private static int[] findFromAnotherLine(String line, int startIndex) {
		int[] res = new int[2];

		if (numbers.contains(line.charAt(startIndex)+"")) {
			int i = startIndex;
			while ( i >= 0 && numbers.contains(line.charAt(i)+"")) {
				i--;
			}
			res[0] = convertStringToInt(findRight(line, i));
		} else {
			res[0] = convertStringToInt(findRight(line, startIndex));
			res[1] = convertStringToInt(findLeft(line, startIndex));
		}

		return res;
	}


	private static int convertStringToInt(String res) {
		return Integer.valueOf(res.length() > 0 ? res : "0");
	}
}