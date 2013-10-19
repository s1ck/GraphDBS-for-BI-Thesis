package de.s1ckboy.thesis.benchmark.neo4j;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.configuration.Configuration;

import de.s1ckboy.thesis.benchmark.Benchmark;
import de.s1ckboy.thesis.benchmark.BenchmarkSuite;
import de.s1ckboy.thesis.benchmark.Configs;
import de.s1ckboy.thesis.benchmark.Constants;
import de.s1ckboy.thesis.benchmark.neo4j.benchmarks.Neo4jImportBenchmark;
import de.s1ckboy.thesis.benchmark.neo4j.benchmarks.Neo4jRandomReadCypher;
import de.s1ckboy.thesis.benchmark.neo4j.benchmarks.Neo4jSimProductsCypher;

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

	// q1 cypher
	if (cfg.getBoolean(Constants.NEO4J_RANDOM_READ_CYPHER)) {
	    Benchmark q = new Neo4jRandomReadCypher();
	    q.setRuns(cfg.getInt(Constants.NEO4J_RANDOM_READ_CYPHER + ".runs"));
	    benchmarks.add(q);
	}

	// q2_cypher
	if (cfg.getBoolean(Constants.NEO4J_SIM_PRODUCTS_CYPHER)) {
	    Benchmark q = new Neo4jSimProductsCypher();
	    q.setRuns(cfg.getInt(Constants.NEO4J_SIM_PRODUCTS_CYPHER + ".runs"));
	    benchmarks.add(q);
	}

	runBenchmarks(benchmarks, logToFile, doWarmup);
    }
}
