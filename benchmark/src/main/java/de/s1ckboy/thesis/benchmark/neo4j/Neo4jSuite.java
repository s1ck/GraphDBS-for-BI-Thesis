package de.s1ckboy.thesis.benchmark.neo4j;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.configuration.Configuration;

import de.s1ckboy.thesis.benchmark.Benchmark;
import de.s1ckboy.thesis.benchmark.BenchmarkSuite;
import de.s1ckboy.thesis.benchmark.Configs;
import de.s1ckboy.thesis.benchmark.Constants;
import de.s1ckboy.thesis.benchmark.neo4j.benchmarks.Neo4jFoafReviewsCypher;
import de.s1ckboy.thesis.benchmark.neo4j.benchmarks.Neo4jImportBenchmark;
import de.s1ckboy.thesis.benchmark.neo4j.benchmarks.Neo4jPathAllCypher;
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

	if (cfg.getBoolean(Constants.NEO4J_RANDOM_READ_CYPHER)) {
	    Benchmark q = new Neo4jRandomReadCypher();
	    q.setRuns(cfg.getInt(Constants.NEO4J_RANDOM_READ_CYPHER + ".runs"));
	    benchmarks.add(q);
	}

	if (cfg.getBoolean(Constants.NEO4J_SIM_PRODUCTS_CYPHER)) {
	    Benchmark q = new Neo4jSimProductsCypher();
	    q.setRuns(cfg.getInt(Constants.NEO4J_SIM_PRODUCTS_CYPHER + ".runs"));
	    benchmarks.add(q);
	}

	if (cfg.getBoolean(Constants.NEO4J_FOAF_REVIEWS_CYPHER)) {
	    Benchmark q = new Neo4jFoafReviewsCypher();
	    q.setRuns(cfg.getInt(Constants.NEO4J_FOAF_REVIEWS_CYPHER + ".runs"));
	    benchmarks.add(q);
	}

	if (cfg.getBoolean(Constants.NEO4J_PATH_ALL_CYPHER)) {
	    Benchmark q = new Neo4jPathAllCypher();
	    q.setRuns(cfg.getInt(Constants.NEO4J_PATH_ALL_CYPHER + ".runs"));
	    benchmarks.add(q);
	}

	runBenchmarks(benchmarks, logToFile, doWarmup);
    }
}
