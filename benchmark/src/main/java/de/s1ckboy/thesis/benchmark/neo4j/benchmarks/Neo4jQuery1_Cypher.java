package de.s1ckboy.thesis.benchmark.neo4j.benchmarks;

import java.util.Iterator;

import org.neo4j.graphdb.Node;
import org.neo4j.helpers.collection.IteratorUtil;

import de.s1ckboy.thesis.benchmark.Constants;
import de.s1ckboy.thesis.benchmark.neo4j.Neo4jCypherBenchmark;
import de.s1ckboy.thesis.benchmark.queries.Query1;

public class Neo4jQuery1_Cypher extends Neo4jCypherBenchmark implements Query1 {
    private long nextID;

    @Override
    public void beforeRun() {
	super.beforeRun();
	if (r.nextBoolean()) {
	    nextID = getRandomProduct();
	} else {
	    nextID = getRandomUser();
	}

	CYPHER_QUERY = String.format("START n=node(%d) RETURN *", nextID);
    }

    @Override
    public void run() {
	super.run();
	// get resulting nodes and their properties
	Iterator<Node> n_column = result.columnAs("n");
	for (Node n : IteratorUtil.asIterable(n_column)) {
	    for (String key : n.getPropertyKeys()) {
		n.getProperty(key);
	    }
	}
    }

    @Override
    public String getName() {
	return Constants.NEO4J_Q1_CYPHER;
    }
}
