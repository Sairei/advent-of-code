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
	private static ArrayList<List<Long>> fileToArrayList(String fileName) {
		BufferedReader reader;

		ArrayList<List<Long>> lines = new ArrayList<List<Long>>();
		try {
			reader = new BufferedReader(new FileReader(fileName));
			String line = reader.readLine();

			while (line != null) {
				List<Long> longArray = Arrays.asList(line.split(" ")).stream().mapToLong(n -> Long.parseLong(n)).boxed().collect(Collectors.toList());
				lines.add(longArray);
				line = reader.readLine();
			}

			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return lines;
	}

	// predict the next value
	private static long predictNextValue(List<Long> histo) {
		ArrayList<ArrayList<Long>> subHist = calculateSubHistoric(histo);

		subHist.get(subHist.size()-1).add(0L);
		for (int i=(subHist.size()-2); i>=0; i--) {
			long newVal = subHist.get(i).get(subHist.get(i).size()-1) + subHist.get(i+1).get(subHist.get(i+1).size()-1);
			subHist.get(i).add(newVal);
		}

		return subHist.get(0).get(subHist.get(0).size()-1);
	}

	// predict the previous value
	private static long predictPrevValue(List<Long> histo) {
		ArrayList<ArrayList<Long>> subHist = calculateSubHistoric(histo);

		subHist.get(subHist.size()-1).add(0L);
		for (int i=(subHist.size()-2); i>=0; i--) {
			long prevVal = subHist.get(i).get(0) - subHist.get(i+1).get(0);
			subHist.get(i).add(0, prevVal);
		}

		return subHist.get(0).get(0);
	}

	private static ArrayList<ArrayList<Long>> calculateSubHistoric(List<Long> histo) {
		ArrayList<ArrayList<Long>> subHist = new ArrayList<ArrayList<Long>>();
		subHist.add(new ArrayList<Long>(histo));
		
		boolean fullZero = false;
		while (!fullZero) {
			ArrayList<Long> lastHist = subHist.get(subHist.size()-1);

			ArrayList<Long> tmp = new ArrayList<Long>();
			for (int i=1; i<lastHist.size(); i++) {
				long sub = lastHist.get(i) - lastHist.get(i-1);
				tmp.add(sub);
			}

			fullZero = true;
			for (long l : tmp) {
				if (l != 0) { fullZero = false; }
			}

			subHist.add(tmp);
		}

		return subHist;
	}

	/********************/
	/** The first part **/
	/********************/

	private static long solvePuzzle1() {
		long res = 0;
		ArrayList<List<Long>> reader = fileToArrayList("puzzle_day_9.txt");

		for (List<Long> historic : reader) {
			res += predictNextValue(historic);
		}

		return res;
	}

	/*********************/
	/** The second part **/
	/*********************/

	private static long solvePuzzle2() {
 		long res = 0;
		ArrayList<List<Long>> reader = fileToArrayList("puzzle_day_9.txt");

		for (List<Long> historic : reader) {
			res += predictPrevValue(historic);
		}

		return res;
   	}
}