import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.stream.Collectors;
import java.util.*;

public class PuzzleDay {

	public static String run;

	public static class Node {
		public String name;
		public String left;
		public String right;

		public Node(String name, String left, String right) {
			this.name = name;
			this.left = left;
			this.right = right;
		}
	}

    public static void main(String[] args) {
		int res_1 = solvePuzzle1();
		System.out.println("Result for puzzle 1 = " + res_1);

		long res_2 = solvePuzzle2();
		System.out.println("Result for puzzle 2 = " + res_2);
    }

	// Transform file into ArrayList
	private static Map<String, Node> fileToMapOfNode(String fileName) {
		BufferedReader reader;

		Map<String, Node> lines = new HashMap<String, Node>();
		try {
			reader = new BufferedReader(new FileReader(fileName));
			String line = reader.readLine();

			while (line != null) {
				if (!line.contains("=")) {
					run = line;
					line = reader.readLine();
				} else {
					String[] splitName = line.split(" = ");
					String[] splitChild = splitName[1].replaceAll("\\(|\\)", "").split(", ");
					Node n = new Node(splitName[0], splitChild[0], splitChild[1]);
					lines.put(splitName[0], n);
				}
				line = reader.readLine();
			}

			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return lines;
	}

	/********************/
	/** The first part **/
	/********************/

	private static int solvePuzzle1() {
		int res = 0;
		Map<String, Node> reader = fileToMapOfNode("puzzle_day_8.txt");

		Node actualNode = reader.get("AAA");
		while (!actualNode.name.equals("ZZZ")) {
			for (char c : run.toCharArray()) {
				if (c == 'R') {
					actualNode = reader.get(actualNode.right);
				} else {
					actualNode = reader.get(actualNode.left);
				}
				res++;
			}
		}

		return res;
	}

	/*********************/
	/** The second part **/
	/*********************/

	private static long solvePuzzle2() {
 		long res = 0;
		Map<String, Node> reader = fileToMapOfNode("puzzle_day_8.txt");

		List<String> keyWithEnd = reader.keySet()
                     .stream()
                     .filter(s -> s.endsWith("A"))
                     .collect(Collectors.toList());

		long[] multiRes = new long[keyWithEnd.size()];
		for (int i=0; i<keyWithEnd.size(); i++) {
			Node actualNode = reader.get(keyWithEnd.get(i));

			while (!actualNode.name.endsWith("Z")) {
				for (char c : run.toCharArray()) {
					if (c == 'R') {
						actualNode = reader.get(actualNode.right);
					} else {
						actualNode = reader.get(actualNode.left);
					}
					multiRes[i]++;
				}
			}
		}

		long x, y, z;
		x = multiRes[0];
		for (int i=1; i<keyWithEnd.size(); i++) {
			y = multiRes[i];
	    	z = leastCommonMultiple(x, y);
			x = z;
		}
		res = x;

		return res;
   	}

	public static long leastCommonMultiple (long Nb1, long Nb2) {
		long Product, Rest, LCM;
			
		Product = Nb1*Nb2;
		Rest   = Nb1%Nb2;
		while(Rest != 0){
			Nb1 = Nb2;
			Nb2 = Rest;
			Rest = Nb1%Nb2;
			}
		LCM = Product/Nb2;
		return LCM;
	}
}