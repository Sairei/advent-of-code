import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.stream.Stream;
import java.util.*;
import org.jgrapht.Graph;
import org.jgrapht.alg.StoerWagnerMinimumCut;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

public class PuzzleDay {

	public static Graph<String, DefaultEdge> graph = new SimpleGraph<>(DefaultEdge.class);

    public static void main(String[] args) {
		long res = solvePuzzle();
		System.out.println("Result for puzzle 1 = " + res);
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
            String[] parts = line.split(": ");
            String parent = parts[0];
            String[] children = parts[1].split(" ");
			
			graph.addVertex(parent);
			for (String c : children) {
                graph.addVertex(c);
                graph.addEdge(parent, c);
            }
        }
	}

	/***************************/
	/** Solve puzzle function **/
	/***************************/

	private static long solvePuzzle() {
		long res = 0;
		ArrayList<String> reader = fileToArrayList("puzzle_day_25.txt");

		constructMap(reader);

		StoerWagnerMinimumCut<String, DefaultEdge> swMinCut = new StoerWagnerMinimumCut<>(graph);
        long minCut = swMinCut.minCut().size();
        res = minCut * (graph.vertexSet().size() - minCut);

		return res;
	}
}