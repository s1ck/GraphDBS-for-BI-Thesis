package de.s1ckboy.thesis.benchmark.neo4j;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.configuration.Configuration;

import de.s1ckboy.thesis.benchmark.Benchmark;
import de.s1ckboy.thesis.benchmark.BenchmarkSuite;
import de.s1ckboy.thesis.benchmark.Configs;
import de.s1ckboy.thesis.benchmark.Constants;
import de.s1ckboy.thesis.benchmark.neo4j.benchmarks.Neo4jImportBenchmark;
import de.s1ckboy.thesis.benchmark.neo4j.benchmarks.Neo4jQuery1_Cypher;
import de.s1ckboy.thesis.benchmark.neo4j.benchmarks.Neo4jQuery2_Cypher;

public class Neo4jSuite extends BenchmarkSuite {

    private Configuration cfg = Configs.get(Neo4jConstants.INSTANCE_NAME);

    @Override
    public void execute() {
	List<Benchmark> benchmarks = new ArrayList<Benchmark>();

	boolean logToFile = cfg.getBoolean("log");
	boolean doWarmup = cfg.getBoolean("warmup");

	/**
	 * Import
	 */
	if (cfg.getBoolean(Constants.IMPORT)) {
	    benchmarks.add(new Neo4jImportBenchmark(new Neo4jImporter(cfg), 1));
	}

	/**
	 * Benchmarks
	 */
	if (cfg.getBoolean(Constants.NEO4J_Q1_CYPHER)) {
	    Benchmark q1 = new Neo4jQuery1_Cypher();
	    q1.setRuns(cfg.getInt(Constants.NEO4J_Q1_CYPHER + ".runs"));
	    benchmarks.add(q1);
	}

	if (cfg.getBoolean(Constants.NEO4J_Q2_CYPHER)) {
	    Benchmark q2 = new Neo4jQuery2_Cypher();
	    q2.setRuns(cfg.getInt(Constants.NEO4J_Q2_CYPHER + ".runs"));
	    benchmarks.add(q2);
	}

	runBenchmarks(benchmarks, logToFile, doWarmup);
    }
}
