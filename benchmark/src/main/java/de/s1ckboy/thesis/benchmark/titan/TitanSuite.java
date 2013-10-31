package de.s1ckboy.thesis.benchmark.titan;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.configuration.Configuration;

import de.s1ckboy.thesis.benchmark.Benchmark;
import de.s1ckboy.thesis.benchmark.BenchmarkSuite;
import de.s1ckboy.thesis.benchmark.Configs;
import de.s1ckboy.thesis.benchmark.Constants;
import de.s1ckboy.thesis.benchmark.titan.benchmarks.TitanFoafReviewsGremlin;
import de.s1ckboy.thesis.benchmark.titan.benchmarks.TitanImportBenchmark;
import de.s1ckboy.thesis.benchmark.titan.benchmarks.TitanPathAllGremlin;
import de.s1ckboy.thesis.benchmark.titan.benchmarks.TitanPathShortestGremlin;
import de.s1ckboy.thesis.benchmark.titan.benchmarks.TitanRandomReadGremlin;
import de.s1ckboy.thesis.benchmark.titan.benchmarks.TitanSimPatternGremlin;
import de.s1ckboy.thesis.benchmark.titan.benchmarks.TitanSimProductsGremlin;
import de.s1ckboy.thesis.benchmark.titan.benchmarks.TitanTopRegionsGremlin;

public class TitanSuite extends BenchmarkSuite {
    private Configuration cfg = Configs.get(TitanConstants.INSTANCE_NAME);

    @Override
    public void execute() {
	List<Benchmark> benchmarks = new ArrayList<Benchmark>();

	boolean logToFile = cfg.getBoolean("log");
	boolean doWarmup = cfg.getBoolean("warmup");

	/**
	 * Import
	 */
	if (cfg.getBoolean(Constants.IMPORT)) {
	    Benchmark i = new TitanImportBenchmark(new TitanImporter(cfg));
	    i.setRuns(1);
	    benchmarks.add(i);
	}

	/**
	 * Benchmarks
	 */
	if (cfg.getBoolean(Constants.TITAN_RANDOM_READ_GREMLIN)) {
	    Benchmark q = new TitanRandomReadGremlin();
	    q.setRuns(cfg.getInt(Constants.TITAN_RANDOM_READ_GREMLIN + ".runs"));
	    benchmarks.add(q);
	}

	if (cfg.getBoolean(Constants.TITAN_SIM_PROUCTS_GREMLIN)) {
	    Benchmark q = new TitanSimProductsGremlin();
	    q.setRuns(cfg.getInt(Constants.TITAN_SIM_PROUCTS_GREMLIN + ".runs"));
	    benchmarks.add(q);
	}

	if (cfg.getBoolean(Constants.TITAN_FOAF_REVIEWS_GREMLIN)) {
	    Benchmark q = new TitanFoafReviewsGremlin();
	    q.setRuns(cfg
		    .getInt(Constants.TITAN_FOAF_REVIEWS_GREMLIN + ".runs"));
	    benchmarks.add(q);
	}

	if (cfg.getBoolean(Constants.TITAN_PATH_ALL_GREMLIN)) {
	    Benchmark q = new TitanPathAllGremlin();
	    q.setRuns(cfg.getInt(Constants.TITAN_PATH_ALL_GREMLIN + ".runs"));
	    benchmarks.add(q);
	}

	if (cfg.getBoolean(Constants.TITAN_PATH_SHORTEST_GREMLIN)) {
	    Benchmark q = new TitanPathShortestGremlin();
	    q.setRuns(cfg.getInt(Constants.TITAN_PATH_SHORTEST_GREMLIN
		    + ".runs"));
	    benchmarks.add(q);
	}

	if (cfg.getBoolean(Constants.TITAN_TOP_REGIONS_GREMLIN)) {
	    Benchmark q = new TitanTopRegionsGremlin();
	    q.setRuns(cfg.getInt(Constants.TITAN_TOP_REGIONS_GREMLIN + ".runs"));
	    benchmarks.add(q);
	}
	
	if (cfg.getBoolean(Constants.TITAN_SIM_PATTERN_GREMLIN)) {
	    Benchmark q = new TitanSimPatternGremlin();
	    q.setRuns(cfg.getInt(Constants.TITAN_SIM_PATTERN_GREMLIN + ".runs"));
	    benchmarks.add(q);
	}

	runBenchmarks(benchmarks, logToFile, doWarmup);
    }
}
