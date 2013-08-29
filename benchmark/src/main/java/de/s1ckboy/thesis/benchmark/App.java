package de.s1ckboy.thesis.benchmark;

import de.s1ckboy.thesis.benchmark.generic.BenchmarkSuite;
import de.s1ckboy.thesis.benchmark.neo4j.Neo4jSuite;


public class App {
    public static void main(String[] args) {
	BenchmarkSuite neo4jSuite = new Neo4jSuite();
	
	neo4jSuite.execute();
    }
}
