import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class PuzzleDay {

	public static class Pos3D {
		int x, y, z;

		public Pos3D(int x, int y, int z) {
			this.x = x;
			this.y = y;
			this.z = z;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) {
				return true;
			}
			if (o == null || getClass() != o.getClass()) {
				return false;
			}
			Pos3D point3D = (Pos3D) o;
			return x == point3D.x && y == point3D.y && z == point3D.z;
		}

		@Override
		public int hashCode() {
			return Objects.hash(x, y, z);
		}

		@Override
		public String toString() {
			return "[" +
				"x=" + x +
				", y=" + y +
				", z=" + z +
				']';
		}
	}

	public static class Brick {
		Pos3D end1, end2;
		Set<Pos3D> allCubes = new HashSet<>();

		public Brick(Pos3D end1, Pos3D end2) {
			this.end1 = end1;
			this.end2 = end2;
			for (int x=end1.x; x<=end2.x; x++) {
				for (int y=end1.y; y<=end2.y; y++) {
					for (int z=end1.z; z<=end2.z; z++) {
						allCubes.add(new Pos3D(x, y, z));
					}
				}	
			}
		}
	}

	public static Map<Integer, Brick> map = new HashMap<>();
	public static Map<Integer, Set<Integer>> supportBrickMap = new HashMap<>();
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
		int id = 1;
		for (String line : reader) {
            String[] split = line.split("~");
            Integer[] end1Array = Arrays.stream(split[0].split(",")).map(Integer::parseInt).toArray(Integer[]::new);
            Integer[] end2Array = Arrays.stream(split[1].split(",")).map(Integer::parseInt).toArray(Integer[]::new);
            Pos3D end1 = new Pos3D(end1Array[0], end1Array[1], end1Array[2]);
            Pos3D end2 = new Pos3D(end2Array[0], end2Array[1], end2Array[2]);
			map.put(id, new Brick(end1, end2));
			id++;
		}

		makeBrickFall();

		constructSupportMap();
	}

	public static void makeBrickFall() {
		Map<Integer, Boolean> isProcessed = new HashMap<>();
		for (Integer key : map.keySet()) {
            isProcessed.put(key, false);
        }
		int currentZ = 1;
		while (isProcessed.values().stream().anyMatch(v -> !v)) {
			for (Map.Entry<Integer, Brick> entry : map.entrySet()) {
				if (isProcessed.get(entry.getKey())) {
                    continue;
                }

                var minZ = Math.min(entry.getValue().end1.z, entry.getValue().end2.z);
                if (minZ == currentZ) {
					Brick brick = entry.getValue();
					int brickId = entry.getKey();

					Brick newbrick = updateBrickEnds(brick, brickId);
					
                    isProcessed.put(brickId, true);
                    map.put(brickId, newbrick);
				}
			}
			
            currentZ++;
		}
	}

	private static Brick updateBrickEnds(Brick brick, int id) {
		while(!brickIsAtRest(brick, id)) {
			Pos3D end1 = brick.end1;
            Pos3D end2 = brick.end2;
            Pos3D newEnd1 = new Pos3D(end1.x, end1.y, end1.z - 1);
            Pos3D newEnd2 = new Pos3D(end2.x, end2.y, end2.z - 1);
            brick = new Brick(newEnd1, newEnd2);
		}
		return brick;
	}

	private static boolean brickIsAtRest(Brick brick, int id) {		
        int minZ = brick.allCubes.stream().map(pos -> pos.z).min(Integer::compareTo).orElseThrow();

		if (minZ == 1) {
			return true;
		}

		for (Pos3D point : brick.allCubes) {
            if (point.z != minZ) {
                continue;
            }
            Pos3D pointBelow = new Pos3D(point.x, point.y, point.z - 1);
            if (map.entrySet().stream()
                .filter(b -> !Objects.equals(b.getKey(), id))
                .map(e -> e.getValue()).anyMatch(b -> b.allCubes.contains(pointBelow))) {
                return true;
            }
        }
		return false;
	}

	private static void constructSupportMap() {
		for (Map.Entry<Integer, Brick> entry : map.entrySet()) {
			int id = entry.getKey();
			Brick brick = entry.getValue();

			int highestZ = brick.allCubes.stream().map(p -> p.z).max(Integer::compareTo).orElseThrow();
			List<Pos3D> posAboveBrick = brick.allCubes.stream()
											.filter(p -> p.z == highestZ)
											.map(p -> new Pos3D(p.x, p.y, p.z+1))
											.toList();
			
			List<Integer> sup = new ArrayList<>();
			for (Map.Entry<Integer, Brick> e : map.entrySet()) {
				if (e.getKey() == id) { continue; }

				if (e.getValue().allCubes.stream().anyMatch(posAboveBrick::contains)) {
					sup.add(e.getKey());
				}
			}
			supportBrickMap.put(id, Set.copyOf(sup));
		}
	}

	private static Map<Integer, Set<Integer>> getSupportsOfSupport(int id) {
		Map<Integer, Set<Integer>> supportsOfSupports = new HashMap<>();

		for(int supId : supportBrickMap.get(id)) {
			List<Integer> ids = new ArrayList<>();
			for (Map.Entry<Integer, Set<Integer>> e : supportBrickMap.entrySet()) {
				if (e.getKey() == id) { continue; }

				if (e.getValue().contains(supId)) {
					ids.add(e.getKey());
				}
			}
            supportsOfSupports.put(supId, Set.copyOf(ids));
		}

		return supportsOfSupports;
	}

	/********************/
	/** The first part **/
	/********************/

	private static long solvePuzzle1() {
		long res = 0;
		ArrayList<String> reader = fileToArrayList("puzzle_day_22.txt");

		constructMap(reader);

		for(int id : supportBrickMap.keySet()) {
			if (supportBrickMap.get(id).isEmpty()) {
				res++;
				continue;
			}
			if (getSupportsOfSupport(id).values().stream().noneMatch(Set::isEmpty)) {
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
		ArrayList<String> reader = fileToArrayList("puzzle_day_22.txt");

		constructMap(reader);

		for (int id : supportBrickMap.keySet()) {
            List<Integer> idToCheck = new ArrayList<>();
            idToCheck.add(id);
            Set<Integer> goneBlocks = new HashSet<>();

			while(!idToCheck.isEmpty()) {
				int currentId = idToCheck.remove(0);
				List<Integer> nextIds = getSupportsOfSupport(currentId).entrySet().stream()
									.filter(s -> s.getValue().isEmpty() || goneBlocks.containsAll(s.getValue()))
									.map(Map.Entry::getKey).toList();

				for (int nextId : nextIds) {
					if (!goneBlocks.contains(nextId)) {
						idToCheck.add(nextId);
						goneBlocks.add(nextId);
					}
				}
			}

			res += goneBlocks.size();
		}

		return res;
	}
}