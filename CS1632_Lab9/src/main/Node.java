package main;

import java.util.ArrayList;
import java.util.List;

public class Node {
	private String name;
	private List<Edge> edges;

	private static class Edge {
		Node target;
		double weight;

		Edge(Node target, double weight) {
			this.target = target;
			this.weight = weight;
		}
	}

	public Node(String name) {
		this.name = name;
		this.edges = new ArrayList<>();
	}

	public String getName() {
		return name;
	}

	public void addEdge(Node target, double weight) {
		for (Edge e : edges) {
			if (e.target == target) {
				throw new IllegalArgumentException(
					"Edge to " + target.getName() + " already exists");
			}
		}
		edges.add(new Edge(target, weight));
	}
	
	public boolean hasEdgeTo(Node target) {
		for (Edge e : edges) {
			if (e.target == target) {
				return true;
			}
		}
		return false;
	}
	
	public List<String> getNeighborNames() {
		List<String> names = new ArrayList<>();
		for (Edge e : edges) {
			names.add(e.target.getName());
		}
		return names;
	}

	public Double getWeightTo(Node target) {
		for (Edge e : edges) {
			if (e.target == target) {
				return e.weight;
			}
		}
		return null;
	}
	
	public List<Node> getNeighbors() {
		List<Node> result = new ArrayList<>();
		for (Edge e : edges) {
			result.add(e.target);
		}
		return result;
	}
}