package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GraphFileReader {
	public ArrayList<Edge> readEdges(String filename) throws FileNotFoundException {
		ArrayList<Edge> edges = new ArrayList<>();
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
				edges.add(new Edge(source, dest, weightDouble));
			} 
			catch (NumberFormatException nfe) {
				System.out.println("Skipping line with bad weight: " + line);
			}
		}
		sc.close();
		
		return edges;
	}
}