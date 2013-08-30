package de.s1ckboy.thesis.benchmark;

import de.s1ckboy.thesis.benchmark.generic.BenchmarkSuite;
import de.s1ckboy.thesis.benchmark.neo4j.Neo4jSuite;

public class App {
    public static void main(String[] args) {
	// Pattern p =
	// Pattern.compile("^\\(([^)]+)\\)-\\[\\:(.*?)\\]->\\(([^)]+)\\)(\\s(\\{.*?\\}))?");
	// Matcher m = p.matcher("(0827229534)-[:BELONGS_TO]->(0)");
	// if (m.matches()) {
	// System.out.println("blubb");
	// System.out.println(m.group(1));
	// System.out.println(m.group(2));
	// System.out.println(m.group(3));
	// }
	BenchmarkSuite neo4jSuite = new Neo4jSuite();

	neo4jSuite.execute();
    }
}
