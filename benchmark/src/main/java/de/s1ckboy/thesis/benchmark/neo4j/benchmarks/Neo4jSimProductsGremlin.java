package de.s1ckboy.thesis.benchmark.neo4j.benchmarks;

import de.s1ckboy.thesis.benchmark.Constants;
import de.s1ckboy.thesis.benchmark.neo4j.Neo4jGremlinBenchmark;
import de.s1ckboy.thesis.benchmark.queries.SimProducts;

public class Neo4jSimProductsGremlin extends Neo4jGremlinBenchmark implements
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
	bindings.put("a", g.getVertex(nextID));

	// execute the query
	super.run();

	for (String title : (Iterable<String>) result) {
//	    System.out.println(title);
	}
    }

    @Override
    public String getName() {
	return Constants.NEO4J_SIM_PRODUCTS_GREMLIN;
    }

}
