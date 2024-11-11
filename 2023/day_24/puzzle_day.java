import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.stream.Stream;
import java.util.*;

public class PuzzleDay {

	public static class Pos3D {
		long x, y, z;
		double xDouble, yDouble, zDouble;

		public Pos3D(long x, long y, long z) {
			this.x = x;
			this.y = y;
			this.z = z;
		}

		public Pos3D(double x, double y, double z) {
			this.xDouble = x;
			this.yDouble = y;
			this.zDouble = z;
		}

		public String toStringDouble() {
			return "[" + xDouble +
				", " + yDouble +
				", " + zDouble +
				']';
		}

		@Override
		public String toString() {
			return "[" + x +
				", " + y +
				", " + z +
				']';
		}
	}
	
	public static class Velocity {
		Long x, y, z;

		public Velocity() {
			this.x = null;
			this.y = null;
			this.z = null;
		}

		public Velocity(long x, long y, long z) {
			this.x = x;
			this.y = y;
			this.z = z;
		}

		@Override
		public String toString() {
			return "[" + x +
				", " + y +
				", " + z +
				']';
		}
	}

	public static class Hailstone {
		Pos3D instantPos;
		Velocity velocity;

		public Hailstone(Pos3D pos, Velocity vel) {
			this.instantPos = pos;
			this.velocity = vel;
		}

		public Pos3D nextPos() {
			return new Pos3D(
				this.instantPos.x + this.velocity.x,
				this.instantPos.y + this.velocity.y,
				this.instantPos.z + this.velocity.z
			);
		}
	}

	public static List<Hailstone> map = new ArrayList<>();

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
        for (String line : reader) {
            String[] split = line.split(" @ ");
            Long[] pSplit = Arrays.stream(split[0].split(", ")).map(Long::parseLong).toArray(Long[]::new);
            Long[] vSplit = Arrays.stream(split[1].split(", ")).map(String::trim).map(Long::parseLong).toArray(Long[]::new);
            Pos3D pos = new Pos3D(pSplit[0], pSplit[1], pSplit[2]);
            Velocity vel = new Velocity(vSplit[0], vSplit[1], vSplit[2]);
			map.add(new Hailstone(pos, vel));
		}
	}

	public static Pos3D hailmapCrossPosition2D(Hailstone h1, Hailstone h2) {

		Pos3D startH1 = h1.instantPos;
		Pos3D pointInH1 = h1.nextPos();
		Pos3D startH2 = h2.instantPos;
		Pos3D PointInH2 = h2.nextPos();

		double a1 = pointInH1.y - startH1.y;
        double b1 = startH1.x - pointInH1.x;
        double c1 = a1 * startH1.x + b1 * startH1.y;

        double a2 = PointInH2.y - startH2.y;
        double b2 = startH2.x - PointInH2.x;
        double c2 = a2 * startH2.x + b2 * startH2.y;

		double delta = a1 * b2 - a2 * b1;
		Pos3D res = new Pos3D(((b2 * c1 - b1 * c2) / delta), ((a1 * c2 - a2 * c1) / delta), 0D);

		return res;
	}

	public static boolean isInArea(Pos3D point, Pos3D minArea, Pos3D maxArea) {
		return point.xDouble >= minArea.x && point.xDouble <= maxArea.x
			&& point.yDouble >= minArea.y && point.yDouble <= maxArea.y
			&& point.zDouble >= minArea.z && point.zDouble <= maxArea.z;
	}

	public static boolean isCrossInFuture(Pos3D crossPoint, Hailstone h, boolean withZ) {
		boolean xInFuture = Math.abs(crossPoint.xDouble - h.instantPos.x) >= Math.abs(crossPoint.xDouble - h.nextPos().x);
		boolean yInFuture = Math.abs(crossPoint.yDouble - h.instantPos.y) >= Math.abs(crossPoint.yDouble - h.nextPos().y);
		boolean zInFuture = withZ ? 
			Math.abs(crossPoint.zDouble - h.instantPos.z) >= Math.abs(crossPoint.zDouble - h.nextPos().z)
			: true;
		
		return xInFuture && yInFuture && zInFuture;
	}

	/********************/
	/** The first part **/
	/********************/

	private static long solvePuzzle1() {
		long res = 0;
		ArrayList<String> reader = fileToArrayList("puzzle_data.txt");

		constructMap(reader);

		Pos3D minArea = new Pos3D(200000000000000L, 200000000000000L, 0);
		Pos3D maxArea = new Pos3D(400000000000000L, 400000000000000L, 0);
		for (int i=0; i<map.size()-1; i++) {
			for (int j=i+1; j<map.size(); j++) {
				Pos3D cross = hailmapCrossPosition2D(map.get(i), map.get(j));
				if (isInArea(cross, minArea, maxArea)) {
					if (isCrossInFuture(cross, map.get(i), false) && isCrossInFuture(cross, map.get(j), false)) {
						res++;
					}
				}
			}
		}

		return res;
	}

	/*********************/
	/** The second part **/
	/*********************/
	
	private static long solvePuzzle2() {
		long res = 0;
		ArrayList<String> reader = fileToArrayList("puzzle_data.txt");

		constructMap(reader);

		Hailstone h1 = map.get(0);
        Hailstone h2 = map.get(1);
 
        int range = 500;
        for (int vx = -range; vx <= range; vx++) {
            for (int vy = -range; vy <= range; vy++) {
                for (int vz = -range; vz <= range; vz++) {
 
                    if (vx == 0 || vy == 0 || vz == 0) {
                        continue;
                    }
 
                    long A = h1.instantPos.x;
					long a = h1.velocity.x - vx;
                    long B = h1.instantPos.y;
					long b = h1.velocity.y - vy;
                    long C = h2.instantPos.x;
					long c = h2.velocity.x - vx;
                    long D = h2.instantPos.y;
					long d = h2.velocity.y - vy;
 
                    // skip if division by 0
                    if (c == 0 || (a * d) - (b * c) == 0) {
                        continue;
                    }
 
                    // Rock intercepts H1 at time t
                    long t = (d * (C - A) - c * (D - B)) / ((a * d) - (b * c));
 
                    // Calculate starting position of rock from intercept point
                    long x = h1.instantPos.x + h1.velocity.x * t - vx * t;
                    long y = h1.instantPos.y + h1.velocity.y * t - vy * t;
                    long z = h1.instantPos.z + h1.velocity.z * t - vz * t;
 
                    
                    // check if this rock throw will hit all hailstones
 
                    boolean hitall = true;
                    for (int i = 0; i < map.size(); i++) {
 
                        Hailstone h = map.get(i);
                        long u;
                        if (h.velocity.x != vx) {
                            u = (x - h.instantPos.x) / (h.velocity.x - vx);
                        } else if (h.velocity.y != vy) {
                            u = (y - h.instantPos.y) / (h.velocity.y - vy);
                        } else if (h.velocity.z != vz) {
                            u = (z - h.instantPos.z) / (h.velocity.z - vz);
                        } else {
                            throw new RuntimeException();
                        }
 
                        if ((x + u * vx != h.instantPos.x + u * h.velocity.x) || (y + u * vy != h.instantPos.y + u * h.velocity.y) || ( z + u * vz != h.instantPos.z + u * h.velocity.z)) {
                            hitall = false;
                            break;
                        }
                    }
 
                    if (hitall) {
						return x+y+z;
                    }
                }
            }
        }
		
        return res;
    }
}