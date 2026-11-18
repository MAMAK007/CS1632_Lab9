package main;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class ShortestPathTest {

	private static final String SAMPLE_CSV = """
		A,B,2
		A,C,3
		A,D,5.5
		B,E,7
		C,E,3
		D,F,5
		E,G,5
		E,H,5.7
		F,H,4.4
		F,I,5
		G,J,4.3
		H,I,2.8
		H,J,4
		""";

	private static List<Node> graph;
	private static Map<String, Node> byName = new HashMap<>();

	@BeforeAll
	static void loadGraph(@TempDir Path tempDir) throws IOException {
		Path f = tempDir.resolve("sample.csv");
		Files.write(f, SAMPLE_CSV.getBytes());
		graph = new GraphFileReader().read(f.toString());
		for (Node n : graph) {
			byName.put(n.getName(), n);
		}
	}

	@Test
	void startNodeHasDistanceZero() {
		Map<Node, Double> dist = Main.dijkstra(graph, byName.get("A")).dist;
		assertEquals(0.0, dist.get(byName.get("A")), 1e-9);
	}

	@Test
	void directEdgesTakeTheirWeight() {
		Map<Node, Double> dist = Main.dijkstra(graph, byName.get("A")).dist;
		assertEquals(2.0, dist.get(byName.get("B")), 1e-9);
		assertEquals(5.5, dist.get(byName.get("D")), 1e-9);
	}

	@Test
	void shorterIndirectRouteWinsOverDirect() {
		// A->E: via B costs 2+7=9, via C costs 3+3=6 — Dijkstra must pick 6
		Map<Node, Double> dist = Main.dijkstra(graph, byName.get("A")).dist;
		assertEquals(6.0, dist.get(byName.get("E")), 1e-9);
	}

	@Test
	void farNodeDistancesComputedCorrectly() {
		// Hand-computed from A:
		// F = A->D->F      = 10.5
		// H = A->E->H      = 11.7
		// I = A->E->H->I   = 14.5
		// J = A->E->G->J   = 15.3
		Map<Node, Double> dist = Main.dijkstra(graph, byName.get("A")).dist;

		assertEquals(10.5, dist.get(byName.get("F")), 1e-9);
		assertEquals(11.7, dist.get(byName.get("H")), 1e-9);
		assertEquals(14.5, dist.get(byName.get("I")), 1e-9);
		assertEquals(15.3, dist.get(byName.get("J")), 1e-9);
	}

	@Test
	void unreachableNodesStayAtInfinity() {
		// Directed graph: nothing points AT B, so starting from C, B is unreachable
		Map<Node, Double> dist = Main.dijkstra(graph, byName.get("C")).dist;

		assertEquals(Double.POSITIVE_INFINITY, dist.get(byName.get("B")));
		assertEquals(3.0, dist.get(byName.get("E")), 1e-9,
			"C->E should still be reachable");
	}
}