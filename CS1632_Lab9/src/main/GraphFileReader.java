package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class GraphFileReader {

	public List<Node> read(String filename) throws FileNotFoundException {
		Map<String, Node> nodeIndex = new HashMap<>();
		Scanner sc = new Scanner(new File(filename));

		while (sc.hasNextLine()) {
			String line = sc.nextLine().trim();
			if (line.isEmpty()) {
				continue;
			}

			String[] parts = line.split(",", -1);
			if (parts.length != 3) {
				System.out.println("Skipping malformed line: " + line);
				continue;
			}

			String source = parts[0].trim();
			String dest = parts[1].trim();
			String weight = parts[2].trim();

			if (source.isEmpty() || dest.isEmpty()) {
				System.out.println("Skipping line with empty node name: " + line);
				continue;
			}

			try {
				double weightDouble = Double.parseDouble(weight);
				
				Node srcNode = getOrCreate(nodeIndex, source);
				Node dstNode = getOrCreate(nodeIndex, dest);

				srcNode.addEdge(dstNode, weightDouble);
			}
			catch (NumberFormatException nfe) {
				System.out.println("Skipping line with bad weight: " + line);
			}
		}
		sc.close();

		return new ArrayList<>(nodeIndex.values());
	}

	private Node getOrCreate(Map<String, Node> index, String name) {
		return index.computeIfAbsent(name, Node::new);
	}
}