import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.stream.Collectors;
import java.util.*;

public class PuzzleDay {

	public static class Workflow {
		String name;
		List<Rule> rules;

		public Workflow(String name) {
			this.name = name;
			this.rules = new LinkedList<>();
		}

		public String toString() {
			String res = name + "{ ";
			for (Rule r : rules) {
				 res += r.toString() + " | ";
			}
			res = res.substring(0, res.length()-3) + "}";
			return res;
		}
	}

	public static class Rule {
		String categorie = "";
		String op = "";
		String value = "";
		String to = "";

		public Rule(String rule) {
			String[] condition = rule.split(":");
			if (rule.contains(":")) {
				this.categorie = condition[0].substring(0, 1);
				this.op = condition[0].substring(1, 2);
				this.value = condition[0].substring(2, condition[0].length());
			}
			this.to = condition[condition.length-1];
		}

		public boolean validate(long val) {
			if (op.equals("<")) {
				return val < Long.parseLong(value);
			} else {
				return val > Long.parseLong(value);
			}
		}

		public String toString() {
			return categorie + op + value + " => " + to;
		}
	}

	public static class Entrie {
		Map<String, Long> map;

		public Entrie() {
			this.map = new HashMap<>();
		}

		public Long getCompleteValue() {
			return map.entrySet().stream().mapToLong(e -> e.getValue()).sum();
		}

		public String toString() {
			String res = "{ ";
			for (Map.Entry<String, Long> entry : map.entrySet()) {
				 res += entry.getKey() + "=" + entry.getValue() + " | ";
			}
			res = res.substring(0, res.length()-3) + "}";
			return res;
		}
	}

	public static Map<String, Workflow> workflowMap = new HashMap<>();
	public static List<Entrie> entriesMap = new ArrayList<>();
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

	private static void constructMaps(ArrayList<String> reader) {
		boolean isWorkflow = true;
		for (String line : reader) {
			if (line.isEmpty()) {
				isWorkflow = false;
				continue;
			}

			if (isWorkflow) {
				String[] nameParts = line.substring(0, line.length()-1).split("\\{");
				String[] parts = nameParts[1].split(",");

				Workflow w = new Workflow(nameParts[0]);
				for (String r : parts) {
					Rule rule = new Rule(r);
					w.rules.add(rule);
				}
				workflowMap.put(nameParts[0], w);
			} else {
				String[] parts = line.substring(1, line.length()-1).split(",");
				Entrie entries = new Entrie();
				for (String e : parts) {
					String[] keyValue = e.split("=");
					entries.map.put(keyValue[0], Long.parseLong(keyValue[1]));
				}
				entriesMap.add(entries);
			}
		}
	}

	public static long startWork() {
		long res = 0;

		for (Entrie e : entriesMap) {
			boolean isOk = work(e, workflowMap.get("in"));
			// System.out.println(isOk + " => " + e);
			res += isOk ? e.getCompleteValue() : 0;
		}

		return res;
	}

	public static boolean work(Entrie entrie, Workflow workflow) {
		boolean res = false;
		// System.out.print(workflow.name + " => ");
		for (Rule rule : workflow.rules) {
			if (rule.categorie.isEmpty()) {
				if (validateWork(rule.to) != null) {
					res = validateWork(rule.to);
					break;
				}
				res = work(entrie, workflowMap.get(rule.to));
				break;
			} else {
				long wantedVal = entrie.map.get(rule.categorie);
				if (rule.validate(wantedVal)) {
					if (validateWork(rule.to) != null) {
						res = validateWork(rule.to);
						break;
					}
					res = work(entrie, workflowMap.get(rule.to));
					break;
				}
			}
		}
		return res;
	}

	public static Boolean validateWork(String to) {
		Boolean res = null;
		if (to.equals("A")) {
			res = true;
		} else if (to.equals("R")) {
			res = false;
		}
		return res;
	}

	/********************/
	/** The first part **/
	/********************/

	private static long solvePuzzle1() {
		long res = 0;
		// ArrayList<String> reader = fileToArrayList("data_test.txt");
		ArrayList<String> reader = fileToArrayList("puzzle_day_19.txt");
		
		constructMaps(reader);

		res = startWork();

		return res;
	}

	/*********************/
	/** The second part **/
	/*********************/

	private static long solvePuzzle2() {
		long res = 0;

		return res;
	}
}