package de.s1ckboy.thesis.benchmark.neo4j.benchmarks;

import java.util.Iterator;

import org.neo4j.helpers.collection.IteratorUtil;

import de.s1ckboy.thesis.benchmark.Constants;
import de.s1ckboy.thesis.benchmark.neo4j.Neo4jCypherBenchmark;
import de.s1ckboy.thesis.benchmark.queries.SimProducts;

public class Neo4jSimProductsCypher extends Neo4jCypherBenchmark implements
	SimProducts {
    @Override
    public void beforeRun() {
	super.beforeRun();
	if (doWarmup()) {
	    nextID = getRandomProduct();
	} else {
	    // TODO
	}
	CYPHER_QUERY = String
		.format("START n=node(%d) MATCH n-[:%s*..2]-s RETURN DISTINCT s.title AS title;",
			nextID, Constants.LABEL_EDGE_SIMILAR_TO);
    }

    @SuppressWarnings("unused")
    @Override
    public void run() {
	super.run();
	Iterator<String> title_column = result.columnAs("title");
	for (String title : IteratorUtil.asIterable(title_column)) {
	    // validation
	    // System.out.println(title);

	}
    }

    @Override
    public String getName() {
	return Constants.NEO4J_SIM_PRODUCTS_CYPHER;
    }
}
