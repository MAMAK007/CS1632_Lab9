package main;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.Test;

public class NodeTest {

	@Test
	void addEdgeMakesTargetReachableAsNeighbor() {
		Node a = new Node("A");
		Node b = new Node("B");
		a.addEdge(b, 2.0);

		assertTrue(a.hasEdgeTo(b), "A should have an edge to B after addEdge");
		assertFalse(b.hasEdgeTo(a), "edge should be one-directional unless mirrored");
	}

	@Test
	void getWeightReturnsWeightOfExistingEdge() {
		Node a = new Node("A");
		Node b = new Node("B");
		a.addEdge(b, 5.5);

		assertEquals(5.5, a.getWeightTo(b), 1e-9);
	}

	@Test
	void getWeightIsNullForMissingEdge() {
		Node a = new Node("A");
		Node c = new Node("C");

		assertNull(a.getWeightTo(c));
	}

	@Test
	void duplicateEdgeIsRejected() {
		Node a = new Node("A");
		Node b = new Node("B");
		a.addEdge(b, 2.0);

		assertThrows(IllegalArgumentException.class, () -> a.addEdge(b, 3.0));
	}

	@Test
	void edgeKeptToSameTargetWithDifferentObject() {
		Node a = new Node("A");
		a.addEdge(new Node("B"), 1.0);
		a.addEdge(new Node("B"), 2.0);

		assertEquals(2, a.getNeighbors().size());
	}

	@Test
	void getNeighborsReturnsDefensiveCopy() {
		Node a = new Node("A");
		Node b = new Node("B");
		a.addEdge(b, 2.0);

		List<Node> neighbors = a.getNeighbors();
		neighbors.clear();

		assertEquals(1, a.getNeighbors().size(), "internal edges survived caller mutation");
	}

	@Test
	void getNeighborNamesMatchesEdges() {
		Node a = new Node("A");
		a.addEdge(new Node("B"), 1.0);
		a.addEdge(new Node("C"), 1.0);

		assertEquals(List.of("B", "C"), a.getNeighborNames());
	}

	@Test
	void newNodeHasEmptyAdjacency() {
		Node n = new Node("X");

		assertNotNull(n.getNeighbors(), "adjacency list should exist, not be null");
		assertTrue(n.getNeighbors().isEmpty());
	}
}