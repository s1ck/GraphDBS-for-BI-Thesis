package de.s1ckboy.thesis.benchmark.neo4j;

import java.util.ArrayList;
import java.util.List;

import de.s1ckboy.thesis.benchmark.Configuration;
import de.s1ckboy.thesis.benchmark.generic.Benchmark;
import de.s1ckboy.thesis.benchmark.generic.BenchmarkSuite;

public class Neo4jSuite extends BenchmarkSuite {

    private Configuration cfg;

    public Neo4jSuite() {
	cfg = Configuration.getInstance("neo4j");
    }

    @Override
    public void execute() {
	List<Benchmark> benchmarks = new ArrayList<Benchmark>();

	boolean logToFile = cfg.getPropertyAsBoolean("log");
	boolean doWarmup = cfg.getPropertyAsBoolean("warmup");

	/**
	 * Import
	 */

	if (cfg.getPropertyAsBoolean("import")) {
	    Benchmark neo4jImportBench = new Import_Neo4j(
		    cfg.getPropertyAsString("database"));
	    neo4jImportBench.setRuns(1);
	    benchmarks.add(neo4jImportBench);
	}

	/**
	 * Benchmarks
	 */

	if (cfg.getPropertyAsBoolean("query1")) {
	    Benchmark query_1 = new Query1_Neo4j();
	    query_1.setRuns(100);
	    benchmarks.add(query_1);
	}

	runBenchmarks(benchmarks, logToFile, doWarmup);
    }
}
