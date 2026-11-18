package main;

import java.util.List;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		GraphFileReader reader = new GraphFileReader();
		List<Edge> edges;
		Scanner sc = new Scanner(System.in);
		
		System.out.print("Enter the graph CSV file name: ");
		String filename = sc.nextLine().trim();
	
		try {
			edges = reader.readEdges(filename);
		} 
		catch (java.io.FileNotFoundException fnfe) {
			System.out.println("Error: could not find file \"" + filename + "\"");
			sc.close();
			return;
		}
	
		if (edges.isEmpty()) {
			System.out.println("No edges found in file — nothing to do.");
			sc.close();
			return;
		}
		
		for (Edge i : edges) {
			
		}
		
		
		sc.close();
	}
}
