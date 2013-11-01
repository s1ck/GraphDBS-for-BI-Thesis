package de.s1ckboy.thesis.benchmark.neo4j;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.configuration.Configuration;

import de.s1ckboy.thesis.benchmark.Benchmark;
import de.s1ckboy.thesis.benchmark.BenchmarkSuite;
import de.s1ckboy.thesis.benchmark.Configs;
import de.s1ckboy.thesis.benchmark.Constants;
import de.s1ckboy.thesis.benchmark.neo4j.benchmarks.Neo4jFoafReviewsCypher;
import de.s1ckboy.thesis.benchmark.neo4j.benchmarks.Neo4jFoafReviewsGremlin;
import de.s1ckboy.thesis.benchmark.neo4j.benchmarks.Neo4jImportBenchmark;
import de.s1ckboy.thesis.benchmark.neo4j.benchmarks.Neo4jPathAllCypher;
import de.s1ckboy.thesis.benchmark.neo4j.benchmarks.Neo4jPathAllGremlin;
import de.s1ckboy.thesis.benchmark.neo4j.benchmarks.Neo4jPathShortestCypher;
import de.s1ckboy.thesis.benchmark.neo4j.benchmarks.Neo4jPathShortestGremlin;
import de.s1ckboy.thesis.benchmark.neo4j.benchmarks.Neo4jRandomReadCypher;
import de.s1ckboy.thesis.benchmark.neo4j.benchmarks.Neo4jRandomReadGremlin;
import de.s1ckboy.thesis.benchmark.neo4j.benchmarks.Neo4jSimPatternsCypher;
import de.s1ckboy.thesis.benchmark.neo4j.benchmarks.Neo4jSimProductsCypher;
import de.s1ckboy.thesis.benchmark.neo4j.benchmarks.Neo4jSimProductsGremlin;
import de.s1ckboy.thesis.benchmark.neo4j.benchmarks.Neo4jTopRegionsCypher;

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

	// random_read
	if (cfg.getBoolean(Constants.NEO4J_RANDOM_READ_CYPHER)) {
	    Benchmark q = new Neo4jRandomReadCypher();
	    q.setRuns(cfg.getInt(Constants.NEO4J_RANDOM_READ_CYPHER + ".runs"));
	    benchmarks.add(q);
	}

	if (cfg.getBoolean(Constants.NEO4J_RANDOM_READ_GREMLIN)) {
	    Benchmark q = new Neo4jRandomReadGremlin();
	    q.setRuns(cfg.getInt(Constants.NEO4J_RANDOM_READ_GREMLIN + ".runs"));
	    benchmarks.add(q);
	}

	// sim_products
	if (cfg.getBoolean(Constants.NEO4J_SIM_PRODUCTS_CYPHER)) {
	    Benchmark q = new Neo4jSimProductsCypher();
	    q.setRuns(cfg.getInt(Constants.NEO4J_SIM_PRODUCTS_CYPHER + ".runs"));
	    benchmarks.add(q);
	}

	if (cfg.getBoolean(Constants.NEO4J_SIM_PRODUCTS_GREMLIN)) {
	    Benchmark q = new Neo4jSimProductsGremlin();
	    q.setRuns(cfg
		    .getInt(Constants.NEO4J_SIM_PRODUCTS_GREMLIN + ".runs"));
	    benchmarks.add(q);
	}

	// foaf_reviews
	if (cfg.getBoolean(Constants.NEO4J_FOAF_REVIEWS_CYPHER)) {
	    Benchmark q = new Neo4jFoafReviewsCypher();
	    q.setRuns(cfg.getInt(Constants.NEO4J_FOAF_REVIEWS_CYPHER + ".runs"));
	    benchmarks.add(q);
	}

	if (cfg.getBoolean(Constants.NEO4J_FOAF_REVIEWS_GREMLIN)) {
	    Benchmark q = new Neo4jFoafReviewsGremlin();
	    q.setRuns(cfg
		    .getInt(Constants.NEO4J_FOAF_REVIEWS_GREMLIN + ".runs"));
	    benchmarks.add(q);
	}

	// path_all
	if (cfg.getBoolean(Constants.NEO4J_PATH_ALL_CYPHER)) {
	    Benchmark q = new Neo4jPathAllCypher();
	    q.setRuns(cfg.getInt(Constants.NEO4J_PATH_ALL_CYPHER + ".runs"));
	    benchmarks.add(q);
	}

	if (cfg.getBoolean(Constants.NEO4J_PATH_ALL_GREMLIN)) {
	    Benchmark q = new Neo4jPathAllGremlin();
	    q.setRuns(cfg.getInt(Constants.NEO4J_PATH_ALL_GREMLIN + ".runs"));
	    benchmarks.add(q);
	}

	// path_shortest
	if (cfg.getBoolean(Constants.NEO4J_PATH_SHORTEST_CYPHER)) {
	    Benchmark q = new Neo4jPathShortestCypher();
	    q.setRuns(cfg
		    .getInt(Constants.NEO4J_PATH_SHORTEST_CYPHER + ".runs"));
	    benchmarks.add(q);
	}

	if (cfg.getBoolean(Constants.NEO4J_PATH_SHORTEST_GREMLIN)) {
	    Benchmark q = new Neo4jPathShortestGremlin();
	    q.setRuns(cfg.getInt(Constants.NEO4J_PATH_SHORTEST_GREMLIN
		    + ".runs"));
	    benchmarks.add(q);
	}

	// top_regions
	if (cfg.getBoolean(Constants.NEO4J_TOP_REGIONS_CYPHER)) {
	    Benchmark q = new Neo4jTopRegionsCypher();
	    q.setRuns(cfg.getInt(Constants.NEO4J_TOP_REGIONS_CYPHER + ".runs"));
	    benchmarks.add(q);
	}

	// sim_patterns
	if (cfg.getBoolean(Constants.NEO4J_SIM_PATTERNS_CYPHER)) {
	    Benchmark q = new Neo4jSimPatternsCypher();
	    q.setRuns(cfg.getInt(Constants.NEO4J_SIM_PATTERNS_CYPHER + ".runs"));
	    benchmarks.add(q);
	}

	runBenchmarks(benchmarks, logToFile, doWarmup);
    }
}
