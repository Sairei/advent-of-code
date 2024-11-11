import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.stream.Collectors;
import java.util.*;

public class PuzzleDay {

	public static Map<Integer, LinkedHashMap<String, Integer>> boxs = new HashMap<>();
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
				
				lines.addAll(Arrays.asList(line.split(",")));
				line = reader.readLine();
			}

			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return lines;
	}

	private static int parseSentence(String sentence) {
		int res = 0;
		for (char c : sentence.toCharArray()) {
			int cAscii = (int) c;
			res += cAscii;
			res *= 17;
			res = res % 256;
		}
		return res;
	}

	private static void constructBoxs(String sentence) {
		String label = sentence.split("=")[0].split("-")[0];
		int box = Integer.valueOf(parseSentence(label));

		if (sentence.indexOf("=") > 0) {
			int lens = Integer.valueOf(sentence.split("=")[1]);

			if (!boxs.containsKey(box)) {
				boxs.put(box, new LinkedHashMap<String, Integer>());
			}

			LinkedHashMap<String, Integer> boxMap = boxs.get(box);
			if (boxMap.containsKey(label)) {
				boxMap.replace(label, lens);
			} else {
				boxMap.put(label, lens);
			}

			boxs.replace(box, boxMap);
		}
		else if (sentence.indexOf("-") > 0) {
			if (boxs.containsKey(box)) {
				LinkedHashMap<String, Integer> boxMap = boxs.get(box);
				if (boxMap.containsKey(label)) {
					boxMap.remove(label);
				}

				if (boxMap.isEmpty()) {
					boxs.remove(box);
				} else {
					boxs.replace(box, boxMap);
				}
			}
		}
	}

	/********************/
	/** The first part **/
	/********************/

	private static long solvePuzzle1() {
		long res = 0;
		ArrayList<String> reader = fileToArrayList("puzzle_data.txt");

		for (String sentence : reader) {
			res += parseSentence(sentence);
		}
		
		return res;
	}

	/*********************/
	/** The second part **/
	/*********************/

	private static long solvePuzzle2() {
		long res = 0;
		ArrayList<String> reader = fileToArrayList("puzzle_data.txt");

		for (String code : reader) {
			constructBoxs(code);
		}

		for (Map.Entry<Integer, LinkedHashMap<String,Integer>> boxMap : boxs.entrySet()) {
			int box = boxMap.getKey();

			int labelIndex = 1;
			LinkedHashMap<String,Integer> labels = boxMap.getValue();
			for (Map.Entry<String,Integer> label : labels.entrySet()) {
				res += (box+1) * labelIndex * label.getValue();
				labelIndex++;
			}
		}
		
		return res;
	}
}