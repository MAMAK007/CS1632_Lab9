package main;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class GraphFileReaderTest {

	@TempDir
	Path tempDir;

	private Path writeCsv(String contents) throws IOException {
		Path f = tempDir.resolve("test_graph.csv");
		Files.write(f, contents.getBytes());
		return f;
	}

	private Map<String, Node> readIndexed(String csv) throws IOException {
		List<Node> nodes = new GraphFileReader().read(writeCsv(csv).toString());
		Map<String, Node> byName = new HashMap<>();
		for (Node n : nodes) {
			byName.put(n.getName(), n);
		}
		return byName;
	}

	@Test
	void parsesBasicEdgesWithCorrectDirection() throws IOException {
		Map<String, Node> g = readIndexed("A,B,2\nA,C,3\n");

		assertEquals(2.0, g.get("A").getWeightTo(g.get("B")), 1e-9);
		assertNull(g.get("B").getWeightTo(g.get("A")), "edge must be directed A->B");
		assertEquals(2, g.get("A").getNeighbors().size());
	}

	@Test
	void repeatedNodeNamesDedupeToOneObject() throws IOException {
		Map<String, Node> g = readIndexed("A,B,2\nA,C,3\nA,D,5.5\n");

		assertEquals(4, g.size(), "A,B,C,D — four distinct names");
		assertEquals(3, g.get("A").getNeighbors().size(),
			"node A accumulated all three edges");
	}

	@Test
	void malformedLinesAreSkippedButValidOnesStillLoad() throws IOException {
		Map<String, Node> g = readIndexed("A,B,2\nthis_line_is_garbage\nC,D,4\n");

		assertTrue(g.containsKey("A"));
		assertTrue(g.containsKey("C"));
		assertEquals(2, g.size(), "garbage row contributed no phantom nodes");
	}

	@Test
	void linesWithBadWeightsAreSkipped() throws IOException {
		Map<String, Node> g = readIndexed("A,B,two\nA,C,3\n");

		assertNull(g.get("A").getWeightTo(g.get("B")),
			"row with unparseable weight must not create an edge");
		assertNull(g.get("B"), "B shouldn't exist as a node if its only row failed");
	}

	@Test
	void blankLinesIgnored() throws IOException {
		Map<String, Node> g = readIndexed("\nA,B,2\n\n\nC,D,1\n\n");

		assertEquals(4, g.size());
	}

	@Test
	void whitespaceAroundFieldsIsTolerated() throws IOException {
		Map<String, Node> g = readIndexed(" A , B , 2 \n");

		assertEquals(2.0, g.get("A").getWeightTo(g.get("B")), 1e-9);
	}

	@Test
	void missingFileThrowsFileNotFoundException() {
		assertThrows(java.io.FileNotFoundException.class,
			() -> new GraphFileReader().read(tempDir.resolve("nope.csv").toString()));
	}
}