import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.stream.Collectors;
import java.util.*;

public class PuzzleDay {

	private static final String LOW = "LOW";
    private static final String HIGH = "HIGH";
    private static final String OFF = "OFF";
    private static final String ON = "ON";
	private static final String BROADCASTER = "broadcaster";
    private static final String INVERTER = "inverter";
    private static final String SWITCH = "switch";

	public static class Module {
		String name;
		String type;
		List<String> destination;
		String state;
		Map<String, String> memory;

		public Module(String name, String type, String state, List<String> destination) {
			this.name = name;
			this.type = type;
			this.state = state;
			this.destination = destination;
			this.memory = new HashMap<>();
		}

		public String switchStateAndPulse() {
			if (this.state.equals(ON)) {
				this.state = OFF;
				return LOW;
			} else {
				this.state = ON;
				return HIGH;
			}
		}
	}

	public static class Pulse {
		String from;
		String type;
		String to;
		String infoSend;

		public Pulse(String from, String type, String to, String infoSend) {
			this.from = from;
			this.type = type;
			this.to = to;
			this.infoSend = infoSend;
		}

		public String toString() {
			String info = "";
			if (!this.infoSend.isEmpty()) {
				info = "(" + this.infoSend + ")";
			}
			return from + info + " -" + type + "-> " + to;
		}
	}

	public static Map<String, Module> map = new HashMap<>();
	public static List<String> processingMap = new ArrayList<>();
	public static final Map<String, Long> part2ModulesToHigh = new HashMap<>();
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

	public static void constructMap(ArrayList<String> reader) {
		Map<String, Set<String>> memoryMap = new HashMap<>();
		for (String line : reader) {
            String[] parts = line.split(" -> ");
			List<String> destinations = Arrays.stream(parts[1].split(", ")).toList();

			String name = parts[0].trim();
			if (name.charAt(0) == '%') {
				name = name.substring(1).trim();
                Module m = new Module(name, SWITCH, OFF, destinations);
                map.put(m.name, m);
            } else if (name.charAt(0) == '&') {
				name = name.substring(1).trim();
                Module m = new Module(name, INVERTER, null, destinations);
                map.put(m.name, m);
            } else {
                Module m = new Module(name, BROADCASTER, null, destinations);
                map.put(m.name, m);
            }

			for (String d : destinations) {
				Set<String> list = memoryMap.get(d)!=null ? memoryMap.get(d) : new HashSet<>();
				list.add(name);
				memoryMap.put(d, list);
			}
		}

		for (Module module : map.values()) {
			if (module.type.equals(INVERTER)) {
				Set<String> list = memoryMap.get(module.name);
				for (String l : list) {
					module.memory.put(l, LOW);
				}
			}
		}
	}

	public static String pushBtn(int nbPush) {
		String processRes = "button -LOW-> broadcaster ";
		List<Pulse> pulseList = processing(map.get(BROADCASTER), new Pulse("button", LOW, BROADCASTER, ""));
		while (!pulseList.isEmpty()) {
			Pulse pulse = pulseList.remove(0);
			processRes += pulse.toString() + " ";

			if (part2ModulesToHigh.containsKey(pulse.from)
                && pulse.type.equals(HIGH)) {
                part2ModulesToHigh.put(pulse.from, (long) nbPush);
            }

			Module to = map.get(pulse.to);
			if (to == null) { continue; }

			if (to.type.equals(INVERTER)) {
				to.memory.put(pulse.from, pulse.type);
				map.put(to.name, to);
			}

			pulseList.addAll(processing(to, pulse));
		}

		return processRes.trim();
	}

	public static List<Pulse> processing(Module mod, Pulse pulse) {
		List<Pulse> res = new ArrayList<>();
		String pulseType = pulse.type;
		
		if (mod.type.equals(BROADCASTER)) {
			for (String dest : mod.destination) {
				Pulse newPulse = new Pulse(mod.name, pulseType, dest, "");
				res.add(newPulse);
			}
		} else if (mod.type.equals(SWITCH)) {
			if (pulseType.equals(LOW)) {
				String infoSend = mod.state;
				String pulseSend = mod.switchStateAndPulse();
				for (String dest : mod.destination) {
					Pulse newPulse = new Pulse(mod.name, pulseSend, dest, infoSend);
					res.add(newPulse);
				}
				map.put(mod.name, mod);
			}
		} else {
			String infoSend = pulse.from + "=" + pulse.type;
			String pulseSend = HIGH;
			if (mod.memory.values().stream().allMatch(type -> type.equals(HIGH))) {
				pulseSend = LOW;
			}
			for (String dest : mod.destination) {
				Pulse newPulse = new Pulse(mod.name, pulseSend, dest, infoSend);
				res.add(newPulse);
			}
		}

		return res;
	}

	public static long countHigh(String pulse) {
		return pulse.split("-" + HIGH + "->").length - 1;
	}

	public static long countLow(String pulse) {
		return pulse.split("-" + LOW + "->").length - 1;
	}

    public static long greatestCommonFactor(long a, long b) {
        if (a == 0 || b == 0) {
			return a + b;
		} else {
			long biggerValue = Math.max(a, b);
			long smallerValue = Math.min(a, b);
			return greatestCommonFactor(smallerValue, biggerValue % smallerValue);
		}
    }

	/********************/
	/** The first part **/
	/********************/

	private static long solvePuzzle1() {
		long res = 0;
		ArrayList<String> reader = fileToArrayList("puzzle_day_20.txt");

		constructMap(reader);

		long resHigh = 0;
		long resLow = 0;
		int nbPush = 1000;
		for (int i=0; i<nbPush; i++) {
			String pushRes = pushBtn(i);
			
			resHigh += countHigh(pushRes);
			resLow += countLow(pushRes);
		}
		res = resHigh * resLow;

		return res;
	}

	/*********************/
	/** The second part **/
	/*********************/

	private static long solvePuzzle2() {
		long res = -1;
		ArrayList<String> reader = fileToArrayList("puzzle_day_20.txt");

		constructMap(reader);

		for (Module mod : map.values()) {
			if (mod.destination.contains("rx")) {
                for (var key : mod.memory.keySet()) {
                    part2ModulesToHigh.put(key, 0L);
                }
            }
        }

		int i = 1;
		while (part2ModulesToHigh.values().stream().anyMatch(l -> l == 0L)) {
			String pushRes = pushBtn(i);
			i++;
		}
		return part2ModulesToHigh.values().stream().reduce(1L, (a, b) -> (a * b) / greatestCommonFactor(a, b));
	}
}