package de.s1ckboy.thesis.benchmark.titan;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.configuration.Configuration;

import de.s1ckboy.thesis.benchmark.Configs;
import de.s1ckboy.thesis.benchmark.generic.Benchmark;
import de.s1ckboy.thesis.benchmark.generic.BenchmarkSuite;
import de.s1ckboy.thesis.benchmark.neo4j.benchmarks.Neo4jQuery1;
import de.s1ckboy.thesis.benchmark.titan.benchmarks.TitanImportBenchmark;

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
	if (cfg.getBoolean("import")) {
	    benchmarks.add(new TitanImportBenchmark(new TitanImporter(cfg), 1));
	}

	/**
	 * Benchmarks
	 */
	if (cfg.getBoolean("query1")) {
	    benchmarks.add(new Neo4jQuery1(cfg.getInt("query1.runs")));
	}

	runBenchmarks(benchmarks, logToFile, doWarmup);
    }
}
