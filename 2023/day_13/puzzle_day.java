import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.stream.Collectors;
import java.util.*;

public class PuzzleDay {

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

	private static int findMirror(ArrayList<String> pattern, int nbErrorMax) {
		int mirrorLine = (-1);

		int nbError = 0;
		int evenOdd = pattern.size() % 2;

		// Start on top
		int i = 0;
		int iSave = -1;
		int jSave = -1;
		int startIndex = pattern.size() - 1 - evenOdd;
		for (int j=startIndex; j>i; j--) {
			String l1 = pattern.get(i);
			String l2 = pattern.get(j);

			nbError += countError(l1.split(""), l2.split(""));
			if (l1.equals(l2) || nbError <= nbErrorMax) {
				if (iSave == -1 && jSave == -1) {
					iSave = i;
					jSave = j;
				}
				if (j == (i+1)) {
					if (nbError == nbErrorMax) { 
						mirrorLine = j; 
					} else {
						i = iSave;
						iSave = -1;
						j = jSave;
						jSave = -1;
					}
				} else {
					i++;
				}
			} 
			else if (nbError > nbErrorMax) {
				if (i > 0) {
					i = 0;
					j++;
				}
				iSave = -1;
				jSave = -1;
				nbError = 0;
			}
		}

		// System.out.println("");

		nbError = 0;
		i = pattern.size()-1;
		iSave = -1;
		jSave = -1;
		for (int j=evenOdd; j<i; j++) {
			String l1 = pattern.get(i);
			String l2 = pattern.get(j);

			nbError += countError(l1.split(""), l2.split(""));
			if (l1.equals(l2) || nbError <= nbErrorMax) {
				if (iSave == -1 && jSave == -1) {
					iSave = i;
					jSave = j;
				}

				if (j == (i-1)) {
					if (nbError == nbErrorMax) {
						mirrorLine = i;
					} else {
						i = iSave;
						iSave = -1;
						j = jSave;
						jSave = -1;
					}
				} else {
					i--;
				}
			}
			else if (nbError > nbErrorMax) {
				if (i < pattern.size()-1) {
					i = pattern.size()-1;
					j--;
				}
				iSave = -1;
				jSave = -1;
				nbError = 0;
			}
		}

		return mirrorLine;
	}

	private static ArrayList<String> rotatePattern(ArrayList<String> pattern) {
		ArrayList<String> rotate = new ArrayList<>();

		for (int i=0; i<pattern.size(); i++) {
			String[] splitLine = pattern.get(i).split("");
			for (int j=0; j<splitLine.length; j++) {
				if (i == 0) {
					rotate.add(splitLine[j]);
				} else {
					rotate.set(j, rotate.get(j) + splitLine[j]);
				}
			}
		}

		return rotate;
	}

	private static int countError(String[] line1, String[] line2) {
		int nbErr = 0;

		for (int i=0; i<line1.length; i++) {
			if (!line1[i].equals(line2[i])) { nbErr++; }
		}

		return nbErr;
	}

	/********************/
	/** The first part **/
	/********************/

	private static long solvePuzzle1() {
		long res = 0;
		ArrayList<String> reader = fileToArrayList("puzzle_day_13.txt");

		ArrayList<String> pattern = new ArrayList<>();
		for (int l=0; l<reader.size(); l++) {
			String line = reader.get(l);

			if (line.length() > 0) {
				pattern.add(line);
			}
			
			if (line.isEmpty() || l == reader.size()-1) {
				int mirrorLine = findMirror(pattern, 0);
				if (mirrorLine == -1) {
					mirrorLine = findMirror(rotatePattern(pattern), 0);
				} else {
					mirrorLine *= 100;
				}

				res += mirrorLine;
				pattern = new ArrayList<>();
			}
		}
		
		return res;
	}

	/*********************/
	/** The second part **/
	/*********************/

	private static long solvePuzzle2() {
		long res = 0;
		ArrayList<String> reader = fileToArrayList("puzzle_day_13.txt");

		ArrayList<String> pattern = new ArrayList<>();
		for (int l=0; l<reader.size(); l++) {
			String line = reader.get(l);

			if (line.length() > 0) {
				pattern.add(line);
			}
			if (line.isEmpty() || l == reader.size()-1) {
				int mirrorLine = findMirror(rotatePattern(pattern), 1);
				if (mirrorLine == -1) {
					mirrorLine = findMirror(pattern, 1);
					mirrorLine *= 100;
				} 

				res += mirrorLine;
				pattern = new ArrayList<>();
			}
		}
		
		return res;
   	}
}