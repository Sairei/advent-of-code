import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.stream.Stream;
import java.util.*;

public class PuzzleDay {

	public static class Coordinate {
		long x, y, z;

		public Coordinate(long x, long y, long z) {
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

	private static void findXAndY(Hailstone hs) {
		double[][] coefs = new double[4][4];
		double[] r = new double[4];
		for (int i=0; i<4; i++) {
			Hailstone h1 = map.get(i);
			Hailstone h2 = map.get(i+1);
            coefs[i][0] = h2.velocity.y - h1.velocity.y;
            coefs[i][1] = h1.velocity.x - h2.velocity.x;
            coefs[i][2] = h1.instantPos.y - h2.instantPos.y;
            coefs[i][3] = h2.instantPos.x - h1.instantPos.x;
			r[i] = 
				-h1.instantPos.x * h1.velocity.y + h1.instantPos.y * h1.velocity.x
				+ h2.instantPos.x * h2.velocity.y - h2.instantPos.y * h2.velocity.x;
		}

		gaussianElimination(coefs, r);

		hs.instantPos.x = Math.round(r[0]);
        hs.instantPos.y = Math.round(r[1]);
        hs.velocity.x = Math.round(r[2]);
        hs.velocity.y = Math.round(r[3]);
	}

	private static void findZ(Hailstone hs) {
		double[][] coefs = new double[2][2];
		double[] r = new double[2];
		for (int i=0; i<2; i++) {
			Hailstone h1 = map.get(i);
			Hailstone h2 = map.get(i+1);
            coefs[i][0] = h1.velocity.x - h2.velocity.x;
            coefs[i][1] = h2.instantPos.x - h1.instantPos.x;
			r[i] = 
				-h1.instantPos.x * h1.velocity.z + h1.instantPos.z * h1.velocity.x
				+ h2.instantPos.x * h2.velocity.z - h2.instantPos.z * h2.velocity.x
				- ((h2.velocity.z - h1.velocity.z) * hs.instantPos.x) - ((h1.instantPos.z - h2.instantPos.z) * hs.velocity.x);
		}

		gaussianElimination(coefs, r);

		hs.instantPos.z = Math.round(r[0]);
        hs.velocity.z = Math.round(r[1]);
	}

	private static void gaussianElimination(double[][] coefficients, double r[]) {
        int nrVariables = coefficients.length;
        for (int i = 0; i < nrVariables; i++) {
            // Select pivot
            double pivot = coefficients[i][i];
            // Normalize row i
            for (int j = 0; j < nrVariables; j++) {
                coefficients[i][j] = coefficients[i][j] / pivot;
            }
            r[i] = r[i] / pivot;
            // Sweep using row i
            for (int k = 0; k < nrVariables; k++) {
                if (k != i) {
                    double factor = coefficients[k][i];
                    for (int j = 0; j < nrVariables; j++) {
                        coefficients[k][j] = coefficients[k][j] - factor * coefficients[i][j];
                    }
                    r[k] = r[k] - factor * r[i];
                }
            }
        }
    }

	/********************/
	/** The first part **/
	/********************/

	private static long solvePuzzle1() {
		long res = 0;
		ArrayList<String> reader = fileToArrayList("puzzle_day_24.txt");

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
		ArrayList<String> reader = fileToArrayList("puzzle_day_24.txt");
		constructMap(reader);

		/* Thanks to Reddit AOC thread for the mention of Gaussian Elimination */

		Hailstone hsRes = new Hailstone(new Pos3D(0, 0, 0), new Velocity(0, 0, 0));
		
		findXAndY(hsRes);
		findZ(hsRes);
		
		res = hsRes.instantPos.x + hsRes.instantPos.y + hsRes.instantPos.z;
		
        return res;
    }
}