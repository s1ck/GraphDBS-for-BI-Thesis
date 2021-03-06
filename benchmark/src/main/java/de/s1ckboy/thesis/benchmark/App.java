package de.s1ckboy.thesis.benchmark;

import org.apache.commons.configuration.Configuration;

import de.s1ckboy.thesis.benchmark.neo4j.Neo4jSuite;
import de.s1ckboy.thesis.benchmark.titan.TitanSuite;

public class App {
    public static void main(String[] args) {
	Configuration cfg = Configs.get("benchmark");

	if (cfg.getBoolean("run.neo4j"))
	    new Neo4jSuite().execute();
	if (cfg.getBoolean("run.titan"))
	    new TitanSuite().execute();	
    }
}
