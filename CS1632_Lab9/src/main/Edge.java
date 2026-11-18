package main;

public class Edge {
	private String source;
	private String dest;
	private double weight;

	public Edge(String source, String dest, double weight) {
		this.source = source;
		this.dest = dest;
		this.weight = weight;
	}

	public String getSource() {
		return source;
	}

	public String getDest() {
		return dest;
	}

	public double getWeight() {
		return weight;
	}
}