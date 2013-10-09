package de.s1ckboy.thesis.benchmark.generic;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import org.apache.commons.configuration.Configuration;
import org.apache.log4j.Logger;

import de.s1ckboy.thesis.benchmark.Configs;
import de.s1ckboy.thesis.generic.Constants;

/**
 * This abstract base class must be extended by any benchmark in the suite. It
 * defines some simple methods which are used during benchmark processing.
 * 
 */
public abstract class Benchmark {
    protected static Logger log = Logger.getLogger(Benchmark.class);

    /**
     * Configuration of that benchmark
     */
    protected Configuration cfg = Configs.get(getDatabaseName());

    /**
     * Number of benchmark runs
     */
    private int runs = Constants.DEFAULT_BENCHMARK_RUNS;

    /**
     * Number of current benchmark runs
     */
    private int currentRun = 0;

    /**
     * Constant seed for same random IDs for each benchmark
     */
    protected Random r = new Random(1337);

    /**
     * Used during warmup and for random selection
     */
    protected List<Long> groupIDs;

    protected List<Long> productIDs;

    protected List<Long> userIDs;

    /**
     * Sets the number of benchmarks to run
     * 
     * @param runs
     *            number of benchmarks
     */
    public void setRuns(int runs) {
	this.runs = runs;
    }

    /**
     * Returns the number of benchmarks to run
     * 
     * @return number of benchmarks
     */
    public int getRuns() {
	return runs;
    }

    /**
     * Returns the measurement unit for that benchmark.
     * 
     * @return Unit (milli- or nanosecond)
     */
    public Unit getUnit() {
	String cfgUnit = cfg.getString(getName() + ".unit", "ms");
	if ("ns".equals(cfgUnit)) {
	    return Unit.NS;
	} else {
	    return Unit.MS;
	}
    }

    /**
     * Set the number of current benchmark run
     * 
     * @param currentRun
     */
    public void setCurrentRun(int currentRun) {
	this.currentRun = currentRun;
    }

    /**
     * Returns the number of current benchmark run
     * 
     * @return
     */
    public int getCurrentRun() {
	return currentRun;
    }

    /**
     * Sets the configuration for that benchmark.
     * 
     * @param cfg
     */
    public void setConfiguration(Configuration cfg) {
	this.cfg = cfg;
    }

    /**
     * Returns the configuration for that benchmark.
     * 
     * @return Configuration
     */
    public Configuration getConfiguration() {
	return cfg;
    }

    /**
     * This method is called once before <code>run()</code> is invoked for the
     * first time.
     */
    public void setUp() {
	groupIDs = new ArrayList<Long>();
	productIDs = new ArrayList<Long>();
	userIDs = new ArrayList<Long>();
    }

    /**
     * This method runs the benchmark
     */
    public abstract void run();

    /**
     * This method is called once after the last call of <code>run()</code>
     */
    public abstract void tearDown();

    /**
     * This method is called before each single call of <code>run()</code>
     */
    public abstract void beforeRun();

    /**
     * This method is called after each single call of <code>run()</code>
     */
    public abstract void afterRun();

    /**
     * This method is called once before the benchmark starts
     */
    public abstract void warmup();

    /**
     * Returns the Benchmarks name
     * 
     * @return name of the benchmark
     */
    public abstract String getName();

    /**
     * Returns the Database Name
     * 
     * @return
     */
    public abstract String getDatabaseName();

    /**
     * Picks a random class and inside the class a random node id.
     * 
     * @return a vertex identifier
     */
    protected Long getRandomVertexID() {
	List<Long> l = null;

	/*
	 * Set ranges so that the probability to pick a class depends on the
	 * number of its instances.
	 */
	int b1 = groupIDs.size();
	int b2 = b1 + productIDs.size();
	int total = b2 + userIDs.size();

	int i = r.nextInt(total);

	if (i >= 0 && i < b1) {
	    l = groupIDs;
	} else if (i >= b1 && i < b2) {
	    l = productIDs;
	} else {
	    l = userIDs;
	}
	return l.get(r.nextInt(l.size()));
    }

    protected Long getRandomProduct() {
	return productIDs.get(r.nextInt(productIDs.size()));
    }

    protected Long getRandomUser() {
	return userIDs.get(r.nextInt(userIDs.size()));
    }

    protected Long getRandomGroup() {
	return groupIDs.get(r.nextInt(groupIDs.size()));
    }

    /**
     * This method calculates some default measurement results: average,
     * standard deviation, minimum and maximum value.
     * 
     * The method can be overwritten and enhanced with custom benchmark results.
     * 
     * @param runtimes
     *            an array with all benchmark results
     * @return a map between measure name (e.g. "average") and its value (e.g.
     *         23)
     */
    public Map<String, Object> getResults(long[] runtimes) {
	Map<String, Object> results = new TreeMap<String, Object>();
	String unit = getUnit().name().toLowerCase();

	// average
	long sum = 0L;
	for (long runtime : runtimes) {
	    sum += runtime;
	}
	double avg = new Double(sum) / runtimes.length;

	results.put("Average [" + unit + "]", avg);

	// stdev, min, max
	long tmp = 0;
	long min = Long.MAX_VALUE;
	long max = Long.MIN_VALUE;
	for (long runtime : runtimes) {
	    tmp += Math.pow((runtime - avg), 2);
	    // min
	    if (runtime < min) {
		min = runtime;
	    }
	    // max
	    if (runtime > max) {
		max = runtime;
	    }
	}
	double stdev = Math.sqrt(new Double(tmp) / (runtimes.length));

	results.put("Stdev [" + unit + "]", stdev);
	results.put("Min [" + unit + "]", min);
	results.put("Max [" + unit + "]", max);

	return results;
    }
}