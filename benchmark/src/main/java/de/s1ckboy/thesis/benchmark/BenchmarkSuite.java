package de.s1ckboy.thesis.benchmark;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.configuration.Configuration;
import org.apache.log4j.Logger;

/**
 * Abstract base class for all benchmark suites.
 */
public abstract class BenchmarkSuite {

    public static Logger log = Logger.getLogger(BenchmarkSuite.class);

    /**
     * Execute the whole benchmark suite.
     */
    public abstract void execute();

    /**
     * Run all benchmarks.
     * 
     * @param benchmarks
     *            a list of benchmarks
     */
    protected static void runBenchmarks(List<Benchmark> benchmarks) {
	runBenchmarks(benchmarks, false, false);
    }

    /**
     * Run all benchmarks.
     * 
     * @param benchmarks
     *            a list of benchmarks
     * @param log2file
     *            true to turn on logging.
     */
    protected static void runBenchmarks(List<Benchmark> benchmarks,
	    boolean log2file, boolean doWarmup) {
	for (Benchmark bm : benchmarks) {
	    runBenchmark(bm, log2file, doWarmup);
	}
    }

    /**
     * Run a benchmark.
     * 
     * @param benchmark
     *            a benchmark
     * @param log2file
     *            true to turn on logging.
     */
    protected static void runBenchmark(Benchmark benchmark, boolean log2file,
	    boolean doWarmup) {
	int runs = benchmark.getRuns();
	Unit unit = benchmark.getUnit();

	long[] totalResults = new long[runs];

	long start;
	long diff;

	long[] results = new long[runs];

	log.info(String.format("Starting Benchmark: %s (%s) with %d runs",
		benchmark.getDatabaseName(), benchmark.getName(), runs));

	benchmark.setUp();

	if (doWarmup) {
	    log.info("starting warmup...");
	    benchmark.warmup();
	    log.info("done");
	}

	// do measurement
	for (int i = 0; i < runs; i++) {
	    log.info(String.format("Starting run %d ...", i));
	    benchmark.beforeRun();
	    benchmark.setCurrentRun(i);
	    if (unit == Unit.NS) {
		start = System.nanoTime();
	    } else {
		start = System.currentTimeMillis();
	    }
	    benchmark.run();
	    if (unit == Unit.NS) {
		diff = System.nanoTime() - start;
	    } else {
		diff = System.currentTimeMillis() - start;
	    }
	    benchmark.afterRun();
	    results[i] = diff;
	    totalResults[i] = diff;
	    log.info(String.format("Done with run %d", i));
	    logMemoryInfo();
	}
	System.out.println();
	benchmark.tearDown();

	for (Entry<String, Object> e : benchmark.getResults(results).entrySet()) {
	    log.info(String.format("%s = %s", e.getKey(), e.getValue()
		    .toString()));
	}

	if (log2file) {
	    storeResults(totalResults, benchmark);
	}

	log.info(String.format("Finished Benchmark: %s (%s)",
		benchmark.getDatabaseName(), benchmark.getName()));
    }

    /**
     * Save the results to file.
     * 
     * @param results
     * @param benchmark
     */
    protected static void storeResults(long[] results, Benchmark benchmark) {
	Configuration cfg = Configs.get(benchmark.getDatabaseName()
		.toLowerCase());

	String dirString = "out/benchmarks/"
		+ getDatabaseName(cfg.getString("storage.directory"))
		+ "/"
		+ benchmark.getName().toLowerCase()
		+ "/"
		+ new SimpleDateFormat("yyyy.MM.dd-HH.mm.ss")
			.format(new Date());

	File dir = new File(dirString);
	if (dir.mkdirs()) {
	    // store results
	    String fileString = dirString + "/"
		    + benchmark.getClass().getSimpleName();
	    try (BufferedWriter writer = new BufferedWriter(new FileWriter(
		    new File(fileString)))) {

		for (int i = 0; i < results.length; i++) {
		    writer.write(String.format("%d\n", results[i]));
		}
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	    // store config
	    fileString = dirString + "/benchmark.properties";
	    try (BufferedWriter writer = new BufferedWriter(new FileWriter(
		    new File(fileString)))) {
		Iterator<String> it = cfg.getKeys();
		String key;
		while (it.hasNext()) {
		    key = it.next();
		    writer.write(String.format("%s=%s\n", key,
			    cfg.getProperty(key)));
		}
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	}
    }

    protected static void logMemoryInfo() {
	log.info(String.format(
		"total / max / free heap memory [MB]: %d / %d / %d", (Runtime
			.getRuntime().totalMemory() / 1024 / 1024), (Runtime
			.getRuntime().maxMemory() / 1024 / 1024), (Runtime
			.getRuntime().freeMemory() / 1024 / 1024)));
    }

    private static String getDatabaseName(String cfgName) {
	return cfgName.substring(cfgName.lastIndexOf('/'));
    }
}
