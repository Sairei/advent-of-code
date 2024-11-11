import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class PuzzleDay {

	record Range(long start, long increment) {}
	private static List<Range> seeds = new ArrayList<>();
	private static TreeMap<Long, Range> seedToSoilMap = new TreeMap<>();
    private static TreeMap<Long, Range> soilToFertilizerMap = new TreeMap<>();
    private static TreeMap<Long, Range> fertilizerToWaterMap = new TreeMap<>();
    private static TreeMap<Long, Range> waterToLightMap = new TreeMap<>();
    private static TreeMap<Long, Range> lightToTemperatureMap = new TreeMap<>();
    private static TreeMap<Long, Range> temperatureToHumidityMap = new TreeMap<>();
    private static TreeMap<Long, Range> humidityToLocationMap = new TreeMap<>();


    public static void main(String[] args) {
		long res_1 = solvePuzzle1();
		System.out.println("Result for puzzle 1 = " + res_1);

		long res_2 = solvePuzzle2();
		System.out.println("Result for puzzle 2 = " + res_2);
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


	/************************************************/
	/** The first part is processed by brute force **/
	/************************************************/

	private static long solvePuzzle1() {
		long res = Long.MAX_VALUE;

		int nbCorresponding = 0;
		ArrayList<String> reader = fileToArrayListOfLines("puzzle_data.txt");
		ArrayList<ArrayList<Long>> seedsMap = new ArrayList<ArrayList<Long>>();

		for (int i=0; i<reader.size(); i++) {
			String line = reader.get(i);
			if (i == 0) {
				String[] seeds = parseSeeds(line);
				for (String s : seeds) {
					ArrayList<Long> sMap = new ArrayList<Long>();
					sMap.add(Long.valueOf(s));
					seedsMap.add(sMap);
				}
			} else if (!line.isEmpty()) {
				if (!line.contains("map")) {
					long[] map = sourceToDest(line);
					for (ArrayList<Long> sMap : seedsMap) {
						correponding(sMap, map, nbCorresponding);
					}
				}
			} else {
				for (ArrayList<Long> sMap : seedsMap) {
					defaultCorreponding(sMap, nbCorresponding);
				}
				nbCorresponding++;
			}
		}

		for (ArrayList<Long> sMap : seedsMap) {
			res = sMap.get(sMap.size()-1) < res ? sMap.get(sMap.size()-1) : res;
		}

		return res;
	}

	// Parse the first line to get the list of seeds
	private static String[] parseSeeds(String seedLine) {
		return seedLine.replace("seeds: ", "").split(" ");
	}

	// Performs default matching
	private static void defaultCorreponding(ArrayList<Long> sMap, int nbCorresponding) {
		if (sMap.size() == nbCorresponding) {
			sMap.add(sMap.get(nbCorresponding-1));
		}
	}

	// Performs matching via mapping table
	private static void correponding(ArrayList<Long> sMap, long[] map, int nbCorresponding) {
		if (sMap.size() == nbCorresponding) {
			long last = sMap.get(sMap.size() - 1);
			if (map[0] <= last && last < (map[0] + map[2])) {
				long corresp = map[1] + (last - map[0]);
				sMap.add(corresp);
			}
		}
	}

	// Parse line from/to
	private static long[] sourceToDest(String line) {
		String[] split = line.split(" ");
		long[] res = new long[3];

		// 0 => min source
		res[0] = Long.valueOf(split[1]);
		// 1 => min dest
		res[1] = Long.valueOf(split[0]);
		// 2 => nb values
		res[2] = Long.valueOf(split[2]);

		return res;
	}


	/**********************************************/
	/** The second part is processed with ranges **/
	/**********************************************/

	private static long solvePuzzle2() {
 		TreeMap<Long, String> inputMap = new TreeMap<>();

		ArrayList<String> reader = fileToArrayListOfLines("puzzle_data.txt");
		long index = 0;
		for (String line : reader) {
			if (line.startsWith("seeds: ")) {
				parseSeedsRange(line);
			} else if (!(index==0 && line.isEmpty()) && (!line.contains("map:"))) {
				inputMap.put(index, line);
				index++;
			}
		}

		createMaps(inputMap);
		return findMinLocation();
	}

	// Parse the first line to get the list of seeds range
	private static void parseSeedsRange(String seedLine) {
		seedLine = seedLine.replace("seeds: ", "");
        String[] seedTab = seedLine.split(" ");

        for (int i = 0; i < seedTab.length; i+=2) {
            String first = seedTab[i];
            if (first.equals("")) {
                continue;
            }
            long start = Long.parseLong(first);
            long range = Long.parseLong(seedTab[i + 1]);
            Range r = new Range(start, range);
            seeds.add(r);
        }
	}

	private static void createMaps(TreeMap<Long, String> inputMap) {
		List<TreeMap<Long, Range>> maps = List.of(
                seedToSoilMap,
                soilToFertilizerMap,
                fertilizerToWaterMap,
                waterToLightMap,
                lightToTemperatureMap,
                temperatureToHumidityMap,
                humidityToLocationMap);

        long index = 0;
        for (TreeMap<Long, Range> specificMap : maps) {
            String line = inputMap.get(index);

            while (line!=null && !line.isEmpty()) {
                addRangeToMap(line, specificMap);
                index++;
                if (index >= inputMap.size()) {
                    break;
                }
                line = inputMap.get(index);
            }
            index++;
        }
	}

	private static void addRangeToMap(String line, TreeMap<Long, Range> map) {
        String[] split = line.split(" ");
        long destinationRangeStart = Long.parseLong(split[0]);
        long sourceRangeStart = Long.parseLong(split[1]);
        long rangeLength = Long.parseLong(split[2]);

        Range r = new Range(destinationRangeStart, rangeLength);
        map.put(sourceRangeStart, r);
    }

	private static long findMinLocation() {
        long minMax = Long.MAX_VALUE;

        for (Range seed : seeds) {
            for (long add = 0; add < seed.increment; add++) {

                long soil = getMapValue(seedToSoilMap, (seed.start + add));
                long fertilizer = getMapValue(soilToFertilizerMap, soil);
                long water = getMapValue(fertilizerToWaterMap, fertilizer);
                long light = getMapValue(waterToLightMap, water);
                long temperature = getMapValue(lightToTemperatureMap, light);
                long humidity = getMapValue(temperatureToHumidityMap, temperature);
                long location = getMapValue(humidityToLocationMap, humidity);
                minMax = Math.min(minMax, location);
            }
        }
        return minMax;
    }

	private static long getMapValue(TreeMap<Long, Range> map, long seed) {
        long result = seed;
        Map.Entry<Long, Range> entry = map.floorEntry(seed);

        if (entry == null) {
            return result;
        }
        long seedKey = entry.getKey();
        Range destination = entry.getValue();

        if (seed == seedKey) {
            result = destination.start;
        } else if (seed <= seedKey + destination.increment) {
            long difference = seed - seedKey;
            result = destination.start + difference;
        }
        return result;
    }
}