import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.stream.Collectors;
import java.util.*;

public class PuzzleDay {

	public static Map<Input, Long> memorize = new HashMap<>();
	public record Input (String condition, List<Integer> groups) {}

    public static void main(String[] args) {
		long res_1 = solvePuzzle1();
		System.out.println("Result for puzzle 1 = " + res_1);

		long res_2 = solvePuzzle2();
		System.out.println("Result for puzzle 2 = " + res_2);
    }

	// Transform file into ArrayList
	private static ArrayList<Input> fileToArrayList(String fileName) {
		BufferedReader reader;

		ArrayList<Input> lines = new ArrayList<Input>();
		try {
			reader = new BufferedReader(new FileReader(fileName));
			String line = reader.readLine();

			while (line != null) {
				String[] part = line.split(" ");
				List<Integer> group = Arrays.asList(part[1].split(",")).stream().map(p -> Integer.valueOf(p)).toList();
				lines.add(new Input(part[0], group));
				line = reader.readLine();
			}

			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return lines;
	}

	private static long countPermutation(Input input) {
		long permutation = 0;

		if (memorize.containsKey(input)) {
			return memorize.get(input);
		}

		if (input.condition.isBlank()) {
			return input.groups.isEmpty() ? 1 : 0;
		}

		char firstChar = input.condition.charAt(0);
		if (firstChar == '.') {
			Input i = new Input(input.condition.substring(1), input.groups);
			permutation = countPermutation(i);
		} else if (firstChar == '?') {
			Input i1 = new Input("." + input.condition.substring(1), input.groups);
			Input i2 = new Input("#" + input.condition.substring(1), input.groups);
			permutation = countPermutation(i1) + countPermutation(i2);
		} else {
			if (input.groups.size() == 0) {
				permutation = 0;
			} else {
				int nbDamage = input.groups.get(0);
				if (nbDamage <= input.condition.length() && input.condition.chars().limit(nbDamage).allMatch(c -> c == '#' || c == '?')) {
					List<Integer> newGroup = input.groups.subList(1, input.groups.size());
					if (nbDamage == input.condition.length()) {
						permutation = newGroup.isEmpty() ? 1 : 0;
					} else if (input.condition.charAt(nbDamage) == '.') {
						Input i = new Input(input.condition.substring(nbDamage+1), newGroup);
						permutation = countPermutation(i);
					} else if (input.condition.charAt(nbDamage) == '?') {
						Input i = new Input("." + input.condition.substring(nbDamage+1), newGroup);
						permutation = countPermutation(i);
					} else {
						permutation = 0;
					}
				} else {
					permutation = 0;
				}
			}
		}

 		memorize.put(input, permutation);
		return permutation;
	}

	private static Input unfoldInput(Input input, int nbTime) {
		String unfoldCond = "";
		List<Integer> unfoldGroups = new ArrayList<Integer>();

		for (int i=1; i<nbTime; i++) {
			unfoldCond += input.condition + "?";
			unfoldGroups.addAll(input.groups);
		}
		unfoldCond += input.condition;
		unfoldGroups.addAll(input.groups);
		return (new Input(unfoldCond, unfoldGroups));
	}

	/********************/
	/** The first part **/
	/********************/

	private static long solvePuzzle1() {
		long res = 0;
		ArrayList<Input> reader = fileToArrayList("puzzle_day_12.txt");

		for (Input i : reader) {
			long nbPerm = countPermutation(i);
			res += nbPerm;
		}
		
		return res;
	}

	/*********************/
	/** The second part **/
	/*********************/

	private static long solvePuzzle2() {
		long res = 0;
		ArrayList<Input> reader = fileToArrayList("puzzle_day_12.txt");

		for (Input i : reader) {
			Input unfoldInput = unfoldInput(i, 5);

			long nbPerm = countPermutation(unfoldInput);
			res += nbPerm;
		}
		
		return res;
   	}
}