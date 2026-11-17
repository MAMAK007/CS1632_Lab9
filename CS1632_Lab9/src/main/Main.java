package main;

import java.util.List;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		Scanner keyboard = new Scanner(System.in);
		System.out.print("Enter the graph CSV file name: ");
		String filename = keyboard.nextLine().trim();
	
		GraphFileReader reader = new GraphFileReader();
		
		List<Edge> edges;
		try {
			edges = reader.readEdges(filename);
		} catch (java.io.FileNotFoundException fnfe) {
			System.out.println("Error: could not find file \"" + filename + "\"");
			keyboard.close();
			return;
		}
	
		if (edges.isEmpty()) {
			System.out.println("No edges found in file — nothing to do.");
			keyboard.close();
			return;
		}
		
		AdjacencyListGraph listGraph = new AdjacencyListGraph();
		for (Edge e : edges) {
			listGraph.addEdge(e.getSrc(), e.getDest(), e.getWeight());
		}
		listGraph.sortNeighborLists();

		AdjacencyMatrixGraph matrixGraph = new AdjacencyMatrixGraph(edges);

		listGraph.print();
		System.out.println();
		matrixGraph.print();
		System.out.println();
		
		// @TODO Stuff
		
		
		
		keyboard.close();
	}
}
