package main;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {

	// Minimal carrier so dijkstra() can hand back both outputs without
	// printShortestPaths needing to know how they were computed
	static class DijkstraResult {
		Map<Node, Double> dist = new HashMap<>();
		Map<Node, Node> prev = new HashMap<>();
	}

	public static void main(String[] args) {
		GraphFileReader reader = new GraphFileReader();
		Scanner sc = new Scanner(System.in);

		System.out.print("Graph file to read from: ");
		String filename = sc.nextLine().trim();

		List<Node> nodes;
		try {
			nodes = reader.read(filename);
		}
		catch (FileNotFoundException fnfe) {
			System.out.println("Error: could not find file \"" + filename + "\"");
			sc.close();
			return;
		}

		if (nodes.isEmpty()) {
			System.out.println("No edges found in file — nothing to do.");
			sc.close();
			return;
		}
		
		Map<String, Node> byName = new HashMap<>();
		for (Node n : nodes) {
			byName.put(n.getName(), n);
		}

		List<String> names = new ArrayList<>();
		for (Node n : nodes) {
			names.add(n.getName());
		}
		System.out.println();
		System.out.println("List of nodes: " + String.join(", ", names));

		while (true) {
			System.out.println();
			System.out.print("Node to start at: ");
			String input = sc.nextLine().trim();

			if (input.isEmpty()) {
				break;
			}

			Node start = byName.get(input);
			if (start == null) {
				System.out.println("No node named \"" + input + "\" in this graph.");
				continue;
			}

			printShortestPaths(nodes, start);
		}

		sc.close();
	}

	private static void printShortestPaths(List<Node> nodes, Node start) {
		DijkstraResult result = dijkstra(nodes, start);

		System.out.println();
		for (Node dest : nodes) {
			double d = result.dist.get(dest);
			if (d == Double.POSITIVE_INFINITY) {
				System.out.println(dest.getName() + " dist: unreachable");
				continue;
			}
			System.out.println(buildPath(result.prev, start, dest) + "  dist: " + format(d));
		}
	}

	// Package-private so tests can call it directly; contains your exact
	// Dijkstra loop, only now it returns results instead of printing
	static DijkstraResult dijkstra(List<Node> nodes, Node start) {
		DijkstraResult result = new DijkstraResult();

		for (Node n : nodes) {
			result.dist.put(n, Double.POSITIVE_INFINITY);
		}
		result.dist.put(start, 0.0);

		List<Node> unvisited = new ArrayList<>(nodes);

		while (!unvisited.isEmpty()) {
			Node current = null;
			for (Node candidate : unvisited) {
				if (current == null || result.dist.get(candidate) < result.dist.get(current)) {
					current = candidate;
				}
			}
			unvisited.remove(current);
			if (result.dist.get(current) == Double.POSITIVE_INFINITY) {
				break;
			}

			for (Node neighbor : current.getNeighbors()) {
				double alt = result.dist.get(current) + current.getWeightTo(neighbor);
				if (alt < result.dist.get(neighbor)) {
					result.dist.put(neighbor, alt);
					result.prev.put(neighbor, current);
				}
			}
		}

		return result;
	}

	private static String buildPath(Map<Node, Node> prev, Node start, Node dest) {
		List<String> steps = new ArrayList<>();
		Node cursor = dest;
		while (cursor != null) {
			steps.add(cursor.getName());
			cursor = prev.get(cursor);
		}
		Collections.reverse(steps);
		return String.join(" -> ", steps);
	}

	private static String format(double d) {
		if (d == Math.rint(d)) {
			return String.valueOf((long) d);
		}
		return String.valueOf(d);
	}
}