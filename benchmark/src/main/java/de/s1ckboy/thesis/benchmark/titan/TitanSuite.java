package de.s1ckboy.thesis.benchmark.titan;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.configuration.Configuration;

import de.s1ckboy.thesis.benchmark.Configs;
import de.s1ckboy.thesis.benchmark.generic.Benchmark;
import de.s1ckboy.thesis.benchmark.generic.BenchmarkSuite;
import de.s1ckboy.thesis.benchmark.titan.benchmarks.TitanImportBenchmark;
import de.s1ckboy.thesis.benchmark.titan.benchmarks.TitanQuery1_CoreAPI;

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
	    Benchmark i = new TitanImportBenchmark(new TitanImporter(cfg));
	    i.setRuns(1);
	    benchmarks.add(i);
	}

	/**
	 * Benchmarks
	 */
	if (cfg.getBoolean("query1")) {
	    Benchmark q1 = new TitanQuery1_CoreAPI();
	    q1.setRuns(cfg.getInt("query1.runs"));
	    benchmarks.add(q1);
	}

	runBenchmarks(benchmarks, logToFile, doWarmup);
    }
}
