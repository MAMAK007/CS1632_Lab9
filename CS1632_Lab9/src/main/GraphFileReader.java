package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GraphFileReader {
	public List<Edge> readEdges(String filename) throws FileNotFoundException {
		List<Edge> edges = new ArrayList<>();
		Scanner fileScanner = new Scanner(new File(filename));
		while (fileScanner.hasNextLine()) {
			String line = fileScanner.nextLine().trim();
			if (line.isEmpty()) {
				continue; // skip blank lines
			}

			String[] parts = line.split(",", -1);
			if (parts.length != 3) {
				System.out.println("Skipping malformed line: " + line);
				continue;
			}

			String src = parts[0].trim();
			String dest = parts[1].trim();
			String weightToken = parts[2].trim();

			if (src.isEmpty() || dest.isEmpty()) {
				System.out.println("Skipping line with empty node name: " + line);
				continue;
			}

			try {
				double weight = Double.parseDouble(weightToken);
				edges.add(new Edge(src, dest, weight));
			} catch (NumberFormatException nfe) {
				System.out.println("Skipping line with bad weight: " + line);
			}
		}
		fileScanner.close();
		return edges;
	}
}