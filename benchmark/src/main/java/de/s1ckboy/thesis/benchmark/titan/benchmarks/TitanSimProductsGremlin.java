package de.s1ckboy.thesis.benchmark.titan.benchmarks;

import de.s1ckboy.thesis.benchmark.Constants;
import de.s1ckboy.thesis.benchmark.queries.SimProducts;
import de.s1ckboy.thesis.benchmark.titan.TitanGremlinBenchmark;

public class TitanSimProductsGremlin extends TitanGremlinBenchmark implements
	SimProducts {

    @Override
    public void setUp() {
	super.setUp();
	GREMLIN_QUERY = String.format(
		"a.both('%s').loop(1){it.loops <= 2}.dedup().title",
		Constants.LABEL_EDGE_SIMILAR_TO);
    }

    @Override
    public void beforeRun() {
	super.beforeRun();

	if (doWarmup()) {
	    nextID = getRandomProduct();
	} else {
	    // TODO
	}
    }

    @SuppressWarnings({ "unchecked", "unused" })
    @Override
    public void run() {
	bindings.put("a", graphDB.getVertex(nextID));

	// execute the query
	super.run();

	for (String title : (Iterable<String>) result) {
	}
    }

    @Override
    public String getName() {
	return Constants.TITAN_SIM_PROUCTS_GREMLIN;
    }
}
